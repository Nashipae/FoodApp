package android.example.foodapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.example.foodapp.Model.Cart;
import android.example.foodapp.viewHolder.OrderViewHolder;
import android.net.Uri;
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
    private String userID,mode,status,transactionNote,name,upiId,amount;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TextView txtGrandTotal, txtCartTotal, txtUserAddress;
    private Integer GrandSum=0;
    String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    Uri uri;
    FirebaseRecyclerAdapter<Cart, OrderViewHolder> adapter;
    FirebaseRecyclerOptions<Cart> options;
    private ArrayList<Cart> cartItems = new ArrayList<Cart>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        GrandSum=0;
        userID = getIntent().getStringExtra("userID");
        mode = getIntent().getStringExtra("mode");
        Log.d("Debug mode: ",mode);
        name = "SANJEEV KUMAR VERMA VANDHNA VERMA";
        upiId = "skv.chip@okicici";
        changeOrder=findViewById(R.id.changeOrder);
        placeOrder=findViewById(R.id.placeOrder);
        transactionNote = "Test payment";

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
                finish();
            }
        });

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode.equals("online")){
                    Log.d("Debug mode: ",mode);
                    onlinePayment();
                }
                else ValidateOrder();
            }
        });

    }

    private void onlinePayment() {
        uri = new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", transactionNote)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();

        if(isAppInstalled()){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
        }
        else {
            Toast.makeText(this, "Google pay not installed", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            status = data.getStringExtra("Status").toLowerCase();
        }
        if((RESULT_OK == resultCode) && status.equals("success") ){
            Toast.makeText(this, "Transaction successful", Toast.LENGTH_SHORT).show();
            ValidateOrder();
        } else {
            Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isAppInstalled() {
        try {
            this.getPackageManager().getApplicationInfo(GOOGLE_PAY_PACKAGE_NAME,0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
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
            orderItem.put("payment",mode);
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
                                        Intent intent = new Intent(PlaceOrderActivity.this,OrderPlacedActivity.class);
                                        intent.putExtra("userID",userID);
                                        startActivity(intent);
                                        finish();
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
                amount = Integer.toString(GrandSum);
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