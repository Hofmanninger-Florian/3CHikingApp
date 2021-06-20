package com.grouphiking.project.a3chikingapp.Preferences;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.grouphiking.project.a3chikingapp.Data.Constants;
import com.grouphiking.project.a3chikingapp.Data.Language;
import com.grouphiking.project.a3chikingapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LanguageSpinner extends Preference {

    private List<String> languages = new ArrayList<String>();
    private Spinner spinner;
    private LanguageListener listener;

    private final String GERMAN = getContext().getString(R.string.sprache_German);
    private final String ENGLISH = getContext().getString(R.string.sprache_English);

    public LanguageSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWidgetLayoutResource(R.layout.pref_languagespinner);
    }

    public LanguageSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setListener(LanguageListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttached() {
        languages.addAll(Arrays.asList(GERMAN, ENGLISH));
        super.onAttached();
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(false);
        spinner = holder.itemView.findViewById(R.id.prefs_language_spinner);
        setAdapter();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView) view.findViewById(R.id.LanguageView);
                if(text.getText().equals(GERMAN) && !Constants.LANGUAGE.equals(Language.GERMAN)){
                    listener.onLanguageChanges(Language.GERMAN);
                }else if(!Constants.LANGUAGE.equals(Language.ENGLISH)){
                    listener.onLanguageChanges(Language.ENGLISH);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Debug", "No Lanugage selected");
            }
        });
    }

    private void setAdapter() {
        if(spinner != null) {
            LanguageSpinnerAdapter adapter = new LanguageSpinnerAdapter(getContext(), R.layout.spinner_item, languages);
            adapter.setDropDownViewResource(R.layout.spinner_item);
            spinner.setAdapter(adapter);
        }
    }

    public interface LanguageListener{
        void onLanguageChanges(Language language);
    }

}
