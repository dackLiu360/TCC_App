package com.br.tcc.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.br.tcc.assistants.Recomendation;
import com.example.victor.tcc.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Recommendations extends Fragment{
    Date todayWithZeroTime = null;
    Intent intent;
    @SuppressLint("ValidFragment")
    public Recommendations(Intent intent) {
        this.intent = intent;
    }

    public Recommendations() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View rootView = inflater.inflate(R.layout.activity_recommendations, container, false);





        ListView listOfRecomendations= (ListView)rootView.findViewById(R.id.list_recomendations);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Recomendations").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        System.out.println("TESTE "+databaseReference.getRoot().getClass());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date today = new Date();

        Calendar cl = Calendar.getInstance();
        cl.setTime(today);
        cl.set(Calendar.HOUR_OF_DAY, 23);
        cl.set(Calendar.MINUTE, 59);
        cl.set(Calendar.SECOND, 59);
        cl.set(Calendar.MILLISECOND, 999);
        System.out.println("TODAY "+cl.getTime().getTime());
        try {
            todayWithZeroTime = format.parse(format.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("REFERENCE "+databaseReference.getRef());
        FirebaseListAdapter<Recomendation> adapter = new FirebaseListAdapter<Recomendation>(this.getActivity(),Recomendation.class,R.layout.list_item_recomendations, databaseReference.orderByChild("startTime").startAt(todayWithZeroTime.getTime()).endAt(cl.getTime().getTime()))

        {
            @Override
            protected void populateView(View v, Recomendation model, int position) {
                Date date = new Date();
                System.out.println("TIME");
                System.out.println((DateFormat.format("dd-MM-yyyy HH:mm:ss", model.getDeadline()))+"    "+(DateFormat.format("dd-MM-yyyy HH:mm:ss",date.getTime())));
                //Get references to the views of list_item.xml
                ProgressBar pb = (ProgressBar) v.findViewById(R.id.pb);
                TextView recomendation_time_day = (TextView)v.findViewById(R.id.recomendation_time_day);
                TextView recomendation_time_time = (TextView)v.findViewById(R.id.recomendation_time_time);
                TextView task_title = (TextView)v.findViewById(R.id.task_title);
                TextView range = (TextView)v.findViewById(R.id.range);

                pb.setProgress((int)model.getProgress());
               recomendation_time_day.setText(DateFormat.format("dd-MM-yyyy", model.getDeadline()));
               recomendation_time_time.setText(DateFormat.format("HH:mm:ss", model.getDeadline()));
               task_title.setText(model.getTaskTitle());
               range.setText(DateFormat.format("HH:mm", model.getStartTime())+ " - "+DateFormat.format("HH:mm", model.getEndTime()));

                Date today = new Date();
                Date todayWithZeroTime2 = null;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    todayWithZeroTime2 = format.parse(format.format(today));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println("Today 1 "+todayWithZeroTime2.getTime()+" Today 2 "+todayWithZeroTime.getTime());
                if(todayWithZeroTime2.getTime()!=todayWithZeroTime.getTime()){
                    Recommendations.this.startActivity(intent);
                }


            }
        };


        listOfRecomendations.setAdapter(adapter);





        return rootView;
    }


}