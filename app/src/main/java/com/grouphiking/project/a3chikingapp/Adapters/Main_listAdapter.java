package com.grouphiking.project.a3chikingapp.Adapters;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.grouphiking.project.a3chikingapp.Data.Trip;
import com.grouphiking.project.a3chikingapp.Data.Type;

import java.util.ArrayList;
import java.util.List;

public class Main_listAdapter extends ArrayAdapter<Trip> {

    public static final String TAG = "TripListAdapter";
    private Context context;
    private int resource;

    public Main_listAdapter(@NonNull Context context, int resource, ArrayList<Trip> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getNAME();
        Location from = getItem(position).getFROM();
        Location to =  getItem(position).getTO();
        Type type = getItem(position).getType();

        Trip trip = new Trip(to,from,name);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);


    }
}
