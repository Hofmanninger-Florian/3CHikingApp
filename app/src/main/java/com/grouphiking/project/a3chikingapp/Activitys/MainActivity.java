package com.grouphiking.project.a3chikingapp.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.grouphiking.project.a3chikingapp.Adapters.Main_listAdapter;
import com.grouphiking.project.a3chikingapp.R;

public class MainActivity extends AppCompatActivity {

    //Views
    private ImageButton buttonSettings;
    private ListView list;
    private FloatingActionButton floatingButton;

    //Adapter
    private Main_listAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Setting stuff
        setViews();
        setListeners();
        setAdapters();
    }

    private void setViews() {
        buttonSettings = (ImageButton)findViewById(R.id.imageButtonSettings);
        list = (ListView)findViewById(R.id.listView_recent);
        floatingButton = (FloatingActionButton)findViewById(R.id.main_addnew_button);
    }

    private void setListeners(){
        if(buttonSettings == null || list == null || floatingButton == null)throw new NullPointerException();
    }

    private void setAdapters(){

    }
}