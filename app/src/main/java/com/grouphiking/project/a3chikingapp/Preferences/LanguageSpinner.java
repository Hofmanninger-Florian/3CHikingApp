package com.grouphiking.project.a3chikingapp.Preferences;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.Preference;

public class LanguageSpinner extends Preference {

    public LanguageSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LanguageSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
}
