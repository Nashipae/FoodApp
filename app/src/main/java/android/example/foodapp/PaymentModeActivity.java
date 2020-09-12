package android.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PaymentModeActivity extends AppCompatActivity {

    private Button continueBtn;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_mode);

        userID = getIntent().getStringExtra("userID");

        continueBtn=findViewById(R.id.continueBtn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PaymentModeActivity.this,PlaceOrderActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });
    }
}