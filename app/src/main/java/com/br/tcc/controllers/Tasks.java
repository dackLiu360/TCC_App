package com.br.tcc.controllers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.br.tcc.assistants.CustomAdapter;
import com.br.tcc.assistants.TaskModel;
import com.br.tcc.assistants.TimeBlockModel;
import com.br.tcc.assistants.TimeModel;
import com.br.tcc.database.remote.GetTasksDAO;
import com.br.tcc.database.remote.GetTimeBlockDAO;
import com.br.tcc.database.remote.GetTimeDAO;
import com.example.victor.tcc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


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
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



c = this.getContext();
        ImageView refresh = this.getActivity().findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                                            .getDefaultSharedPreferences(c);
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
                                                            .getDefaultSharedPreferences(c);
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
                                                                                .getDefaultSharedPreferences(c);
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
                                                                    Intent intent = new Intent(c, HomePage.class);
                                                                    c.startActivity(intent);
                                                                }else{
                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    };





                                                    GetTimeBlockDAO gtbdao = new GetTimeBlockDAO(times_r2JSON,responseListener3);
                                                    RequestQueue queue = Volley.newRequestQueue(c);
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

                                GetTimeDAO gtdao = new GetTimeDAO(FirebaseAuth.getInstance().getCurrentUser().getUid(),responseListener2);
                                RequestQueue queue = Volley.newRequestQueue(c);
                                queue.add(gtdao);


                            }else{
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Response.ErrorListener errorListener4 = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (volleyError.networkResponse == null) {
                            if (volleyError.getClass().equals(TimeoutError.class)) {
                                // Show timeout error message
                                Toast.makeText(c,
                                        "Oops. Timeout error!",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                };

                GetTasksDAO gtsksdao = new GetTasksDAO(FirebaseAuth.getInstance().getCurrentUser().getUid(),responseListener4,errorListener4);
                RequestQueue queue = Volley.newRequestQueue(c);
                queue.add(gtsksdao);



            }
        });






        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(c);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("TaskList", "");
        System.out.println("TASKLIST "+json);


        Type type2 = new TypeToken<ArrayList<TaskModel>>() {}.getType();
        ArrayList<TaskModel> listTasks = gson.fromJson(json, type2);


        adapter= new CustomAdapter(listTasks,getActivity(),fm, a);
        setListAdapter(adapter);


    }

}
