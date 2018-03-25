package com.br.tcc.controllers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.br.tcc.assistants.CustomTimePickerDialog;
import com.br.tcc.database.local.DataDAO;
import com.br.tcc.database.local.TasksDAO;
import com.br.tcc.database.local.TimeBlockDAO;
import com.br.tcc.database.local.UserDAO;
import com.br.tcc.database.remote.GetTasksDAO;
import com.br.tcc.database.remote.GetTimeBlockDAO;
import com.br.tcc.database.remote.GetTimeDAO;
import com.br.tcc.database.remote.TaskDAO;
import com.example.victor.tcc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class AddTask extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public static TimePicker timePicker;
    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;

    int mHour;
    int mMinute;

    TextView et_show_date_time;
    Button btn_set_date_time;

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {



        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new CustomTimePickerDialog(getActivity(),this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timePicker.setCurrentHour(hourOfDay);
            timePicker.setCurrentMinute(minute);
        }
    }
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        et_show_date_time = (TextView) findViewById(R.id.et_show_date_time);
        btn_set_date_time = (Button) findViewById(R.id.btn_set_date_time);

        final EditText titleTask = (EditText) findViewById(R.id.title);
        final EditText subjectTask = (EditText) findViewById(R.id.subject);
        final EditText descriptionTask = (EditText) findViewById(R.id.description);
        final EditText hourTask = (EditText) findViewById(R.id.hour);
        final EditText minuteTask = (EditText) findViewById(R.id.minute);
        final Button buttonAddTask = (Button) findViewById(R.id.addTask);



        btn_set_date_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });
        final TasksDAO tasksDAO = new TasksDAO(this);
        tasksDAO.create();
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
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String title = titleTask.getText().toString();
                final String subject = subjectTask.getText().toString();
                final String description = descriptionTask.getText().toString();
                final String hour = hourTask.getText().toString();
                final String minute = minuteTask.getText().toString();
                final String end_date = et_show_date_time.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("");
                            System.out.println("RESPOSTA "+response.toString());
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {



                                Response.Listener<String> responseListener4 = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response4) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response4);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if(success){
                                                try
                                                {
                                                    JSONArray jArray = jsonResponse.getJSONArray("tasksArray");
                                                    for (int i = 0; i < jArray.length(); i++)
                                                    {
                                                        JSONObject json_data = jArray.getJSONObject(i);
                                                        tasksDAO.addData(json_data.getString("id_task"),json_data.getString("id_user"),json_data.getString("title"),json_data.getString("subject"),json_data.getString("description"),json_data.getString("estimated_time"),json_data.getString("deadline"),json_data.getString("progress"));
                                                        System.out.println("TABELA TASKS");
                                                    }
                                                    System.out.println("AGORA1");
                                                    Intent intent = new Intent(AddTask.this, HomePage.class);
                                                    AddTask.this.startActivity(intent);


                                                }
                                                catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                }
                                                Intent intent = new Intent(AddTask.this, HomePage.class);
                                                AddTask.this.startActivity(intent);
                                            }else{
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };




                                Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response2) {
                                        final TimeBlockDAO timeBlockDAO = new TimeBlockDAO(AddTask.this);
                                        timeBlockDAO.create();
                                        final DataDAO dataDAO = new DataDAO(AddTask.this);
                                        dataDAO.create();
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
                                                        RequestQueue queue = Volley.newRequestQueue(AddTask.this);
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

                                GetTasksDAO gtsksdao = new GetTasksDAO(user_id,responseListener4);
                                RequestQueue queue = Volley.newRequestQueue(AddTask.this);
                                queue.add(gtsksdao);


                                GetTimeDAO gtdao = new GetTimeDAO(user_id,responseListener2);
                                queue = Volley.newRequestQueue(AddTask.this);
                                queue.add(gtdao);



                            } else {
                                String totalAvailable = jsonResponse.getString("available");
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddTask.this);
                                builder.setMessage("Você possui apenas "+totalAvailable+" horas até a data da entrega, e quer cadastrar "+hour+":"+minute+" Horas."+ " Cadastre mais horas acessando o seu Perfil").setNegativeButton("Ok", null).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                TaskDAO rdao = new TaskDAO(user_id,title, subject, description, hour+":"+minute, end_date, responseListener);
                RequestQueue queue = Volley.newRequestQueue(AddTask.this);
                queue.add(rdao);


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
                intent = new Intent(AddTask.this, Profile.class);
                AddTask.this.startActivity(intent);
                break;
            case R.id.buttonHome:
                intent = new Intent(AddTask.this, HomePage.class);
                AddTask.this.startActivity(intent);
                break;

            case R.id.buttonLogout:
                AlertDialog.Builder builder=new AlertDialog.Builder(AddTask.this);
                builder.setMessage("Você quer mesmo sair?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        finish();
                        Intent i=new Intent();
                        i = new Intent(AddTask.this, ActivityMain.class);
                        AddTask.this.startActivity(i);

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


    public void onRadioInvidigualClicked(View view) {
    }

    public void onRadioGroupClicked(View view) {
    }
    private void datePicker(){

// Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {



                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                        date_time = year + "-"+ (monthOfYear + 1 + "-" + dayOfMonth);
                        //*************Call Time Picker Here ********************
                        timePicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    private void timePicker(){
// Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,int minute) {

                        mHour = hourOfDay;
                        mMinute = minute;

                        et_show_date_time.setText(date_time+" "+hourOfDay + ":" + minute);
                        date_time = date_time+" "+hourOfDay + ":" + minute;
                    }
                }, mHour, mMinute, false);

        timePickerDialog.show();
    }
}
