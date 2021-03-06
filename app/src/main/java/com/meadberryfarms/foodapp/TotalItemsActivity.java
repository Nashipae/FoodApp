package com.meadberryfarms.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import com.meadberryfarms.foodapp.Model.Cart;

import com.meadberryfarms.foodapp.viewHolder.CartViewHolder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class TotalItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button placeOrder;
    private TextView txtGrandTotal, txtCartTotal;
    private Integer GrandSum=0;
    private String userID;
    FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter;
    FirebaseRecyclerOptions<Cart> options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_items);

        userID = getIntent().getStringExtra("userID");

        recyclerView = findViewById(R.id.cartList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtGrandTotal = findViewById(R.id.cartGrandTotal);
        txtCartTotal = findViewById(R.id.cartTotalPrice);

        placeOrder=findViewById(R.id.placeOrder);

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GrandSum>0){
                    Intent intent=new Intent(TotalItemsActivity.this,PersonInformationActivity.class);
                    intent.putExtra("userID",userID);
                    startActivity(intent);
                    finish();
                }
                else Toast.makeText(TotalItemsActivity.this, "Try adding some products to cart first...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("CartList");

        options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery
                (
                    cartListRef.child("User View").child(userID).child("Products"),Cart.class
                )
                .build();

        adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model) {
                GrandSum += Integer.parseInt(model.getQuantity())*Integer.parseInt(model.getPrice());
                holder.txtPName.setText(model.getPname());
                holder.txtPQuantity.setText("Quantity: " +model.getQuantity());
                holder.txtPPrice.setText("Rs." +model.getPrice()+"/-");
                holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cartListRef.child("User View").child(userID).child("Products")
                                .child(model.getPid()).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(TotalItemsActivity.this, "Item removed from cart...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        GrandSum-=Integer.parseInt(model.getQuantity())*Integer.parseInt(model.getPrice());
                        txtGrandTotal.setText(Integer.toString(GrandSum)+".00");
                        txtCartTotal.setText(Integer.toString(GrandSum)+".00");
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TotalItemsActivity.this, ProductDescriptionActivity.class);
                        intent.putExtra("userID",userID);
                        intent.putExtra("pid",model.getPid());
                        startActivity(intent);
                    }
                });
                Picasso.get().load(model.getImage()).into(holder.image);
                txtGrandTotal.setText(Integer.toString(GrandSum)+".00");
                txtCartTotal.setText(Integer.toString(GrandSum)+".00");
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return  holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null) {
            adapter.stopListening();
        }
    }
}