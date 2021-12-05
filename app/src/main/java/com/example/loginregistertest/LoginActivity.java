package com.example.loginregistertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText accountInput, passwordInput;
    Button login_btn, register_btn;
    String email, password, is_signed_in;
    private static final String URL_LOGIN = "https://dcardanalysislaravel-sedok4caqq-de.a.run.app/api/login";
    SharedPreferences mPreferences;
    String sharedprofFile = "com.protocoderspoint.registration_login";
    SharedPreferences.Editor preferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_login);

        mPreferences=getSharedPreferences(sharedprofFile,MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();

        accountInput = findViewById(R.id.accountInput);
        passwordInput = findViewById(R.id.passwordInput);
        login_btn = findViewById(R.id.login_btn);
        register_btn = findViewById(R.id.register_btn);

        login_btn.setOnClickListener(v -> {
            email = accountInput.getText().toString().trim();
            password = passwordInput.getText().toString().trim();
            login();
        });

        register_btn.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    public void login() {
        HttpsTrustManager.allowAllSSL();
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        if (message != "Unauthorised") {
                            String name = jsonObject.getString("name");
                            String token = jsonObject.getString("token");
                            preferencesEditor.putString("issignedin", "true");
                            preferencesEditor.putString("token", token);
                            preferencesEditor.putString("name", name);
                            preferencesEditor.apply();
                            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Login Failed " + message, Toast.LENGTH_LONG).show();
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (e.getMessage() == null) {
                            Toast.makeText(getApplicationContext(), "登入中", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "response Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, error -> {
            if (error.getMessage() == null) {
                Toast.makeText(getApplicationContext(), "登入中", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "StringRequest Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
        queue.add(stringRequest);
    }
}