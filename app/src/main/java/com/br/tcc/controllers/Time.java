package com.br.tcc.controllers;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.br.tcc.database.local.UserDAO;
import com.example.victor.tcc.R;
import com.br.tcc.database.remote.TimeDAO;
import com.br.tcc.assistants.TimePickerFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Time extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationView navigationView = findViewById(R.id.navMenuHome);

        setContentView(R.layout.activity_time);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerLayout.requestLayout();
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mDrawerLayout.bringToFront();
        setNavigationViewListener();
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String id = null;
        Intent intent = getIntent();
        final UserDAO udao = new UserDAO(this);
        Cursor data = udao.getData();
        while(data.moveToNext()){

            id = data.getString(0);

        }
        final String user_id = id;
        final Button initialTime = (Button) findViewById(R.id.initial_time);
        final Button finalTime = (Button) findViewById(R.id.final_time);
        final Button confirm = (Button) findViewById(R.id.confirm);
        final TextView initialTimeLabel = (TextView) findViewById(R.id.initialTimeLabel);
        final TextView finalTimeLabel = (TextView) findViewById(R.id.finalTimeLabel);
        final CheckBox monday = (CheckBox)findViewById(R.id.monday);
        final CheckBox tuesday = (CheckBox)findViewById(R.id.tuesday);
        final CheckBox wednesday = (CheckBox)findViewById(R.id.wednesday);
        final CheckBox thursday = (CheckBox)findViewById(R.id.thursday);
        final CheckBox friday = (CheckBox)findViewById(R.id.friday);
        final CheckBox saturday = (CheckBox)findViewById(R.id.saturday);
        final CheckBox sunday = (CheckBox)findViewById(R.id.sunday);
        final ArrayList<CheckBox> days = new ArrayList<>();
        days.add(monday);
        days.add(tuesday);
        days.add(wednesday);
        days.add(thursday);
        days.add(friday);
        days.add(saturday);
        days.add(sunday);

        initialTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment(initialTime.getId(), initialTimeLabel.getId());
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });
        finalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment(finalTime.getId(), finalTimeLabel.getId());
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (CheckBox dayOfWeek : days) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if(success){
                                    Intent intent = new Intent(Time.this, Profile.class);
                                    Time.this.startActivity(intent);
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Time.this);
                                    builder.setMessage("Falhou").setNegativeButton("Tentar novamente", null).create().show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    if(dayOfWeek.isChecked()){
                        TimeDAO tdao = new TimeDAO(user_id,dayOfWeek.getText().toString(),initialTimeLabel.getText().toString()+":00",finalTimeLabel.getText().toString()+":00", responseListener);
                        System.out.println(user_id);
                        System.out.println(dayOfWeek.getText().toString());
                        System.out.println(initialTimeLabel.getText().toString()+":00");
                        System.out.println(finalTimeLabel.getText().toString()+":00");
                        RequestQueue queue = Volley.newRequestQueue(Time.this);
                        queue.add(tdao);
                    }
                }

            }
        });

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
                intent = new Intent(Time.this, Profile.class);
                Time.this.startActivity(intent);
                break;
            case R.id.buttonHome:
                intent = new Intent(Time.this, HomePage.class);
                Time.this.startActivity(intent);
                break;
            case R.id.buttonLogout:
                AlertDialog.Builder builder=new AlertDialog.Builder(Time.this);
                builder.setMessage("Você quer mesmo sair?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        finish();
                        Intent i=new Intent();
                        i = new Intent(Time.this, Login.class);
                        Time.this.startActivity(i);

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
