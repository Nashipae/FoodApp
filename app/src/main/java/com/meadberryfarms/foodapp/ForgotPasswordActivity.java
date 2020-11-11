package com.meadberryfarms.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button nextBtn;
    private EditText inputPhone;
    private DatabaseReference RootRef;
    private String dbname = "Users";
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        nextBtn=(Button) findViewById(R.id.nextBtn);
        inputPhone = (EditText) findViewById(R.id.phone);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = inputPhone.getText().toString();
                validatePhone();
            }

        });
    }

    private void validatePhone(){

        if(phone.length()!=10)return;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(dbname).child(phone).exists()){
                    Intent intent = new Intent(ForgotPasswordActivity.this, EnterOTPActivity.class);
                    intent.putExtra("USERPHONE",phone);
                    intent.putExtra("userID",phone);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(ForgotPasswordActivity.this,inputPhone.getText().toString() + "mobile number is not registered",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}