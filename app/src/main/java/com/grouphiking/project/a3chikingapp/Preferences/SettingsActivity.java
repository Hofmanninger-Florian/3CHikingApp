package com.grouphiking.project.a3chikingapp.Preferences;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceFragmentCompat;

import com.grouphiking.project.a3chikingapp.Data.Constants;
import com.grouphiking.project.a3chikingapp.Data.Language;
import com.grouphiking.project.a3chikingapp.R;

import java.util.Locale;

import static com.grouphiking.project.a3chikingapp.R.color.Button_Color_Dark;

public class SettingsActivity extends AppCompatActivity {

    private LanguageSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.setTransition(this, new LinearInterpolator());
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.hike_settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        spinner.setListener(new LanguageSpinner.LanguageListener() {
            @Override
            public void onLanguageChanges(Language language) {
                if(language != null){
                    getPreferences(Context.MODE_PRIVATE).edit().putString(getString(R.string.SpinnerKey), language.toString()).apply();
                    setLocale(language.getLanguage());
                }
            }
        });
    }

    private void setLocale(String locale){
        Locale myLocal = new Locale(locale);
        Resources res = getResources();
        DisplayMetrics metrics = res.getDisplayMetrics();
        Configuration configs = res.getConfiguration();
        configs.setLocale(myLocal);
        res.updateConfiguration(configs, metrics);
        recreate();
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


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}