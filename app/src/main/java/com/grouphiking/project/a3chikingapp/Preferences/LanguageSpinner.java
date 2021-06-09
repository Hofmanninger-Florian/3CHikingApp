package com.grouphiking.project.a3chikingapp.Preferences;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.Preference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LanguageSpinner extends Preference {

    private List<String> languages = new ArrayList<String>();

    public LanguageSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onAttached() {
        languages.addAll(Arrays.asList("German", "English"));
        super.onAttached();
    }

    public LanguageSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
}
