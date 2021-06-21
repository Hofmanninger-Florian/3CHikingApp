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
    public static LanguageListener listener;

    private final String GERMAN = getContext().getString(R.string.sprache_German);
    private final String ENGLISH = getContext().getString(R.string.sprache_English);

    public LanguageSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWidgetLayoutResource(R.layout.pref_languagespinner);
    }

    public LanguageSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @Override
    public void onAttached() {
        if(Constants.LANGUAGE == Language.GERMAN){
            languages.addAll(Arrays.asList(GERMAN, ENGLISH));
        }else{
            languages.addAll(Arrays.asList(ENGLISH, GERMAN));
        }
        super.onAttached();
    }

    public void setListener(LanguageListener listener) {
        LanguageSpinner.listener = listener;
    }

    public LanguageListener getListener() {
        return listener;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(false);
        spinner = holder.itemView.findViewById(R.id.prefs_language_spinner);
        setAdapter();
        setTheListener();
    }

    private void setAdapter() {
        if(spinner != null) {
            LanguageSpinnerAdapter adapter = new LanguageSpinnerAdapter(getContext(), R.layout.spinner_item, languages);
            adapter.setDropDownViewResource(R.layout.spinner_item);
            spinner.setAdapter(adapter);
        }
    }

    private void setTheListener(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(Constants.SPINNER_ALREADY){
                    setSelectedAction(position, view);
                }else{
                    Constants.setSpinnerAlready(true);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Debug", "No Lanugage selected");
            }
        });
    }

    private void setSelectedAction(int position, View view){
        TextView text = (TextView) view.findViewById(R.id.LanguageView);
        if(text.getText().equals(GERMAN) && !Constants.LANGUAGE.equals(Language.GERMAN)){
            if(listener != null){
                listener.onLanguageChanges(Language.GERMAN);
            }
        }else if(text.getText().equals(ENGLISH) && !Constants.LANGUAGE.equals(Language.ENGLISH)){
            if(listener != null){
                listener.onLanguageChanges(Language.ENGLISH);
            }
        }
    }

    public interface LanguageListener{
        void onLanguageChanges(Language language);
    }

}
