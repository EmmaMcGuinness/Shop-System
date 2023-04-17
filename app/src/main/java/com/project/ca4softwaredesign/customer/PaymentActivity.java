package com.project.ca4softwaredesign.customer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.project.ca4softwaredesign.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    Button buyNow;
    double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

         List<CartModel> list = (ArrayList<CartModel>)getIntent().getSerializableExtra("itemList");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            total = bundle.getDouble("total");
        }

        CardForm cardForm = findViewById(R.id.card_form);
         buyNow = findViewById(R.id.btnBuy);

         buyNow.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(cardForm.isValid()) {
                  AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PaymentActivity.this);
                                      alertBuilder.setTitle("Confirm before purchase");
                                      alertBuilder.setMessage("Card number: " + cardForm.getCardNumber() + "\n" +
                                              "Card expiry date: " + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                                              "Card CVV: " + cardForm.getCvv());
                                     alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                             dialog.dismiss();
                                             Toast.makeText(PaymentActivity.this, "Thank you for your purchase", Toast.LENGTH_LONG).show();
                                             Intent intent = new Intent(PaymentActivity.this, PlacedOrderActivity.class);
                                              intent.putExtra("itemList", (Serializable) list);
                                              intent.putExtra("total", total);
                                              startActivity(intent);

                                         }
                                     });
                                     alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                              dialog.dismiss();
                                         }
                                     });
                                     AlertDialog alertDialog = alertBuilder.create();
                                     alertDialog.show();

                 }else{
                     Toast.makeText(PaymentActivity.this, "Please Complete the form", Toast.LENGTH_LONG).show();;

                 }
             }
         });

         cardForm.cardRequired(true)
                 .expirationRequired(true)
                 .cvvRequired(true)
                 .setup(PaymentActivity.this);

         cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

    }
}