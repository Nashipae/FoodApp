package android.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.example.foodapp.Model.Cart;
import android.example.foodapp.viewHolder.OrderViewHolder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class PlaceOrderActivity extends AppCompatActivity {

    private Button changeOrder,placeOrder;
    private String userID;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Cart, OrderViewHolder> adapter;
    FirebaseRecyclerOptions<Cart> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        userID = getIntent().getStringExtra("userID");
        changeOrder=findViewById(R.id.changeOrder);
        placeOrder=findViewById(R.id.placeOrder);

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
                Intent intent=new Intent(PlaceOrderActivity.this,OrderPlacedActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
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

        adapter = new FirebaseRecyclerAdapter<Cart, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull final Cart model) {
                holder.txtPName.setText(model.getPname());
                holder.txtPQuantity.setText("Quantity: " +model.getQuantity());
                holder.txtPPrice.setText("Price: " +model.getPrice());
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
            adapter.stopListening();
        }
    }
}