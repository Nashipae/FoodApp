package android.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class PaymentModeActivity extends AppCompatActivity {

    private Button continueBtn;
    private String userID,mode;
    private RadioButton cod, online;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_mode);

        userID = getIntent().getStringExtra("userID");
        cod = findViewById(R.id.Hindi);
        online = findViewById(R.id.English);

        continueBtn=findViewById(R.id.continueBtn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cod.isChecked())mode = "COD";
                else mode = "online";
                Intent intent=new Intent(PaymentModeActivity.this,PlaceOrderActivity.class);
                intent.putExtra("userID",userID);
                intent.putExtra("mode",mode);
                startActivity(intent);
                finish();
            }
        });
    }
}