package com.project.ca4softwaredesign.admin;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.project.ca4softwaredesign.Product;
import com.project.ca4softwaredesign.R;

import java.util.ArrayList;

public class HomeAdminFragment extends Fragment {
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String category, manufacturer, filter, imageUrl;
    SearchView searchView;
    Spinner sFilter;

    private StorageTask uploadTask;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storagePicRef = firebaseStorage.getReference().child("images/products/product.jpg");

    ProductAdminAdapter productAdminAdapter;

    private final ArrayList<Product> productList = new ArrayList<>();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ActivityResultLauncher<Intent> launcher;

    public HomeAdminFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_admin, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        Glide.with(this);

        searchView = view.findViewById(R.id.search);
        search();
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

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
        floatingActionButton = view.findViewById(R.id.addProduct);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();

            }

        });

         launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImage = data.getData();
                            uploadImage(selectedImage, storagePicRef);

                        }
                    }
                });


        return view;
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
        productAdminAdapter.searchDataList(searchList);
    }

    private void addProduct() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.add_product, null);
        myDialog.setView(myView);

        storagePicRef = FirebaseStorage.getInstance().getReference().child("Product pic");

        AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);
        dialog.show();

        EditText editTitle = myView.findViewById(R.id.editTextTitle);

        Spinner sCategory = myView.findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCategory.setAdapter(adapter);

        Spinner sManufacturer = myView.findViewById(R.id.manufacturerSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.manufacturer, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sManufacturer.setAdapter(adapter2);

        EditText editPrice = myView.findViewById(R.id.editTextPrice);
        EditText editQuantity = myView.findViewById(R.id.editTextQuantity);

        Button changeImage = myView.findViewById(R.id.change_image_btn);
        Button save = myView.findViewById(R.id.save_button);
        Button cancel = myView.findViewById(R.id.cancel_button);

        sCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sManufacturer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                manufacturer = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                launcher.launch(intent);
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
                String title = editTitle.getText().toString().trim();
                String price = editPrice.getText().toString().trim();
                int quantity = Integer.parseInt(editQuantity.getText().toString());

                if (title.isEmpty()) {
                    editTitle.setError("Title is required");
                    editTitle.requestFocus();
                    return;
                }
                if (category.isEmpty()) {
                    Toast.makeText(getActivity(), "Category is required", Toast.LENGTH_SHORT).show();
                    sCategory.requestFocus();
                    return;
                }
                if (manufacturer.isEmpty()) {
                    Toast.makeText(getActivity(), "Manufacturer is required", Toast.LENGTH_SHORT).show();
                    sManufacturer.requestFocus();
                    return;
                }
                if (price.isEmpty()) {
                    editPrice.setError("Price is required");
                    editPrice.requestFocus();
                    return;
                }
                if (quantity == 0) {
                    editQuantity.setError("Quantity is required");
                    editQuantity.requestFocus();
                    return;
                }



                Product product = new Product(title, category, manufacturer, price, quantity, imageUrl);
                FirebaseDatabase.getInstance().getReference("Products").push()
                        .setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(getActivity(), "Event successfully added", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(getActivity(), "Event not added", Toast.LENGTH_LONG).show();
                                }

                            }
                        });


            }

        });
    }

    private void getImage() {

    }

    private void uploadImage(Uri selectedImage, StorageReference storagePicRef) {
        StorageReference imageRef = storagePicRef.child(selectedImage.getLastPathSegment());


        UploadTask uploadTask = imageRef.putFile(selectedImage);

        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    imageUrl = downloadUri.toString();
                    Toast.makeText(getActivity(), "Image added", Toast.LENGTH_SHORT ).show();
                });
            } else {

            }
        });
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
                    final String getProductId = events.getKey();
                    final int getQuantity = events.child("quantity").getValue(Integer.class);
                    final String imageUrl = events.child("imageUrl").getValue(String.class);

                    Product product = new Product(getTitle, getCategory, getManufacturer, getPrice, getProductId, getQuantity, imageUrl);

                    productList.add(product);

                }
                productAdminAdapter = new ProductAdminAdapter(getActivity(), productList);
                recyclerView.setAdapter(productAdminAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}