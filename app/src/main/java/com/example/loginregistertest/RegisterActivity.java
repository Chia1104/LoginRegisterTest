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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText nameInput, emailInput, passwordInput2, cpasswordInput;
    String name, email, password, cpassword;
    Button login_btn, register_btn;
    private static final String URL_REGISTER = "https://dcardanalysislaravel-sedok4caqq-de.a.run.app/api/register";
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
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        if (message.equals("success")) {
                            String token = jsonObject.getString("token");
                            preferencesEditor.putString("token", token);
                            preferencesEditor.apply();
                            Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }, error -> {

            try {
                JSONObject jsonObject = new JSONObject((Map) error);
                String message = jsonObject.getString("message");
                JSONObject obj = new JSONObject(message);
                String email = obj.getString("email");
                String name = obj.getString("name");
                String password = obj.getString("password");
                String c_password = obj.getString("c_password");
                Toast.makeText(getApplicationContext(), "failed " + email + name + password + c_password, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }) {
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Accept-Encoding", "gzip, deflate, br");
                params.put("Accept", "application/json");
                params.put("Conection", "keep-alive");
                return params;
            }
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
        queue.add(stringRequest);
    }
}