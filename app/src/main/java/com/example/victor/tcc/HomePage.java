package com.example.victor.tcc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        final Button buttonProfile = (Button) findViewById(R.id.buttonProfile);

         TextView textoInicial = (TextView) findViewById(R.id.textoInicial);
        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        final String user_id = intent.getStringExtra("user_id");
        textoInicial.setText("Bem-vindo "+username);

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, Profile.class);
                intent.putExtra("username", username);
                intent.putExtra("user_id", user_id);
                HomePage.this.startActivity(intent);
            }
        });



    }
}
