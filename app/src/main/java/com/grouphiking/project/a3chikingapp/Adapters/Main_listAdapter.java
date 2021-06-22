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
import com.grouphiking.project.a3chikingapp.R;

import java.util.ArrayList;
import java.util.List;

public class Main_listAdapter extends ArrayAdapter<Trip> {

    public static final String TAG = "TripListAdapter";
    private Context context;
    private int resource;
    ArrayList<Trip> tripList;
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
        Trip trip = new Trip(type,to,from,name);
        if(trip.getType().equals(Type.BIKE)){
            resource = R.layout.list_item_layout_bike;
        } else {
            resource = R.layout.list_item_layout_bike;
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(resource, parent, false);
        TextView textView = (TextView)view.findViewById(R.id.textView_item);
        textView.setText(name);

        return view;
    }
}
