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
        textoInicial.setText("Bem-vindo "+username);

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, Profile.class);
                intent.putExtra("username", username);
                HomePage.this.startActivity(intent);
            }
        });



    }
}
