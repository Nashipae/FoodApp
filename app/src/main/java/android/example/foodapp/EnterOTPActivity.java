package android.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chaos.view.PinView;

public class EnterOTPActivity extends AppCompatActivity {

    private Button nextBtn;
    private String[] digits;
    private String otp;
    private EditText num1,num2,num3,num4;
    private EditText[] editTexts;
    private String USERPHONE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_o_t_p);
        num1 = (EditText) findViewById(R.id.otp1);
        num2 = (EditText) findViewById(R.id.otp2);
        num3 = (EditText) findViewById(R.id.otp3);
        num4 = (EditText) findViewById(R.id.otp4);

        num1.addTextChangedListener(new GenericTextWatcher(num1));
        num2.addTextChangedListener(new GenericTextWatcher(num2));
        num3.addTextChangedListener(new GenericTextWatcher(num3));
        num4.addTextChangedListener(new GenericTextWatcher(num4));

        nextBtn=(Button) findViewById(R.id.nextBtn);
        editTexts = new EditText[]{num1, num2, num3, num4};
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

    public class GenericTextWatcher implements TextWatcher
    {
        private View view;
        private GenericTextWatcher(View view)
        {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch(view.getId())
            {

                case R.id.otp1:
                    if(text.length()==1)
                        num2.requestFocus();
                    break;
                case R.id.otp2:
                    if(text.length()==1)
                        num3.requestFocus();
                    else if(text.length()==0)
                        num1.requestFocus();
                    break;
                case R.id.otp3:
                    if(text.length()==1)
                        num4.requestFocus();
                    else if(text.length()==0)
                        num2.requestFocus();
                    break;
                case R.id.otp4:
                    if(text.length()==0)
                        num3.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }

}

