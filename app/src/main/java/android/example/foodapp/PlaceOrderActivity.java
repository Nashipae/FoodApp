package android.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.example.foodapp.Model.Cart;
import android.example.foodapp.viewHolder.OrderViewHolder;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

public class PlaceOrderActivity extends AppCompatActivity {

    private Button changeOrder,placeOrder;
    private String userID;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView txtGrandTotal, txtCartTotal, txtUserAddress;
    private Integer GrandSum=0;
    FirebaseRecyclerAdapter<Cart, OrderViewHolder> adapter;
    FirebaseRecyclerOptions<Cart> options;
    private ArrayList<Cart> cartItems = new ArrayList<Cart>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        userID = getIntent().getStringExtra("userID");
        changeOrder=findViewById(R.id.changeOrder);
        placeOrder=findViewById(R.id.placeOrder);


        txtGrandTotal = findViewById(R.id.cartGrandTotal);
        txtCartTotal = findViewById(R.id.cartTotalPrice);
        txtUserAddress = findViewById(R.id.userAddress);

        setUserAddress();

        recyclerView = findViewById(R.id.cartList);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        changeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PlaceOrderActivity.this,TotalItemsActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateOrder();
            }
        });

    }

    private void setUserAddress() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                txtUserAddress.setText(snapshot.child("address").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ValidateOrder() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MMM dd hh:mm:ss");
        String timestamp = simpleDateFormat.format(calendar.getTime());
        DatabaseReference OrderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        final DatabaseReference CartRef = FirebaseDatabase.getInstance().getReference().child("CartList").child("User View").child(userID);

        for(Cart items: cartItems){

            final DatabaseReference UserView = OrderRef.child("User View").child(userID).child(timestamp+"_"+items.getPid()+"_"+userID);
            DatabaseReference AdminView = OrderRef.child("Admin View").child("Products").child(timestamp+"_"+items.getPid()+"_"+userID);
            final HashMap<String, Object> orderItem = new HashMap<>();
            orderItem.put("orderID",timestamp+"_"+items.getPid()+"_"+userID);
            orderItem.put("quantity", items.getQuantity());
            orderItem.put("price", items.getPrice());
            orderItem.put("total", Integer.toString(Integer.valueOf(items.getPrice())*Integer.valueOf(items.getQuantity())));
            orderItem.put("orderOnDate",timestamp);
            orderItem.put("orderReceivedDate","NULL");
            orderItem.put("payment","COD");
            orderItem.put("status","Order Booked");
            orderItem.put("user",userID);
            orderItem.put("pid",items.getPid());
            orderItem.put("address",txtUserAddress.getText());

            AdminView.updateChildren(orderItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    UserView.updateChildren(orderItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                CartRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(PlaceOrderActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(PlaceOrderActivity.this,HomeActivity.class);
                                        intent.putExtra("userID",userID);
                                        startActivity(intent);
                                    }
                                });
                            }
                            else{
                                Toast.makeText(PlaceOrderActivity.this, "Order Placement failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            });


//            orderID,quantity,price,total,orderOnDate, orderReceivedDate, payment, status,pid
        }
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

        adapter = new FirebaseRecyclerAdapter<Cart, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final Cart model) {
                cartItems.add(model);
                holder.txtPName.setText(model.getPname());
                holder.txtPQuantity.setText("Quantity: " +model.getQuantity());
                holder.txtPPrice.setText("Price: " +model.getPrice());

                GrandSum += Integer.parseInt(model.getQuantity())*Integer.parseInt(model.getPrice());
                txtGrandTotal.setText(Integer.toString(GrandSum)+".00");
                txtCartTotal.setText(Integer.toString(GrandSum)+".00");
                Picasso.get().load(model.getImage()).into(holder.image);
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_items_layout,parent,false);
                OrderViewHolder holder = new OrderViewHolder(view);
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
            Toast.makeText(this, "Displayed all available items in cart", Toast.LENGTH_SHORT).show();
            adapter.stopListening();
        }
    }
}