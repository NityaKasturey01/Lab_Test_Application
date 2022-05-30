package com.example.labtestapplication.adapter;

import android.content.Context;
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


        import android.content.Context;
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

public class MyPopularTests extends RecyclerView.Adapter<MyPopularTests.MyHolder> implements Filterable {

    private Context context;
    private ArrayList<LabTestData> list;
    private ArrayList<LabTestData> labsArrayList;
    private static OnItemClickListener listener;

    /*public MyAdapter(ArrayList<Actors> list, ArrayList<Actors> actorsArrayList) {
        this.list = list;
        this.actorsArrayList = actorsArrayList;
    }*/

    public MyPopularTests(Context context, ArrayList<LabTestData> list)
    {
        this.context = context;
        this.list = list;
        this.labsArrayList = new ArrayList<>(this.list);;
    }

    public MyPopularTests(){}

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        MyPopularTests.listener = listener;
    }

    /*public void setOnItemClickListener(Response.Listener<String> listener){
        MyTestAdapter.listener = (OnItemClickListener) listener;
    }*/

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mypopularcards, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        LabTestData labTests = labsArrayList.get(position);
        //Picasso.with(context).load(labTests.getImage()).centerCrop().fit().noFade().into(holder.testImageCard);
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
            testName = itemView.findViewById(R.id.testNamePopularCard);
            collectionType = itemView.findViewById(R.id.testCollectionpopularCard);
            parameters = itemView.findViewById(R.id.parametersPopularCard);
            cost = itemView.findViewById(R.id.costPopularCard);
            addToCart = itemView.findViewById(R.id.addToCartPopularCard);
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
        //labsArrayList.clear();
        String filterPattern = coll.trim();
        for (LabTestData item : labsArrayList){
            //Log.i("coll", item.getCollection_type());
            //Log.i("filter", filterPattern);
            if (item.getCollection_type().contains(filterPattern)){
                filteredList.add(item);
                // Log.i("result", item.getCollection_type());
            }
        }
        labsArrayList.clear();
        labsArrayList = filteredList;
    }
}

