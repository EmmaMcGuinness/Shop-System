package com.project.ca4softwaredesign.customer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.project.ca4softwaredesign.Product;
import com.project.ca4softwaredesign.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailImg;
    TextView price, rating, title, quantity;
    int totalQuantity = 0;
    int totalPrice = 1;


    Button addToCart;
    ImageView addItem, removeItem;
    Product product = null;
    Toolbar toolbar;

    FirebaseStorage firebaseStorage;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Object object = getIntent().getSerializableExtra("detail");
        if(object instanceof Product){
            product = (Product) object;

        }

        detailImg = findViewById(R.id.detailed_img);
        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);
        addToCart = findViewById(R.id.add_To_Cart);
        price = findViewById(R.id.detailed_price);
        rating = findViewById(R.id.detailed_rating);
        title = findViewById(R.id.detail_name);
        quantity = findViewById(R.id.quantity);

        if(product != null){
            price.setText(product.getPrice());
            title.setText(product.getTitle());

        }

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity < 10){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));
                }
            }
        });
        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(totalQuantity > 1){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                }
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalPrice = Integer.parseInt(product.getPrice())*totalQuantity;
                addedToCart();
            }


        });





    }
    private void addedToCart() {

        final HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("productName", title.getText().toString());
        cartMap.put("productPrice", price.getText().toString());
        cartMap.put("totalQuantity", quantity.getText().toString());
        cartMap.put("totalPrice", totalPrice);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child("AddToCart").push()
                .setValue(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(DetailedActivity.this, "Item added to Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
