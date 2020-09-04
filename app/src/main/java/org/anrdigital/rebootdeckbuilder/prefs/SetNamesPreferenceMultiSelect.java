package org.anrdigital.rebootdeckbuilder.prefs;

import android.content.Context;
import android.util.AttributeSet;

import org.anrdigital.rebootdeckbuilder.helper.AppManager;

public class SetNamesPreferenceMultiSelect extends ListPreferenceMultiSelect {

    public SetNamesPreferenceMultiSelect(Context context) {
        super(context);
    }

    public SetNamesPreferenceMultiSelect(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public CharSequence[] getEntries() {
        return AppManager.getInstance().getSetNames().toArray(new CharSequence[0]);
    }

    @Override
    public CharSequence[] getEntryValues() {
        return getEntries();
    }


}
