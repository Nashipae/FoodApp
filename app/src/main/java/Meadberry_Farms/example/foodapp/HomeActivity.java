package Meadberry_Farms.example.foodapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import Meadberry_Farms.example.foodapp.Model.Products;
import Meadberry_Farms.example.foodapp.Model.Users;

import android.example.foodapp.R;

import Meadberry_Farms.example.foodapp.viewHolder.ProductViewHolder;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.BuildConfig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private TextView txtUserName, txtUserPhone;
    private CircleImageView userImage;
    private Menu menu;
    private AppBarConfiguration mAppBarConfiguration;
    private DatabaseReference UsersRef;
    private RecyclerView productRecyclerView;
    private FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;
    private FirebaseRecyclerOptions<Products> options;
    private String userID;
    private Users user;
    private ImageButton homeType0,homeType1,homeType2,ham_icon;
    private Button allTypes;

    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        setNavigationViewListener();


        productRecyclerView = (RecyclerView) findViewById(R.id.productRecyclerView);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, true);
        productRecyclerView.setLayoutManager(layoutManager);
        userID = getIntent().getStringExtra("userID");
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        homeType0 = findViewById(R.id.homeType0);
        homeType1 = findViewById(R.id.homeType1);
        homeType2 = findViewById(R.id.homeType2);
        homeType2 = findViewById(R.id.homeType2);
        allTypes = findViewById(R.id.all_category);

        homeType0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeType0.setImageResource(R.drawable.cow);
                homeType1.setImageResource(R.drawable.fish_dull);
                homeType2.setImageResource(R.drawable.bull_dull);
                allTypes.setTextColor(getResources().getColor(R.color.colorPrimary));
                allTypes.setBackgroundResource(R.drawable.all_btn_background);
                changeQuery("0");
            }
        });

        allTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeType0.setImageResource(R.drawable.cow_dull);
                homeType1.setImageResource(R.drawable.fish_dull);
                homeType2.setImageResource(R.drawable.bull_dull);
                allTypes.setBackgroundResource(R.drawable.btn_rounded);
                allTypes.setTextColor(Color.WHITE);
                changeQuery("all");
            }
        });

        homeType1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeType0.setImageResource(R.drawable.cow_dull);
                homeType1.setImageResource(R.drawable.fish);
                homeType2.setImageResource(R.drawable.bull_dull);
                allTypes.setTextColor(getResources().getColor(R.color.colorPrimary));
                allTypes.setBackgroundResource(R.drawable.all_btn_background);
                changeQuery("1");
            }
        });

        homeType2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeType0.setImageResource(R.drawable.cow_dull);
                homeType1.setImageResource(R.drawable.fish_dull);
                homeType2.setImageResource(R.drawable.bull);
                allTypes.setTextColor(getResources().getColor(R.color.colorPrimary));
                allTypes.setBackgroundResource(R.drawable.all_btn_background);
                changeQuery("2");
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("Home");
//
//        toolbar.setNavigationIcon(R.drawable.hamburger_icon);
//        setSupportActionBar(toolbar);

         ham_icon= findViewById(R.id.ham_icon);
         ham_icon.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                 if(!drawer.isDrawerOpen(Gravity.LEFT)) drawer.openDrawer(Gravity.LEFT);
                 else drawer.closeDrawer(Gravity.LEFT);
             }
         });





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
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                int id=item.getItemId();

                switch (id) {

                    case R.id.nav_dashboard:{
                        Intent intent= new Intent(HomeActivity.this,DashboardActivity.class);
                        intent.putExtra("userID",userID);
                        startActivity(intent);
                        break;
                    }
//                    case R.id.nav_language:{
//                        Intent intent= new Intent(HomeActivity.this,SelectLanguageActivity.class);
//                        intent.putExtra("userID",userID);
//                        startActivity(intent);
//                        break;
//                    }
                    case R.id.delivery_history:{
                        Intent intent= new Intent(HomeActivity.this,UserDeliveryHistoryActivity.class);
                        intent.putExtra("userID",userID);
                        startActivity(intent);
                        break;
                    }

                    case R.id.nav_share:{
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                            String shareMessage= "\nLet me recommend you this application\n\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch(Exception e) {
                            e.toString();
                        }
                        break;
                    }

                    case R.id.nav_contact:{
                        Intent intent= new Intent(HomeActivity.this,ContactUsActivity.class);
                        intent.putExtra("userID",userID);
                        startActivity(intent);
                        break;
                    }

                    case R.id.nav_rate:{
                        try{
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
                        }
                        catch (ActivityNotFoundException e){
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                        }
                        break;
                    }

                    case R.id.nav_logout:{
                        Intent intent= new Intent(HomeActivity.this,LoginActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
                return true;
            }
        });
        

        View headerView = navigationView.getHeaderView(0);
        txtUserName = headerView.findViewById(R.id.nav_user_name);
        txtUserPhone = headerView.findViewById(R.id.user_phone_number);
        userImage = headerView.findViewById(R.id.profile_image);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);
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
                    Picasso.get().load(user.getImage()).into(userImage);
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



//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }



    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");

        //setting the query
        Query query = ProductRef.orderByChild("prod_weight");

        //options for firebase adapter for products
        options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(query,Products.class)
                .build();

        //adapter for products on home display
        adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                holder.prodPrice.setText("Rs. "+model.getProd_price()+"/-");
                holder.prodName.setText(model.getProd_name());
                holder.prodWeight.setText(model.getProd_weight()+ "Kg");

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

    private void changeQuery(String type){
        final DatabaseReference ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");

        //setting the new query
        Query query;
        if(type!="all") query = ProductRef.orderByChild("prod_type").equalTo(type);
        else query = ProductRef.orderByChild("prod_type");

        //changing the options
        options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(query, Products.class)
                .build();

        // Change options of adapter.
        adapter.updateOptions(options);
    }



    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null) {
            adapter.stopListening();
        }
    }
}