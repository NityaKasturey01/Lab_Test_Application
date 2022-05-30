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
import com.example.labtestapplication.model.Pathology;
import com.example.labtestapplication.model.User;
import com.example.labtestapplication.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class PathologyRegister extends AppCompatActivity implements View.OnClickListener {

    private static AppCompatEditText dnamePR, fullnamePR, contactPR, passwordPR, addressPR;
    private AppCompatTextView loginPR;
    private AppCompatButton registerBtnPR;
    private StringRequest stringRequest;
    private VolleySingleton singleton;
    private SharedPreferences preferences;
    private static final String KEY_DNAME = "dname";
    private static final String KEY_FULL_NAME = "fullname";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_CONTACT = "contact";
    private static final String KEY_ADDRESS = "address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathology_register);
        setTitle("PATHOLOGY REGISTRATION");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        init();
        loginPR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PathologyRegister.this, PathologyLogin.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBtnPR.setOnClickListener(this);
    }

    private void init() {
        dnamePR = findViewById(R.id.dnamePR);
        fullnamePR = findViewById(R.id.fullnamePR);
        passwordPR = findViewById(R.id.passwordPR);
        contactPR = findViewById(R.id.contactPR);
        addressPR = findViewById(R.id.addressPR);
        loginPR = findViewById(R.id.loginPR);
        registerBtnPR = findViewById(R.id.registerBtnPR);
    }

    @Override
    public void onClick(View v) {
        if(v == registerBtnPR){
            registerPathology();
        }
    }

    private void registerPathology() {
        Pathology pathology = new PathologyRegister.DAOClass().setData();
        stringRequest = new StringRequest(Request.Method.POST, Constants.REGISTER_PATHOLOGY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("INFO", response);
                if(pathology!=null) {
                    if (response.equals("success")) {
                        Toast.makeText(getApplicationContext(), "Pathology Registered Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), PathologyLogin.class);
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
                hashMap.put(KEY_DNAME, pathology.getDname());
                hashMap.put(KEY_FULL_NAME, pathology.getFullname());
                hashMap.put(KEY_PASSWORD, pathology.getPassword());
                hashMap.put(KEY_CONTACT, pathology.getContact());
                hashMap.put(KEY_ADDRESS, pathology.getAddress());
                return hashMap;
            }
        };
        singleton = VolleySingleton.getInstance(this);
        singleton.addToRequestQueue(stringRequest);
    }

    public static class DAOClass {
        public Pathology setData()
        {
            String dname = dnamePR.getText().toString().trim();
            String fullname = fullnamePR.getText().toString().trim();
            String password = passwordPR.getText().toString().trim();
            String contact = contactPR.getText().toString().trim();
            String address = addressPR.getText().toString().trim();
            if (TextUtils.isEmpty(dname))
                dnamePR.setError("Please Fill the Field");
            else if (TextUtils.isEmpty(fullname))
                fullnamePR.setError("Please Fill the Field");
            else if (TextUtils.isEmpty(password))
                passwordPR.setError("Please Fill the Field");
            else if (password.length()<8)
                passwordPR.setError("Password is too short");
            else if (TextUtils.isEmpty(contact))
                contactPR.setError("Please Fill the Field");
            else if (contact.length()!=10)
                contactPR.setError("Incorrect Contact Number");
            else {
                Log.i("Info", dname+fullname+password+contact+address);
                return new Pathology(dname, fullname, password, contact, address);
            }
            return null;
        }
    }
}