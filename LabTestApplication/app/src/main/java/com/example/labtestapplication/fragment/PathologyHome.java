package com.example.labtestapplication.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.labtestapplication.R;
import com.example.labtestapplication.VolleySingleton;
import com.example.labtestapplication.adapter.MyTestAdapter;
import com.example.labtestapplication.model.LabTestData;
import com.example.labtestapplication.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PathologyHome extends Fragment {

    View view;
    private TextView dnameHome, fullnameHome, pathologyContactHome, pathologyAddressHome;
    private AppCompatButton addTests;
    private RecyclerView recyclerViewPtests;
    private SharedPreferences preferences;
    private ArrayList<LabTestData> labTestsArrayList;
    private Bundle bundle;
    private MyTestAdapter myTestAdapter;
    private static final String KEY_DNAME = "dname";


    public PathologyHome() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pathology_home, container, false);
        dnameHome = view.findViewById(R.id.dnameHome);
        fullnameHome = view.findViewById(R.id.fullnameHome);
        pathologyContactHome = view.findViewById(R.id.pathologyContactHome);
        pathologyAddressHome = view.findViewById(R.id.pathologyAddressHome);
        addTests = view.findViewById(R.id.addPathologyTests);
        recyclerViewPtests = view.findViewById(R.id.recylerTestsPathology);
        preferences = getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        dnameHome.setText(preferences.getString("dname", ""));
        fullnameHome.setText(preferences.getString("fullname", ""));
        pathologyContactHome.setText(preferences.getString("contact", ""));
        pathologyAddressHome.setText(preferences.getString("address", ""));
        recyclerViewPtests.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewPtests.setHasFixedSize(true);
        labTestsArrayList = new ArrayList<>();
        loadData();
        addTests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddTest();
            }
        });
        return view;
    }

    private void openAddTest() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        Fragment fragment = new AddPathologyTest();
        transaction.replace(getId(), fragment).commit();
    }

    private void loadData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.LAB_TESTS_PATHOLOGY,
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
                            recyclerViewPtests.setAdapter(myTestAdapter);

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
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_DNAME, preferences.getString("dname", ""));
                return hashMap;
            }
        };
        VolleySingleton singleton = VolleySingleton.getInstance(getActivity());
        singleton.addToRequestQueue(stringRequest);
    }

}