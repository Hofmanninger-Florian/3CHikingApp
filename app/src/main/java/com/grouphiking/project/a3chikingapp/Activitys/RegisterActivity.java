package com.grouphiking.project.a3chikingapp.Activitys;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.grouphiking.project.a3chikingapp.Data.Constants;
import com.grouphiking.project.a3chikingapp.Data.Trip;
import com.grouphiking.project.a3chikingapp.Data.User;
import com.grouphiking.project.a3chikingapp.Preferences.MyContextWrapper;
import com.grouphiking.project.a3chikingapp.R;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {


    private String username;
    private String password;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Constants.setTransition(this, new AnticipateInterpolator());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        supportStartPostponedEnterTransition();
        Button registerButton = (Button)findViewById(R.id.button_register);
        TextView textView = (TextView)findViewById(R.id.already_have_account);
        RegisterActivity act = this;
        textView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(act).toBundle());
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRegister();
            }
        });
    }

    //On-Methodes

    @Override
    protected void onResume() {
        if(Constants.isLoggedIn()){
            intentMain();
        }
        super.onResume();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        super.attachBaseContext(MyContextWrapper.wrap(newBase,preferences.getString(newBase.getString(R.string.SpinnerKey), Constants.LANGUAGE.getLanguage())));
    }

    public void checkRegister(){
        //Values
        layout = (RelativeLayout)findViewById(R.id.register_contextlayout);
        EditText editText_username_register = (EditText) findViewById(R.id.username_textField_register);
        EditText editText_password_register = (EditText) findViewById(R.id.password_textField_register);
        EditText editText_reenter_password = (EditText) findViewById(R.id.reenter_password_textField);

        if(editText_username_register.getText().toString().equals("") || editText_password_register.getText().toString().equals("") || editText_reenter_password.getText().toString().equals("")) {
            Snackbar.make(layout, R.string.login_snackbarLabel, Snackbar.LENGTH_SHORT).show();
        } else if(!editText_password_register.getText().toString().equals(editText_reenter_password.getText().toString())){
            Snackbar.make(layout, R.string.register_snackbarLabel, Snackbar.LENGTH_SHORT).show();
        } else {
            username = editText_username_register.getText().toString();
            password = editText_password_register.getText().toString();
            Constants.getUsersGet(username, layout, this, null);
        }
    }

    public void proofResults(){
        if(Constants.getWorkingUser() == null){
            Constants.WORKING_USER = new User(username, password, new ArrayList<Trip>(), Constants.LANGUAGE, Constants.MODE);
            Constants.addUser(Constants.WORKING_USER, layout);
            intentMain();
        }else{
            Snackbar.make(layout, R.string.firebase_user_Already, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void intentMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        Constants.setLoggedIn(true);
    }
}