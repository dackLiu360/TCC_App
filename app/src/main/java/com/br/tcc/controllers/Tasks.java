package com.br.tcc.controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.tcc.assistants.CustomAdapter;
import com.br.tcc.assistants.TaskModel;
import com.example.victor.tcc.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Tasks extends ListFragment {
Context c;
    private static CustomAdapter adapter;
    public Tasks() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public Tasks(Context c) {
        this.c = c;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.activity_tasks, container, false);

        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        System.out.println("AGORA2");

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(c);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("TaskList", "");

        final ArrayList<TaskModel> listTasks  = gson.fromJson(json, ArrayList.class);


        System.out.println("TAMANHO N "+listTasks);


        adapter= new CustomAdapter(listTasks,getActivity());
        setListAdapter(adapter);

    }

}
