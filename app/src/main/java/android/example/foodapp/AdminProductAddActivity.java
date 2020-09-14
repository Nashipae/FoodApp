package android.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class AdminProductAddActivity extends AppCompatActivity {

    private String prodType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_add);

        prodType = getIntent().getStringExtra("type");

//        uncomment for testing
//        Toast.makeText(this, prodType, Toast.LENGTH_SHORT).show();
        
    }
}