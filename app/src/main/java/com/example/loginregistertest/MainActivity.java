package com.example.loginregistertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SharedPreferences mPreferences;
    String sharedprofFile = "com.protocoderspoint.registration_login";
    SharedPreferences.Editor preferencesEditor;
    String is_signed_in, ptoken, pname;
    Button logout_btn;
    TextView hello_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_main);

        logout_btn = findViewById(R.id.logout_btn);
        hello_txt = findViewById(R.id.hello_txt);

        mPreferences=getSharedPreferences(sharedprofFile,MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        ptoken = mPreferences.getString("token","null");
        pname = mPreferences.getString("name","null");
        hello_txt.setText("Hello " + pname);

        if(ptoken.equals("null"))
        {
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        }

        logout_btn.setOnClickListener(v -> {
            preferencesEditor.putString("issignedin", "false");
            preferencesEditor.putString("token", "null");
            preferencesEditor.putString("name", "null");
            preferencesEditor.apply();
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        });
    }
}