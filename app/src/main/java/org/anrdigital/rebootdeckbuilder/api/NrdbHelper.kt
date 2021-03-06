package org.anrdigital.rebootdeckbuilder.api

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import org.anrdigital.rebootdeckbuilder.R
import org.anrdigital.rebootdeckbuilder.appauth.AuthStateManager
import org.anrdigital.rebootdeckbuilder.appauth.TokenActivity
import org.anrdigital.rebootdeckbuilder.game.Card
import org.anrdigital.rebootdeckbuilder.helper.AppManager
import net.openid.appauth.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

object NrdbHelper {
    var isSignedIn = false
    const val NRDB_SECRET = "lvlwz4israoo44kso088c4g40kwwco40008w04sgswc084kcw"

    /* Shows a card's page on netrunnerdb.com */
    fun ShowNrdbWebPage(context: Context, card: Card) {
        val url = String.format(context.getString(R.string.nrdb_card_url), card.code)
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }

    /* OAuth SignIn for Nrdb */
    fun doNrdbSignIn(context: Context) {
        val serviceConfiguration = AuthorizationServiceConfiguration(
                Uri.parse(context.getString(R.string.nrdb_auth_endpoint)) /* auth endpoint */,
                Uri.parse(context.getString(R.string.nrdb_token_endpoint)) /* token endpoint */
        )

        val clientId = context.getString(R.string.nrdb_oauth_clientid)
        val redirectUri: Uri = Uri.parse(context.getString(R.string.oauth_callback_uri))
        val builder = AuthorizationRequest.Builder(
                serviceConfiguration,
                clientId,
                "code",
                redirectUri
        )

        val request = builder.build()
        val authorizationService = AuthorizationService(context)
        val postAuthorizationIntent = Intent(context, TokenActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(context, request.hashCode(), postAuthorizationIntent, 0)
        authorizationService.performAuthorizationRequest(request, pendingIntent)

        isSignedIn = true;
    }

    fun getDateDeckLists(date: Date, context: Context,
                                 onSuccess: (Response<NrdbDeckLists>)->Unit) {
        //todo: cache response?
        val dateString = SimpleDateFormat("yyyy-MM-dd").format(date)
//        val dateString = "2020-07-28"

        val apiService = NrdbClient().getApiService(context)
        val call = apiService.getDateDeckLists(dateString)
        Toast.makeText(context, R.string.accessing_nrdb, Toast.LENGTH_SHORT).show()

        call.enqueue(object : Callback<NrdbDeckLists> {
            override fun onResponse(call: Call<NrdbDeckLists>, response: Response<NrdbDeckLists>) {
                Log.i(AppManager.LOGCAT, "Success NRDB Response: decklists by_date")
                onSuccess(response)
            }

            override fun onFailure(call: Call<NrdbDeckLists>, t: Throwable) {
                Log.e(AppManager.LOGCAT, "Error NRDB Response: decklists by_date")
            }
        })
    }

    fun getMyPrivateDeckLists(context: Context,
                                      onSuccess: (Response<NrdbDeckLists>)->Unit) {
        //todo: split out token refresh?
        AuthStateManager.getInstance(context).current
                .performActionWithFreshTokens(AuthorizationService(context)
                ) {
                    _, _, ex ->
                    if (ex != null) {
                        this.doNrdbSignOut(context)
                    }
                    else {
                        privateDeckListsAction(context, onSuccess)
                    }
                }
    }

    private fun privateDeckListsAction(context: Context, onSuccess: (Response<NrdbDeckLists>) -> Unit) {
        //todo: cache response?
        val apiService = NrdbClient().getApiService(context)
        val call = apiService.getMyDeckLists()
        Toast.makeText(context, R.string.accessing_nrdb, Toast.LENGTH_SHORT).show()

        call.enqueue(object : Callback<NrdbDeckLists> {
            override fun onResponse(call: Call<NrdbDeckLists>, response: Response<NrdbDeckLists>) {
                Log.i(AppManager.LOGCAT, "Success NRDB Response: decklists by_date")
                onSuccess(response)
            }

            override fun onFailure(call: Call<NrdbDeckLists>, t: Throwable) {
                Log.e(AppManager.LOGCAT, "Error NRDB Response: decklists by_date")
            }
        })
    }

    fun doNrdbSignOut(context: Context) {
        isSignedIn = false

        // discard the authorization and token state, but retain the configuration and
        // dynamic client registration (if applicable), to save from retrieving them again.
        val mStateManager = AuthStateManager.getInstance(context)
        val currentState: AuthState = mStateManager.current
        if (currentState.authorizationServiceConfiguration != null) {
            val clearedState = AuthState(currentState.authorizationServiceConfiguration!!)
            if (currentState.lastRegistrationResponse != null) {
                clearedState.update(currentState.lastRegistrationResponse)
            }
            mStateManager.replace(clearedState)
        }
    }

}