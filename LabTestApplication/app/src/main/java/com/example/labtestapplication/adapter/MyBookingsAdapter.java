package com.example.labtestapplication.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.labtestapplication.R;
import com.example.labtestapplication.VolleySingleton;
import com.example.labtestapplication.model.Bookings;
import com.example.labtestapplication.model.Member;
import com.example.labtestapplication.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyBookingsAdapter  extends RecyclerView.Adapter<MyBookingsAdapter.MyHolder>{
    private Context context;
    private ArrayList<Bookings> list;
    private ArrayList<Bookings> bookingsArrayList;
    private static MyBookingsAdapter.OnItemClickListener listener;
    private SharedPreferences preferences;
    private static final String KEY_EMAIL = "email";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_SG = "sg";
    private static final String KEY_SR = "sr";
    private static final String KEY_RP = "rp";
    private static final String KEY_RR = "rr";
    private String Sgiven = "unchecked", Srecieved = "unchecked", Rprovided = "unchecked", Rrecieved = "unchecked";


    /*public MyAdapter(ArrayList<Actors> list, ArrayList<Actors> actorsArrayList) {
        this.list = list;
        this.actorsArrayList = actorsArrayList;
    }*/

    public MyBookingsAdapter(Context context, ArrayList<Bookings> list)
    {
        this.context = context;
        this.list = list;
        this.bookingsArrayList = new ArrayList<>(this.list);;
    }

    public MyBookingsAdapter(){}

    public MyBookingsAdapter(FragmentActivity activity, Bookings bookings) {
    }


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(MyBookingsAdapter.OnItemClickListener listener){
        MyBookingsAdapter.listener = listener;
    }

    /*public void setOnItemClickListener(Response.Listener<String> listener){
        MyTestAdapter.listener = (OnItemClickListener) listener;
    }*/

    @NonNull
    @Override
    public MyBookingsAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mybookings, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBookingsAdapter.MyHolder holder, int position) {
        Bookings bookings = bookingsArrayList.get(position);
        //Picasso.with(context).load(member.getImage()).centerCrop().fit().noFade().into(holder.testImageCard);
        holder.testName.setText(bookings.getTestName());
        holder.members.setText(bookings.getMembers());
        holder.dateU.setText(bookings.getDate()+" "+bookings.getTime());
        holder.collecionU.setText(bookings.getCollectionType());
        holder.pincodeU.setText(bookings.getArea()+", "+bookings.getCity()+", "+bookings.getPicode());
        holder.contactU.setText(bookings.getContact());
        holder.priceU.setText(bookings.getPrice());
        preferences = context.getSharedPreferences("mypref", Context.MODE_PRIVATE);
        if (preferences.contains("username")){
            holder.sampleGiven.setEnabled(true);
            holder.sampleRecieved.setEnabled(false);
            holder.reportProvided.setEnabled(false);
            holder.reportRecieved.setEnabled(true);

            if (bookings.getSample_given().equals("checked")){
                holder.sampleGiven.setChecked(true);
            }
            if (bookings.getSample_recieved().equals("checked")){
                holder.sampleRecieved.setChecked(true);
            }
            if (bookings.getReports_provided().equals("checked")){
                holder.reportProvided.setChecked(true);
            }
            if (bookings.getReports_recieved().equals("checked")){
                holder.reportRecieved.setChecked(true);
            }
            holder.Ssubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.sampleGiven.isChecked())
                        Sgiven = "checked";
                    if(holder.sampleRecieved.isChecked())
                        Srecieved = "checked";
                    if(holder.reportProvided.isChecked())
                        Rprovided = "checked";
                    if(holder.reportRecieved.isChecked())
                        Rrecieved = "checked";
                    loadData(bookings.getDate(), bookings.getTime(), Sgiven, Srecieved, Rprovided, Rrecieved);
                }
            });
        }else if (preferences.contains("dname")){
            holder.sampleGiven.setEnabled(false);
            holder.sampleRecieved.setEnabled(true);
            holder.reportProvided.setEnabled(true);
            holder.reportRecieved.setEnabled(false);
            if (bookings.getSample_given().equals("checked")){
                holder.sampleGiven.setChecked(true);
            }
            if (bookings.getSample_recieved().equals("checked")){
                holder.sampleRecieved.setChecked(true);
            }
            if (bookings.getReports_provided().equals("checked")){
                holder.reportProvided.setChecked(true);
            }
            if (bookings.getReports_recieved().equals("checked")){
                holder.reportRecieved.setChecked(true);
            }
            holder.Ssubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.sampleGiven.isChecked())
                        Sgiven = "checked";
                    if(holder.sampleRecieved.isChecked())
                        Srecieved = "checked";
                    if(holder.reportProvided.isChecked())
                        Rprovided = "checked";
                    if(holder.reportRecieved.isChecked())
                        Rrecieved = "checked";
                    loadData(bookings.getDate(), bookings.getTime(), Sgiven, Srecieved, Rprovided, Rrecieved);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return bookingsArrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        //private AppCompatImageView testImageCard;
        private TextView testName, members, dateU, collecionU, pincodeU, contactU, priceU;
        private CheckBox sampleGiven, sampleRecieved, reportRecieved, reportProvided;
        private SharedPreferences preferences;
        private ImageButton Ssubmit, Rsubmit;
        private String Sgiven = "unchecked", Srecieved = "unchecked", Rprovided = "unchecked", Rrecieved = "unchecked";

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            testName = itemView.findViewById(R.id.bookTest);
            members = itemView.findViewById(R.id.bookMembers);
            dateU = itemView.findViewById(R.id.bookDate);
            collecionU = itemView.findViewById(R.id.bookCollection);
            pincodeU = itemView.findViewById(R.id.bookAddress);
            contactU = itemView.findViewById(R.id.bookContact);
            priceU = itemView.findViewById(R.id.bookPrice);
            sampleGiven = itemView.findViewById(R.id.sampleGiven);
            sampleRecieved = itemView.findViewById(R.id.sampleRecieved);
            reportProvided = itemView.findViewById(R.id.reportsProvided);
            reportRecieved = itemView.findViewById(R.id.reportsRecieved);
            Ssubmit = itemView.findViewById(R.id.Ssubmit);
            Rsubmit = itemView.findViewById(R.id.Rsubmit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    private void loadData(String date, String time, String SG, String SR, String RP, String RR) {
        Log.i("infoData", date+time+SG+SR+RP+RR);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.UPDATE_STATUS_BOOK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("INFO", response);
                        if (response.equals("success")){
                            Toast.makeText(context, "Status Updated", Toast.LENGTH_SHORT).show();

                        }
                }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERRORMybookAdap", error.getMessage());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_DATE, date);
                hashMap.put(KEY_TIME, time);
                hashMap.put(KEY_SG, SG);
                hashMap.put(KEY_SR, SR);
                hashMap.put(KEY_RP, RP);
                hashMap.put(KEY_RR, RR);
                return hashMap;
            }
        };
        VolleySingleton singleton = VolleySingleton.getInstance(context);
        singleton.addToRequestQueue(stringRequest);
    }
}
