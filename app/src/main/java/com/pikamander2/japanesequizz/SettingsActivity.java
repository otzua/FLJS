package com.pikamander2.japanesequizz;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Settings");
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_container, new GeneralPreferenceFragment())
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return true;
    }

    public static class GeneralPreferenceFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.pref_general, rootKey);

            bindPreferenceSummaryToValue(findPreference("choices_list"));
            bindPreferenceSummaryToValue(findPreference("count_list"));
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            if (preference == null) return;

            preference.setOnPreferenceChangeListener((pref, value) -> {
                String stringValue = value.toString();

                if (pref instanceof ListPreference) {
                    ListPreference listPreference = (ListPreference) pref;
                    int index = listPreference.findIndexOfValue(stringValue);
                    pref.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
                } else {
                    pref.setSummary(stringValue);
                }
                return true;
            });

            // Trigger immediately with current value
            String currentValue = PreferenceManager
                    .getDefaultSharedPreferences(preference.getContext())
                    .getString(preference.getKey(), "");

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(currentValue);
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
            } else {
                preference.setSummary(currentValue);
            }
        }
    }
}
