package com.example.victor.tcc;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Time extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        Intent intent = getIntent();
        final String user_id = intent.getStringExtra("user_id");
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
                    System.out.println("AQUI");
                    TimeDAO tdao = new TimeDAO(user_id,dayOfWeek.getText().toString(),initialTimeLabel.getText().toString()+":00",finalTimeLabel.getText().toString()+":00", responseListener);
                    System.out.println(user_id);
                    System.out.println(dayOfWeek.getText().toString());
                    System.out.println(initialTimeLabel.getText().toString()+":00");
                    System.out.println(finalTimeLabel.getText().toString()+":00");
                    System.out.println("AQUI2");
                    RequestQueue queue = Volley.newRequestQueue(Time.this);
                    System.out.println("AQUI3");
                    queue.add(tdao);
                    System.out.println("AQUI4");
                }
                }

            }
        });

    }
}
