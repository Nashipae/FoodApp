package Meadberry_Farms.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.foodapp.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminProductTypeActivity extends AppCompatActivity {

    private Button typeCow, typeFish, typeBuff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_type);

        typeBuff = findViewById(R.id.typeBuff);
        typeCow = findViewById(R.id.typeCow);
        typeFish = findViewById(R.id.typeFish);

        typeBuff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProductTypeActivity.this, AdminProductAddActivity.class);
                intent.putExtra("type","2");
                startActivity(intent);
            }
        });

        typeFish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProductTypeActivity.this, AdminProductAddActivity.class);
                intent.putExtra("type","1");
                startActivity(intent);
            }
        });

        typeCow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProductTypeActivity.this, AdminProductAddActivity.class);
                intent.putExtra("type","0");
                startActivity(intent);
            }
        });
    }
}