package org.anrdigital.rebootdeckbuilder.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;

import org.anrdigital.rebootdeckbuilder.db.CardRepository;
import org.anrdigital.rebootdeckbuilder.fragments.SettingsFragment;
import org.anrdigital.rebootdeckbuilder.game.Format;

import java.util.ArrayList;
import java.util.Set;

public class SettingsProvider implements ISettingsProvider {
    private Context context;

    public SettingsProvider(Context context) {
        this.context = context;
    }

    @Override
    @NonNull
    public CardRepository.CardRepositoryPreferences getCardRepositoryPreferences() {
        return new CardRepository.CardRepositoryPreferences(3);
    }

    @Override
    public String getLanguagePref() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SettingsFragment.KEY_PREF_LANGUAGE, "en");
    }

    @Override
    public int getDefaultFormatId() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String formatIdString = preferences.getString(SettingsFragment.KEY_PREF_DEFAULT_FORMAT, String.valueOf(Format.FORMAT_STANDARD));
        return Integer.parseInt(formatIdString);
    }

    @Override
    public ArrayList<String> getMyCollection() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        ArrayList<String> packCodes = new ArrayList<>();
        Set<String> setting = preferences.getStringSet(SettingsFragment.KEY_PREF_COLLECTION, null);
        if (setting != null) {
            packCodes.addAll(setting);
        }
        return packCodes;
    }

    @Override
    public boolean getHideNonVirtualApex() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("pref_HideNonVirtualApex", true);
    }

}
