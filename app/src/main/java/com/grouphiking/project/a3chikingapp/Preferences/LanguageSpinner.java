package com.grouphiking.project.a3chikingapp.Preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.grouphiking.project.a3chikingapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LanguageSpinner extends Preference {

    private List<String> languages = new ArrayList<String>();

    private Spinner spinner;

    public LanguageSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWidgetLayoutResource(R.layout.pref_languagespinner);
    }

    public LanguageSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    public void onAttached() {
        languages.addAll(Arrays.asList("German", "English"));
        super.onAttached();
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(false);
        spinner = holder.itemView.findViewById(R.id.prefs_language_spinner);
        setAdapter();
    }

    private void setAdapter() {
        if(spinner != null) {
            LanguageSpinnerAdapter adapter = new LanguageSpinnerAdapter(getContext(), R.layout.spinner_item, languages);
            adapter.setDropDownViewResource(R.layout.spinner_item);
            spinner.setAdapter(adapter);
        }
    }

}
