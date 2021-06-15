package com.grouphiking.project.a3chikingapp.Preferences;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.grouphiking.project.a3chikingapp.R;

import java.util.List;

public class LanguageSpinnerAdapter extends ArrayAdapter<String> {

    private LayoutInflater inflater;
    private int resource = 0;

    public LanguageSpinnerAdapter(@NonNull Context context, int resource ,@NonNull List<String> objects) {
        super(context, 0, objects);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return setViews(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return setViews(position,convertView,parent);
    }

    private View setViews(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        String language = getItem(position);
        View view = null;
        if(resource != 0) {
            view = convertView == null ? inflater.inflate(resource, parent, false) : convertView;
            TextView languageView= ((TextView)view.findViewById(R.id.LanguageView));
            if(language != null){
                languageView.setText(language);
            }else{
                throw new NullPointerException("No Languages");
            }
        }
        return  view;
    }

    public class ViewHolder{
        private TextView m_view;
    }
}
