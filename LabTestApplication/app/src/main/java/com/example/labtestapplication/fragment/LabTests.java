package com.example.labtestapplication.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.labtestapplication.R;
import com.example.labtestapplication.VolleySingleton;
import com.example.labtestapplication.adapter.MyTestAdapter;
import com.example.labtestapplication.model.LabTestData;
import com.example.labtestapplication.util.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LabTests extends Fragment implements MyTestAdapter.OnItemClickListener, View.OnClickListener {


    private AppCompatImageView testImageCard;
    private AppCompatTextView testName, collectionType, parameters, cost;
    private ImageButton addToCart;
    private SearchView searchView;
    private AppCompatCheckBox checkHome, checkLab;
    private RecyclerView recyclerViewTests;
    private ArrayList<LabTestData> labTestsArrayList;
    private Bundle bundle;
    private MyTestAdapter myTestAdapter;
    View view;

    public LabTests() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_lab_tests, container, false);
        this.getActivity().setTitle("Lab Tests");
        searchView = view.findViewById(R.id.searchTest);
        checkLab = view.findViewById(R.id.checkLab);
        checkHome = view.findViewById(R.id.checkHome);
        recyclerViewTests = view.findViewById(R.id.recylerViewTests);
        recyclerViewTests.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewTests.setHasFixedSize(true);
        labTestsArrayList = new ArrayList<>();
        loadData();
        checkLab.setOnClickListener(this);
        checkHome.setOnClickListener(this);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        return view;
    }

    private void loadData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.LAB_TESTS,
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
                                LabTestData labTests = new LabTestData(jObj.getString("name"),
                                        jObj.getString("parameter"),
                                        jObj.getString("collection_type"),
                                        jObj.getString("field"),
                                        jObj.getString("cost"),
                                        jObj.getString("lab_location"),
                                        jObj.getString("lab_contact"),
                                        jObj.getString("description"),
                                        jObj.getString("age_limit"),
                                        jObj.getString("pre_req"),
                                        jObj.getString("report"),
                                        jObj.getString("image"));
                                labTests.setDname(jObj.getString("dname"));
                                //labTests.setAge_limit(jObj.getString("age_limit"));
                                labTestsArrayList.add(labTests);
                            }

                            myTestAdapter= new MyTestAdapter(getActivity(), labTestsArrayList);
                            recyclerViewTests.setAdapter(myTestAdapter);

                            myTestAdapter.setOnItemClickListener(new MyTestAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {

                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    JSONObject jObj = jsonArray.getJSONObject(position);
                                    //labTests.setAge_limit(jObj.getString("age_limit"));
                                    bundle = new Bundle();
                                    bundle.putString("image", jObj.getString("image"));
                                    bundle.putString("testname", jObj.getString("name"));
                                    bundle.putString("parameter", jObj.getString("parameter"));
                                    bundle.putString("collection", jObj.getString("collection_type"));
                                    bundle.putString("field", jObj.getString("field"));
                                    bundle.putString("cost", jObj.getString("cost"));
                                    bundle.putString("pathloc", jObj.getString("lab_location"));
                                    bundle.putString("pathcontact", jObj.getString("lab_contact"));
                                    bundle.putString("description", jObj.getString("description"));
                                    bundle.putString("age", jObj.getString("age_limit"));
                                    bundle.putString("prereq", jObj.getString("pre_req"));
                                    bundle.putString("report", jObj.getString("report"));
                                    bundle.putString("dname", jObj.getString("dname"));

                                }catch (JSONException e){
                                        Log.e("ERROR", e.getMessage());
                                    }

                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.addToBackStack(null);
                                    transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                    Fragment fragment = new LabTestDetails();
                                    fragment.setArguments(bundle);
                                    transaction.replace(getId(), fragment).commit();
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
        });
        VolleySingleton singleton = VolleySingleton.getInstance(getActivity());
        singleton.addToRequestQueue(stringRequest);
    }

    public void search(){
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("info", newText);
                myTestAdapter = new MyTestAdapter(getActivity(), labTestsArrayList);
                myTestAdapter.getFilter().filter(newText);
                recyclerViewTests.setAdapter(myTestAdapter);
                myTestAdapter.setOnItemClickListener(new MyTestAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {

                        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.LAB_TESTS,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try{
                                            JSONObject jsonObject = new JSONObject(response);
                                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                                            JSONObject jObj = jsonArray.getJSONObject(position);
                                            //labTests.setAge_limit(jObj.getString("age_limit"));
                                            bundle = new Bundle();
                                            bundle.putString("image", jObj.getString("image"));
                                            bundle.putString("testname", jObj.getString("name"));
                                            bundle.putString("parameter", jObj.getString("parameter"));
                                            bundle.putString("collection", jObj.getString("collection_type"));
                                            bundle.putString("field", jObj.getString("field"));
                                            bundle.putString("cost", jObj.getString("cost"));
                                            bundle.putString("pathloc", jObj.getString("lab_location"));
                                            bundle.putString("pathcontact", jObj.getString("lab_contact"));
                                            bundle.putString("description", jObj.getString("description"));
                                            bundle.putString("age", jObj.getString("age_limit"));
                                            bundle.putString("prereq", jObj.getString("pre_req"));
                                            bundle.putString("report", jObj.getString("report"));
                                            bundle.putString("dname", jObj.getString("dname"));
                                        }catch (JSONException e){
                                            Log.e("ERROR", e.getMessage());
                                        }

                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                        transaction.addToBackStack(null);
                                        transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                                        Fragment fragment = new LabTestDetails();
                                        fragment.setArguments(bundle);
                                        transaction.replace(getId(), fragment).commit();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        VolleySingleton singleton = VolleySingleton.getInstance(getActivity());
                        singleton.addToRequestQueue(stringRequest);
                    }
                    });
                return false;
            }
        });
    }




    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onClick(View v) {
        String seq = "";
        if (v==checkHome){
            if (checkLab.isChecked() && checkHome.isChecked())
                seq = "";
            else if (!checkLab.isChecked() && checkHome.isChecked())
                seq = "Home";
            else if (checkLab.isChecked() && !checkHome.isChecked())
                seq = "Lab";
            myTestAdapter = new MyTestAdapter(getActivity(), labTestsArrayList);
            Log.i("seq", seq);
            myTestAdapter.collectionFilter(seq);
        }
        if (v==checkLab){
            if (checkHome.isChecked() && checkLab.isChecked())
                seq = "";
            else if (!checkHome.isChecked() && checkLab.isChecked())
                seq = "Lab";
            else if (checkHome.isChecked() && !checkLab.isChecked())
                seq = "Home";
            myTestAdapter = new MyTestAdapter(getActivity(), labTestsArrayList);
            Log.i("seq", seq);
            myTestAdapter.collectionFilter(seq);
        }
        recyclerViewTests.setAdapter(myTestAdapter);
    }
}