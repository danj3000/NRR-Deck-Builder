package org.anrdigital.rebootdeckbuilder.game;

import org.anrdigital.rebootdeckbuilder.fragments.SettingsFragment;
import org.anrdigital.rebootdeckbuilder.helper.AppManager;

public class NetRunnerBD {

    public static final String BASE_URL = "https://nrdb.reteki.fun/";
    public static final String URL_API_SEARCH = "/api/search/";
    private static final String URL_GET_ALL_CARDS = "api/2.0/public/cards?_locale=%s";
    private static final String URL_GET_ALL_PACKS = "api/2.0/public/packs";
    private static final String URL_GET_MWL = "api/2.0/public/mwl";

    // new github pages data location
//    public static final String URL_CARDS_JSON = "https://anrdigital.github.io/ANR-Deck-Builder/app/src/main/res/raw/cardsv2.json";
//    public static final String URL_PACKS_JSON = "https://anrdigital.github.io/ANR-Deck-Builder/app/src/main/res/raw/packs.json";
//    public static final String URL_MWL_JSON = "https://anrdigital.github.io/ANR-Deck-Builder/app/src/main/res/raw/mwl.json";
    public static final String URL_CYCLES_JSON = "https://danj3000.github.io/NRR-Deck-Builder/app/src/main/res/raw/cycles.json";
    public static final String URL_FORMATS_JSON = "https://danj3000.github.io/NRR-Deck-Builder/app/src/main/res/raw/formats.json";
    public static final String URL_ROTATIONS_JSON = "https://danj3000.github.io/NRR-Deck-Builder/app/src/main/res/raw/rotations.json";

    public static String getAllCardsUrl() {
        return BASE_URL + String.format(URL_GET_ALL_CARDS, AppManager.getInstance().getSharedPrefs().getString(SettingsFragment.KEY_PREF_LANGUAGE, "en"));
    }

    public static String getAllPacksUrl() {
        return BASE_URL + URL_GET_ALL_PACKS;
    }

    public static String getMWLUrl() {
        return BASE_URL + URL_GET_MWL;
    }

    public static String getFormatsUrl() {
        return URL_FORMATS_JSON;
    }

    public static String getCyclesUrl() {
        return URL_CYCLES_JSON;
    }

    public static String getRotationsUrl() {
        return URL_ROTATIONS_JSON;
    }
}
