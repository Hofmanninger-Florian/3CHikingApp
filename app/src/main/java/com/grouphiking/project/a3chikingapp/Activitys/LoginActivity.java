package com.grouphiking.project.a3chikingapp.Activitys;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import android.app.ActivityOptions;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.grouphiking.project.a3chikingapp.Activitys.MainActivity;
import com.grouphiking.project.a3chikingapp.Data.Constants;
import com.grouphiking.project.a3chikingapp.Data.Trip;
import com.grouphiking.project.a3chikingapp.Data.User;
import com.grouphiking.project.a3chikingapp.Preferences.MyContextWrapper;
import com.grouphiking.project.a3chikingapp.R;
import com.grouphiking.project.a3chikingapp.Services.MyService;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    public static ArrayList<User> userList = new ArrayList<User>();
    public static final String CHANNEL_ID = "exampleServiceChannel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.setTransition(this, new AccelerateInterpolator());
        setContentView(R.layout.login_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        TextView textView = (TextView) findViewById(R.id.no_account);
        createNotificationChannel();
        LoginActivity act = this;
        startService();

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("aaa");
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(act).toBundle());
            }
        });
        Button loginButton = (Button)findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("in onClick");
                checkLogin();
            }
        });

    }

    //On-Functions


    @Override
    protected void onResume() {
        super.onResume();
        if(Constants.loggedIn) {
            intentMain();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        super.attachBaseContext(MyContextWrapper.wrap(newBase,preferences.getString(newBase.getString(R.string.SpinnerKey), Constants.LANGUAGE.getLanguage())));
    }

    private void checkLogin(){
        System.out.println("in method");
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.login_contextlayout);
        EditText editText_username = (EditText) findViewById(R.id.username_textField);
        EditText editText_password = (EditText) findViewById(R.id.password_textField);
        String username = editText_username.getText().toString();
        if(editText_username.getText().toString().equals("") || editText_password.getText().toString().equals("")){
            Snackbar.make(layout, R.string.login_snackbarLabel, Snackbar.LENGTH_SHORT).show();
        }else {
            Constants.getUsersGet(username, layout, null, this);
        }
    }

    public void proofResults(){
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.login_contextlayout);
        if(Constants.getWorkingUser() != null){
            intentMain();
        }else{
            Snackbar.make(layout, R.string.firebase_user_notAlready, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void intentMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        ActivityCompat.finishAffinity(this);
        Constants.setLoggedIn(true);
    }

    public void stopService(){
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    public void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID,"Example Service Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public void startService(){
        Intent intent = new Intent(this, MyService.class);
        String msg = "App l??uft gerade";
        intent.putExtra("msg", msg);
        startService(intent);
    }

}