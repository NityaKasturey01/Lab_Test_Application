package com.example.labtestapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.labtestapplication.fragment.Explore;
import com.example.labtestapplication.fragment.HomePage;
import com.example.labtestapplication.fragment.LabTests;
import com.example.labtestapplication.fragment.MyBookings;
import com.example.labtestapplication.fragment.PathologyBookings;
import com.example.labtestapplication.fragment.PathologyHome;
import com.example.labtestapplication.fragment.PathologyReports;
import com.example.labtestapplication.fragment.Profile;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class PathologyDetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathology_details);
//        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //setTitle("PATHOLOGY HOME");
        preferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        setTitle(preferences.getString("dname", "")+" Pathology");
        ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#FF018786"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNaviPathology);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        Fragment fragment;
        if (id == R.id.homePathology){
            fragment = new PathologyHome();
            transaction.replace(R.id.nav_host_fragment_Path, fragment).commit();
        }
        else if (id == R.id.bookPathology){
            fragment = new PathologyBookings();
            transaction.replace(R.id.nav_host_fragment_Path, fragment).commit();
        }
        else if (id == R.id.reportsPathology){
            fragment = new PathologyReports();
            transaction.replace(R.id.nav_host_fragment_Path, fragment).commit();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logoutpath, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(id==R.id.logout_path) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("dname");
            editor.remove("fullname");
            editor.remove("password");
            editor.remove("contact");
            editor.remove("address");
            editor.clear();
            editor.apply();
            editor.commit();
            startActivity(new Intent(getApplicationContext(), PathologyLogin.class));
        }
        return true;
    }
}