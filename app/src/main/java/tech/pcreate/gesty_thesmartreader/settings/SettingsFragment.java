/*
 * Copyright (c) @ 2019 Yash Prakash
 */

package tech.pcreate.gesty_thesmartreader.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import tech.pcreate.gesty_thesmartreader.R;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        SharedPreferences int_preference = getActivity().getSharedPreferences(getString(R.string.scroll_mode_integer), MODE_PRIVATE);
        SharedPreferences.Editor editor = int_preference.edit();

        Preference preference = getPreferenceScreen().findPreference(getString(R.string.scrollMode));
        preference.setOnPreferenceChangeListener((preference1, newValue) -> {
            editor.putInt(getString(R.string.scroll_mode_integer), Integer.parseInt(String.valueOf(newValue)));
            editor.apply();

            return true;
        });


    }
}
