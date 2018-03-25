package com.br.tcc.controllers;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
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
import com.br.tcc.database.local.DataDAO;
import com.br.tcc.database.local.TimeBlockDAO;
import com.br.tcc.database.local.UserDAO;
import com.br.tcc.database.remote.GetTimeBlockDAO;
import com.br.tcc.database.remote.GetTimeDAO;
import com.example.victor.tcc.R;
import com.br.tcc.database.remote.TimeDAO;
import com.br.tcc.assistants.TimePickerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Time extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationView navigationView = findViewById(R.id.navMenuHome);

        setContentView(R.layout.activity_time);

        final Spinner[] dropdown = {findViewById(R.id.repeatTime)};
        String[] items = new String[]{"1", "2", "3", "4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown[0].setAdapter(adapter);



        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerLayout.requestLayout();
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mDrawerLayout.bringToFront();
        setNavigationViewListener();
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String id = null;
        String id2 = null;
        Intent intent = getIntent();
        final UserDAO udao = new UserDAO(this);
        Cursor data = udao.getData();
        while(data.moveToNext()){

            id = data.getString(0);

        }

        final String user_id = id;
        final Button time_start = (Button) findViewById(R.id.time_start);
        final Button time_end = (Button) findViewById(R.id.time_end);
        time_end.setEnabled(false);
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

        time_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment(time_start.getId(), initialTimeLabel.getId(), time_end);
               newFragment.show(getFragmentManager(),"TimePicker");


            }
        });
        time_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment(time_end.getId(), finalTimeLabel.getId(), confirm);
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar date = Calendar.getInstance();
                Calendar dateAssistent;
                final Boolean[] isFinished = {false};
                int totalChecked = 0;
                for (CheckBox dayOfWeek : days) {
                    if(dayOfWeek.isChecked()){
                        totalChecked++;
                    }
                }

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


                    if(totalChecked == 0){

                        AlertDialog.Builder builder = new AlertDialog.Builder(Time.this);
                        builder.setMessage("Selecione pelo menos 1 dia da semana").setNegativeButton("Ok", null).create().show();
                    }
                    else if(dateEnd.getTime() - dateStart.getTime()<=0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Time.this);
                        builder.setMessage("O horário final deve ser maior que o horário inicial").setNegativeButton("Ok", null).create().show();
                    }else{
                        final TimeBlockDAO timeBlockDAO = new TimeBlockDAO(Time.this);
                        timeBlockDAO.create();
                        final DataDAO dataDAO = new DataDAO(Time.this);
                        dataDAO.create();



                        for (CheckBox dayOfWeek : days) {

                    dateAssistent = Calendar.getInstance();
                    for (int i = 0; i < Integer.parseInt(repeat.getSelectedItem().toString()) ; i++) {


                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);


                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response2) {
                                            isFinished[0] = false;
                                            try {
                                                JSONObject jsonResponse = new JSONObject(response2);
                                                boolean success = jsonResponse.getBoolean("success");
                                                if(success){
                                                    try
                                                    {

                                                        JSONArray jArray = jsonResponse.getJSONArray("timesArray");
                                                        for (int i = 0; i < jArray.length(); i++)
                                                        {
                                                            JSONObject json_data = jArray.getJSONObject(i);
                                                            dataDAO.addData(json_data.getString("id_time"),json_data.getString("id_user"),json_data.getString("day"));
                                                            System.out.println("Adicionou "+json_data.getString("id_time")+" "+json_data.getString("id_user")+" "+json_data.getString("day"));




                                                            Response.Listener<String> responseListener3 = new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response23) {
                                                                    isFinished[0] = false;
                                                                    try {
                                                                        JSONObject jsonResponse = new JSONObject(response23);
                                                                        boolean success = jsonResponse.getBoolean("success");
                                                                        if(success){
                                                                            try
                                                                            {
                                                                                JSONArray jArray2 = jsonResponse.getJSONArray("timesBlockArray");
                                                                                for (int i = 0; i < jArray2.length(); i++)
                                                                                {
                                                                                    JSONObject json_data = jArray2.getJSONObject(i);
                                                                                    timeBlockDAO.addData(json_data.getString("id_time_block"),json_data.getString("id_time"),json_data.getString("time_start"),json_data.getString("time_end"),json_data.getString("part"), json_data.getString("availability"));
                                                                                    System.out.println("ADICIONOU2 "+json_data.getString("id_time_block")+" "+json_data.getString("id_time")+" "+json_data.getString("time_start")+" "+json_data.getString("time_end")+" "+json_data.getString("part")+" "+ json_data.getString("availability"));
                                                                                }
                                                                            }
                                                                            catch (Exception e)
                                                                            {
                                                                                e.printStackTrace();
                                                                            }

                                                                        }else{
                                                                        }

                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            };





                                                            GetTimeBlockDAO gtbdao = new GetTimeBlockDAO(json_data.getString("id_time"),responseListener3);
                                                            RequestQueue queue = Volley.newRequestQueue(Time.this);
                                                            queue.add(gtbdao);
                                                        }


                                                    }
                                                    catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                }else{
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };




                                    GetTimeDAO gtdao = new GetTimeDAO(user_id,responseListener2);
                                    RequestQueue queue = Volley.newRequestQueue(Time.this);
                                    queue.add(gtdao);

                                    //Faz fica checando até a variavel parar de ser mudada para false
                                    int delay = 0; // delay for 0 sec.
                                    int period = 300;
                                    final Timer timer = new Timer();
                                    final Handler handler = new Handler();
                                    timer.scheduleAtFixedRate(new TimerTask()
                                    {
                                        public void run()
                                        {
                                            System.out.println("AQUI");
                                            isFinished[0] = true;

                                            handler.postDelayed(new Runnable() {

                                                public void run() {

                                                    if(isFinished[0]==true){

                                                        timer.cancel();
                                                        Intent intent = new Intent(Time.this, Profile.class);
                                                        Time.this.startActivity(intent);
                                                    }
                                                }
                                            }, 600);
                                        }
                                    }, delay, period);


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

                        int cDayAssistent= dateAssistent.get(Calendar.DAY_OF_MONTH);
                        int cMonthAssistent = dateAssistent.get(Calendar.MONTH);
                        int cYearAssistent = dateAssistent.get(Calendar.YEAR);
                        int assistentOfMothAssistent = cMonthAssistent+1;
                        String AssistentOfassistentOfMothAssistent="";
                        if(assistentOfMothAssistent>=1&&assistentOfMothAssistent<=9){
                            AssistentOfassistentOfMothAssistent = "0"+assistentOfMothAssistent;
                        }else{
                            AssistentOfassistentOfMothAssistent = Integer.toString(assistentOfMothAssistent);
                        }

                        TimeDAO tdao = new TimeDAO(user_id, cYearAssistent +"-"+ AssistentOfassistentOfMothAssistent+"-"+ cDayAssistent, initialTimeLabel.getText().toString() + ":00", finalTimeLabel.getText().toString() + ":00", responseListener);

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

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }



}
