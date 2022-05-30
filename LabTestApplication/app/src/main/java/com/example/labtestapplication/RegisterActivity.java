package com.example.labtestapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.labtestapplication.model.User;
import com.example.labtestapplication.util.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static AppCompatEditText usernameRegister, emailRegister, passwordRegister, contactRegister;
    private static AppCompatButton signupBtn;
    private static AppCompatTextView loginRegister;
    private StringRequest stringRequest;
    private VolleySingleton singleton;
    private SharedPreferences preferences;
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_CONTACT = "contact";
    private static final String KEY_IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("REGISTER");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        signupBtn.setOnClickListener(this);
    }

    private void init() {
        usernameRegister = findViewById(R.id.usernameRegister);
        emailRegister = findViewById(R.id.emailRegister);
        passwordRegister = findViewById(R.id.passwordRegister);
        contactRegister = findViewById(R.id.contactRegister);
        loginRegister = findViewById(R.id.loginTextView);
        signupBtn = findViewById(R.id.registerBtn);
    }

    @Override
    public void onClick(View v) {
        if(v == signupBtn){
            registerUser();
        }
    }

    private void registerUser() {
        User user = new DAOClass().setData();
        stringRequest = new StringRequest(Request.Method.POST, Constants.REG_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("INFO", response);
                if(user!=null) {
                    if (response.equals("success")) {
                        Toast.makeText(getApplicationContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginAct.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_USERNAME, user.getUsername());
                hashMap.put(KEY_EMAIL, user.getEmail());
                hashMap.put(KEY_PASSWORD, user.getPassword());
                hashMap.put(KEY_CONTACT, user.getContact());
                return hashMap;
            }
        };
        singleton = VolleySingleton.getInstance(this);
        singleton.addToRequestQueue(stringRequest);
    }

    public static class DAOClass {
        public User setData()
        {
            String uname = usernameRegister.getText().toString().trim();
            String email = emailRegister.getText().toString().trim();
            String password = passwordRegister.getText().toString().trim();
            String contact = contactRegister.getText().toString().trim();
            if (TextUtils.isEmpty(uname))
                usernameRegister.setError("Please Fill the Field");
            else if (TextUtils.isDigitsOnly(uname))
                usernameRegister.setError("Numbers are not Allowed");
            else if (TextUtils.isEmpty(email))
                emailRegister.setError("Please Fill the Field");
            else if (TextUtils.isEmpty(password))
                passwordRegister.setError("Please Fill the Field");
            else if (password.length()<8)
                passwordRegister.setError("Password is too short");
            else if (TextUtils.isEmpty(contact))
                contactRegister.setError("Please Fill the Field");
            else if (!TextUtils.isDigitsOnly(contact))
                contactRegister.setError("Numbers are only Allowed");
            else if (contact.length()!=10)
                contactRegister.setError("Incorrect Contact Number");
            else {

                return new User(uname, email, password, contact);
            }
            return null;
        }
    }
}