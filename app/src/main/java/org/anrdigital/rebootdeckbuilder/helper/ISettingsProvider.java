package org.anrdigital.rebootdeckbuilder.helper;

import androidx.annotation.NonNull;

import org.anrdigital.rebootdeckbuilder.db.CardRepository;

import java.util.ArrayList;

public interface ISettingsProvider {
    @NonNull
    CardRepository.CardRepositoryPreferences getCardRepositoryPreferences();

    String getLanguagePref();

    int getDefaultFormatId();

    ArrayList<String> getMyCollection();

    boolean getHideNonVirtualApex();
}
