package org.anrdigital.rebootdeckbuilder.fragments.nrdb

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.anrdigital.rebootdeckbuilder.api.NrdbDeckLists
import org.anrdigital.rebootdeckbuilder.api.NrdbHelper
import org.anrdigital.rebootdeckbuilder.db.CardRepository
import org.anrdigital.rebootdeckbuilder.db.IDeckRepository
import org.anrdigital.rebootdeckbuilder.game.Deck
import org.anrdigital.rebootdeckbuilder.helper.NrdbDeckFactory
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class NrdbFragmentViewModel(private val cardRepository: CardRepository, private val deckRepository: IDeckRepository) : ViewModel() {
    val isSignedIn: Boolean
        get() = NrdbHelper.isSignedIn

    private var todaysDeckLists = MutableLiveData<ArrayList<Deck>>()
    private var privateDecks = MutableLiveData<ArrayList<Deck>>()

    fun nrdbSignIn(context: Context) {
        NrdbHelper.doNrdbSignIn(context)
    }

    fun nrdbSignOut(context: Context) {
        NrdbHelper.doNrdbSignOut(context)
    }

    fun getNrdbDeckLists(context: Context): MutableLiveData<ArrayList<Deck>> {
        // trigger async refresh
        val today = Date()
        NrdbHelper.getDateDeckLists(today, context, ::updateDateDeckLists)

        return todaysDeckLists
    }

    fun getNrdbPrivateDecks(context: Context): MutableLiveData<ArrayList<Deck>> {
        NrdbHelper.getMyPrivateDeckLists(context, ::updatePrivateDecks)
        return privateDecks
    }

    private fun updateDateDeckLists(response: Response<NrdbDeckLists>) {
        val result = ArrayList<Deck>()
        val data = response.body()!!.data
        for (nrdbDeckList in data) {
            val deck = NrdbDeckFactory(cardRepository).convertToDeck(nrdbDeckList)
            result.add(deck)
        }
        todaysDeckLists.value = result
    }

    private fun updatePrivateDecks(response: Response<NrdbDeckLists>) {
        val result = ArrayList<Deck>()
        val data = response.body()!!.data
        for (nrdbDeckList in data) {
            val deck = NrdbDeckFactory(cardRepository).convertToDeck(nrdbDeckList)
            result.add(deck)
        }
        privateDecks.value = result
    }

    fun cloneDeck(deck: Deck) {
        deckRepository.cloneDeck(deck);
    }


}
