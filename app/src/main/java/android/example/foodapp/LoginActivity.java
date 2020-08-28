package android.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.foodapp.Model.Users;
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

public class LoginActivity extends AppCompatActivity {
    private Button dummyRegister,loginBtn,registerBtn; /*register button added*/
    private EditText inputUsername,inputPassword;
    private String dbname = "Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        dummyRegister = (Button) findViewById(R.id.register);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn); /*changes */

//        inputUsername = (EditText) findViewById(R.id.username);
//        inputPassword = (EditText) findViewById(R.id.password);

//        changing activity on clicking register Button



        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

//        dummyRegister.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Intent intent = new Intent(LoginActivity.this, DummyRegisterActivity.class);
//                startActivity(intent);
//            }
//        });

//        loginBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                LoginUser();
//            }
//        });
    }

//    private void LoginUser() {
//        final DatabaseReference RootRef;
//        final String username = inputUsername.getText().toString();
//        final String password = inputPassword.getText().toString();
//        RootRef = FirebaseDatabase.getInstance().getReference();
//        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.child(dbname).child(username).exists()) {
//                    Users usersData = snapshot.child(dbname).child(username).getValue(Users.class);
//                    if(usersData.getPassword().equals(password)){
//                        Toast.makeText(LoginActivity.this, "Successful Login",Toast.LENGTH_SHORT).show();
//                        // do something when login is successful
//                    }
//                    else {
//                        Toast.makeText(LoginActivity.this, "Failed Login",Toast.LENGTH_SHORT).show();
//                    }
//                } else  {
//                    Toast.makeText(LoginActivity.this, "No such username", Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//    }
}
