package com.project.ca4softwaredesign.customer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.ca4softwaredesign.R;
import com.project.ca4softwaredesign.Product;
import com.project.ca4softwaredesign.ProductAdapter;

import java.util.ArrayList;

public class HomeCustomerFragment extends Fragment {
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProductAdapter productAdapter;
    Spinner sFilter;
    String filter;
    SearchView searchView;
    private final ArrayList<Product> productList = new ArrayList<>();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_customer, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchView = view.findViewById(R.id.search);
        search();
        sFilter = view.findViewById(R.id.filterSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.filter, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sFilter.setAdapter(adapter);

        addProductsToRecycler();

        return view;
    }

    private void addProductsToRecycler() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                productList.clear();


                for (DataSnapshot events : snapshot.child("Products").getChildren()) {
                    final String getTitle = events.child("title").getValue(String.class);
                    final String getCategory = events.child("category").getValue(String.class);
                    final String getManufacturer = events.child("manufacturer").getValue(String.class);
                    final String getPrice = events.child("price").getValue(String.class);

                    Product product = new Product(getTitle, getCategory, getManufacturer, getPrice);

                    productList.add(product);

                }
                productAdapter = new ProductAdapter(getActivity(), productList);
                recyclerView.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void search() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchList(s);
                return true;
            }
        });
    }
    public void searchList(String text){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ArrayList<Product> searchList = new ArrayList<>();
        for(Product product: productList){
            if(filter.equalsIgnoreCase( "title")) {
                if (product.getTitle().toLowerCase().contains(text.toLowerCase())) {
                    searchList.add(product);
                }
            }else if(filter.equalsIgnoreCase( "category")){
                if (product.getCategory().toLowerCase().contains(text.toLowerCase())) {
                    searchList.add(product);
                }
            }else if(filter.equalsIgnoreCase( "manufacturer")){
                if (product.getManufacturer().toLowerCase().contains(text.toLowerCase())) {
                    searchList.add(product);
                }
            }
        }
        productAdapter.searchDataList(searchList);
    }
}