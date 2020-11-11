package com.meadberryfarms.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewPasswordActivity extends AppCompatActivity {

    private Button showHideBtnRegEnter,showHideBtnRegConfirm,doneBtn;
    private String newPassword,newPasswordConfirm;
    private EditText inputPassword,inputPasswordConfirm;
    private DatabaseReference RootRef;
    private String dbname = "Users";
    private String USERPHONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        inputPassword = findViewById(R.id.password);
        inputPasswordConfirm = findViewById(R.id.passwordConfirm);
        showHideBtnRegEnter = (Button) findViewById(R.id.showHideBtnRegEnter);
        showHideBtnRegConfirm = (Button) findViewById(R.id.showHideBtnRegConfirm);
        doneBtn=(Button) findViewById(R.id.doneBtn);
        USERPHONE = getIntent().getStringExtra("USERPHONE");

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
                SetNewPassword();
            }
        });
    }

    private void SetNewPassword() {
        newPassword = inputPassword.getText().toString();
        newPasswordConfirm = inputPasswordConfirm.getText().toString();
        if(newPassword.length()<8 || !isValidPassword(newPassword)) Toast.makeText(NewPasswordActivity.this,"Password must be at least 8 entities long and must contain at least 1 alphbet, 1 number and 1 special character",Toast.LENGTH_SHORT).show();
        else if(!(newPassword.equals(newPasswordConfirm)))Toast.makeText(NewPasswordActivity.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
        else {
            RootRef = FirebaseDatabase.getInstance().getReference();
            RootRef.child(dbname).child(USERPHONE).child("password").setValue(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(NewPasswordActivity.this,"Password successfully updated",Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(NewPasswordActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NewPasswordActivity.this,"Password update failed, Try again later",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();

    }
}