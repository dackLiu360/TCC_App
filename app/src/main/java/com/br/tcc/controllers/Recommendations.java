package com.br.tcc.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.google.firebase.database.FirebaseDatabase;


public class Recommendations extends Fragment{

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
        FirebaseListAdapter<Recomendation> adapter = new FirebaseListAdapter<Recomendation>(this.getActivity(),Recomendation.class,R.layout.list_item_recomendations, FirebaseDatabase.getInstance().getReference().child("Recomendations").child(FirebaseAuth.getInstance().getCurrentUser().getUid()))

        {
            @Override
            protected void populateView(View v, Recomendation model, int position) {

                //Get references to the views of list_item.xml
                ProgressBar pb = (ProgressBar) v.findViewById(R.id.pb);
                TextView recomendation_time_day = (TextView)v.findViewById(R.id.recomendation_time_day);
                TextView recomendation_time_time = (TextView)v.findViewById(R.id.recomendation_time_time);
                TextView task_title = (TextView)v.findViewById(R.id.task_title);
                TextView range = (TextView)v.findViewById(R.id.range);

                pb.setProgress((int)model.getProgress());
               recomendation_time_day.setText(DateFormat.format("dd-MM-yyyy", model.getDeadline()));
               recomendation_time_time.setText(DateFormat.format("HH:mm:ss", model.getDeadline()));

                ;

               task_title.setText(model.getTaskTitle());
               range.setText(DateFormat.format("HH:mm", model.getStartTime())+ " - "+DateFormat.format("HH:mm", model.getEndTime()));
            }
        };


        listOfRecomendations.setAdapter(adapter);





        return rootView;
    }


}