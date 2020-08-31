package android.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EnterOTPActivity extends AppCompatActivity {

    private Button nextBtn;
    private String[] digits;
    private String otp;
    private EditText num1,num2,num3,num4;
    private String USERPHONE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_o_t_p);
        num1 = (EditText) findViewById(R.id.otp1);
        num2 = (EditText) findViewById(R.id.otp2);
        num3 = (EditText) findViewById(R.id.otp3);
        num4 = (EditText) findViewById(R.id.otp4);
        nextBtn=(Button) findViewById(R.id.nextBtn);
        USERPHONE = getIntent().getStringExtra("USERPHONE");


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOtp();
                if(joinEditText(new EditText[]{num1,num2,num3,num4}).equals(otp)){
                    Intent intent =new Intent(EnterOTPActivity.this,NewPasswordActivity.class);
                    intent.putExtra("USERPHONE",USERPHONE);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(EnterOTPActivity.this,"Incorrect OTP recieved",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendOtp() {
        otp = "1234";
    }

    private String joinEditText(EditText[] texts){
        String str = "";
        for(int i=0;i<texts.length;i++){
            str+=texts[i].getText().toString();
        }
        return str;
    }
}