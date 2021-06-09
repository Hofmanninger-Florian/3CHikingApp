package com.grouphiking.project.a3chikingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<User> userList = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        Button loginButton = (Button)findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void onClickListenerSettings1(View view) {
    }

    public void checkLogin(){
        EditText editText_username = findViewById(R.id.username_textField);
        EditText editText_password = findViewById(R.id.password_textField);
        if(editText_username.getText().equals("") || editText_password.getText().equals("")){
            finish();
        } else{
            setContentView(R.layout.activity_main);
        }
    }
}