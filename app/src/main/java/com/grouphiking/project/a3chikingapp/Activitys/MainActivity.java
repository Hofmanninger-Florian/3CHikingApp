package com.grouphiking.project.a3chikingapp.Activitys;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
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
import com.grouphiking.project.a3chikingapp.Data.Trip;
import com.grouphiking.project.a3chikingapp.Data.Type;
import com.grouphiking.project.a3chikingapp.Data.User;
import com.grouphiking.project.a3chikingapp.Preferences.MyContextWrapper;
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

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        super.attachBaseContext(MyContextWrapper.wrap(newBase,preferences.getString(newBase.getString(R.string.SpinnerKey), Constants.LANGUAGE.getLanguage())));
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

    @Override
    protected void onResume() {
        if(Constants.LANGUAGE_SET){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Constants.LANGUAGE_SET = false;
        }
        super.onResume();
    }

    //Settings start
    private void startSettings(){
        Intent settings = new Intent(this, SettingsActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startActivity(settings, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        }
    }

    //Go To Add Dialog
    private void OpenAddDialog(){
        Add_Dialog_frag fragment = new Add_Dialog_frag();
        fragment.show(getSupportFragmentManager(),
                fragment.getTag());
    }

    private void setAdapters(){
        User workingUser = Constants.getWorkingUser();
        Main_listAdapter listAdapter = new Main_listAdapter(this,R.layout.list_item_layout_bike,workingUser.getTrips());
        list.setAdapter(listAdapter);


    }

}