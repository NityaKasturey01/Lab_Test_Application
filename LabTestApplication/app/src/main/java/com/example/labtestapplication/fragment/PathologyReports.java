package com.example.labtestapplication.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.labtestapplication.R;
import com.example.labtestapplication.VolleySingleton;
import com.example.labtestapplication.adapter.MyBookingsAdapter;
import com.example.labtestapplication.adapter.MyReportsAdapter;
import com.example.labtestapplication.adapter.MyTestAdapter;
import com.example.labtestapplication.model.Bookings;
import com.example.labtestapplication.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PathologyReports extends Fragment implements MyReportsAdapter.OnItemClickListener {

    private View view;
    private SearchView searchViewReports;
    private RecyclerView recyclerViewReports;
    private ArrayList<Bookings> reportsArrayList;
    private MyReportsAdapter myReportsAdapter;
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DNAME = "dname";
    private SharedPreferences preferences;
    private Bundle bundle;

    public PathologyReports() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pathology_reports, container, false);
        searchViewReports = view.findViewById(R.id.searchReports);
        recyclerViewReports = view.findViewById(R.id.recylerViewReports);
        recyclerViewReports.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerViewReports.setHasFixedSize(true);
        reportsArrayList = new ArrayList<>();
        preferences = getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        loadData();
        getActivity().setTitle("Reports");
        searchViewReports.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
        return view;
    }

    private void loadData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FETCH_ALL_BOOKINGS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("INFO", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            Bookings bookings = null;
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jObj = jsonArray.getJSONObject(i);
                                Log.i("information", jObj.getString("rp"));
                                if (jObj.getString("rp").equals("unchecked"))
                                {
                                    Log.i("infoUncheck", jObj.getString("rp"));
                                    bookings = new Bookings(jObj.getString("testname"),
                                            jObj.getString("members"),
                                            jObj.getString("date"),
                                            jObj.getString("time"));
                                    bookings.setReport(jObj.getString("report"));
                                    reportsArrayList.add(bookings);
                                }
                            }

                            myReportsAdapter= new MyReportsAdapter(getActivity(), reportsArrayList);
                            recyclerViewReports.setAdapter(myReportsAdapter);
                        }catch (JSONException e){
                            Log.e("ERRORPatho", e.getMessage());
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
                hashMap.put(KEY_DNAME, preferences.getString("dname",""));
                return hashMap;
            }
        };
        VolleySingleton singleton = VolleySingleton.getInstance(getActivity());
        singleton.addToRequestQueue(stringRequest);
    }

    public void search(){
        searchViewReports.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchViewReports.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("info", newText);
                myReportsAdapter = new MyReportsAdapter(getActivity(), reportsArrayList);
                myReportsAdapter.getFilter().filter(newText);
                recyclerViewReports.setAdapter(myReportsAdapter);
                /*myReportsAdapter.setOnItemClickListener(new MyReportsAdapter.OnItemClickListener() {
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
                                            bundle.putString("testname", jObj.getString("testname"));
                                            bundle.putString("parameter", jObj.getString("members"));
                                            bundle.putString("collection", jObj.getString("date"));
                                            bundle.putString("field", jObj.getString("time"));
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
                });*/
                return false;
            }
        });
    }


    @Override
    public void onItemClick(int position) {

    }
}