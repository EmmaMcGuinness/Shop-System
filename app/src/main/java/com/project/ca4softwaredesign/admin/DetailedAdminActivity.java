package com.project.ca4softwaredesign.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.project.ca4softwaredesign.Product;
import com.project.ca4softwaredesign.R;
import com.project.ca4softwaredesign.customer.ReviewActivity;
import com.project.ca4softwaredesign.customer.ReviewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailedAdminActivity extends AppCompatActivity {

    ImageView detailImg;
    TextView price, reviewText, title, quantity;
    ImageView reviewImg;
    int totalQuantity = 0;
    int totalPrice = 1;
    int quant = 0;
    String productId, category, manufacturer, fPrice, fTitle;


    Button addRemove;
    ImageView addItem, removeItem;
    Product product = null;
    Toolbar toolbar;

    FirebaseStorage firebaseStorage;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");
    private final ArrayList<ReviewModel> ratingList = new ArrayList<>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_admin);

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
        addRemove = findViewById(R.id.add_remove);
        price = findViewById(R.id.detailed_price);
        title = findViewById(R.id.detail_name);
        quantity = findViewById(R.id.quantity);

        if(product != null){
            price.setText(product.getPrice());
            title.setText(product.getTitle());
            productId = product.getProductId();
            quantity.setText(String.valueOf(product.getQuantity()));
            category = product.getCategory();
            manufacturer = product.getManufacturer();

        }
        fPrice = String.valueOf(product.getPrice());
        fTitle = String.valueOf(product.getTitle());
        quant = Integer.parseInt(String.valueOf(product.getQuantity()));


        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quant < 10){
                    quant++;
                    quantity.setText(String.valueOf(quant));
                }
            }
        });
        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(quant > 1){
                    quant--;
                    quantity.setText(String.valueOf(quant));
                }
            }
        });

        addRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNewQuantity();
            }

        });

    }

    private void setNewQuantity() {

        Product product = new Product(fTitle, category, manufacturer, fPrice, quant);

        databaseReference.child(productId).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(DetailedAdminActivity.this, "Stock Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }

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
