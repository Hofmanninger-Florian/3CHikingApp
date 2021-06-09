package com.grouphiking.project.a3chikingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.grouphiking.project.a3chikingapp.Data.User;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    ArrayList<User> userList = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        TextView textView = (TextView)findViewById(R.id.no_account);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("aaa");
                checkRegister();
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
        System.out.println("in checkLogin");
        EditText editText_username = (EditText)findViewById(R.id.username_textField);
        EditText editText_password = (EditText)findViewById(R.id.password_textField);

        if(editText_username.getText().toString().equals("") || editText_password.getText().toString().equals("")){
            Toast.makeText(this, "Invalid Password or Username", Toast.LENGTH_SHORT).show();
        } else {
            boolean b = false;
            for(User u : userList){
                if(editText_username.getText().toString().equals(u.getUsername()) && editText_password.getText().toString().equals(u.getPassword())){
                    b = true;
                }
            }
            if(b){
                //Intent intent = new Intent(this, MainActivity.class);
            }



        }
    }
    public void checkRegister(){
        //add
        /*String username = editText_username.getText().toString();
        String password = editText_password.getText().toString();
        userList.add(new User(username, password));*/
    }
}