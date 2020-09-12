package android.example.foodapp;

import android.content.Intent;
import android.example.foodapp.Model.Products;
import android.example.foodapp.Model.Users;
import android.example.foodapp.viewHolder.ProductViewHolder;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeActivity extends AppCompatActivity {

    private TextView txtUserName, txtUserPhone;
    private Menu menu;
    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference UsersRef;
    private RecyclerView productRecyclerView;
    private FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;
    private FirebaseRecyclerOptions<Products> options;
    private String userID;
    private Users user;

    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        productRecyclerView = (RecyclerView) findViewById(R.id.productRecyclerView);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, true);
        productRecyclerView.setLayoutManager(layoutManager);
        userID = getIntent().getStringExtra("userID");
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TotalItemsActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        txtUserName = headerView.findViewById(R.id.nav_user_name);
        txtUserPhone = headerView.findViewById(R.id.user_phone_number);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavBarUpdateUser();

    }

    private void NavBarUpdateUser() {
        // adding info about user in the navigation bar
        UsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(userID).exists()){
                    user = snapshot.child(userID).getValue(Users.class);
                    txtUserName.setText(user.getUsername());
                    txtUserPhone.setText(user.getPhone());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");

        //options for firebase adapter for products
        options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductRef,Products.class)
                .build();

        //adapter for products on home display
        adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                holder.prodPrice.setText(model.getProd_price());
                holder.prodName.setText(model.getProd_name());
                View.OnClickListener viewMore = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent  = new Intent(HomeActivity.this, ProductDescriptionActivity.class);
                        intent.putExtra("pid",model.getProd_id());
                        intent.putExtra("userID",userID);
                        startActivity(intent);
                    }
                };
                holder.itemView.setOnClickListener(viewMore);
                holder.prodViewMore.setOnClickListener(viewMore);
                Picasso.get().load(model.getImage()).into(holder.prodImage);

            }
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent,false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };
        productRecyclerView.setAdapter(adapter);
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