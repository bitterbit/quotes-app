package com.gtr.quotes;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * This fragment shows the preferences for the first header.
 */
public class PrefFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //PreferenceManager.setDefaultValues(getActivity(), R.layout., false);

        // Load the preferences from an XML resource

        addPreferencesFromResource(R.xml.settings_layout);
    }
}
