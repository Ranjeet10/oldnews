package com.bajratechnologies.nagariknews.views.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.bajratechnologies.nagariknews.R;


/**
 * Created by ronem on 1/28/16.
 */
public class PreferenceFramgment extends PreferenceFragment {
//    SharedPreferences prefs;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        Preference p = findPreference(getString(R.string.preference_checkbox_key));
        p.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.i("prefs", newValue + "");
                return true;
            }
        });

    }


}
