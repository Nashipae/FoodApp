package android.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.example.foodapp.Model.Products;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static java.lang.Integer.parseInt;

public class ProductDescriptionActivity extends AppCompatActivity {

    private TextView prodName,prodPrice, prodWeight, prodDescription, quantity;
    private ImageView image;
    private Button viewCart, buyNow;
    private ImageButton decreaseQuantity, increaseQuantity;
    private String pid;
    private String imageURL;
    private String userID;

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
        image = findViewById(R.id.prodImage);
        pid = getIntent().getStringExtra("pid");
        userID = getIntent().getStringExtra("userID");

        Log.d("myAnalysis", "onCreate: " + pid);

        getProductDetails();

        increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity.setText(String.valueOf(parseInt(quantity.getText().toString())+1));
            }
        });

        decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int present = parseInt(quantity.getText().toString());
                if(present>1) quantity.setText(String.valueOf(present-1));
            }
        });

        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddToCart();
            }
        });

        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDescriptionActivity.this, TotalItemsActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });

    }

    private void AddToCart() {
        String timestamp;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy dd MMM hh:mm:ss");
        timestamp = format.format(calendar.getTime());

        DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("CartList");
        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("pid",pid);
        cartMap.put("pname",prodName.getText().toString());
        cartMap.put("price",prodPrice.getText().toString());
        cartMap.put("timestamp",timestamp);
        cartMap.put("quantity",quantity.getText().toString());
        cartMap.put("image",imageURL);
        cartMap.put("pid",pid);

        cartListRef.child("User View").child(userID).child("Products")
                .child(pid)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ProductDescriptionActivity.this, "Item added to cart...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProductDescriptionActivity.this, HomeActivity.class);
                            intent.putExtra("userID",userID);
                            startActivity(intent);
                        }
                    }
                })
        ;


    }

    private void getProductDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Product details...");
        progressDialog.setCancelable(false);


        final DatabaseReference prodReference = FirebaseDatabase.getInstance().getReference().child("Products");
        Log.d("products",pid);
        prodReference.child(pid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    progressDialog.show();
                    Products products = snapshot.getValue(Products.class);
                    prodName.setText(products.getProd_name());
                    prodPrice.setText(products.getProd_price());
                    prodWeight.setText(products.getProd_weight());
                    prodDescription.setText(products.getProd_desc());
                    Picasso.get().load(products.getImage()).into(image);
                    imageURL = products.getImage();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}