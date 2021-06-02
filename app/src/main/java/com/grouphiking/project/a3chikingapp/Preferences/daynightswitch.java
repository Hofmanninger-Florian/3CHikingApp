package com.grouphiking.project.a3chikingapp.Preferences;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.grouphiking.project.a3chikingapp.R;

public class daynightswitch extends Preference {


    public daynightswitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWidgetLayoutResource(R.layout.perf_daynightswitch);
    }

    public daynightswitch(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(false);
        //Set Buttonfunktions
    }


}
