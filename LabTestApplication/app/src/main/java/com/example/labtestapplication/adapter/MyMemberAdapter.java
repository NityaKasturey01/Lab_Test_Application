package com.example.labtestapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labtestapplication.R;
import com.example.labtestapplication.model.LabTestData;
import com.example.labtestapplication.model.Member;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyMemberAdapter extends RecyclerView.Adapter<MyMemberAdapter.MyHolder>{

    private Context context;
    private ArrayList<Member> list;
    private ArrayList<Member> labsArrayList;
    private static MyMemberAdapter.OnItemClickListener listener;

    /*public MyAdapter(ArrayList<Actors> list, ArrayList<Actors> actorsArrayList) {
        this.list = list;
        this.actorsArrayList = actorsArrayList;
    }*/

    public MyMemberAdapter(Context context, ArrayList<Member> list)
    {
        this.context = context;
        this.list = list;
        this.labsArrayList = new ArrayList<>(this.list);;
    }

    public MyMemberAdapter(){}


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(MyMemberAdapter.OnItemClickListener listener){
        MyMemberAdapter.listener = listener;
    }

    /*public void setOnItemClickListener(Response.Listener<String> listener){
        MyTestAdapter.listener = (OnItemClickListener) listener;
    }*/

    @NonNull
    @Override
    public MyMemberAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mymemberscard, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMemberAdapter.MyHolder holder, int position) {
        Member member= labsArrayList.get(position);
        //Picasso.with(context).load(member.getImage()).centerCrop().fit().noFade().into(holder.testImageCard);
        holder.memberNameCard.setText(member.getName());
        holder.memberAgeCard.setText("Age " + member.getAge());
        holder.memberGenderCard.setText(member.getGender());
    }

    @Override
    public int getItemCount() {
        return labsArrayList.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        //private AppCompatImageView testImageCard;
        private TextView memberNameCard, memberAgeCard, memberGenderCard;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            memberAgeCard = itemView.findViewById(R.id.memberAgeCard);
            memberNameCard = itemView.findViewById(R.id.memberNameCard);
            memberGenderCard = itemView.findViewById(R.id.memberGenderCard);
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

}
