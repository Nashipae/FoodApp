package Meadberry_Farms.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import com.meadberryfarms.foodapp.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectLanguageActivity extends AppCompatActivity {

    private Button saveBtn;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        userID = getIntent().getStringExtra("userID");

        saveBtn=findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(SelectLanguageActivity.this,HomeActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });


    }
}