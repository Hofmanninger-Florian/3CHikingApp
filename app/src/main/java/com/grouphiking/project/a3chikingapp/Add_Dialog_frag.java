package com.grouphiking.project.a3chikingapp;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.grouphiking.project.a3chikingapp.Data.Trip;

import org.w3c.dom.Text;

public class Add_Dialog_frag extends BottomSheetDialogFragment {

    //Main-Layout
    private View layout;

    //Views
    private TextInputEditText mt_tripName;
    private TextInputEditText mt_fromPlace;
    private TextInputEditText mt_toPlace;
    private TextInputEditText mt_description;
    private FrameLayout mt_add_Button;

    //Values
    private Location FROM = new Location(LocationManager.GPS_PROVIDER);
    private Location TO = new Location(LocationManager.GPS_PROVIDER);

    public Add_Dialog_frag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
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
            mt_description = (TextInputEditText) layout.findViewById(R.id.add_edit_tripDesc);
            return true;
        }else{
            throw new NullPointerException("View not loaded");
        }
    }

    public void setListeners(boolean executed){
        mt_fromPlace.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                reloadFrom();
                return true;
            }
        });
        mt_toPlace.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                reloadto();
                return true;
            }
        });
        mt_add_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add(executed);
            }
        });
    }

    private void reloadto() {
        //API_Action
    }

    private void reloadFrom() {
        //API_Action
    }

    //Adding
    private void add(boolean exe){
        Trip t = null;
        if(exe){
            t = new Trip(FROM, TO, mt_tripName.getText().toString(), mt_description.getText().toString());
            //Store internal and on Firebase
        }
    }

    public void setLayout(View layout) {
        this.layout = layout;
    }
}