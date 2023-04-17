package com.project.ca4softwaredesign;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    TextView profileName, profileRole, profileAddress, profileEmail, profilePassword, profilePhone;
    Button logout;
    private FirebaseUser user;
    private String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileName = view.findViewById(R.id.name);
        profileRole = view.findViewById(R.id.role);
        profileAddress = view.findViewById(R.id.address);
        profileEmail = view.findViewById(R.id.email);
        profilePhone = view.findViewById(R.id.phone);
        profilePassword = view.findViewById(R.id.password);
        logout = view.findViewById(R.id.logOut);



        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        DatabaseReference fireDB = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        fireDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userObj = snapshot.getValue(User.class);
                profileName.setText(userObj.getName());
                profileRole.setText(userObj.getAdmin());
                profileEmail.setText(userObj.getEmail());
                profilePhone.setText(userObj.getPhoneNumber());
                profileAddress.setText(userObj.getAddress());
                profilePassword.setText(userObj.getPassword());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);
            }
        });
        return view;

    }
}