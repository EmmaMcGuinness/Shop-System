package com.project.ca4softwaredesign.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.project.ca4softwaredesign.R;

import java.util.ArrayList;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>{
    private final Context context;
    private ArrayList<OrderModel> myDataSet;



    //constructor
    public OrderDetailAdapter(Context context, ArrayList<OrderModel> myDataSet) {
        this.context = context;
        this.myDataSet = myDataSet;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public OrderDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        return new OrderDetailAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_order_detail, parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailAdapter.ViewHolder holder, int position) {
        OrderModel orderModel = myDataSet.get(position);
        holder.productName.setText(orderModel.getProductName());
        holder.quantity.setText(orderModel.getQuantity());


    }

    @Override
    public int getItemCount() {

        return myDataSet.size();
    }

    public void searchDataList(ArrayList<OrderModel> searchList){
        myDataSet = searchList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView productName, quantity;
        CardView card;

        public ViewHolder(@NonNull View view){
            super(view);

            productName = view.findViewById(R.id.productNameTextView);
            quantity = view.findViewById(R.id.quantityTextView);
            card = view.findViewById(R.id.card);
        }
    }


}
