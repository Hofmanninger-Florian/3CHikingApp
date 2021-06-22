package com.grouphiking.project.a3chikingapp.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.grouphiking.project.a3chikingapp.Data.Constants;
import com.grouphiking.project.a3chikingapp.Data.Mode;
import com.grouphiking.project.a3chikingapp.R;

public class daynightswitch extends Preference {

    private Button button_night;
    private Button button_day;

    public daynightswitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWidgetLayoutResource(R.layout.perf_daynightswitch);
    }

    public daynightswitch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        button_day = (Button) holder.findViewById(R.id.pref_button_day);
        button_night = (Button) holder.findViewById(R.id.pref_button_night);

        //set default "Selected" button
        if (Constants.DEF_MODE) {
            Constants.setMODE(Mode.NIGHT);
            button_night.setBackgroundColor(Color.parseColor(Constants.NIGHT_COLOR));
            SharedPreferences.Editor editor = getSharedPreferences().edit();
            editor.putBoolean(getContext().getString(R.string.SwitchKey),Constants.MODE.isValue()).apply();
        } else {
            Constants.setMODE(Mode.DAY);
            button_day.setBackgroundColor(Color.parseColor(Constants.DAY_COLOR));
            SharedPreferences.Editor editor = getSharedPreferences().edit();
            editor.putBoolean(getContext().getString(R.string.SwitchKey),Constants.MODE.isValue()).apply();
        }
        //Updating
        setButtons();
    }

    private void setButtons() {
        button_night.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Constants.DEF_MODE = true;
                SharedPreferences.Editor editor = getSharedPreferences().edit();
                editor.putString(getContext().getString(R.string.SwitchKey), Mode.NIGHT.toString());
                editor.apply();
                Constants.getWorkingUser().setMode(Mode.NIGHT);
                Constants.updateUser(Constants.getView());
                Constants.setLISTENERPOST(new Constants.postExecuteListner() {
                    @Override
                    public void onpostExecute() {
                        Log.d("Debug", "Updated everything");
                    }
                });
            }
        });
        button_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Constants.DEF_MODE = false;
                SharedPreferences.Editor editor = getSharedPreferences().edit();
                editor.putString(getContext().getString(R.string.SwitchKey), Mode.DAY.toString());
                editor.apply();
                Constants.getWorkingUser().setMode(Mode.DAY);
                Constants.updateUser(Constants.getView());
                Constants.setLISTENERPOST(new Constants.postExecuteListner() {
                    @Override
                    public void onpostExecute() {
                        Log.d("Debug", "Updated everything");
                    }
                });
            }
        });

    }

}
