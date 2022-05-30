package com.example.labtestapplication.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.labtestapplication.R;
import com.example.labtestapplication.VolleySingleton;
import com.example.labtestapplication.adapter.MyPopularTests;
import com.example.labtestapplication.adapter.MyTestAdapter;
import com.example.labtestapplication.model.LabTestData;
import com.example.labtestapplication.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomePage extends Fragment {

    private AppCompatImageView testImageCard;
    private AppCompatTextView testName, collectionType, parameters, cost;
    private ImageButton addToCart;
    private SearchView searchView;
    private AppCompatCheckBox checkHome, checkLab;
    private RecyclerView recyclerViewPopular;
    private ArrayList<LabTestData> labTestsArrayList;
    private Bundle bundle;
    private MyPopularTests myPopularTests;

    View view;

    public HomePage() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home_page, container, false);
        getActivity().setTitle("Home");
        recyclerViewPopular = view.findViewById(R.id.recylerViewpopularHome);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewPopular.setLayoutManager(llm);
        recyclerViewPopular.setHasFixedSize(false);
        labTestsArrayList = new ArrayList<>();
        loadData();
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
                                //labTests.setAge_limit(jObj.getString("age_limit"));
                                labTestsArrayList.add(labTests);
                            }

                            myPopularTests= new MyPopularTests(getActivity(), labTestsArrayList);
                            recyclerViewPopular.setAdapter(myPopularTests);

                            myPopularTests.setOnItemClickListener(new MyPopularTests.OnItemClickListener() {
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
}