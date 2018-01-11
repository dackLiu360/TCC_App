package com.br.tcc.controllers;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.br.tcc.database.local.UserDAO;
import com.example.victor.tcc.R;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final UserDAO udao = new UserDAO(this);
        String username = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Cursor data = udao.getData();
        while(data.moveToNext()){

            username = data.getString(1);

        }
        final Button buttonProfile = (Button) findViewById(R.id.buttonProfile);

         TextView textoInicial = (TextView) findViewById(R.id.textoInicial);

        textoInicial.setText("Bem-vindo "+username);


        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePage.this, Profile.class);
                HomePage.this.startActivity(intent);
            }
        });



    }
}
