package com.project.ca4softwaredesign;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ca4softwaredesign.customer.DetailedActivity;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    private final Context context;
    private ArrayList<Product> myDataSet;


    //constructor
    public ProductAdapter(Context context, ArrayList<Product> myDataSet) {
        this.context = context;
        this.myDataSet = myDataSet;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_layout, parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,  int position) {

        holder.title.setText(myDataSet.get(position).getTitle());
        holder.price.setText(myDataSet.get(position).getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailedActivity.class);
                i.putExtra("detail", myDataSet.get(position));

                context.startActivity(i);
            }
        });



    }


    @Override
    public int getItemCount() {

        return myDataSet.size();
    }

    public void searchDataList(ArrayList<Product> searchList){
        myDataSet = searchList;
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView title, price;
        CardView card;

        public MyViewHolder(@NonNull View view){
            super(view);

            title = view.findViewById(R.id.titleTextView);
            price = view.findViewById(R.id.priceTextView);
            card = view.findViewById(R.id.card);
        }
    }



}
