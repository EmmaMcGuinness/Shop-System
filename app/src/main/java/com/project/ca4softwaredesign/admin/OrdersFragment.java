package com.project.ca4softwaredesign.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.ca4softwaredesign.R;
import com.project.ca4softwaredesign.customer.ReviewActivity;
import com.project.ca4softwaredesign.customer.ReviewAdapter;
import com.project.ca4softwaredesign.customer.ReviewModel;

import java.util.ArrayList;

public class OrdersFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String getT;

    ArrayList<OrderModel> orderList = new ArrayList<>();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        addReviewsToRecycler();

        return view;
    }
    private void addReviewsToRecycler() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();

                for (DataSnapshot events : snapshot.child("Orders").getChildren()) {
                    final String getUserId = events.child("userId").getValue(String.class);
                    final String getTotal = events.child("totalPrice").getValue(String.class);
                    final String getOrderId = events.getKey();

                   // getT = String.valueOf(getTotal);

                    Log.d("Order", getUserId + " " + getTotal+ " "+ getOrderId);

                    OrderModel orderModel = new OrderModel(getOrderId,getUserId, getTotal);

                    orderList.add(orderModel);

                }
                OrderAdapter orderAdapter = new OrderAdapter(getActivity(), orderList);
                recyclerView.setAdapter(orderAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}