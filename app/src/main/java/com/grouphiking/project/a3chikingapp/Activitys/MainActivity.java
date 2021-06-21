package com.grouphiking.project.a3chikingapp.Activitys;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.grouphiking.project.a3chikingapp.Adapters.Main_listAdapter;
import com.grouphiking.project.a3chikingapp.Add_Dialog_frag;
import com.grouphiking.project.a3chikingapp.Data.Constants;
import com.grouphiking.project.a3chikingapp.Preferences.SettingsActivity;
import com.grouphiking.project.a3chikingapp.R;

public class MainActivity extends AppCompatActivity {

    //Main Activity Variables
    private ImageButton buttonNewDialog;
    private ImageButton buttonSettings;

    //Views
    private ListView list;
    private FloatingActionButton floatingButton;

    //Adapter
    private Main_listAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.setTransition(this, new LinearInterpolator());
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //Setting stuff
        setViews();
        setListeners();
        setAdapters();




    }

    private void setViews() {
        buttonSettings = (ImageButton)findViewById(R.id.imageButtonSettings);
        buttonNewDialog = (ImageButton)findViewById(R.id.main_addnew_button);

        list = (ListView)findViewById(R.id.listView_recent);
        floatingButton = (FloatingActionButton)findViewById(R.id.main_addnew_button);
    }

    private void setListeners(){
        if(buttonSettings == null || list == null || floatingButton == null)throw new NullPointerException();
        buttonSettings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startSettings();
            }
        });

        buttonNewDialog.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                OpenAddDialog();
            }
        });

    }


    //Settings start
    private void startSettings(){
        Intent settings = new Intent(this, SettingsActivity.class);
        startActivity(settings, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    //Go To Add Dialog
    private void OpenAddDialog(){
        Add_Dialog_frag fragment = new Add_Dialog_frag();
        fragment.show(getSupportFragmentManager(),
                fragment.getTag());
    }

    private void setAdapters(){

    }


}