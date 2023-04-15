package com.project.ca4softwaredesign.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.ca4softwaredesign.Product;
import com.project.ca4softwaredesign.ProductAdapter;
import com.project.ca4softwaredesign.R;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {
    Button writeReview;
    String rating, productId, review;
    FirebaseUser user;
    Toolbar toolbar;
    ReviewAdapter reviewAdapter;
    EditText editReview;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private final ArrayList<ReviewModel> reviewList = new ArrayList<>();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(ReviewActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        user = FirebaseAuth.getInstance().getCurrentUser();

        writeReview = findViewById(R.id.writeReview);

        productId = getIntent().getStringExtra("ProductId");

        writeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReview();

            }
        });

        addReviewsToRecycler();

    }

    private void addReview() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(ReviewActivity.this);
        LayoutInflater inflater = LayoutInflater.from(ReviewActivity.this);
        View myView = inflater.inflate(R.layout.add_review, null);
        myDialog.setView(myView);

        AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);
        dialog.show();

        editReview = myView.findViewById(R.id.editReview);
        Log.d("review", String.valueOf(editReview));

        Spinner sRate = myView.findViewById(R.id.ratingSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ReviewActivity.this, R.array.rate, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sRate.setAdapter(adapter);

        Button save = myView.findViewById(R.id.save_button);
        Button cancel = myView.findViewById(R.id.cancel_button);

        sRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rating = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                review = editReview.getText().toString();


                if (review.isEmpty()) {
                    editReview.setError("Review is required");
                    editReview.requestFocus();
                    return;
                }
                if (rating.isEmpty()) {
                    Toast.makeText(ReviewActivity.this, "Rating is required", Toast.LENGTH_SHORT).show();
                    sRate.requestFocus();
                    return;
                }


                ReviewModel reviewModel = new ReviewModel(review, rating, user.getPhoneNumber(),productId);
                FirebaseDatabase.getInstance().getReference("Reviews").push()
                        .setValue(reviewModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(ReviewActivity.this, "Review successfully added", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(ReviewActivity.this, "Review not added", Toast.LENGTH_LONG).show();
                                }

                            }
                        });


            }

        });
    }
    private void addReviewsToRecycler() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                reviewList.clear();


                for (DataSnapshot events : snapshot.child("Reviews").getChildren()) {
                    final String getReview = events.child("review").getValue(String.class);
                    final String getRating = events.child("rating").getValue(String.class);
                    final String getUserPhone = events.child("userPhone").getValue(String.class);
                    final String getProductId = events.child("productId").getValue(String.class);

                    ReviewModel reviewModel = new ReviewModel(getReview, getRating, getUserPhone, getProductId);

                    reviewList.add(reviewModel);

                }
                reviewAdapter = new ReviewAdapter(ReviewActivity.this, reviewList);
                recyclerView.setAdapter(reviewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}