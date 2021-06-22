package com.grouphiking.project.a3chikingapp.Preferences;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.grouphiking.project.a3chikingapp.Activitys.MainActivity;
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
        setContentView(R.layout.settings_activity);
        Constants.setView(findViewById(R.id.settings_act));
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
                                Constants.setLANGUAGE(language);
                                //For Updating
                                Constants.getWorkingUser().setLanguage(language);
                                Constants.updateUser(Constants.getView());
                                Constants.setLISTENERPOST(new Constants.postExecuteListner() {
                                    @Override
                                    public void onpostExecute() {
                                        Log.d("Debug", "Everything Up to Date");
                                    }
                                });
                                Constants.setSpinnerAlready(false);
                                Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                                startActivity(intent);

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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        super.attachBaseContext(MyContextWrapper.wrap(newBase,preferences.getString(newBase.getString(R.string.SpinnerKey), Constants.LANGUAGE.getLanguage())));
        Constants.LANGUAGE_SET = true;
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