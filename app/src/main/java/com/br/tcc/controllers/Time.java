package com.br.tcc.controllers;

import android.app.DialogFragment;
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
import android.widget.CheckBox;
import android.widget.Spinner;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Time extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationView navigationView = findViewById(R.id.navMenuHome);

        setContentView(R.layout.activity_time);

        Spinner dropdown = findViewById(R.id.repeatTime);
        String[] items = new String[]{"1", "2", "3", "4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);


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
        finalTime.setEnabled(false);
        final Button confirm = (Button) findViewById(R.id.confirm);
        confirm.setEnabled(false);
        final TextView initialTimeLabel = (TextView) findViewById(R.id.initialTimeLabel);
        final TextView finalTimeLabel = (TextView) findViewById(R.id.finalTimeLabel);
        final Spinner repeat = (Spinner) findViewById(R.id.repeatTime);
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
                DialogFragment newFragment = new TimePickerFragment(initialTime.getId(), initialTimeLabel.getId(), finalTime);
               newFragment.show(getFragmentManager(),"TimePicker");
                System.out.println("ID :"+initialTimeLabel.getId());

            }
        });
        finalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment(finalTime.getId(), finalTimeLabel.getId(), confirm);
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar date = Calendar.getInstance();
                Calendar dateAssistent;

                int totalChecked = 0;
                for (CheckBox dayOfWeek : days) {
                    if(dayOfWeek.isChecked()){
                        totalChecked++;
                    }
                }
                System.out.println(days.size());
                String dtStart = initialTimeLabel.getText().toString();
                String dtEnd = finalTimeLabel.getText().toString();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                Date dateStart = null;
                Date dateEnd = null;
                try {
                    dateStart = format.parse(dtStart+":00");
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    dateEnd = format.parse(dtEnd+":00");
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("Start: "+dateEnd.getTime());
                System.out.println("End: "+dateStart.getTime());
                System.out.println("DIFERENÇA: "+(dateEnd.getTime() - dateStart.getTime()));

                    if(totalChecked == 0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Time.this);
                        builder.setMessage("Selecione pelo menos 1 dia da semana").setNegativeButton("Ok", null).create().show();
                    }
                    else if(dateEnd.getTime() - dateStart.getTime()<=0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Time.this);
                        builder.setMessage("O horário final deve ser maior que o horário inicial").setNegativeButton("Ok", null).create().show();
                    }else{


                for (CheckBox dayOfWeek : days) {
                    System.out.println("associa denovo");
                    dateAssistent = Calendar.getInstance();
                    for (int i = 0; i < Integer.parseInt(repeat.getSelectedItem().toString()) ; i++) {


                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    Intent intent = new Intent(Time.this, Profile.class);
                                    Time.this.startActivity(intent);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Time.this);
                                    builder.setMessage("Falhou").setNegativeButton("Tentar novamente", null).create().show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    if (dayOfWeek.isChecked()) {
                        String dayString = dayOfWeek.getText().toString();
                        if (dayString.equals("Segunda-feira")) {
                            System.out.println(dateAssistent.get(Calendar.DAY_OF_WEEK));
                            while (dateAssistent.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                                dateAssistent.add(Calendar.DATE, 1);
                            }
                        }
                        if (dayString.equals("Terça-feira")) {
                            while (dateAssistent.get(Calendar.DAY_OF_WEEK) != Calendar.TUESDAY) {
                                dateAssistent.add(Calendar.DATE, 1);
                            }
                        }
                        if (dayString.equals("Quarta-feira")) {
                            while (dateAssistent.get(Calendar.DAY_OF_WEEK) != Calendar.WEDNESDAY) {
                                dateAssistent.add(Calendar.DATE, 1);
                            }
                        }
                        if (dayString.equals("Quinta-feira")) {
                            while (dateAssistent.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY) {
                                dateAssistent.add(Calendar.DATE, 1);
                            }
                        }
                        if (dayString.equals("Sexta-feira")) {
                            while (dateAssistent.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
                                dateAssistent.add(Calendar.DATE, 1);
                            }
                        }
                        if (dayString.equals("Sabado")) {
                            while (dateAssistent.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                                dateAssistent.add(Calendar.DATE, 1);
                            }
                        }
                        if (dayString.equals("Domingo")) {
                            while (dateAssistent.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                                dateAssistent.add(Calendar.DATE, 1);
                            }
                        }
                        System.out.println("aqui");
                        int cDayAssistent= dateAssistent.get(Calendar.DAY_OF_MONTH);
                        int cMonthAssistent = dateAssistent.get(Calendar.MONTH);
                        int cYearAssistent = dateAssistent.get(Calendar.YEAR);
                        TimeDAO tdao = new TimeDAO(user_id, cYearAssistent +"-"+ (cMonthAssistent+1)+"-"+ cDayAssistent, initialTimeLabel.getText().toString() + ":00", finalTimeLabel.getText().toString() + ":00", responseListener);
                        System.out.println("DATA GRAVADA: "+cYearAssistent +"-"+ (cMonthAssistent+1)+"-"+ cDayAssistent);
                        RequestQueue queue = Volley.newRequestQueue(Time.this);
                        queue.add(tdao);
                        dateAssistent.add(Calendar.DATE, 1);
                    }

                }
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
                        i = new Intent(Time.this, ActivityMain.class);
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
