package com.example.a2alexf68.poi;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by 2alexf68 on 04/05/2017.
 */
public class Preference extends PreferenceActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
