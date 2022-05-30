package com.example.labtestapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.labtestapplication.fragment.Explore;
import com.example.labtestapplication.fragment.HomePage;
import com.example.labtestapplication.fragment.LabTests;
import com.example.labtestapplication.fragment.MyBookings;
import com.example.labtestapplication.fragment.PathologyReports;
import com.example.labtestapplication.fragment.Profile;
import com.example.labtestapplication.fragment.UserReports;
import com.example.labtestapplication.model.User;
import com.example.labtestapplication.util.Constants;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView usernameView, emailView;
    private ImageView imageNav;
    private Button loginBtnNav;
    private NavigationView navigationView;
    private SharedPreferences preferences;
    private VolleySingleton singleton;
    private StringRequest stringRequest;
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private GoogleSignInClient mGoogleSignInClient;
    private AppBarConfiguration mAppBarConfiguration;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("HOME");
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#FF018786"));
       toolbar.getOverflowIcon().setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        usernameView = findViewById(R.id.usernameNav);
        emailView = findViewById(R.id.emailNav);
        loginBtnNav = findViewById(R.id.loginBtnNav);
        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this::onConnectionFailed)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }



    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onResume() {
        super.onResume();
        Bitmap bitmap = null;
        //loadProducts();
        usernameView = navigationView.getHeaderView(0).findViewById(R.id.usernameNav);
        emailView = navigationView.getHeaderView(0).findViewById(R.id.emailNav);
        imageNav = navigationView.getHeaderView(0).findViewById(R.id.imageNav1);

        usernameView.setText(preferences.getString("username", ""));
        emailView.setText(preferences.getString("email", ""));

        if(preferences.getString("image", "")!=null && preferences.getString("image", "")!=""){
            Picasso.with(MainActivity.this).load(preferences.getString("image", "")).placeholder(R.drawable.userabc).into(imageNav);
            //new DownloadImage().execute();
        }else{
            Log.i("INFO", "no img");
        }
    }

    private void loadProducts() {
        stringRequest = new StringRequest(Request.Method.DEPRECATED_GET_OR_POST, Constants.USER_DATA_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("INFO", response);
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject obj = new JSONObject(response);
                            //JSONArray product = obj.getJSONArray("username");
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("username", obj.getString("username"));
                            editor.putString("contact", obj.getString("contact"));
                            editor.putString("image", obj.getString("image"));
                            editor.apply();
                            editor.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("tagconvertstr", "["+response+"]");
                            Log.e("ERROR", e.getMessage());
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
                hashMap.put(KEY_EMAIL, preferences.getString("email", ""));
                hashMap.put(KEY_PASSWORD, preferences.getString("password", ""));
                return hashMap;
            }
        };
        singleton = VolleySingleton.getInstance(this);
        singleton.addToRequestQueue(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(id==R.id.notificationNavButton){
            Toast.makeText(getApplicationContext(), "notification", Toast.LENGTH_SHORT).show();
        }else if (id==R.id.profileNavButton){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            Fragment fragment;
            fragment = new Profile();
            transaction.replace(R.id.nav_host_fragment, fragment).commit();
            navigationView.setCheckedItem(R.id.profileBot);
        }
        return true;
    }

@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        Fragment fragment;
        if (id ==  R.id.nav_Bookings){
            fragment = new MyBookings();
            transaction.replace(R.id.nav_host_fragment, fragment).commit();
        }else if (id == R.id.nav_Reports){
            fragment = new UserReports();
            transaction.replace(R.id.nav_host_fragment, fragment).commit();
        }else if (id == R.id.nav_editProfile){
            intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_logout){
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("email");
            editor.remove("username");
            editor.remove("image");
            editor.remove("contact");
            editor.remove("password");
            editor.clear();
            editor.apply();
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NotNull Status status) {
                            if (status.isSuccess()){
                                startActivity(new Intent(getApplicationContext(), LoginAct.class));
                            }else{
                                Toast.makeText(getApplicationContext(),"Session not close",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            startActivity(new Intent(getApplicationContext(), LoginAct.class));
            finish();
        }

        else if (id == R.id.homeBot){
            fragment = new HomePage();
            transaction.replace(R.id.nav_host_fragment, fragment).commit();
        }
        else if (id == R.id.testBot){
            fragment = new LabTests();
            transaction.replace(R.id.nav_host_fragment, fragment).commit();
        }
        else if (id == R.id.exploreBot){
            fragment = new Explore();
            transaction.replace(R.id.nav_host_fragment, fragment).commit();
        }
        else if (id == R.id.profileBot){
            fragment = new Profile();
            transaction.replace(R.id.nav_host_fragment, fragment).commit();
        }
        return true;
    }


    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


}