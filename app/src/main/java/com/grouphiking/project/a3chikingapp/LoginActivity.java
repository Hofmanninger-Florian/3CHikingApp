package com.grouphiking.project.a3chikingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                System.out.println("Hallo");
            }
        });

    }

    public void onClickListenerSettings1(View view) {
    }
}