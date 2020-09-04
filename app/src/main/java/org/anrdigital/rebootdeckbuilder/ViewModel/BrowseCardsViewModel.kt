package org.anrdigital.rebootdeckbuilder.ViewModel

import androidx.lifecycle.ViewModel
import org.anrdigital.rebootdeckbuilder.db.CardRepository
import org.anrdigital.rebootdeckbuilder.game.CardList
import org.anrdigital.rebootdeckbuilder.game.CardPool
import org.anrdigital.rebootdeckbuilder.game.Format
import org.anrdigital.rebootdeckbuilder.helper.ISettingsProvider
import java.util.*

class BrowseCardsViewModel(private val cardRepo: CardRepository, settingsProvider: ISettingsProvider) : ViewModel() {
    private var mPool: CardPool = cardRepo.cardPool
    var packFilter = ArrayList<String>()
    var cardList: CardList? = null
        private set
    private val settingsProvider: ISettingsProvider
    var searchText = ""
    var browseFormat: Format
    fun init() {
        cardList = mPool.getCards()
    }

    fun doSearch(searchText: String?) {
        cardList!!.clear()
        cardList!!.addAll(cardRepo.searchCards(searchText, mPool))
    }

    fun updatePackFilter(packFilter: ArrayList<String>, format: Format) {
        browseFormat = format
        this.packFilter = packFilter
        mPool = cardRepo.getCardPool(format, packFilter)
    }

    fun useMyCollectionAsFilter() {
        updatePackFilter(settingsProvider.myCollection, browseFormat)
    }

    init {
        browseFormat = cardRepo.defaultFormat
        this.settingsProvider = settingsProvider
    }
}