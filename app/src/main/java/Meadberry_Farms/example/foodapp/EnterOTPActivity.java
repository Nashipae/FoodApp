package Meadberry_Farms.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.foodapp.R;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class EnterOTPActivity extends AppCompatActivity {

    private Button nextBtn;
    private String[] digits;
    private String otp;
    private EditText num1,num2,num3,num4;
    private EditText[] editTexts;
    private String USERPHONE;
    private int randomOtp;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_o_t_p);
        userID = getIntent().getStringExtra("userID");
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

        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        sendOtp();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(joinEditText(new EditText[]{num1,num2,num3,num4}).equals(Integer.toString(randomOtp))){
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
        try {
            // Construct data
            String apiKey = "apikey=" + "XiVfDCguQpk-d8pv26clK53u7gbtnYFpHih5y5oMPm";
            Random random = new Random();
            randomOtp = random.nextInt(9999);
            Toast.makeText(this, "Sending OTP", Toast.LENGTH_SHORT).show();
            String message = "&message=" + userID + " Your otp is "+randomOtp;
            String sender = "&sender=" + "TXTLCL";
            String numbers = "&numbers=" + "91"+userID ;

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();
            Toast.makeText(this, "OTP sent successfully", Toast.LENGTH_SHORT).show();
//            return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS "+e);
            Toast.makeText(this, "OTP send failed: "+e, Toast.LENGTH_SHORT).show();
//            return "Error "+e;
        }
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

