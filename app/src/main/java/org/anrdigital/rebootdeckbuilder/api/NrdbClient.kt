package org.anrdigital.rebootdeckbuilder.api

import android.content.Context
import okhttp3.OkHttpClient
import org.anrdigital.rebootdeckbuilder.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NrdbClient {
    private lateinit var apiService: NrdbApiService

    fun getApiService(context: Context): NrdbApiService {

        // Initialize ApiService if not initialized yet
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                    .baseUrl(context.getString(R.string.nrdb_api_root))
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okhttpClient(context)) // Add our Okhttp client
                    .build()

            apiService = retrofit.create(NrdbApiService::class.java)
        }

        return apiService
    }

    /**
     * Initialize OkhttpClient with our interceptor
     */
    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(context))
                .build()
    }

}