package android.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                Intent intent = new Intent(DummyRegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}