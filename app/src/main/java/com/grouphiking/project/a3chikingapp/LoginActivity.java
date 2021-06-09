package com.grouphiking.project.a3chikingapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.grouphiking.project.a3chikingapp.Data.User;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    ArrayList<User> LogeduserList = new ArrayList<User>();

    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        getActionBar().hide();
        getSupportActionBar().hide();
        password = (EditText)findViewById(R.id.password_textField);
        username = (EditText)findViewById(R.id.username_textField);
        ((Button)findViewById(R.id.button_login)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }




}