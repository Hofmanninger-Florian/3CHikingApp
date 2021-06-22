package com.grouphiking.project.a3chikingapp.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.grouphiking.project.a3chikingapp.Data.Trip;

public class Main_listAdapter extends ArrayAdapter<Trip> {


    public Main_listAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
