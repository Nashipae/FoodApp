package com.meadberryfarms.foodapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.Query;
import com.meadberryfarms.foodapp.Model.Products;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.meadberryfarms.foodapp.viewHolder.AdminProdsDeleteViewHolder;

public class AdminProductDelete extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Products> options;
    private FirebaseRecyclerAdapter<Products, AdminProdsDeleteViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_delete);

        recyclerView = findViewById(R.id.products);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    protected void onStart() {
        super.onStart();
        Query query = FirebaseDatabase.getInstance().getReference().child("Products");
        final DatabaseReference ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");
        options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(query, Products.class).build();
        adapter = new FirebaseRecyclerAdapter<Products, AdminProdsDeleteViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminProdsDeleteViewHolder holder, int position, @NonNull final Products model) {
                holder.pnametxt.setText(model.prod_name);
                holder.pidtxt.setText(model.prod_id);
                holder.deletebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProductRef.child(model.prod_id).removeValue();
                    }
                });

            }

            @NonNull
            @Override
            public AdminProdsDeleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_delete_product_item, parent, false);
                return new AdminProdsDeleteViewHolder(view);
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