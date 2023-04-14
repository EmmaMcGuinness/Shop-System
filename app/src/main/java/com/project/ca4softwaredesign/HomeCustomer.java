package com.project.ca4softwaredesign;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeCustomer extends AppCompatActivity {
    private FirebaseUser user;
    private String userID;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);
        View headerView = navigationView.getHeaderView(0);
        TextView headerName = headerView.findViewById(R.id.nav_header_name);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        fireDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userObj = snapshot.getValue(User.class);
                headerName.setText(userObj.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
