package com.example.labtestapplication.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.labtestapplication.R;
import com.example.labtestapplication.VolleySingleton;
import com.example.labtestapplication.util.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class AddPathologyTest extends Fragment {

    View view;
    private AppCompatEditText testnameNew, parametersNew, fieldNew, descriptionNew, prereqNew, ageNew, reportsNew, costNew;
    private AppCompatCheckBox homeNew, labNew;
    private AppCompatImageView imageNew;
    private AppCompatButton addTestNew;
    private TextView colType;
    private String collection;
    private SharedPreferences preferences;
    private static final String KEY_TESTNAME = "name";
    private static final String KEY_PARAMETER = "parameter";
    private static final String KEY_COLLECTION = "collection_type";
    private static final String KEY_FIELD = "field";
    private static final String KEY_COST = "cost";
    private static final String KEY_LAB_LOC = "lab_location";
    private static final String KEY_LAB_CONTACT = "lab_contact";
    private static final String KEY_DESC = "description";
    private static final String KEY_AGE = "age_limit";
    private static final String KEY_PRE_REQ = "pre_req";
    private static final String KEY_REPORT = "report";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_DNAME = "dname";
    private AlertDialog.Builder adb;
    private static final int PICK_IMAGE_REQUEST = 1;
    public static final int RESULT_OK = -1;
    private Bitmap bitmap;
    private VolleySingleton singleton;

    public AddPathologyTest() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_pathology_test, container, false);
        getActivity().setTitle("Add Test");
        preferences = getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        testnameNew = view.findViewById(R.id.testNameNew);
        parametersNew = view.findViewById(R.id.parametersNew);
        fieldNew = view.findViewById(R.id.fieldNew);
        descriptionNew = view.findViewById(R.id.descriptionNew);
        prereqNew = view.findViewById(R.id.prereqNew);
        ageNew = view.findViewById(R.id.ageNew);
        reportsNew = view.findViewById(R.id.reportNew);
        costNew = view.findViewById(R.id.costNew);
        homeNew = view.findViewById(R.id.homeNew);
        labNew = view.findViewById(R.id.labNew);
        addTestNew = view.findViewById(R.id.addBtnNew);
        imageNew = view.findViewById(R.id.imageNew);
        colType = view.findViewById(R.id.colTypeNew);
        imageNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        adb = new AlertDialog.Builder(getActivity());

        addTestNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyInput();
            }
        });
        return view;
    }

    private void verifyInput() {
            String testname = testnameNew.getText().toString().trim();
            String parameter = parametersNew.getText().toString().trim();
            String field = fieldNew.getText().toString().trim();
            String desc = descriptionNew.getText().toString().trim();
            String prereq = prereqNew.getText().toString().trim();
            String age = ageNew.getText().toString().trim();
            String report = reportsNew.getText().toString().trim();
            String cost = costNew.getText().toString().trim();


        if (homeNew.isChecked())
            collection="Home";
        else if (labNew.isChecked())
            collection="Lab";
        else if (labNew.isChecked() && homeNew.isChecked())
            collection="Home/Lab";
        else if (!labNew.isChecked() && !homeNew.isChecked())
            colType.setError("Choose Collection Type");

            if (TextUtils.isEmpty(testname))
                testnameNew.setError("Field is Empty");
            else if (TextUtils.isEmpty(parameter))
                parametersNew.setError("Field is Empty");
            else if (!TextUtils.isDigitsOnly(parameter))
                parametersNew.setError("Enter numbers only");
            else if (TextUtils.isEmpty(field))
                fieldNew.setError("Field is Empty");
            else if (TextUtils.isDigitsOnly(field))
                fieldNew.setError("Numbers are not allowed");
            else if (TextUtils.isEmpty(desc))
                descriptionNew.setError("Field is Empty");
            else if (TextUtils.isEmpty(prereq))
                prereqNew.setError("Field is Empty");
            else if (TextUtils.isEmpty(age))
                ageNew.setError("Field is Empty");
            else if (!TextUtils.isDigitsOnly(age))
                ageNew.setError("Enters numbers only");
            else if (TextUtils.isEmpty(report))
                reportsNew.setError("Field is Empty");
            else if (TextUtils.isEmpty(cost))
                costNew.setError("Field is Empty");
            else if (!TextUtils.isDigitsOnly(cost))
                costNew.setError("Enters numbers only");
            else{
                addData(testname, parameter, collection, field, cost,
                        preferences.getString("fullname","") + ", "+ preferences.getString("address", ""),
                        preferences.getString("contact", ""), age, desc, prereq, report);
            }
    }

    private void addData(String testname, String parameter, String collection, String field, String cost, String address, String contact, String age, String desc, String prereq, String report) {
        String image = getStringImage(bitmap);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.ADD_TEST,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("INFO", response);
                if (response.equals("success")) {
                    adb.setTitle("Test Added Successfully");
                    adb.setIcon(R.drawable.checkbox_check_icon);
                    adb.setMessage("Test "+testname+" Added");
                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.addToBackStack(null);
                            transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            Fragment fragment = new PathologyHome();
                            transaction.replace(getId(), fragment).commit();
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
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_TESTNAME, testname);
                hashMap.put(KEY_PARAMETER, parameter);
                hashMap.put(KEY_COLLECTION, collection);
                hashMap.put(KEY_FIELD, field);
                hashMap.put(KEY_COST, cost);
                hashMap.put(KEY_LAB_LOC, address);
                hashMap.put(KEY_LAB_CONTACT, contact);
                hashMap.put(KEY_DESC, desc);
                hashMap.put(KEY_AGE, age);
                hashMap.put(KEY_PRE_REQ, prereq);
                hashMap.put(KEY_REPORT, report);
                hashMap.put(KEY_IMAGE, image);
                hashMap.put(KEY_DNAME, preferences.getString("dname",""));
                return hashMap;
            }
        };
        singleton = VolleySingleton.getInstance(getActivity());
        singleton.addToRequestQueue(stringRequest);
    }

    public void pickImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_REQUEST);
    }
    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST &&
                resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                imageNew.setVisibility(View.VISIBLE);
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageNew.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}