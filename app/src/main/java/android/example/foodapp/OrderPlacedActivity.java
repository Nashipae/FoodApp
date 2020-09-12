package android.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OrderPlacedActivity extends AppCompatActivity {

    private Button BackToHome;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);

        userID = getIntent().getStringExtra("userID");
        BackToHome = (Button) findViewById(R.id.done_order_home);
        BackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderPlacedActivity.this, HomeActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });
    }
}