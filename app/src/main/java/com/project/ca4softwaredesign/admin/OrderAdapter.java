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

import com.project.ca4softwaredesign.Product;
import com.project.ca4softwaredesign.R;
import com.project.ca4softwaredesign.customer.DetailedActivity;
import com.project.ca4softwaredesign.customer.ReviewAdapter;
import com.project.ca4softwaredesign.customer.ReviewModel;

import java.util.ArrayList;

public class OrderAdapter  extends RecyclerView.Adapter<OrderAdapter.MyViewHolder>{
    private final Context context;
    private ArrayList<OrderModel> myDataSet;


    //constructor
    public OrderAdapter(Context context, ArrayList<OrderModel> myDataSet) {
        this.context = context;
        this.myDataSet = myDataSet;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_order, parent , false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.MyViewHolder holder, int position) {
        OrderModel orderModel = myDataSet.get(position);
        holder.orderId.setText(orderModel.getOrderId());
        holder.userId.setText(orderModel.getUserId());
        holder.total.setText(orderModel.getTotal());

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(context, OrderDetails.class);
               i.putExtra("details", orderModel);
               context.startActivity(i);

           }
       });

    }

    @Override
    public int getItemCount() {

        return myDataSet.size();
    }

    public void searchDataList(ArrayList<OrderModel> searchList){
        myDataSet = searchList;
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final TextView orderId, userId, total;
        CardView card;

        public MyViewHolder(@NonNull View view){
            super(view);

            orderId = view.findViewById(R.id.orderIdTextView);
            userId = view.findViewById(R.id.userIdTextView);
            total = view.findViewById(R.id.totalTextView);
            card = view.findViewById(R.id.card);
        }
    }


}
