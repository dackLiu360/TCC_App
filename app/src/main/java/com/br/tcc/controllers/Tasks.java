package com.br.tcc.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.br.tcc.assistants.CustomAdapter;
import com.br.tcc.assistants.TaskModel;
import com.br.tcc.assistants.TimeBlockModel;
import com.br.tcc.assistants.TimeModel;
import com.example.victor.tcc.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class Tasks extends ListFragment {
Context c;
FragmentManager fm;
Activity a;
    private static CustomAdapter adapter;
    public Tasks() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public Tasks(Context c, FragmentManager fm, Activity a) {
        this.c = c;
        this.fm = fm;
        this.a = a;
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
        OverScrollDecoratorHelper.setUpStaticOverScroll(rootView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
        OverScrollDecoratorHelper.setUpStaticOverScroll(rootView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(c);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("TaskList", "");

        Type type2 = new TypeToken<ArrayList<TaskModel>>() {}.getType();
        ArrayList<TaskModel> listTasks = gson.fromJson(json, type2);


        adapter= new CustomAdapter(listTasks,getActivity(),fm, a);
        setListAdapter(adapter);


    }

}
