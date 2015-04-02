package com.liberic.bitcoinwallet.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.liberic.bitcoinwallet.R;
import com.liberic.bitcoinwallet.util.Constant;
import com.liberic.bitcoinwallet.util.Globals;
import com.liberic.bitcoinwallet.util.Interface;

public class SettingsActivity extends ActionBarActivity {
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Settings);
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Interface.getRates(this,true);

        getFragmentManager().beginTransaction().replace(R.id.content_wrapper, new GeneralPreferenceFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class GeneralPreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
            ListPreference lp = (ListPreference) findPreference("currency_of_list");
            //lp.setEntries(Globals.entries);
            lp.setEntryValues(Globals.values);
            SharedPreferences preferences = getActivity().getSharedPreferences(Constant.PREF_CURRENT_USER,MODE_PRIVATE);
            lp.setSummary(preferences.getString(Constant.CURRENCY_TYPE, null));
            lp.setValue(String.valueOf(preferences.getFloat(Constant.CURRENCY_VALUE, 0.0f)));
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            updatePreference(findPreference(key));
        }

        private void updatePreference(Preference p) {
            SharedPreferences preferences = getActivity().getSharedPreferences(Constant.PREF_CURRENT_USER,MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            if (p.getKey().equals("currency_of_list")) {
                ListPreference listPref = (ListPreference) p;
                p.setSummary(listPref.getEntry());
                editor.putString(Constant.CURRENCY_TYPE, String.valueOf(listPref.getSummary()));
                editor.putFloat(Constant.CURRENCY_VALUE, (float) Double.parseDouble(listPref.getValue()));
            }
            editor.apply();
        }
    }
}
