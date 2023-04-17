package com.project.ca4softwaredesign.customer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.ca4softwaredesign.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    RecyclerView recyclerView;
    CartAdapter cartAdapter;
    List<CartModel> cartModelList;
    Button payNow;
    ImageView delete;
    Button enter;
    double total, finalTotal, code= 0;

    TextView overTotalAmount;
    EditText discount;
    String discountCode = "";
    String userID;
    FirebaseUser user;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth auth;

    public CartFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        auth = FirebaseAuth.getInstance();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        cartModelList = new ArrayList<>();
        cartAdapter = new CartAdapter(getActivity(), cartModelList);
        recyclerView.setAdapter(cartAdapter);
        discount = view.findViewById(R.id.discountEditText);
        overTotalAmount = view.findViewById(R.id.textView);
        payNow = view.findViewById(R.id.pay_now);
        enter = view.findViewById(R.id.enterButton);


        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mMessageReciever, new IntentFilter("TotalAmount"));

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users").child(userID);

        fireDB.child("AddToCart").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
              if(task.isSuccessful()){
                  for (DataSnapshot snapshot : task.getResult().getChildren()){
                     String productId = snapshot.getKey();
                     CartModel cartModel = snapshot.getValue(CartModel.class);
                     cartModel.setProductId(productId);

                      cartModelList.add(cartModel);
                      cartAdapter.notifyDataSetChanged();
                  }
              }
            }
        });

        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PaymentActivity.class );
                intent.putExtra("itemList", (Serializable) cartModelList);
                intent.putExtra("total", total);
                startActivity(intent);
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discountCode = discount.getText().toString().trim();
            }
        });



        return view;
    }
    public BroadcastReceiver mMessageReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            total = intent.getDoubleExtra("totalAmount", 0);

            overTotalAmount.setText("Total Price: €" + total);

        }
    };
}