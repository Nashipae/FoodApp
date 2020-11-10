package Meadberry_Farms.example.foodapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import Meadberry_Farms.example.foodapp.Model.Users;

import com.meadberryfarms.foodapp.R;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity {

    private Button save_changes_btn;
    private String userID;
    private TextView name, city, pin, address;
    private CircleImageView image;
    private Uri imageURI;
    private String dwnImageUrl;
    private int GalleryPick;

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
        image = findViewById(R.id.profile_image_dashboard);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        FillInInfo();

        save_changes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditUserInfo();
            }
        });
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null){
            imageURI = data.getData();
            image.setImageURI(imageURI);
        }
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
                Picasso.get().load(users.getImage()).into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void EditUserInfo() {
        if(imageURI!=null){
            Toast.makeText(this, "Please wait saving changes ...", Toast.LENGTH_SHORT).show();
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("userImages").child(userID + ".jpg");
            final UploadTask uploadTask = storageReference.putFile(imageURI);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DashboardActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(DashboardActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw task.getException();
                            }
                            dwnImageUrl = storageReference.getDownloadUrl().toString();
                            return storageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                dwnImageUrl = task.getResult().toString();
                                saveToDB();
                            }
                        }
                    });
                }
            });
        }
        else {
            saveToDB();
        }
    }

    private void saveToDB() {
        DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        HashMap<String, Object> users = new HashMap<>();
        users.put("username",name.getText().toString());
        users.put("city",city.getText().toString());
        users.put("pin",pin.getText().toString());
        users.put("address",address.getText().toString());
        if(imageURI!=null) users.put("image",dwnImageUrl);
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