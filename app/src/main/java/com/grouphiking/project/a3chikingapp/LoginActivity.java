package com.grouphiking.project.a3chikingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.grouphiking.project.a3chikingapp.Data.User;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    ArrayList<User> userList = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        Button loginButton = (Button)findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("in onClick");
                checkLogin();
            }
        });

    }

    private void checkLogin(){
        System.out.println("in method");
        EditText editText_username = findViewById(R.id.username_textField);
        EditText editText_password = findViewById(R.id.password_textField);
        if(editText_username.getText().equals("") || editText_password.getText().equals("")){
            Toast.makeText(this, "Invalid Password or Username", Toast.LENGTH_SHORT).show();
        } else {
            setContentView(R.layout.activity_main);
        }
    }
}