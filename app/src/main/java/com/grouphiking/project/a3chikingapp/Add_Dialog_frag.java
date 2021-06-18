package com.grouphiking.project.a3chikingapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.grouphiking.project.a3chikingapp.Activitys.MapActionActivity;
import com.grouphiking.project.a3chikingapp.Data.Trip;

import org.w3c.dom.Text;

public class Add_Dialog_frag extends BottomSheetDialogFragment {

    //Main-Layout
    private View layout;

    //Views
    private TextInputEditText mt_tripName;
    private TextInputEditText mt_fromPlace;
    private TextInputEditText mt_toPlace;
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
            //t = new Trip(FROM, TO, mt_tripName.getText().toString());
            //Store internal and on Firebase
        }
        launchNewAct();

    }

    private void launchNewAct(){
        this.dismiss();
        Intent actionAct = new Intent(getActivity().getApplicationContext(), MapActionActivity.class);
        startActivity(actionAct);
    }

    public void setLayout(View layout) {
        this.layout = layout;
    }
}