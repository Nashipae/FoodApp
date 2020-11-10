package Meadberry_Farms.example.foodapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.example.foodapp.R;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminProductAddActivity extends AppCompatActivity {

    private String prodType,prodName,prodWeight,prodPrice,prodDesc, dwnImageUrl,prodKey;
    private Button addProduct;
    private EditText txtprodName,txtprodWeight,txtprodPrice,txtprodDesc;
    private ImageView prodImage;
    private int GalleryPick;
    private Uri imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_add);

        prodType = getIntent().getStringExtra("type");
        addProduct = findViewById(R.id.addProduct);
        txtprodName = findViewById(R.id.ProdName);
        txtprodWeight = findViewById(R.id.ProdWeight);
        txtprodPrice = findViewById(R.id.ProdPrice);
        txtprodDesc = findViewById(R.id.ProdDesc);
        prodImage = findViewById(R.id.ProdImage);

        prodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminProductAddActivity.this, "Starting process please wait", Toast.LENGTH_SHORT).show();
                ValidateProduct();
            }
        });

//        uncomment for testing
//        Toast.makeText(this, prodType, Toast.LENGTH_SHORT).show();

    }

    private void ValidateProduct() {
        prodName=txtprodName.getText().toString();
        prodPrice=txtprodPrice.getText().toString();
        prodWeight=txtprodWeight.getText().toString();
        prodDesc=txtprodDesc.getText().toString();
        if(imageURI==null){
            Toast.makeText(this, "Image must be selected", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(prodDesc)){
            Toast.makeText(this, "Description can not be empty", Toast.LENGTH_SHORT).show();
        } else {
            StoreInfo();
        }
    }

    private void StoreInfo() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        String currentDate = simpleDateFormat.format(calendar.getTime());
        prodKey = currentDate;

        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("productImages").child(imageURI.getLastPathSegment() + currentDate + ".jpg");
        final UploadTask uploadTask = storageReference.putFile(imageURI);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminProductAddActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminProductAddActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AdminProductAddActivity.this, "getting Product image url", Toast.LENGTH_SHORT).show();
                            saveToDB();
                        }
                    }
                });
            }
        });
    }

    private void saveToDB() {
        HashMap<String, Object> product = new HashMap<>();
        product.put("image",dwnImageUrl);
        product.put("prod_desc",prodDesc);
        product.put("prod_id",prodKey);
        product.put("prod_name",prodName);
        product.put("prod_price",prodPrice);
        product.put("prod_raters","0");
        product.put("prod_rating","0");
        product.put("prod_stock","10");
        product.put("prod_type",prodType);
        product.put("prod_weight",prodWeight);

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(prodKey).updateChildren(product)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(AdminProductAddActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AdminProductAddActivity.this,AdminProductTypeActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(AdminProductAddActivity.this, "upload failed: "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
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
            prodImage.setImageURI(imageURI);
        }
    }
}