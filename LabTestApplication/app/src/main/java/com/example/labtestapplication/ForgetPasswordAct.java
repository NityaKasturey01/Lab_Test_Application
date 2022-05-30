package com.example.labtestapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.labtestapplication.util.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ForgetPasswordAct extends AppCompatActivity {
    private EditText emailEdit, enterPassword, confrimPassword;
    private Button submit, update;
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private VolleySingleton singleton;
    private StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        emailEdit = findViewById(R.id.emailTextPassword);
        submit = findViewById(R.id.submitPassword);
        enterPassword = findViewById(R.id.enterPassword);
        confrimPassword = findViewById(R.id.confirmPassword);
        update = findViewById(R.id.updatePassword);
    }

    @Override
    protected void onResume() {
        super.onResume();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEdit.getText().toString().trim();
                if(TextUtils.isEmpty(email))
                {
                    emailEdit.setError("Please Fill the Field");
                }else{
                    forgotPassword(email);
                }
            }
        });
    }

    private void forgotPassword(String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.FORGOT_PASSWORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("INFO", response);
                        if(response.equals("success"))
                        {
                            Toast.makeText(getApplicationContext(), "Password has been sent to Your Email Id", Toast.LENGTH_SHORT).show();
                            emailEdit.setVisibility(View.GONE);
                            submit.setVisibility(View.GONE);
                            enterPassword.setVisibility(View.VISIBLE);
                            confrimPassword.setVisibility(View.VISIBLE);
                            update.setVisibility(View.VISIBLE);
                            update.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    updatePassword(email);
                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put(KEY_EMAIL, email);
                return map;
            }
        };
        singleton = VolleySingleton.getInstance(this);
        singleton.addToRequestQueue(stringRequest);
    }

    private void updatePassword(String email) {
        String password = enterPassword.getText().toString().trim();
        String confirm = confrimPassword.getText().toString().trim();
        if (password.equals(confirm)){
        stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_PASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("INFO", response);
                        if (response.equals("success")){
                            Toast.makeText(getApplicationContext(), "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginAct.class));
                        }else {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashmap = new HashMap<>();
                hashmap.put(KEY_EMAIL, email);
                hashmap.put(KEY_PASSWORD, password);
                return hashmap;
            }
        };
        singleton = VolleySingleton.getInstance(this);
        singleton.addToRequestQueue(stringRequest);
        }else
            confrimPassword.setError("Password does not Matches");
    }
}