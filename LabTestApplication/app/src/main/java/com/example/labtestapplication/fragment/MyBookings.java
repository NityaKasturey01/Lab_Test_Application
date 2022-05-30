package com.example.labtestapplication.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.labtestapplication.R;
import com.example.labtestapplication.VolleySingleton;
import com.example.labtestapplication.adapter.MyBookingsAdapter;
import com.example.labtestapplication.adapter.MyTestAdapter;
import com.example.labtestapplication.model.Bookings;
import com.example.labtestapplication.model.LabTestData;
import com.example.labtestapplication.util.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MyBookings extends Fragment {

    View view;
    private LinearLayout prevBook, curBook;
    private RecyclerView curRecyclerView, prevRecyclerView;
    private ArrayList<Bookings> bookingsArrayList, prevBookingArrayList;
    private MyBookingsAdapter myBookingsAdapter, prevBookingAdapter;
    private static final String KEY_EMAIL = "email";
    private SharedPreferences preferences;


    public MyBookings() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_bookings, container, false);
        preferences = getActivity().getSharedPreferences("mypref", Context.MODE_PRIVATE);
        curBook = view.findViewById(R.id.currentBookLayout);
        prevBook = view.findViewById(R.id.previousBookLayout);
        curRecyclerView = view.findViewById(R.id.curBookRecycler);
        prevRecyclerView = view.findViewById(R.id.prevBookRecycler);
        prevRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        prevRecyclerView.setHasFixedSize(true);
        curRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        curRecyclerView.setHasFixedSize(true);
        bookingsArrayList = new ArrayList<>();
        prevBookingArrayList = new ArrayList<>();
        loadData();
        getActivity().setTitle("My Bookings");
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.myBookingNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.bookings){
                    curBook.setVisibility(View.VISIBLE);
                    prevBook.setVisibility(View.INVISIBLE);
                }else if (id == R.id.preBookings){
                    prevBook.setVisibility(View.VISIBLE);
                    curBook.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });
        return view;
    }
    private void loadData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.FETCH_BOOKINGS_URL,
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
                                bookings = new Bookings(jObj.getString("testname"),
                                        jObj.getString("members"),
                                        jObj.getString("collection_type"),
                                        jObj.getString("date"),
                                        jObj.getString("time"),
                                        jObj.getString("pincode"),
                                        jObj.getString("city"),
                                        jObj.getString("area"),
                                        jObj.getString("contact"),
                                        jObj.getString("price"),
                                        jObj.getString("status"),
                                        jObj.getString("sg"),
                                        jObj.getString("sr"),
                                        jObj.getString("rp"),
                                        jObj.getString("rr"));
                                bookings.setReport(jObj.getString("report"));

                                /*LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());
                                Log.i("curr",currentDate.toString());
                                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-M-dd");
                                LocalDate recreatedLocalDate = LocalDate.parse(bookings.getDate(),df);*/
                                if (bookings.getSample_given().equals("checked") && bookings.getReports_recieved().equals("checked") && bookings.getReports_provided().equals("checked") && bookings.getSample_recieved().equals("checked"))
                                {
                                    prevBookingArrayList.add(bookings);
                                }
                                else
                                {
                                    bookingsArrayList.add(bookings);
                                }
                            }

                            myBookingsAdapter= new MyBookingsAdapter(getActivity(), bookingsArrayList);
                            curRecyclerView.setAdapter(myBookingsAdapter);
                            prevBookingAdapter = new MyBookingsAdapter(getActivity(), prevBookingArrayList);
                            prevRecyclerView.setAdapter(prevBookingAdapter);

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
        VolleySingleton singleton = VolleySingleton.getInstance(getActivity());
        singleton.addToRequestQueue(stringRequest);
    }
}