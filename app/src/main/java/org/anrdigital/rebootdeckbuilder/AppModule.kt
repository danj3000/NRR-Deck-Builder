package org.anrdigital.rebootdeckbuilder

import android.app.Application
import org.anrdigital.rebootdeckbuilder.ViewModel.BrowseCardsViewModel
import org.anrdigital.rebootdeckbuilder.ViewModel.DeckActivityViewModel
import org.anrdigital.rebootdeckbuilder.ViewModel.FullScreenViewModel
import org.anrdigital.rebootdeckbuilder.ViewModel.MainActivityViewModel
import org.anrdigital.rebootdeckbuilder.db.*
import org.anrdigital.rebootdeckbuilder.fragments.nrdb.NrdbFragmentViewModel
import org.anrdigital.rebootdeckbuilder.helper.ISettingsProvider
import org.anrdigital.rebootdeckbuilder.helper.LocalFileHelper
import org.anrdigital.rebootdeckbuilder.helper.SettingsProvider
import org.koin.android.ext.android.startKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {

    single<IDeckRepository> { DeckRepository(get(), get()) }
    single<ISettingsProvider> { SettingsProvider(androidContext()) }

    single { CardRepository(androidContext(), get(), get()) }
    single { DatabaseHelper(androidContext()) }

    factory { JSONDataLoader(get()) }
    factory { LocalFileHelper(androidContext()) }

    viewModel { DeckActivityViewModel(get(), get(), get()) }
    viewModel { BrowseCardsViewModel(get(), get()) }
    viewModel { MainActivityViewModel (get(), get()) }
    viewModel { FullScreenViewModel(get(), get())}
    viewModel { NrdbFragmentViewModel(get(), get()) }
}

open class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule))
    }
}