package android.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.foodapp.Model.Users;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {

    private Button save_changes_btn;
    private String userID;
    private TextView name, city, pin, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userID = getIntent().getStringExtra("userID");
        name = findViewById(R.id.dashboard_name);
        city = findViewById(R.id.dashboard_city);
        pin = findViewById(R.id.dashboard_pin);
        address = findViewById(R.id.dashboard_address);
        save_changes_btn=findViewById(R.id.save_changes_btn);

        FillInInfo();

        save_changes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditUserInfo();

            }
        });
    }

    private void FillInInfo() {
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.child(userID).getValue(Users.class);
                name.setText(users.getUsername());
                city.setText(users.getCity());
                pin.setText(users.getPin());
                address.setText(users.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void EditUserInfo() {
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        HashMap<String, Object> users = new HashMap<>();
        users.put("username",name.getText().toString());
        users.put("city",city.getText().toString());
        users.put("pin",pin.getText().toString());
        users.put("address",address.getText().toString());
        UserRef.updateChildren(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(DashboardActivity.this, "Applied Changes Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(DashboardActivity.this,HomeActivity.class);
                    intent.putExtra("userID",userID);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(DashboardActivity.this, "Error: Try Again Later", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}