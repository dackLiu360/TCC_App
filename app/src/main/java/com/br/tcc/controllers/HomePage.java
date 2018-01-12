package com.br.tcc.controllers;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.br.tcc.database.local.UserDAO;
import com.example.victor.tcc.R;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationView navigationView = findViewById(R.id.navMenuHome);

        setContentView(R.layout.activity_home_page);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerLayout.requestLayout();
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mDrawerLayout.bringToFront();
        setNavigationViewListener();
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final UserDAO udao = new UserDAO(this);
        String username = null;


        Cursor data = udao.getData();
        while(data.moveToNext()){

            username = data.getString(1);

        }
        final Button buttonProfile = (Button) findViewById(R.id.buttonProfile);

         TextView textoInicial = (TextView) findViewById(R.id.textoInicial);

        textoInicial.setText("Bem-vindo "+username);




    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(mToggle.onOptionsItemSelected(item)){


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {

            case R.id.buttonProfile:
                intent = new Intent(HomePage.this, Profile.class);
                HomePage.this.startActivity(intent);
                break;
            case R.id.buttonHome:
                intent = new Intent(HomePage.this, HomePage.class);
                HomePage.this.startActivity(intent);
                break;
        }
        return true;
    }
    private void setNavigationViewListener() {
        NavigationView navigationView = findViewById(R.id.navMenuHome);
        System.out.println(navigationView.toString());
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }
}
