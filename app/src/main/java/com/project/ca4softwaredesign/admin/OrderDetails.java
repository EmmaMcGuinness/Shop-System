package com.project.ca4softwaredesign.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.ca4softwaredesign.Product;
import com.project.ca4softwaredesign.R;

import java.util.ArrayList;

public class OrderDetails extends AppCompatActivity {
    OrderModel orderModel = null;
    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String orderId;
    OrderDetailAdapter orderDetailAdapter;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
    private final ArrayList<OrderModel> orderDetailsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(OrderDetails.this);
        recyclerView.setLayoutManager(layoutManager);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final Object object = getIntent().getSerializableExtra("details");
        if(object instanceof OrderModel){
            orderModel = (OrderModel) object;

        }


        if(orderModel != null) {
            orderId = orderModel.getOrderId();

        }
        addReviewsToRecycler();

    }
    private void addReviewsToRecycler() {

        databaseReference.child(orderId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderDetailsList.clear();

                for (DataSnapshot events : snapshot.child("Items").getChildren()) {
                    final String getProductName = events.child("name").getValue(String.class);
                    final String getQuantity = events.child("quantity").getValue(String.class);


                    OrderModel orderModel = new OrderModel(getProductName, getQuantity);

                    orderDetailsList.add(orderModel);

                }
                orderDetailAdapter = new OrderDetailAdapter(OrderDetails.this, orderDetailsList);
                recyclerView.setAdapter(orderDetailAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}