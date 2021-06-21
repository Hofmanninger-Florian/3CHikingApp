package com.grouphiking.project.a3chikingapp.Preferences;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.grouphiking.project.a3chikingapp.Data.Constants;
import com.grouphiking.project.a3chikingapp.Data.Language;
import com.grouphiking.project.a3chikingapp.R;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {


    public final Constants constant = new Constants();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.setTransition(this, new LinearInterpolator());
        if(Constants.SPINNER_COUNT >= Constants.SPINNER_CALLS){
            setLocale(Constants.LANGUAGE.getLanguage());
        }
        setContentView(R.layout.settings_activity);
        SettingsFragment fragment = null;
        if (savedInstanceState == null) {
            fragment = new SettingsFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.hike_settings, fragment)
                    .commit();
            fragment.setConstants(constant);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        Constants.setLISTENER(new Constants.onValueChange() {
            @Override
            public void valueChanged() {
                if(constant.getSPINNER() != null && constant.getSPINNER().getListener() == null) {
                    LanguageSpinner.LanguageListener listener = new LanguageSpinner.LanguageListener() {
                        @Override
                        public void onLanguageChanges(Language language) {
                            if (language != null) {
                                getPreferences(Context.MODE_PRIVATE).edit().putString(getString(R.string.SpinnerKey), language.toString()).apply();
                                recreate();
                            }
                        }
                    };
                    constant.getSPINNER().setListener(listener);
                }
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    public void setLocale(String locale){
        Locale myLocal = new Locale(locale);
        Locale.setDefault(myLocal);
        Configuration config = new Configuration();
        config.setLocale(myLocal);
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        public Constants constant;

        public void setConstants(Constants constants) {
            this.constant = constants;
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            if(constant != null)constant.setSPINNER(findPreference(getString(R.string.SpinnerKey)));
        }
    }
}