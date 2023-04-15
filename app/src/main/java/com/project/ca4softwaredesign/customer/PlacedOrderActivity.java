package com.project.ca4softwaredesign.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.project.ca4softwaredesign.HomeCustomer;
import com.project.ca4softwaredesign.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlacedOrderActivity extends AppCompatActivity {

    FirebaseAuth auth;
    String userID;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_order);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        List<CartModel> list = (ArrayList<CartModel>)getIntent().getSerializableExtra("itemList");

        if(list != null && list.size() > 0){
            for(CartModel model : list){
                final HashMap<String, Object> cartMap = new HashMap<>();

                cartMap.put("productName", model.getProductName());
                cartMap.put("productPrice", model.getProductPrice());
                cartMap.put("totalQuantity", model.getTotalQuantity());
                cartMap.put("totalPrice", model.getTotalPrice());
                cartMap.put("userId", userID);

                FirebaseAuth auth = FirebaseAuth.getInstance();

                FirebaseDatabase.getInstance().getReference("Orders").push()
                        .setValue(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(PlacedOrderActivity.this, "Your Order has been Placed!", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(PlacedOrderActivity.this, HomeCustomer.class);
                                startActivity(i);
                            }
                        });
            }

        }
    }
}