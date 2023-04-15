package com.project.ca4softwaredesign.customer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ca4softwaredesign.Product;
import com.project.ca4softwaredesign.ProductAdapter;
import com.project.ca4softwaredesign.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder>{
    private final Context context;
    private ArrayList<ReviewModel> myDataSet;


    //constructor
    public ReviewAdapter(Context context, ArrayList<ReviewModel> myDataSet) {
        this.context = context;
        this.myDataSet = myDataSet;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_review, parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.review.setText(myDataSet.get(position).getReview());
        holder.rating.setText(myDataSet.get(position).getRating());
    }

    @Override
    public int getItemCount() {

        return myDataSet.size();
    }

    public void searchDataList(ArrayList<ReviewModel> searchList){
        myDataSet = searchList;
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView review, rating;
        CardView card;

        public MyViewHolder(@NonNull View view){
            super(view);

            review = view.findViewById(R.id.reviewTextView);
            rating = view.findViewById(R.id.ratingTextView);
            card = view.findViewById(R.id.card);
        }
    }


}
