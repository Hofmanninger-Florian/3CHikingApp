package com.grouphiking.project.a3chikingapp.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.grouphiking.project.a3chikingapp.Activitys.MainActivity;
import com.grouphiking.project.a3chikingapp.Data.User;
import com.grouphiking.project.a3chikingapp.R;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    public static ArrayList<User> userList = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userList.add(new User("testUser","test"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        TextView textView = (TextView) findViewById(R.id.no_account);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("aaa");
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
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

    private void checkLogin(){
        System.out.println("in method");
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.login_contextlayout);
        EditText editText_username = (EditText) findViewById(R.id.username_textField);
        EditText editText_password = (EditText) findViewById(R.id.password_textField);
        if(editText_username.getText().toString().equals("") || editText_password.getText().toString().equals("")
                || wrongUser(editText_username.getText().toString(), editText_password.getText().toString())){
            Snackbar.make(layout, R.string.login_snackbarLabel, Snackbar.LENGTH_SHORT).show();
        } else {
            boolean b = false;
            for(User u : userList){
                if(editText_username.getText().toString().equals(u.getUsername()) && editText_password.getText().toString().equals(u.getPassword())){
                    b = true;
                }
            }
            if(b){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }



    public boolean wrongUser(String name, String pwd){
        boolean result = false;
        for(User u : userList){
            if(name.equals(u.getUsername()) && pwd.equals(u.getPassword())){
                result = false;
            } else{
                result = true;
            }
        }
        return result;
    }
}