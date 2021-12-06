package com.example.loginregistertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    SharedPreferences mPreferences;
    String sharedprofFile = "com.protocoderspoint.registration_login";
    private static final String URL_DETAILS = "https://dcardanalysislaravel-sedok4caqq-de.a.run.app/api/details";
    SharedPreferences.Editor preferencesEditor;
    String ptoken;
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
        details();
        logout_btn.setOnClickListener(v -> {
            preferencesEditor.clear().commit();
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        });
    }

    public void details() {
        mPreferences = getSharedPreferences(sharedprofFile,MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        ptoken = mPreferences.getString("token","null");
        HttpsTrustManager.allowAllSSL();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DETAILS,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");

                        if (message.equals("success")) {
                            String name = jsonObject.getString("name");
                            hello_txt.setText("Hello " + name);
                        } else {
                            preferencesEditor.clear().commit();
                            Toast.makeText(getApplicationContext(), "請重新登入", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }

                    } catch (Exception e) {
                        preferencesEditor.clear().commit();
                        Toast.makeText(getApplicationContext(), "請重新登入", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                        e.printStackTrace();
                    }
                }, error -> {
            preferencesEditor.clear().commit();
            Toast.makeText(getApplicationContext(), "請重新登入", Toast.LENGTH_LONG).show();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Accept-Encoding", "gzip, deflate, br");
                params.put("Accept", "application/json");
                params.put("Conection", "keep-alive");
                params.put("Authorization", "Bearer " + ptoken);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}