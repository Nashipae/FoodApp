package android.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        num1.addTextChangedListener(new PinTextWatcher(0));
        num2.addTextChangedListener(new PinTextWatcher(1));
        num3.addTextChangedListener(new PinTextWatcher(2));
        num4.addTextChangedListener(new PinTextWatcher(3));

        num1.setOnKeyListener(new PinOnKeyListener(0));
        num2.setOnKeyListener(new PinOnKeyListener(1));
        num3.setOnKeyListener(new PinOnKeyListener(2));
        num4.setOnKeyListener(new PinOnKeyListener(3));

        nextBtn=(Button) findViewById(R.id.nextBtn);
        editTexts = new EditText[]{num1, num2, num2, num2};
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

    public class PinTextWatcher implements TextWatcher {

        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newTypedString = s.subSequence(start, start + count).toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = newTypedString;

            /* Detect paste event and set first char */
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0)); // TODO: We can fill out other EditTexts

            editTexts[currentIndex].removeTextChangedListener(this);
            editTexts[currentIndex].setText(text);
            editTexts[currentIndex].setSelection(text.length());
            editTexts[currentIndex].addTextChangedListener(this);

            if (text.length() == 1)
                moveToNext();
            else if (text.length() == 0)
                moveToPrevious();
        }

        private void moveToNext() {
            if (!isLast)
                editTexts[currentIndex + 1].requestFocus();

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                editTexts[currentIndex].clearFocus();
                hideKeyboard();
            }
        }

        private void moveToPrevious() {
            if (!isFirst)
                editTexts[currentIndex - 1].requestFocus();
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : editTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }

        private void hideKeyboard() {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public class PinOnKeyListener implements View.OnKeyListener {

        private int currentIndex;

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (editTexts[currentIndex].getText().toString().isEmpty() && currentIndex != 0)
                    editTexts[currentIndex - 1].requestFocus();
            }
            return false;
        }
    }
}