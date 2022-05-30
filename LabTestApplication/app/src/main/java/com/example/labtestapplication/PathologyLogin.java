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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.labtestapplication.model.Pathology;
import com.example.labtestapplication.model.User;
import com.example.labtestapplication.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PathologyLogin extends AppCompatActivity implements View.OnClickListener {

    private static AppCompatEditText dnamePL, passwordPL;
    private AppCompatButton loginBtnPL;
    private AppCompatTextView registerPL;
    private SharedPreferences preferences;
    private static final String KEY_DNAME = "dname";
    private static final String KEY_PASSWORD = "password";
    private StringRequest stringRequest;
    private VolleySingleton singleton;
    private ImageView userLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathology_login);
        setTitle("PATHOLOGY LOGIN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginBtnPL.setOnClickListener(this);
        registerPL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PathologyLogin.this, PathologyRegister.class));
            }
        });
        userLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PathologyLogin.this, LoginAct.class));
            }
        });
    }

    private void init() {
        dnamePL = findViewById(R.id.dnamePL);
        passwordPL = findViewById(R.id.passwordPL);
        loginBtnPL = findViewById(R.id.LoginBtnPL);
        registerPL = findViewById(R.id.registerPL);
        userLoginBtn = findViewById(R.id.userLoginImage);
    }

    @Override
    public void onClick(View v) {
        if(v == loginBtnPL){
            loginPathology();
        }
    }

    private void loginPathology() {
        Pathology pathology = new PathologyLogin.DAOClass().setData();
        if(pathology!=null) {
            stringRequest = new StringRequest(Request.Method.POST, Constants.LOGIN_PATHOLOGY,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.i("INFO", response);

                            if (!response.equals("error")) {
                                Toast.makeText(getApplicationContext(), "Pathology Login Successful", Toast.LENGTH_SHORT).show();
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("dname", obj.getString("dname"));
                                    editor.putString("fullname", obj.getString("fullname"));
                                    editor.putString("password", obj.getString("password"));
                                    editor.putString("contact", obj.getString("contact"));
                                    editor.putString("address", obj.getString("address"));
                                    editor.apply();
                                    editor.commit();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.i("tagconvertstr", "["+response+"]");
                                    Log.e("ERROR", e.getMessage());
                                }
                                startActivity(new Intent(getApplicationContext(), PathologyDetailsActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ERROR", error.getMessage());
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(KEY_DNAME, pathology.getDname());
                    hashMap.put(KEY_PASSWORD, pathology.getPassword());
                    return hashMap;
                }
            };
            singleton = VolleySingleton.getInstance(this);
            singleton.addToRequestQueue(stringRequest);
        }
    }

    public static class DAOClass
    {
        public Pathology setData()
        {
            String dname = dnamePL.getText().toString().trim();
            String password = passwordPL.getText().toString().trim();
            if(TextUtils.isEmpty(dname))
                dnamePL.setError("Please Fill the Field");
            else if(TextUtils.isEmpty(password))
                passwordPL.setError("Please Fill the Field");
            else if(password.length()<8)
                passwordPL.setError("Password is too Short");
            else{
                Pathology pathology = new Pathology();
                pathology.setDname(dname);
                pathology.setPassword(password);
                return pathology;
            }
            return null;
        }
    }
}