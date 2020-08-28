package android.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DummyRegisterActivity extends AppCompatActivity {
    private Button dummyRegister;
    private EditText inputName,inputPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_register);
        dummyRegister = (Button) findViewById(R.id.dummyRegister);
        inputName = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);

        dummyRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                CreateAccount();
            }
        });

    }

    private void CreateAccount() {
        final DatabaseReference RootRef;
        final String username = inputName.getText().toString();
        final String password = inputPassword.getText().toString();
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(username).exists())){
                    HashMap<String,Object> userdataMap =new HashMap<>();
                    userdataMap.put("username",username);
                    userdataMap.put("password",password);
                    RootRef.child("Users").child(username).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(DummyRegisterActivity.this,"succesfully created login",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DummyRegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(DummyRegisterActivity.this, "this username exists ", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}