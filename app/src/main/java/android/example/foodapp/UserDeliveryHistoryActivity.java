package android.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.example.foodapp.Model.Cart;
import android.example.foodapp.Model.Order;
import android.example.foodapp.Model.Products;
import android.example.foodapp.viewHolder.CartViewHolder;
import android.example.foodapp.viewHolder.OrderViewHolder;
import android.example.foodapp.viewHolder.UserOrderViewHolder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserDeliveryHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Order> options;
    private FirebaseRecyclerAdapter<Order, UserOrderViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_delivery_history);

        userID = getIntent().getStringExtra("userID");
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference OrderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("User View").child(userID);

        options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(OrderRef, Order.class).build();

        adapter = new FirebaseRecyclerAdapter<Order, UserOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final UserOrderViewHolder holder, int position, @NonNull Order model) {
                DatabaseReference product = FirebaseDatabase.getInstance().getReference().child("Products").child(model.getPid());
                product.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        holder.name.setText(snapshot.child("prod_name").getValue().toString());
                        holder.desc.setText(snapshot.child("prod_desc").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
                holder.total.setText(model.getTotal());
                holder.price.setText(model.getPrice());
                holder.quantity.setText(model.getQuantity());
                holder.status.setText(model.getStatus());
                holder.deliveryDate.setText(model.getOrderReceivedDate());
                holder.orderDate.setText(model.getOrderOnDate());

            }

            @NonNull
            @Override
            public UserOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_order_list_item,parent,false);
                UserOrderViewHolder holder = new UserOrderViewHolder(view);
                return holder;
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