package com.example.labtestapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.labtestapplication.adapter.MyMemberAdapter;
import com.example.labtestapplication.adapter.MyTestAdapter;
import com.example.labtestapplication.fragment.LabTestDetails;
import com.example.labtestapplication.model.LabTestData;
import com.example.labtestapplication.model.Member;
import com.example.labtestapplication.util.Constants;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddUserPopupAct extends AppCompatActivity {

    private AppCompatButton addMemberButton;
    private Button closePopUp, saveButton;
    private CircularImageView deleteMember;
    private RecyclerView recyclerViewMembers;
    private EditText memberNameEdit, memberAgeEdit, memberGenderEdit;
    private MyMemberAdapter memberAdapter;
    private ArrayList<Member> memberArrayList;
    private VolleySingleton singleton;
    private SharedPreferences preferences;
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "name";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_popup);
        getSupportActionBar().hide();
        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        init();

        recyclerViewMembers = findViewById(R.id.recylerViewMembers);
        recyclerViewMembers.setLayoutManager(new GridLayoutManager(this, 1));
        //recyclerViewMembers.setLayoutManager(new ListView(this, 2));
        recyclerViewMembers.setHasFixedSize(true);
        memberArrayList = new ArrayList<>();
        loadData();
        closePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewMembers.setVisibility(View.INVISIBLE);
                addMemberButton.setVisibility(View.INVISIBLE);
                addMemberButton.setEnabled(false);
                saveButton.setVisibility(View.VISIBLE);
                memberNameEdit.setVisibility(View.VISIBLE);
                memberAgeEdit.setVisibility(View.VISIBLE);
                memberGenderEdit.setVisibility(View.VISIBLE);
                saveButton.setEnabled(true);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
                recyclerViewMembers.setVisibility(View.VISIBLE);
                addMemberButton.setVisibility(View.VISIBLE);
                addMemberButton.setEnabled(true);
                memberNameEdit.setText("");
                memberAgeEdit.setText("");
                memberGenderEdit.setText("");
                saveButton.setVisibility(View.INVISIBLE);
                memberNameEdit.setVisibility(View.INVISIBLE);
                memberAgeEdit.setVisibility(View.INVISIBLE);
                memberGenderEdit.setVisibility(View.INVISIBLE);
                saveButton.setEnabled(false);
            }
        });


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.7));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x=0;
        params.y=-20;

        getWindow().setAttributes(params);
    }

    private void deleteMemberData(int position) {
        Member member = memberArrayList.get(position);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.DELETE_MEMBER_URL,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("INFO", response);
                    if (response.equals("success")) {
                        Toast.makeText(getApplicationContext(), "Member Data Deleted Successfully",
                                Toast.LENGTH_SHORT).show();
                        memberArrayList.clear();
                        loadData();
                    } else {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_EMAIL, preferences.getString("email",""));
                hashMap.put(KEY_NAME, member.getName());
                return hashMap;
            }
        };
        singleton = VolleySingleton.getInstance(this);
        singleton.addToRequestQueue(stringRequest);
    }

    private void addData() {
        String name = memberNameEdit.getText().toString().trim();
        String age = memberAgeEdit.getText().toString().trim();
        String gender = memberGenderEdit.getText().toString().trim();
        if (TextUtils.isEmpty(name))
            memberNameEdit.setError("Field is Empty");
        else if (TextUtils.isEmpty(age))
            memberAgeEdit.setError("Field is Empty");
        else if (TextUtils.isEmpty(gender))
            memberGenderEdit.setError("Field is Empty");
        else{
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.INSERT_MEMBER_URL,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("INFO", response);
                    if(name!=null && age!=null && gender!=null) {
                        if (response.equals("success")) {
                            Toast.makeText(getApplicationContext(), "Member Added Successfully",
                                    Toast.LENGTH_SHORT).show();
                            //recyclerViewMembers.removeAllViews();
                            memberArrayList.clear();
                            loadData();
                        } else {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        }
                    } }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(KEY_EMAIL, preferences.getString("email",""));
                    hashMap.put(KEY_NAME, name);
                    hashMap.put(KEY_AGE, age);
                    hashMap.put(KEY_GENDER, gender);
                    return hashMap;
                }
            };
            singleton = VolleySingleton.getInstance(this);
            singleton.addToRequestQueue(stringRequest);
        }
    }

    private void init() {
        closePopUp = findViewById(R.id.closePopUp);
        recyclerViewMembers = findViewById(R.id.recylerViewMembers);
        addMemberButton = findViewById(R.id.addMemberButton);
        saveButton = findViewById(R.id.saveMemberButton);
        memberNameEdit = findViewById(R.id.memberNameEdit);
        memberAgeEdit = findViewById(R.id.memberAgeEdit);
        memberGenderEdit = findViewById(R.id.memberGenderEdit);
        deleteMember = findViewById(R.id.deleteMember);
    }

    private void loadData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MEMBERS_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("INFO", response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jObj = jsonArray.getJSONObject(i);
                                Member memberData = new Member(jObj.getString("name"),
                                        jObj.getString("age"), jObj.getString("gender"));
                                memberArrayList.add(memberData);
                            }
                            memberAdapter= new MyMemberAdapter(getApplicationContext(), memberArrayList);
                            recyclerViewMembers.setAdapter(memberAdapter);
                            memberAdapter.setOnItemClickListener(new MyMemberAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    deleteMember.setVisibility(View.VISIBLE);
                                    deleteMember.setEnabled(true);
                                    deleteMember.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            deleteMemberData(position);
                                        }
                                    });
                                }
                            });
                        }catch (JSONException e){
                            Log.e("ERROR", e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.getMessage());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_EMAIL, preferences.getString("email", ""));
                return hashMap;
            }
        };
        singleton = VolleySingleton.getInstance(this);
        singleton.addToRequestQueue(stringRequest);
    }

}