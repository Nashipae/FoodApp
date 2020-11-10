package Meadberry_Farms.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Meadberry_Farms.example.foodapp.Model.Order;

import com.meadberryfarms.foodapp.R;

import Meadberry_Farms.example.foodapp.viewHolder.AdminActiveOrdersViewHolder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdminTrackOrdersActivivty extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseRecyclerOptions<Order> options;
    private FirebaseRecyclerAdapter<Order, AdminActiveOrdersViewHolder> adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button active,processing,delivered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_track_orders_activivty);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        active = findViewById(R.id.activeOrders);
        processing = findViewById(R.id.processingOrders);
        delivered = findViewById(R.id.deliveredOrders);

        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrders(0);
            }
        });

        processing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrders(1);
            }
        });

        delivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrders(2);
            }
        });

    }

    private void showOrders(final int status) {
        final DatabaseReference OrderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("Admin View").child("Products");
        final DatabaseReference userOrderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child("User View");
        Query query;
        if(status==0) query = OrderRef.orderByChild("status").equalTo("Order Booked");
        else if(status==1) query = OrderRef.orderByChild("status").equalTo("Processing");
        else query = OrderRef.orderByChild("status").equalTo("Delivered");

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
                if(status==0) holder.accept.setText("Accept Order");
                else if(status==1) holder.accept.setText("Mark Delivered");
                else holder.accept.setText("View Active Orders");
                holder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(status==0){
                            OrderRef.child(model.getOrderID()).child("status").setValue("Processing");
                            userOrderRef.child(model.getUser()).child(model.getOrderID()).child("status").setValue("Processing");
                            Toast.makeText(AdminTrackOrdersActivivty.this, "updated status", Toast.LENGTH_SHORT).show();
                        }
                        else if(status==1){
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MMM dd");
                            String todayDate =  simpleDateFormat.format(calendar.getTime());

                            OrderRef.child(model.getOrderID()).child("status").setValue("Delivered");
                            OrderRef.child(model.getOrderID()).child("orderReceivedDate").setValue(todayDate);
                            userOrderRef.child(model.getUser()).child(model.getOrderID()).child("status").setValue("Delivered");
                            userOrderRef.child(model.getUser()).child(model.getOrderID()).child("orderReceivedDate").setValue(todayDate);
                            Toast.makeText(AdminTrackOrdersActivivty.this, "updated status", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            showOrders(0);
                        }
                    }
                });
                if(status==0 || status==1)holder.reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderRef.child(model.getOrderID()).child("status").setValue("Rejected");
                        userOrderRef.child(model.getUser()).child(model.getOrderID()).child("status").setValue("Rejected");
                        Toast.makeText(AdminTrackOrdersActivivty.this, "updated status", Toast.LENGTH_SHORT).show();
                    }
                });
                else {
                    holder.reject.setText("View Processing Orders");
                    holder.reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showOrders(1);
                        }
                    });
                }


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