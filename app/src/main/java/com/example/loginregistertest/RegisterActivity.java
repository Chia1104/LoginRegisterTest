package com.example.loginregistertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText nameInput, emailInput, passwordInput2, cpasswordInput;
    String name, email, password, cpassword;
    Button login_btn, register_btn;
    private static final String URL_REGISTER = "https://fathomless-fjord-03751.herokuapp.com/api/register";
    SharedPreferences mPreferences;
    String sharedprofFile = "com.protocoderspoint.registration_login";
    SharedPreferences.Editor preferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput2 = findViewById(R.id.passwordInput2);
        cpasswordInput = findViewById(R.id.cpasswordInput);

        login_btn.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
        register_btn.setOnClickListener(v -> {
            name = nameInput.getText().toString().trim();
            email = emailInput.getText().toString().trim();
            password = passwordInput2.getText().toString().trim();
            cpassword = cpasswordInput.getText().toString().trim();
            register();
        });
    }

    public void register() {
        HttpsTrustManager.allowAllSSL();
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
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
                            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "register Failed " + message, Toast.LENGTH_LONG).show();
                            finish();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        if (e.getMessage() == null) {
                            Toast.makeText(getApplicationContext(), "註冊中", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "response Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, error -> {
            if (error.getMessage() == null) {
                Toast.makeText(getApplicationContext(), "註冊中", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "StringRequest Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("c_password", cpassword);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
        queue.add(stringRequest);
    }
}