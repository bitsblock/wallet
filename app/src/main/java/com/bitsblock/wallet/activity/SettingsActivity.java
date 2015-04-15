package com.bitsblock.wallet.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bitsblock.wallet.R;
import com.bitsblock.wallet.util.Constant;
import com.bitsblock.wallet.util.Globals;
import com.bitsblock.wallet.util.Interface;

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
        private Activity activity;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
            ListPreference lp = (ListPreference) findPreference("currency_of_list");
            lp.setEntryValues(Globals.values);
            SharedPreferences preferences = getActivity().getSharedPreferences(Constant.PREF_CURRENT_USER,MODE_PRIVATE);
            activity = getActivity();
            lp.setSummary(preferences.getString(Constant.CURRENCY_TYPE, null));
            lp.setValue(String.valueOf(preferences.getFloat(Constant.CURRENCY_VALUE, 0.0f)));
            lp.setDefaultValue(preferences.getString(Constant.CURRENCY_TYPE, null));

            ListPreference lp2 = (ListPreference) findPreference("type_of_input");
            lp2.setSummary(preferences.getString(Constant.TYPE_INPUT,null));
            lp2.setValue(preferences.getString(Constant.TYPE_INPUT, null));
            lp2.setDefaultValue(preferences.getString(Constant.TYPE_INPUT, null));
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            updatePreference(findPreference(key));
        }

        private void updatePreference(Preference p) {
            SharedPreferences preferences = activity.getSharedPreferences(Constant.PREF_CURRENT_USER, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            if (p.getKey().equals("currency_of_list") && ((ListPreference)p).getEntry() != null) {
                ListPreference listPref = (ListPreference) p;
                p.setSummary(listPref.getEntry());
                editor.putString(Constant.CURRENCY_TYPE, String.valueOf(listPref.getSummary()));
                editor.putFloat(Constant.CURRENCY_VALUE, (float) Double.parseDouble(listPref.getValue()));
            }
            if (p.getKey().equals("type_of_input") && ((ListPreference)p).getEntry() != null) {
                ListPreference listPref = (ListPreference) p;
                p.setSummary(listPref.getEntry());
                editor.putString(Constant.TYPE_INPUT, String.valueOf(listPref.getSummary()));
            }

            editor.apply();
        }
    }
}
