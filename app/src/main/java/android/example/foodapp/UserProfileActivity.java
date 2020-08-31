package android.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.example.foodapp.Model.Users;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserProfileActivity extends AppCompatActivity {
    private EditText username,pincode,city,new_password,new_password_confirm;
    private TextView phone;
    private Button editUser;
    private String user_key;
    private String original_password;
    DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
    String dbname = "Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        username = this.<EditText>findViewById(R.id.username);
        phone = this.<EditText>findViewById(R.id.phone);
        pincode = this.<EditText>findViewById(R.id.pincode);
        city     = this.<EditText>findViewById(R.id.city);
        new_password = this.<EditText>findViewById(R.id.new_password);
        new_password_confirm = this.<EditText>findViewById(R.id.new_password_confirm);
        user_key = getIntent().getStringExtra("Userphone");
        fillValues();

        editUser = this.<Button>findViewById(R.id.editUser);
        editUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editValues();
            }
        });
    }

    private void editValues() {

        final String susername = username.getText().toString();
        final String scity = city.getText().toString();
        final String spincode = pincode.getText().toString();
        final String snew_password = new_password.getText().toString();
        final String snew_password_confirm = new_password_confirm.getText().toString();

        if(susername.length()==0)Toast.makeText(UserProfileActivity.this,"Username cannot be empty",Toast.LENGTH_SHORT).show();
        else if(scity.length()==0)Toast.makeText(UserProfileActivity.this,"City field cannot be empty",Toast.LENGTH_SHORT).show();
        else if(spincode.length()==0)Toast.makeText(UserProfileActivity.this,"Postal Code field cannot be empty",Toast.LENGTH_SHORT).show();
        else if(!(snew_password.equals(snew_password_confirm)))Toast.makeText(UserProfileActivity.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
        else if(!(snew_password.length()==0) && (snew_password.length()<8  || !isValidPassword(snew_password)))Toast.makeText(UserProfileActivity.this,"Password must contain minimum 8 characters at least 1 Alphabet, 1 Number and 1 Special Character," + snew_password,Toast.LENGTH_LONG).show();
        else {

            RootRef.child(dbname).child(user_key).setValue(
                new Users(susername,
                        original_password=(snew_password.length()>0)?snew_password:original_password,
                        user_key,
                        scity,
                        spincode))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UserProfileActivity.this,"Account Settings updated",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserProfileActivity.this,"Account Settings updated",Toast.LENGTH_SHORT).show();
                    }
            });

        }
    }

    private boolean isValidPassword(String string) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(string);
        return matcher.matches();
    }

    private void fillValues() {
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(dbname).child(user_key).exists()){
                    Users userData = snapshot.child(dbname).child(user_key).getValue(Users.class);
                    username.setText(userData.getUsername());
                    phone.setText(userData.getPhone());
                    pincode.setText(userData.getPin());
                    city.setText(userData.getCity());
                    original_password=userData.getPassword();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }
}