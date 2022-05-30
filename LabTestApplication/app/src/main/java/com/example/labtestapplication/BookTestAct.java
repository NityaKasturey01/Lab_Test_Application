package com.example.labtestapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.labtestapplication.adapter.MyMemberAdapter;
import com.example.labtestapplication.model.Member;
import com.example.labtestapplication.util.Constants;
import com.google.android.material.slider.Slider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookTestAct extends AppCompatActivity {

    private AppCompatEditText dateEdit, pincode, city, areaUser, contactUser;
    private AppCompatSpinner spinner;
    private AppCompatTextView selectMembers, testName, addressLab, contactLab;
    private TextView priceTest;
    private RadioGroup collection;
    private AppCompatRadioButton homeRB, labRB;
    private AppCompatButton ConfrimBtn;
    private  String[] myarray;
    private String item = null;
    private LinearLayout homelayout, lablayout;
    private String collection_type = "Home";
    boolean[] selectedData;
    ArrayList<Integer> memberList = new ArrayList<>();
    String[] memberArray;
    private SharedPreferences preferences;
    private VolleySingleton singleton;
    private Bundle bundle;
    private static final String KEY_EMAIL = "email";
    private static final String KEY_TEST_NAME = "testname";
    private static final String KEY_MEMBERS = "members";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_COLL_TYPE = "collection_type";
    private static final String KEY_PINCODE = "pincode";
    private static final String KEY_CITY = "city";
    private static final String KEY_AREA = "area";
    private static final String KEY_CONTACT = "contact";
    private static final String KEY_PRICE = "price";
    private static final String KEY_DNAME = "dname";
    private AlertDialog.Builder adb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_test);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Book Test");
        preferences = getSharedPreferences("mypref", MODE_PRIVATE);
        init();
        loadData();
        myarray = getResources().getStringArray(R.array.time);
        selectMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        BookTestAct.this
                );
                builder.setTitle("Select Members for Test");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(memberArray, selectedData, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i, boolean isChecked) {
                        if (isChecked){
                            memberList.add(i);
                            Collections.sort(memberList);
                        }else {
                            memberList.remove(i);
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int j=0; j<memberList.size(); j++){
                            stringBuilder.append(memberArray[memberList.get(j)]);
                            priceTest.setText("Rs "+memberList.size()*Integer.parseInt(bundle.getString("cost")));
                            if (j!=memberList.size()-1){
                                stringBuilder.append(", ");
                            }
                        }
                        selectMembers.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int j=0; j<selectedData.length; j++){
                            selectedData[j] = false;
                            memberList.clear();
                            selectMembers.setText("");
                        }
                    }
                });
                builder.show();
            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(BookTestAct.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;
                        String date = year+"-"+month+"-"+dayOfMonth;
                        dateEdit.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        collection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = collection.getCheckedRadioButtonId();
                switch (id)
                {
                    case R.id.homeRadio: homelayout.setVisibility(View.VISIBLE);
                                        lablayout.setVisibility(View.INVISIBLE);
                                        collection_type="Home";
                                        break;
                    case R.id.labRadio:lablayout.setVisibility(View.VISIBLE);
                                    homelayout.setVisibility(View.INVISIBLE);
                                    collection_type="Lab";
                                    break;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        bundle = getIntent().getExtras();
        testName.setText(bundle.getString("testname"));
        addressLab.setText(bundle.getString("pathloc"));
        contactLab.setText(bundle.getString("pathcontact"));
        priceTest.setText("Rs "+memberList.size()*bundle.getInt("cost"));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item,myarray)
        {
            @Override
            public boolean isEnabled(int position) {
                //return super.isEnabled(position);
                if(position>0)
                    return true;
                else
                    return false;
            }
        };

        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0) {
                    item = (String) parent.getItemAtPosition(position);
                    //Toast.makeText(SpinnerActivity.this, item, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adb = new AlertDialog.Builder(this);

        ConfrimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyInput();
            }
        });
    }


    private void loadData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.MEMBERS_DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("INFO", response);
                        List<String> listm = new ArrayList<String>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jObj = jsonArray.getJSONObject(i);
                                /*Member memberData = new Member(jObj.getString("name"),
                                        jObj.getString("age"), jObj.getString("gender"));*/
                                listm.add(jObj.getString("name"));
                            }
                            String[] simpleArray = new String[ listm.size() ];
                            listm.toArray( simpleArray );
                            memberArray = new String[listm.size()];
                            memberArray = simpleArray;
                            selectedData = new boolean[memberArray.length];
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


    @SuppressLint("SetTextI18n")
    private void verifyInput() {
        if (collection_type.equals("Home")){
            String dateU = dateEdit.getText().toString().trim();
            String area = areaUser.getText().toString().trim();
            String pin = pincode.getText().toString().trim();
            String cityUser = city.getText().toString().trim();
            String contact = contactUser.getText().toString().trim();
            String members = selectMembers.getText().toString().trim();
            if (TextUtils.isEmpty(members))
                selectMembers.setError("Select Members");
            else if (TextUtils.isEmpty(pin))
                pincode.setError("Field is Empty");
            else if (!TextUtils.isDigitsOnly(pin))
                pincode.setError("Enter numbers only");
            else if (TextUtils.isEmpty(cityUser))
                city.setError("Field is Empty");
            else if (TextUtils.isDigitsOnly(cityUser))
                city.setError("Numbers are not allowed");
            else if (TextUtils.isEmpty(area))
                areaUser.setError("Field is Empty");
            else if (TextUtils.isEmpty(contact))
                contactUser.setError("Field is Empty");
            else if (!TextUtils.isDigitsOnly(contact))
                contactUser.setError("Enters numbers only");
            else if (contact.length()!=10)
                contactUser.setError("Number should be of 10 digits");
            else{
                addData(members, dateU, pin, cityUser, area, contact);
            }
        }
    }


    private void init() {
        selectMembers = findViewById(R.id.membersAdded);
        dateEdit = findViewById(R.id.dateUser);
        spinner = findViewById(R.id.spinnerTime);
        pincode = findViewById(R.id.pincodeUser);
        city = findViewById(R.id.cityStateUser);
        areaUser = findViewById(R.id.areaUser);
        contactUser = findViewById(R.id.contactSampler);
        testName = findViewById(R.id.testNameBook);
        addressLab = findViewById(R.id.areaLab);
        contactLab = findViewById(R.id.contactLab);
        priceTest = findViewById(R.id.priceTest);
        collection = findViewById(R.id.collectionradio);
        homeRB = findViewById(R.id.homeRadio);
        labRB = findViewById(R.id.labRadio);
        ConfrimBtn = findViewById(R.id.confirmBookBtn);
        homelayout = findViewById(R.id.homeLayout);
        lablayout = findViewById(R.id.labLayout);
    }

    private void addData(String members, String date, String pin, String city, String area, String contact) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.INSERT_BOOKINGS_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("INFO", response);
                        if (response.equals("success")) {
                            adb.setTitle("Booking Information");
                            adb.setIcon(R.drawable.checkbox_check_icon);
                            adb.setMessage("Booking Slot: "+date+" "+item);
                            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            adb.create().show();
                        } else {
                            adb.setTitle("Alert");
                            adb.setIcon(R.drawable.warning_icon);
                            adb.setMessage(response);
                            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            adb.create().show();
                        }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(KEY_EMAIL, preferences.getString("email",""));
                    hashMap.put(KEY_TEST_NAME, testName.getText().toString());
                    hashMap.put(KEY_MEMBERS, members);
                    hashMap.put(KEY_DATE, date);
                    hashMap.put(KEY_TIME, item);
                    hashMap.put(KEY_COLL_TYPE, collection_type);
                    hashMap.put(KEY_PINCODE, pin);
                    hashMap.put(KEY_CITY, city);
                    hashMap.put(KEY_AREA, area);
                    hashMap.put(KEY_CONTACT, contact);
                    hashMap.put(KEY_PRICE, String.valueOf(memberList.size()*Integer.parseInt(bundle.getString("cost"))));
                    hashMap.put(KEY_DNAME, bundle.getString("dname"));
                    return hashMap;
                }
            };
            singleton = VolleySingleton.getInstance(this);
            singleton.addToRequestQueue(stringRequest);
        }

}