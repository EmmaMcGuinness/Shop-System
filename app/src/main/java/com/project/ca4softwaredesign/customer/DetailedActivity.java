package com.project.ca4softwaredesign.customer;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.project.ca4softwaredesign.Product;
import com.project.ca4softwaredesign.R;
import com.project.ca4softwaredesign.admin.DetailedAdminActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailImg;
    TextView price, reviewText, title, quantity;
    ImageView reviewImg;
    int totalQuantity = 0;
    int totalPrice = 1;
    String productId, category, manufacturer, fPrice, fTitle;
    int quant = 0;
    int finalQuant = 0;

    Button addToCart;
    ImageView addItem, removeItem;
    Product product = null;
    Toolbar toolbar;

    FirebaseStorage firebaseStorage;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final ArrayList<ReviewModel> ratingList = new ArrayList<>();





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
        reviewText = findViewById(R.id.textViewReview);
        reviewImg = findViewById(R.id.imageReview);
        title = findViewById(R.id.detail_name);
        quantity = findViewById(R.id.quantity);

        if(product != null){
            price.setText(product.getPrice());
            title.setText(product.getTitle());
            productId = product.getProductId();
            quant = product.getQuantity();
            category = product.getCategory();
            manufacturer = product.getManufacturer();

        }
        fPrice = String.valueOf(product.getPrice());
        fTitle = String.valueOf(product.getTitle());

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
                updateQuantity();
            }


        });

        reviewImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailedActivity.this, ReviewActivity.class);
                i.putExtra("ProductId", productId);
                startActivity(i);

            }
        });

        rating();





    }

    private void rating() {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ratingList.clear();


                for (DataSnapshot events : snapshot.child("Reviews").getChildren()) {
                    final String getReview = events.child("review").getValue(String.class);
                    final String getRating = events.child("rating").getValue(String.class);
                    final String getUserPhone = events.child("userPhone").getValue(String.class);
                    final String getProductId = events.child("productId").getValue(String.class);

                    ReviewModel reviewModel = new ReviewModel(getReview, getRating, getUserPhone, getProductId);

                    ratingList.add(reviewModel);

                }
                calculateRating();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void calculateRating() {
        double sum=0.0, number=0.0;
        for (int i =0; i < ratingList.size(); i+= 1){
            double rating = Double.parseDouble(ratingList.get(i).getRating());

            number = number+ rating;
            sum = number/ratingList.size();
        }
        reviewText.setText(String.valueOf(sum));
    }


    private void addedToCart() {

        final HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("productName", title.getText().toString());
        cartMap.put("productPrice", price.getText().toString());
        cartMap.put("totalQuantity", quantity.getText().toString());
        cartMap.put("totalPrice", totalPrice);
        cartMap.put("productId", productId);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child("AddToCart").push()
                .setValue(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(DetailedActivity.this, "Item added to Cart", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateQuantity() {
        finalQuant = quant - totalQuantity;

        Product product = new Product(fTitle, category, manufacturer, fPrice, finalQuant);

        databaseReference.child("Products").child(productId).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
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
