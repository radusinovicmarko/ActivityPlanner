package com.example.activityplanner.ui;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.activityplanner.R;

public class MySettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

}