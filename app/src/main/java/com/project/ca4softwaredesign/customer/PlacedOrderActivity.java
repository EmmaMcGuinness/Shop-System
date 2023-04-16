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
import com.google.firebase.database.DatabaseReference;
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
    int total;
    String sTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_order);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        List<CartModel> list = (ArrayList<CartModel>)getIntent().getSerializableExtra("itemList");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            total = bundle.getInt("total");
        }
        sTotal = String.valueOf(total);

        if(list != null && list.size() > 0){
                final HashMap<String, Object> cartMap = new HashMap<>();

                cartMap.put("totalPrice", sTotal);
                cartMap.put("userId", userID);

               DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Orders").push();

                     ref.setValue(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                for(int i=0; i<list.size(); i++){
                                    String productId = list.get(i).productId;
                                    String name = list.get(i).productName;
                                    String cost = String.valueOf(list.get(i).getTotalPrice());
                                    String quantity = list.get(i).totalQuantity;

                                    HashMap<String, String> hashMap1 = new HashMap<>();
                                    hashMap1.put("pId", productId);
                                    hashMap1.put("name", name);
                                    hashMap1.put("cost", cost);
                                    hashMap1.put("quantity", quantity);

                                    ref.child("Items").child(productId).setValue(hashMap1);

                                }

                                Toast.makeText(PlacedOrderActivity.this, "Your Order has been Placed!", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(PlacedOrderActivity.this, HomeCustomer.class);
                                startActivity(i);
                            }
                        });


        }
    }
}