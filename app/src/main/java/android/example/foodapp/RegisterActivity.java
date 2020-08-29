package android.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.foodapp.Model.Users;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {


    private Button signinBtn,doneBtn;
    private EditText inputUsername,inputPhone,inputCity,inputPin,inputPassword,inputPasswordConfirm;
    private String dbname = "Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        signinBtn =(Button) findViewById((R.id.signIn));
        doneBtn = (Button) findViewById(R.id.doneBtn);
        inputUsername = findViewById(R.id.username);
        inputPhone = findViewById(R.id.phone);
        inputCity = findViewById(R.id.city);
        inputPin = findViewById(R.id.pincode);
        inputPassword = findViewById(R.id.password);
        inputPasswordConfirm = findViewById(R.id.passwordConfirm);

        signinBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){;
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String username = inputUsername.getText().toString();
        final String city = inputCity.getText().toString();
        final String pin = inputPin.getText().toString();
        final String password = inputPassword.getText().toString();
        final String passwordConfirm = inputPasswordConfirm.getText().toString();
        final String phone = inputPhone.getText().toString();
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(dbname).child(username).exists()){
                    Toast.makeText(RegisterActivity.this,"The entered username already exists",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(username.isEmpty())Toast.makeText(RegisterActivity.this,"Username cannot be empty",Toast.LENGTH_SHORT).show();
                    else if(city.isEmpty())Toast.makeText(RegisterActivity.this,"City field cannot be empty",Toast.LENGTH_SHORT).show();
                    else if(pin.isEmpty())Toast.makeText(RegisterActivity.this,"Postal Code field cannot be empty",Toast.LENGTH_SHORT).show();
                    else if(password.isEmpty())Toast.makeText(RegisterActivity.this,"Password field cannot be empty",Toast.LENGTH_SHORT).show();
                    else if(passwordConfirm.isEmpty())Toast.makeText(RegisterActivity.this,"Please confirm password",Toast.LENGTH_SHORT).show();
                    else if(!(password.equals(passwordConfirm)))Toast.makeText(RegisterActivity.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
                    else if((password.length()<8  || !isValidPassword(password)))Toast.makeText(RegisterActivity.this,"Password must contain minimum 8 characters at least 1 Alphabet, 1 Number and 1 Special Character,",Toast.LENGTH_LONG).show();
                    else {
                        Users userData = new Users(username,password,phone,city,pin);
//                        Map<String,Object> userMap = userData.toMap();
                        RootRef.child(dbname).child(username).setValue(userData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(RegisterActivity.this,"Registration successful",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterActivity.this,"Due to some technical reasons the registration failed. Please try again after some time",Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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