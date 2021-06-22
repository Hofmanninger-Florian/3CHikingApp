package com.grouphiking.project.a3chikingapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.grouphiking.project.a3chikingapp.Activitys.MapActionActivity;
import com.grouphiking.project.a3chikingapp.Activitys.PlacesGetActivity;
import com.grouphiking.project.a3chikingapp.Data.Constants;
import com.grouphiking.project.a3chikingapp.Data.Trip;
import com.grouphiking.project.a3chikingapp.Data.Type;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;

import java.io.Serializable;

public class Add_Dialog_frag extends BottomSheetDialogFragment {

    //Main-Layout
    private View layout;

    //Views
    private TextInputEditText mt_tripName;
    private TextInputEditText mt_fromPlace;
    private TextInputEditText mt_toPlace;
    private TextInputLayout mt_fromPlaceLayout;
    private TextInputLayout mt_toPlaceLayout;
    private FrameLayout mt_add_Button;
    private RadioButton mt_Hike;
    private RadioButton mt_Bike;


    //Values
    private Location FROM = new Location(LocationManager.GPS_PROVIDER);
    private Location TO = new Location(LocationManager.GPS_PROVIDER);
    private Type type;
    private Trip trip;

    public Add_Dialog_frag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog)super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                ((BottomSheetDialog) dialog).getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setLayout(inflater.inflate(R.layout.fragment_add__dialog_frag, container, false));
        setListeners(setup());
        return layout;
    }

    public boolean setup() throws NullPointerException{
        if(layout != null){
            mt_add_Button = (FrameLayout) layout.findViewById(R.id.add_button_ok);
            mt_tripName = (TextInputEditText) layout.findViewById(R.id.add_edit_tripname);
            mt_fromPlace = (TextInputEditText) layout.findViewById(R.id.add_edit_fromtrip);
            mt_toPlace = (TextInputEditText) layout.findViewById(R.id.add_edit_tripto);
            mt_fromPlaceLayout = (TextInputLayout) layout.findViewById(R.id.add_from_layout);
            mt_toPlaceLayout = (TextInputLayout) layout.findViewById(R.id.add_to_layout);
            mt_Bike = (RadioButton) layout.findViewById(R.id.add_radioBike);
            mt_Hike = (RadioButton) layout.findViewById(R.id.add_radioHike);
            return true;
        }else{
            throw new NullPointerException("View not loaded");
        }
    }

    public void setListeners(boolean executed){
        mt_toPlaceLayout.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlaces(true);
            }
        });
        mt_fromPlaceLayout.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlaces(false);
            }
        });
        mt_add_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(executed);
            }
        });
        mt_Hike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = Type.HIKE;
            }
        });
        mt_Bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = Type.BIKE;
            }
        });
    }

    private void startPlaces(boolean to){
        Constants.setFEATURECHANGE_LISTENER(new Constants.onFeatureChange() {
            @Override
            public void onFeatureChanged(CarmenFeature feature) {
                Point point = (Point)feature.geometry();
                if(to){
                    TO.setLongitude(point.longitude());
                    TO.setLatitude(point.latitude());
                    mt_toPlace.setText(feature.placeName());
                }else{
                    FROM.setLatitude(point.latitude());
                    FROM.setLatitude(point.longitude());
                    mt_fromPlace.setText(feature.placeName());
                }
            }
        });
        Intent places = new Intent(getActivity().getApplicationContext(), PlacesGetActivity.class);
        startActivity(places);
    }



    //Adding
    private void add(boolean exe){
        Trip t = null;
        if(exe && canGoON()){
            t = new Trip(type, FROM, TO, mt_tripName.getText().toString());
            trip = t;
            Constants.getWorkingUser().getTrips().add(t);
            Constants.updateUser(layout);
            Constants.setLISTENERPOST(new Constants.postExecuteListner() {
                @Override
                public void onpostExecute() {
                    Log.d("Debug", "It is Updated");
                    launchNewAct(trip);
                }
            });
        }else if(!canGoON()){
            Snackbar.make(layout, R.string.add_item_NothingSelected, BaseTransientBottomBar.LENGTH_SHORT).show();
        }

    }

    private boolean canGoON(){
        return (mt_Bike.isChecked() || mt_Hike.isChecked()) && !mt_fromPlace.getText().equals("") && !mt_tripName.getText().equals("") && !mt_toPlace.getText().equals("");
    }

    private void launchNewAct(Trip t){
        this.dismiss();
        Intent actionAct = new Intent(getActivity().getApplicationContext(), MapActionActivity.class);
        actionAct.putExtra(Constants.TRIP, (Serializable) t);
        startActivity(actionAct);
    }
    public void setLayout(View layout) {
        this.layout = layout;
    }
}