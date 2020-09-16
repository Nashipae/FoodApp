package android.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.example.foodapp.Model.Cart;
import android.example.foodapp.Model.Order;
import android.example.foodapp.Model.Products;
import android.example.foodapp.viewHolder.AdminActiveOrdersViewHolder;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminTrackOrdersActivivty extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Order> options;
    private FirebaseRecyclerAdapter<Order, AdminActiveOrdersViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_delivery_history);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference OrderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("Admin View").child("Products");
        final DatabaseReference userOrderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("User View");

        Query query = OrderRef.orderByChild("status").equalTo("Order Booked");

        options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class).build();

        adapter = new FirebaseRecyclerAdapter<Order, AdminActiveOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final AdminActiveOrdersViewHolder holder, int position, @NonNull final Order model) {

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

                holder.userID.setText(model.getUser());
                holder.userAddress.setText(model.getAddress());
                holder.total.setText(model.getTotal());
                holder.price.setText(model.getPrice());
                holder.quantity.setText(model.getQuantity());
                holder.status.setText(model.getStatus());
                holder.deliveryDate.setText(model.getOrderReceivedDate());
                holder.orderDate.setText(model.getOrderOnDate());
                holder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderRef.child(model.getOrderID()).child("status").setValue("Processing");
                        userOrderRef.child(model.getUser()).child(model.getOrderID()).child("status").setValue("Processing");
                        Toast.makeText(AdminTrackOrdersActivivty.this, "updated status", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderRef.child(model.getOrderID()).child("status").setValue("Rejected");
                        userOrderRef.child(model.getUser()).child(model.getOrderID()).child("status").setValue("Rejected");
                        Toast.makeText(AdminTrackOrdersActivivty.this, "updated status", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @NonNull
            @Override
            public AdminActiveOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_active_orders_item,parent,false);
                AdminActiveOrdersViewHolder holder = new AdminActiveOrdersViewHolder(view);
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