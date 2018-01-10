package com.example.victor.tcc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        final Button buttonAddTime = (Button) findViewById(R.id.buttonAddTime);
        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");

        buttonAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Time.class);
                intent.putExtra("username", username);
                Profile.this.startActivity(intent);

            }
        });
    }
}
