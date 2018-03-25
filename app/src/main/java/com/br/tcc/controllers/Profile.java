package com.br.tcc.controllers;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.br.tcc.database.local.DataDAO;
import com.example.victor.tcc.R;

import java.util.ArrayList;


public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationView navigationView = findViewById(R.id.navMenuHome);
        ListView timeList = (ListView) findViewById(R.id.listTime);
        setContentView(R.layout.activity_profile);
        final DataDAO dataDAO = new DataDAO(this);
        dataDAO.create();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerLayout.requestLayout();
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mDrawerLayout.bringToFront();
        setNavigationViewListener();
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Button buttonAddTime = (Button) findViewById(R.id.buttonAddTime);

        buttonAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Time.class);
                Profile.this.startActivity(intent);

            }
        });

        ArrayList<String> theList = new ArrayList<>();
        Cursor data = dataDAO.getData();
        if(data.getCount()==0){
            Toast.makeText(Profile.this, "Ta zerado", Toast.LENGTH_LONG).show();

        }else{
            while(data.moveToNext()){
                theList.add(data.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
                timeList.setAdapter(listAdapter);
            }
        }
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
                intent = new Intent(Profile.this, Profile.class);
                Profile.this.startActivity(intent);
                break;
            case R.id.buttonHome:
                intent = new Intent(Profile.this, HomePage.class);
                Profile.this.startActivity(intent);
                break;

            case R.id.buttonLogout:
                AlertDialog.Builder builder=new AlertDialog.Builder(Profile.this);
                builder.setMessage("Você quer mesmo sair?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        finish();
                        Intent i=new Intent();
                        i = new Intent(Profile.this, ActivityMain.class);
                        Profile.this.startActivity(i);

                        finish();

                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert=builder.create();
                alert.show();


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
