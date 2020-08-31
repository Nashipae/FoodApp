package android.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewPasswordActivity extends AppCompatActivity {

    private Button showHideBtnRegEnter,showHideBtnRegConfirm,doneBtn;

    private EditText inputPassword,inputPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        inputPassword = findViewById(R.id.password);
        inputPasswordConfirm = findViewById(R.id.passwordConfirm);
        showHideBtnRegEnter = (Button) findViewById(R.id.showHideBtnRegEnter);
        showHideBtnRegConfirm = (Button) findViewById(R.id.showHideBtnRegConfirm);
        doneBtn=(Button) findViewById(R.id.doneBtn);

        showHideBtnRegEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showHideBtnRegEnter.getText().equals("SHOW")){
                    inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showHideBtnRegEnter.setText("HIDE");
                    showHideBtnRegEnter.setTextSize(13);
                }else if (showHideBtnRegEnter.getText().equals("HIDE")){
                    showHideBtnRegEnter.setText("SHOW");
                    inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        showHideBtnRegConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showHideBtnRegConfirm.getText().equals("SHOW")){
                    inputPasswordConfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showHideBtnRegConfirm.setText("HIDE");
                    showHideBtnRegConfirm.setTextSize(13);
                }else if (showHideBtnRegConfirm.getText().equals("HIDE")){
                    showHideBtnRegConfirm.setText("SHOW");
                    inputPasswordConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(NewPasswordActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}