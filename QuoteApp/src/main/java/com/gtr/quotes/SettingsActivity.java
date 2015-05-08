package com.gtr.quotes;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class SettingsActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefFragment()).commit();
    }
}

/**
 * This fragment shows the preferences for the first header.
 */
class PrefFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //PreferenceManager.setDefaultValues(getActivity(), R.layout., false);

        // Load the preferences from an XML resource

        addPreferencesFromResource(R.xml.settings_layout);
    }
}
