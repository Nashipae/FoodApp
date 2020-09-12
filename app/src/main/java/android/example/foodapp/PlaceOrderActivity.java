package android.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlaceOrderActivity extends AppCompatActivity {

    private Button changeOrder,placeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        changeOrder=findViewById(R.id.changeOrder);
        placeOrder=findViewById(R.id.placeOrder);

        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(PlaceOrderActivity.this,OrderPlacedActivity.class);
                startActivity(intent2);
            }
        });
    }
}