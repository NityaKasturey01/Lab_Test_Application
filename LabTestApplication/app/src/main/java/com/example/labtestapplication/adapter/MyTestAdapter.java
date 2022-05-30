package com.example.labtestapplication.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labtestapplication.R;
import com.example.labtestapplication.model.LabTestData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyTestAdapter extends RecyclerView.Adapter<MyTestAdapter.MyHolder> implements Filterable {

    private Context context;
    private ArrayList<LabTestData> list;
    private ArrayList<LabTestData> labsArrayList;
    private static OnItemClickListener listener;

    /*public MyAdapter(ArrayList<Actors> list, ArrayList<Actors> actorsArrayList) {
        this.list = list;
        this.actorsArrayList = actorsArrayList;
    }*/

    public MyTestAdapter(Context context, ArrayList<LabTestData> list)
    {
        this.context = context;
        this.list = list;
        this.labsArrayList = new ArrayList<>(this.list);
    }

    public MyTestAdapter(){}

    /*@Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                     labsArrayList = list;
                } else {
                    List<LabTestData> filteredList = new ArrayList<>();
                    for (LabTestData row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getCollection_type().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    labsArrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = labsArrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                labsArrayList = (ArrayList<LabTestData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }*/


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        MyTestAdapter.listener = listener;
    }

    /*public void setOnItemClickListener(Response.Listener<String> listener){
        MyTestAdapter.listener = (OnItemClickListener) listener;
    }*/

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mytestcard, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        LabTestData labTests = labsArrayList.get(position);
        Picasso.with(context).load(labTests.getImage()).centerCrop().fit().noFade().into(holder.testImageCard);
        holder.testName.setText(labTests.getName());
        holder.collectionType.setText(labTests.getCollection_type());
        holder.parameters.setText(labTests.getParameter()+" Parameters");
        holder.cost.setText(labTests.getCost());
    }

    @Override
    public int getItemCount() {
        return labsArrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        private AppCompatImageView testImageCard;
        private AppCompatTextView testName, collectionType, parameters, cost;
        private ImageButton addToCart;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            testImageCard = itemView.findViewById(R.id.testImageCard);
            testName = itemView.findViewById(R.id.testNameCard);
            collectionType = itemView.findViewById(R.id.testCollectionCard);
            parameters = itemView.findViewById(R.id.parametersCard);
            cost = itemView.findViewById(R.id.costCard);
            addToCart = itemView.findViewById(R.id.addToCartCard);
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
            ArrayList<LabTestData> filteredList = new ArrayList<>();
            if (constraint==null|| constraint.length()==0){
                filteredList.addAll(list);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (LabTestData item : labsArrayList){
                    if (item.getName().toLowerCase().contains(filterPattern)){
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
            labsArrayList.clear();
            labsArrayList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public void collectionFilter(String coll){
        ArrayList<LabTestData> filteredList = new ArrayList<>();
        String filterPattern = coll.trim();
        for (LabTestData item : labsArrayList){
            if (item.getCollection_type().contains(filterPattern)){
                filteredList.add(item);
            }
        }
        labsArrayList.clear();
        labsArrayList = filteredList;
    }
}
