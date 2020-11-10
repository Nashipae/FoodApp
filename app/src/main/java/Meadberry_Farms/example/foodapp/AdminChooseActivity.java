package Meadberry_Farms.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import com.meadberryfarms.foodapp.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminChooseActivity extends AppCompatActivity {

    private Button Add,Order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_choose);

        Add = findViewById(R.id.addProduct);
        Order = findViewById(R.id.editOrders);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminChooseActivity.this, AdminProductTypeActivity.class);
                startActivity(intent);
            }
        });

        Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminChooseActivity.this, AdminTrackOrdersActivivty.class);
                startActivity(intent);
            }
        });
    }
}