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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText adminID, adminPassword;
    private Button login,showHideBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_admin_login);

        adminID = findViewById(R.id.adminID);
        adminPassword = findViewById(R.id.adminPassword);
        login = findViewById(R.id.adminLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateAdmin();
            }
        });

        showHideBtn=findViewById(R.id.showHideBtn);
        showHideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showHideBtn.getText().equals("SHOW")) {
                    adminPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showHideBtn.setText("HIDE");
                    showHideBtn.setTextSize(13);
                } else if (showHideBtn.getText().equals("HIDE")) {
                    showHideBtn.setText("SHOW");
                    adminPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    private void authenticateAdmin() {
        final DatabaseReference AdminRef = FirebaseDatabase.getInstance().getReference().child("Admin");

        AdminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(adminID.getText().toString()).exists()){
                    if(snapshot.child(adminID.getText().toString()).child("password").getValue().equals(adminPassword.getText().toString())){
                        Toast.makeText(AdminLoginActivity.this, "Authenticated", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminLoginActivity.this, AdminChooseActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(AdminLoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(AdminLoginActivity.this, "ID does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminLoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}