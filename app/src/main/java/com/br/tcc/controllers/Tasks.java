package com.br.tcc.controllers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.br.tcc.assistants.CustomAdapter;
import com.br.tcc.assistants.TaskModel;
import com.example.victor.tcc.R;

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
        final ArrayList<TaskModel> listTasks = new ArrayList<>();
        final TasksDAO tdao = new TasksDAO(c);
        Cursor dataTasks = tdao.getData();
        while(dataTasks.moveToNext()){
            System.out.println("ENTROU NO WHILE");
            TaskModel tmodel = new TaskModel(dataTasks.getString(0),dataTasks.getString(1) ,dataTasks.getString(2) ,dataTasks.getString(3) ,dataTasks.getString(4) ,dataTasks.getString(5) ,dataTasks.getString(6) ,dataTasks.getString(7));
            listTasks.add(tmodel);
        }
        System.out.println("TAMANHO N "+listTasks);


        adapter= new CustomAdapter(listTasks,getActivity());
        setListAdapter(adapter);

    }

}
