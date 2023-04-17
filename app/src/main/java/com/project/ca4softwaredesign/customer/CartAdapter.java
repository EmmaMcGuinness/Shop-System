package com.project.ca4softwaredesign.customer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.ca4softwaredesign.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    List<CartModel> cartModelList;
    double totalPrice = 0;
    String userID;
    FirebaseUser user;
    FirebaseAuth auth;

    public CartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.name.setText(cartModelList.get(position).getProductName());
        holder.price.setText(cartModelList.get(position).getProductPrice());
        holder.quantity.setText(cartModelList.get(position).getTotalQuantity().trim());
        holder.totalPrice.setText(String.valueOf(cartModelList.get(position).getTotalPrice()));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                userID = user.getUid();
                DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users").child(userID);
                fireDB.child("AddToCart").child(cartModelList.get(holder.getAdapterPosition()).getProductId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            cartModelList.remove(cartModelList.get(holder.getAdapterPosition()));
                            notifyDataSetChanged();
                            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        //pass total amount to Cart fragment
        totalPrice = totalPrice + cartModelList.get(position).getTotalPrice();
        Intent intent = new Intent("TotalAmount");
        intent.putExtra("totalAmount", totalPrice);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name,price,quantity,totalPrice;
        ImageView delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            quantity = itemView.findViewById(R.id.total_quantity);
            totalPrice = itemView.findViewById(R.id.total_price);
            delete = itemView.findViewById(R.id.delete);

        }
    }
}
