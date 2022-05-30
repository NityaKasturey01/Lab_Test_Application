package com.example.labtestapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labtestapplication.PathologyDetailsActivity;
import com.example.labtestapplication.R;
import com.example.labtestapplication.UploadDownloadReports;
import com.example.labtestapplication.model.Bookings;
import com.example.labtestapplication.model.LabTestData;
import com.example.labtestapplication.model.Member;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyReportsAdapter extends RecyclerView.Adapter<MyReportsAdapter.MyHolder> implements Filterable {
    private Context context;
    private ArrayList<Bookings> list;
    private ArrayList<Bookings> reportsArrayList;
    private static MyReportsAdapter.OnItemClickListener listener;
    private SharedPreferences preferences;

    /*public MyAdapter(ArrayList<Actors> list, ArrayList<Actors> actorsArrayList) {
        this.list = list;
        this.actorsArrayList = actorsArrayList;
    }*/

    public MyReportsAdapter(Context context, ArrayList<Bookings> list)
    {
        Log.i("reportsAdap", list.toString());
        this.context = context;
        this.list = list;
        this.reportsArrayList = new ArrayList<>(this.list);
    }

    public MyReportsAdapter(){}


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(MyReportsAdapter.OnItemClickListener listener){
        MyReportsAdapter.listener = listener;
    }

    /*public void setOnItemClickListener(Response.Listener<String> listener){
        MyTestAdapter.listener = (OnItemClickListener) listener;
    }*/

    @NonNull
    @Override
    public MyReportsAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reportscard, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyReportsAdapter.MyHolder holder, int position){
        Bookings bookings = reportsArrayList.get(position);
        //Picasso.with(context).load(member.getImage()).centerCrop().fit().noFade().into(holder.testImageCard);
        holder.testName.setText(bookings.getTestName());
        holder.membername.setText(bookings.getMembers());
        holder.date.setText(bookings.getDate());
        holder.time.setText(bookings.getTime());
        preferences = context.getSharedPreferences("mypref", Context.MODE_PRIVATE);
        if (preferences.contains("username")){
            holder.downloadPDF.setVisibility(View.VISIBLE);
            holder.uploadPdfReport.setVisibility(View.GONE);
            holder.downloadPDF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UploadDownloadReports.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("testname", bookings.getTestName());
                    bundle.putString("members", bookings.getMembers());
                    bundle.putString("date", bookings.getDate());
                    bundle.putString("time", bookings.getTime());
                    bundle.putString("report", bookings.getReport());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }else if (preferences.contains("dname")){
            holder.downloadPDF.setVisibility(View.GONE);
            holder.uploadPdfReport.setVisibility(View.VISIBLE);
            holder.uploadPdfReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UploadDownloadReports.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("testname", bookings.getTestName());
                    bundle.putString("members", bookings.getMembers());
                    bundle.putString("date", bookings.getDate());
                    bundle.putString("time", bookings.getTime());
                    bundle.putString("report", bookings.getReport());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return reportsArrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        //private AppCompatImageView testImageCard;
        private AppCompatTextView testName, membername, date, time;
        private ImageView pdfreport;
        private AppCompatButton uploadPdfReport, downloadPDF;
        private AppCompatEditText editPDF;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            testName = itemView.findViewById(R.id.reportCardTestname);
            membername = itemView.findViewById(R.id.reportCardUsername);
            date = itemView.findViewById(R.id.reportCardDate);
            time = itemView.findViewById(R.id.reportCardTime);
            pdfreport = itemView.findViewById(R.id.pdfReport);
            uploadPdfReport = itemView.findViewById(R.id.uploadPdfReport);
            downloadPDF = itemView.findViewById(R.id.downloadPdfReport);
//            editPDF = itemView.findViewById(R.id.pdfPath);
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

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private final Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Bookings> filteredList = new ArrayList<>();
            if (constraint==null|| constraint.length()==0){
                filteredList.addAll(list);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Bookings item : reportsArrayList){
                    if (item.getTestName().toLowerCase().contains(filterPattern) ||
                            item.getMembers().toLowerCase().contains(filterPattern) ||
                            item.getDate().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            Log.i("result", results.values.toString());
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            reportsArrayList.clear();
            reportsArrayList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}
