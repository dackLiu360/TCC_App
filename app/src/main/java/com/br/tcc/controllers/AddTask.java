package com.br.tcc.controllers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.br.tcc.assistants.CustomTimePickerDialog;
import com.br.tcc.assistants.Recomendation;
import com.br.tcc.assistants.TaskModel;
import com.br.tcc.assistants.TimeBlockModel;
import com.br.tcc.assistants.TimeModel;
import com.br.tcc.database.remote.GetTasksDAO;
import com.br.tcc.database.remote.GetTimeBlockDAO;
import com.br.tcc.database.remote.GetTimeDAO;
import com.br.tcc.database.remote.TaskDAO;
import com.br.tcc.database.remote.TaskInsertedDAO;
import com.example.victor.tcc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class AddTask extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    public static TimePicker timePicker;
    String date_time = "";
    Activity activity = this;
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
        Button increment = findViewById(R.id.increment);
        Button decrement = findViewById(R.id.decrement);
        final Button addMember = findViewById(R.id.add_member);
        RadioGroup type = findViewById(R.id.taskGroupId);
        RadioButton individual = findViewById(R.id.radio_individual);
        RadioButton group = findViewById(R.id.radio_group);
        type.check(R.id.radio_individual);





        final EditText titleTask = (EditText) findViewById(R.id.title);
        final EditText subjectTask = (EditText) findViewById(R.id.subject);
        final EditText descriptionTask = (EditText) findViewById(R.id.description);
        final EditText groupMember = (EditText) findViewById(R.id.groupMember);
        final TextView timeLabel = findViewById(R.id.timeLabel);
        final TextView members = findViewById(R.id.members);
        final Button buttonAddTask = (Button) findViewById(R.id.addTask);
        final int[] hour = new int[1];
        final int[] minute = new int[1];

        addMember.setVisibility(View.GONE);
        groupMember.setVisibility(View.GONE);
        members.setVisibility(View.GONE);



        individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                members.setText("");
                addMember.setVisibility(View.GONE);
                groupMember.setVisibility(View.GONE);
                members.setVisibility(View.GONE);
            }
        });

        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMember.setVisibility(View.VISIBLE);
                groupMember.setVisibility(View.VISIBLE);
                members.setVisibility(View.VISIBLE);
            }
        });



        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = timeLabel.getText().toString();
                String[] timeSplit = time.split(":");
                int hour = Integer.parseInt(timeSplit[0]);
                if(hour<=13) {
                    if (timeSplit[1].equals("00")) {
                        timeLabel.setText(timeSplit[0] + ":30");
                    }
                    if (timeSplit[1].equals("30")) {
                            timeLabel.setText((hour + 1) + ":00");
                    }
                }
            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = timeLabel.getText().toString();
                String[] timeSplit = time.split(":");
                int hour = Integer.parseInt(timeSplit[0]);
                if(!(hour==0&&timeSplit[1].equals("30"))) {
                    if (timeSplit[1].equals("00")) {
                        timeLabel.setText((hour - 1) + ":30");
                    }
                    if (timeSplit[1].equals("30")) {
                        timeLabel.setText((hour) + ":00");
                    }
                }
            }
        });

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (groupMember.getText().toString().matches("")) {
                    Toast.makeText(activity, "Digite um Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(members.getText().toString().matches("")){
                    members.setText(groupMember.getText().toString());

                }else{
                    members.setText(members.getText().toString()+","+groupMember.getText().toString());

                }

                groupMember.setText("");
            }
        });






        btn_set_date_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        mDrawerLayout.requestLayout();
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mDrawerLayout.bringToFront();

        setNavigationViewListener();
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        final String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();




        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titleTask.getText().toString().matches("")) {
                    Toast.makeText(activity, "Digite um Título", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (subjectTask.getText().toString().matches("")) {
                    Toast.makeText(activity, "Digite uma Matéria", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (descriptionTask.getText().toString().matches("")) {
                    Toast.makeText(activity, "Digite uma Descrição", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (date_time.matches("")) {
                    Toast.makeText(activity, "Entre com Data e Hora", Toast.LENGTH_SHORT).show();
                    return;
                }
                String time = timeLabel.getText().toString();
                String[] timeSplit = time.split(":");
                hour[0] = Integer.parseInt(timeSplit[0]);
                minute[0] = Integer.parseInt(timeSplit[1]);


                final String title = titleTask.getText().toString();
                final String subject = subjectTask.getText().toString();
                final String description = descriptionTask.getText().toString();
                final String end_date = et_show_date_time.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("RESPOSTA TASK "+response);
                            JSONObject jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");


                            if (success) {
                                String lastTaskInserted = jsonResponse.getString("taskInserted");

                                Response.Listener<String> responseListener6 = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response6) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response6);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if(success==true){
                                                JSONArray jArray = jsonResponse.getJSONArray("taskBlockArray");
                                                jArray = sortJsonArrayByDate(jArray);
                                                long timeBlockBefore=0;
                                                long timeStart=0;
                                                long timeEnd=0;
                                                Date dateStartToInsert = null;
                                                Date dateEndToInsert= null;
                                                long progress = 0;
                                                int counter = 0;
                                                boolean last=false;
                                                for (int i = 0; i < jArray.length(); i++) {
                                                    if(i==jArray.length()-1) {
                                                        last = true;
                                                    }
                                                    JSONObject json_data = jArray.getJSONObject(i);
                                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                    Date dateStart = null;
                                                    Date dateEnd = null;
                                                    SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
                                                    Date day = null;



                                                    try {
                                                        dateStart = format.parse(json_data.getString("day")+" "+json_data.getString("time_start"));
                                                        dateEnd= format.parse(json_data.getString("day")+" "+json_data.getString("time_end"));
                                                        day = dayFormat.parse(json_data.getString("day"));
                                                        long time = dateStart.getTime();

                                                        if(time-timeBlockBefore==1800000 && last==false){
                                                            timeEnd = dateEnd.getTime();
                                                            dateEndToInsert = dateEnd;
                                                            counter++;
                                                        }else{
                                                            if(timeStart==0 && last==false){
                                                                timeStart = dateStart.getTime();
                                                                dateStartToInsert = dateStart;
                                                                counter++;
                                                            }else{
                                                                if(last==true &&timeStart==0){
                                                                    timeStart = dateStart.getTime();
                                                                    dateStartToInsert = dateStart;

                                                                    timeEnd = dateEnd.getTime();
                                                                    dateEndToInsert = dateEnd;
                                                                }
                                                                if(last==true){
                                                                    timeEnd = dateEnd.getTime();
                                                                    dateEndToInsert = dateEnd;
                                                                }
                                                                if(counter == 0){
                                                                    timeStart = dateStart.getTime();
                                                                    dateStartToInsert = dateStart;

                                                                    timeEnd = dateEnd.getTime();
                                                                    dateEndToInsert = dateEnd;
                                                                }
                                                                System.out.println("Data inserida: "+dateStartToInsert.toString()+"---"+dateEndToInsert.toString());
                                                                double progressToCommit = ((progress/((double)(jArray.length())))*100);
                                                                System.out.println();
                                                                Date deadline= format.parse(json_data.getString("deadline"));
                                                                FirebaseDatabase.getInstance().getReference().child("Recomendations").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(new Recomendation(json_data.getString("title"), json_data.getString("subject"), timeStart, timeEnd,deadline.getTime(), progressToCommit, false, day.getTime()));
                                                                timeStart = dateStart.getTime();
                                                                dateStartToInsert = dateStart;
                                                                progress = i;
                                                                counter=0;
                                                            }
                                                        }

                                                        timeBlockBefore=time;
                                                    } catch (ParseException e) {
                                                        // TODO Auto-generated catch block
                                                        e.printStackTrace();
                                                    }

                                                }

                                            }else{
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                TaskInsertedDAO tidao = new TaskInsertedDAO(lastTaskInserted, responseListener6);
                                RequestQueue queue = Volley.newRequestQueue(AddTask.this);
                                queue.add(tidao);



                                Response.Listener<String> responseListener4 = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response4) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response4);
                                            boolean success = jsonResponse.getBoolean("success");
                                            if(success){


                                                try
                                                {
                                                    ArrayList<TaskModel> tmodelList = new ArrayList<>();
                                                    JSONArray jArray = jsonResponse.getJSONArray("tasksArray");
                                                    for (int i = 0; i < jArray.length(); i++)
                                                    {
                                                        JSONObject json_data = jArray.getJSONObject(i);
                                                        TaskModel tmodel = new TaskModel(json_data.getString("id_task"),json_data.getString("id_user"),json_data.getString("title"),json_data.getString("subject"),json_data.getString("description"),json_data.getString("estimated_time"),json_data.getString("deadline"),json_data.getString("progress"),json_data.getString("group"));
                                                        tmodelList.add(tmodel);
                                                    }
                                                    SharedPreferences appSharedPrefs = PreferenceManager
                                                            .getDefaultSharedPreferences(getApplicationContext());
                                                    SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                                                    Gson gson = new Gson();
                                                    prefsEditor.putString("TaskList", gson.toJson(tmodelList));
                                                    prefsEditor.apply();
                                                    prefsEditor.commit();


                                                }
                                                catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                }





                                                Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response2) {

                                                        try {
                                                            JSONObject jsonResponse = new JSONObject(response2);
                                                            boolean success = jsonResponse.getBoolean("success");
                                                            if(success){
                                                                try
                                                                {

                                                                    JSONArray jArray = jsonResponse.getJSONArray("timesArray");
                                                                    ArrayList<String> times_r2 = new ArrayList<String>();
                                                                    ArrayList<TimeModel> tmlist = new ArrayList<>();
                                                                    JSONArray times_r2JSON = new JSONArray();
                                                                    for (int i = 0; i < jArray.length(); i++) {
                                                                        JSONObject json_data = jArray.getJSONObject(i);
                                                                        times_r2.add(json_data.getString("id_time")+","+json_data.getString("id_user")+","+json_data.getString("day"));
                                                                        times_r2JSON.put(json_data.getString("id_time"));
                                                                        TimeModel tmodel = new TimeModel(json_data.getString("id_time"), json_data.getString("id_user"), json_data.getString("day"));
                                                                        tmlist.add(tmodel);
                                                                    }


                                                                    SharedPreferences appSharedPrefs = PreferenceManager
                                                                            .getDefaultSharedPreferences(getApplicationContext());
                                                                    SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                                                                    Gson gson = new Gson();
                                                                    prefsEditor.putString("TimeList", gson.toJson(tmlist));
                                                                    prefsEditor.apply();
                                                                    prefsEditor.commit();



                                                                    Response.Listener<String> responseListener3 = new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String response23) {
                                                                            try {
                                                                                JSONObject jsonResponse = new JSONObject(response23);
                                                                                boolean success = jsonResponse.getBoolean("success");
                                                                                if(success){
                                                                                    ArrayList<TimeBlockModel> listTbmodel = new ArrayList<>();
                                                                                    try
                                                                                    {
                                                                                        JSONArray jArray2 = jsonResponse.getJSONArray("timesBlockArray");
                                                                                        for (int i = 0; i < jArray2.length(); i++)
                                                                                        {
                                                                                            JSONObject json_data = jArray2.getJSONObject(i);
                                                                                            TimeBlockModel tbmodel = new TimeBlockModel(json_data.getString("id_time_block"),json_data.getString("id_time"), json_data.getString("time_start"), json_data.getString("time_end"), json_data.getString("part"),json_data.getString("availability"));
                                                                                            listTbmodel.add(tbmodel);


                                                                                        }
                                                                                        SharedPreferences appSharedPrefs = PreferenceManager
                                                                                                .getDefaultSharedPreferences(getApplicationContext());
                                                                                        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                                                                                        Gson gson = new Gson();
                                                                                        prefsEditor.putString("TimeBlockList", gson.toJson(listTbmodel));
                                                                                        prefsEditor.apply();
                                                                                        prefsEditor.commit();
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





                                                                    GetTimeBlockDAO gtbdao = new GetTimeBlockDAO(times_r2JSON,responseListener3);
                                                                    RequestQueue queue = Volley.newRequestQueue(AddTask.this);
                                                                    queue.add(gtbdao);



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
                                                RequestQueue queue = Volley.newRequestQueue(AddTask.this);
                                                queue.add(gtdao);

                                                
                                            }else{
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                GetTasksDAO gtsksdao = new GetTasksDAO(user_id,responseListener4);
                                queue = Volley.newRequestQueue(AddTask.this);
                                queue.add(gtsksdao);


                            } else {
                                String totalAvailable = jsonResponse.getString("available");
                                AlertDialog.Builder builder = new AlertDialog.Builder(AddTask.this);
                                builder.setMessage("Você possui apenas "+totalAvailable+" horas até a data da entrega, e quer cadastrar "+ hour[0] +":"+ minute[0] +" Horas."+ " Cadastre mais horas acessando o seu Perfil").setNegativeButton("Ok", null).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                String group = "0";
if(members.getText().toString().matches("")){
    group = "0";
}else{
    group = "1";
}
                TaskDAO rdao = new TaskDAO(user_id,title, subject, description, hour[0] +":"+ minute[0], end_date, members.getText().toString(), group,responseListener);
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

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
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

    public static JSONArray sortJsonArrayByDate(JSONArray array) {
        List<JSONObject> jsons = new ArrayList<JSONObject>();
        for (int i = 0; i < array.length(); i++) {
            try {
                jsons.add(array.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(jsons, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject lhs, JSONObject rhs) {

                Date dateStart1 = null;
                Date dateStart2 = null;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    dateStart1 =  format.parse(lhs.getString("day")+" "+lhs.getString("time_start"));
                    dateStart2 =  format.parse(rhs.getString("day")+" "+rhs.getString("time_start"));
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Here you could parse string id to integer and then compare.
                return dateStart1.compareTo(dateStart2);
            }
        });
        return new JSONArray(jsons);
    }
}
