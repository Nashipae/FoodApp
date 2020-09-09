package android.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.example.foodapp.Model.Products;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductDescriptionActivity extends AppCompatActivity {

    private TextView prodName,prodPrice, prodWeight, prodDescription, quantity;
    private Button viewCart, buyNow;
    private ImageButton decreaseQuantity, increaseQuantity;
    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);

        prodName = this.<TextView>findViewById(R.id.prodName);
        prodPrice = this.<TextView>findViewById(R.id.prodPrice);
        prodWeight = this.<TextView>findViewById(R.id.prodWeight);
        prodDescription = this.<TextView>findViewById(R.id.prodDesc);
        quantity = this.<TextView>findViewById(R.id.quantity);
        viewCart = this.<Button>findViewById(R.id.viewCart);
        decreaseQuantity = this.<ImageButton>findViewById(R.id.decreaseQuantity);
        increaseQuantity = this.<ImageButton>findViewById(R.id.increaseQuantity);
        buyNow = this.<Button>findViewById(R.id.buyNow);
        pid = getIntent().getStringExtra("pid");

        Log.d("myAnalysis", "onCreate: " + pid);

        getProductDetails();

    }

    private void getProductDetails() {
        final DatabaseReference prodReference = FirebaseDatabase.getInstance().getReference().child("Products");
        Log.d("products",pid);
        prodReference.child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Products products = snapshot.getValue(Products.class);
//                    if(products==null)Log.d("products","null value");
//                    else Log.d("products","obtainable value");
                    prodName.setText(products.getProd_name());
                    prodPrice.setText(products.getProd_price());
                    prodWeight.setText(products.getProd_weight());
                    prodDescription.setText(products.getProd_desc());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}