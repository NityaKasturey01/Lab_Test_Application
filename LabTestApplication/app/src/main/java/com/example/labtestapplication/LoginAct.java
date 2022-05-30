package com.example.labtestapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.labtestapplication.model.User;
import com.example.labtestapplication.util.Constants;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LoginAct extends AppCompatActivity implements View.OnClickListener {

    private ImageView PathologyImage;
    private static AppCompatEditText emailLogin, passwordLogin;
    private static AppCompatButton signinBtn;
    private static AppCompatTextView registerTextView, forgetPassword;
    private SharedPreferences preferences;
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private StringRequest stringRequest;
    private VolleySingleton singleton;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("LOGIN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        init();

        GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton=(SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        PathologyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginAct.this, PathologyLogin.class));
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        Log.i("acc", account.getEmail());
        if (account!=null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        Intent intent = new Intent(LoginAct.this, MainActivity.class);
        //@SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", account.getDisplayName());
        editor.putString("password", "");
        editor.putString("email", account.getEmail());
        editor.putString("contact", "");
        editor.putString("image", String.valueOf(account.getPhotoUrl()));
        editor.apply();
        editor.commit();
        startActivity(intent);
        finish();
    }

    private void init() {
        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        signinBtn = findViewById(R.id.LoginLoginPage);
        registerTextView = findViewById(R.id.registerTextView);
        forgetPassword =findViewById(R.id.forgetPassword);
        PathologyImage = findViewById(R.id.pathologyLoginImage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        signinBtn.setOnClickListener(this);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgetPasswordAct.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == signinBtn){
            loginUser();
        }
    }

    private void loginUser() {
        User user = new DAOClass().setData();
        if(user!=null) {
            stringRequest = new StringRequest(Request.Method.POST, Constants.USER_DATA_URL,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.i("INFO", response);

                            if (!response.equals("error")) {
                                Toast.makeText(getApplicationContext(), "User Login Successful", Toast.LENGTH_SHORT).show();
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    //JSONArray product = obj.getJSONArray("username");
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("username", obj.getString("username"));
                                    editor.putString("password", obj.getString("password"));
                                    editor.putString("email", obj.getString("email"));
                                    editor.putString("contact", obj.getString("contact"));
                                    editor.putString("image", obj.getString("image"));
                                    editor.apply();
                                    editor.commit();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.i("tagconvertstr", "["+response+"]");
                                    Log.e("ERROR", e.getMessage());
                                }
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
                    hashMap.put(KEY_EMAIL, user.getEmail());
                    hashMap.put(KEY_PASSWORD, user.getPassword());
                    return hashMap;
                }
            };
            singleton = VolleySingleton.getInstance(this);
            singleton.addToRequestQueue(stringRequest);
        }
    }

    public static class DAOClass
    {
        public User setData()
        {
            String email = emailLogin.getText().toString().trim();
            String password = passwordLogin.getText().toString().trim();
            if(TextUtils.isEmpty(email))
                emailLogin.setError("Please Fill the Field");
            else if(TextUtils.isEmpty(password))
                passwordLogin.setError("Please Fill the Field");
            else if(password.length()<8)
                passwordLogin.setError("Password is too Short");
            else{
                User user = new User();
                user.setEmail(email);
                user.setPassword(password);
                return user;
            }
            return null;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("WARN", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }


}