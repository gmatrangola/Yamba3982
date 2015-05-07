package com.newcircle.yamba;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by geoff on 5/7/15.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
