package android.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.example.foodapp.Model.Users;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends AppCompatActivity {
    private Button loginBtn,registerBtn; /*register button added*/
    private EditText inputUsername,inputPassword;
    private CheckBox saved_preferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private SharedPreferences loginPreferences;
    private String dbname = "Users";
    private static final String PREFS_NAME = "android.example.foodapp.PREFERENCE_FILE_KEY";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        saved_preferences = (CheckBox) findViewById(R.id.save_login);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBtn = (Button) findViewById(R.id.registerBtn); /*changes */
        inputUsername = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);
        loginPreferences = getSharedPreferences(PREFS_NAME,0);
        loginPrefsEditor = loginPreferences.edit();
        String saved_username = loginPreferences.getString(PREF_USERNAME, null);
        String saved_password = loginPreferences.getString(PREF_PASSWORD, null);
        if (!(saved_username == null || saved_password == null)) {
            inputUsername.setText(saved_username);
            inputPassword.setText(saved_password);
        }
//        changing activity on clicking register Button

        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                LoginUser();
            }
        });
    }

    private void LoginUser() {
        final DatabaseReference RootRef;
        final String username = inputUsername.getText().toString();
        final String password = inputPassword.getText().toString();

        // Remember password functionality
        if(!saved_preferences.isChecked()){
            loginPrefsEditor
                    .putString(PREF_USERNAME, null)
                    .putString(PREF_PASSWORD, null)
                    .apply();
        } else {
            loginPrefsEditor
                    .putString(PREF_USERNAME, username)
                    .putString(PREF_PASSWORD, password)
                    .apply();
        }

        // connection to database
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(dbname).child(username).exists()) {
                    Users usersData = snapshot.child(dbname).child(username).getValue(Users.class);
                    if(usersData.getPassword().equals(password)){
                        Toast.makeText(LoginActivity.this, "Successful Login",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Incorrect Password",Toast.LENGTH_SHORT).show();
                    }
                }
                else  {
                    Toast.makeText(LoginActivity.this, "No such username exists", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
