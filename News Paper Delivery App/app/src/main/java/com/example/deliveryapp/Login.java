package com.example.deliveryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    String UserID = "admin";
    String Password = "12345";
    EditText etUserID;
    EditText etPassword;
    TextView tvMessage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUserID = (EditText) findViewById(R.id.etUserID);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
    }

    public void btnLogin(View v)
    {
        if(etUserID.getText().toString().equalsIgnoreCase(UserID) && etPassword.getText().toString().equals(Password))
        {
            Intent i = new Intent(this,Home.class);
            startActivity(i);
        }
        else
        {
            tvMessage.setText("Invalid Username and Password");
        }
    }
}