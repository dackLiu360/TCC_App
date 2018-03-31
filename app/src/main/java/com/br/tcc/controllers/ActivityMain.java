package com.br.tcc.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.br.tcc.assistants.TaskModel;
import com.br.tcc.assistants.TimeBlockModel;
import com.br.tcc.assistants.TimeModel;
import com.br.tcc.database.remote.CheckLoginDAO;
import com.br.tcc.database.remote.GetTasksDAO;
import com.br.tcc.database.remote.GetTimeBlockDAO;
import com.br.tcc.database.remote.GetTimeDAO;
import com.br.tcc.database.remote.LoginDAO;
import com.br.tcc.database.remote.RegisterDAO;
import com.example.victor.tcc.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.SQLOutput;
import java.util.ArrayList;


public class ActivityMain extends Activity {
    LottieAnimationView animationView;
    private int SIGN_IN_REQUEST_CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LottieAnimationView animationView;
        final RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.layout1);
        final RelativeLayout layout2 = (RelativeLayout) findViewById(R.id.layout2);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            layout1.setVisibility(View.INVISIBLE);
            layout2.setVisibility(View.INVISIBLE);
            animationView = findViewById(R.id.loadAnimation);
            animationView.setAnimation("loading.json");

            animationView.loop(true);
            animationView.setVisibility(View.VISIBLE);
            animationView.bringToFront();
            animationView.playAnimation();

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        System.out.println("RESPOSTA: "+response);
                        System.out.println("Fim Resposta: ");
                        JSONObject jsonResponse = new JSONObject(response);

                        boolean exists = jsonResponse.getBoolean("exists");
                        if(exists==false){
                            System.out.println("AQUI11");

                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    System.out.println("AQUI22");
                                    try {

                                        JSONObject jsonResponse = new JSONObject(response);
                                        boolean success = jsonResponse.getBoolean("success");
                                        if (success) {
                                                populate();
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this);
                                            builder.setMessage("O Cadastro falhou").setNegativeButton("Tentar novamente", null).create().show();

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            RegisterDAO rdao = new RegisterDAO(FirebaseAuth.getInstance().getCurrentUser().getEmail(), FirebaseAuth.getInstance().getCurrentUser().getUid(), responseListener);
                            RequestQueue queue = Volley.newRequestQueue(ActivityMain.this);
                            queue.add(rdao);


                        }else{
                            populate();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            CheckLoginDAO cldao = new CheckLoginDAO(FirebaseAuth.getInstance().getCurrentUser().getUid(), responseListener);
            RequestQueue queue = Volley.newRequestQueue(ActivityMain.this);
            queue.add(cldao);
            //startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        }else{
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            final Button buttonLoginEmail = (Button) findViewById(R.id.buttonEmail);
            final Button buttonLoginGoogle = (Button) findViewById(R.id.buttonLoginGoogle);



            buttonLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setProviders(AuthUI.EMAIL_PROVIDER).build(),SIGN_IN_REQUEST_CODE);
                System.out.println("ACABOU");




            }
        });


            buttonLoginGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setProviders(AuthUI.GOOGLE_PROVIDER).build(),SIGN_IN_REQUEST_CODE);
                }
            });

       }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                Intent intent = new Intent(ActivityMain.this, ActivityMain.class);
                ActivityMain.this.startActivity(intent);
            }
            else{
                System.out.println("ERRO: "+requestCode);
            }
        }
    }

    public void populate(){



        Response.Listener<String> responseListener4 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response4) {
                try {
                    JSONObject jsonResponse = new JSONObject(response4);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        try {
                            ArrayList<TaskModel> tmodelList = new ArrayList<>();
                            JSONArray jArray = jsonResponse.getJSONArray("tasksArray");
                            System.out.println("JARRAY "+jArray);
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject json_data = jArray.getJSONObject(i);
                                TaskModel tmodel = new TaskModel(json_data.getString("id_task"), json_data.getString("id_user"), json_data.getString("title"), json_data.getString("subject"), json_data.getString("description"), json_data.getString("estimated_time"), json_data.getString("deadline"), json_data.getString("progress"));
                                System.out.println("TMODEL "+tmodel);
                                tmodelList.add(tmodel);
                            }
                            SharedPreferences appSharedPrefs = PreferenceManager
                                    .getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                            Gson gson = new Gson();
                            prefsEditor.putString("TaskList", gson.toJson(tmodelList));
                            System.out.println("TASK LIST NO MAIN "+gson.toJson(tmodelList));
                            prefsEditor.apply();
                            prefsEditor.commit();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response2) {

                                try {
                                    JSONObject jsonResponse = new JSONObject(response2);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {

                                        try {

                                            JSONArray jArray = jsonResponse.getJSONArray("timesArray");
                                            ArrayList<String> times_r2 = new ArrayList<String>();
                                            ArrayList<TimeModel> tmlist = new ArrayList<>();
                                            JSONArray times_r2JSON = new JSONArray();
                                            for (int i = 0; i < jArray.length(); i++) {
                                                JSONObject json_data = jArray.getJSONObject(i);
                                                times_r2.add(json_data.getString("id_time") + "," + json_data.getString("id_user") + "," + json_data.getString("day"));
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
                                                        if (success) {
                                                            ArrayList<TimeBlockModel> listTbmodel = new ArrayList<>();
                                                            try {
                                                                JSONArray jArray2 = jsonResponse.getJSONArray("timesBlockArray");
                                                                for (int i = 0; i < jArray2.length(); i++) {
                                                                    JSONObject json_data = jArray2.getJSONObject(i);
                                                                    TimeBlockModel tbmodel = new TimeBlockModel(json_data.getString("id_time_block"), json_data.getString("id_time"), json_data.getString("time_start"), json_data.getString("time_end"), json_data.getString("part"), json_data.getString("availability"));

                                                                    listTbmodel.add(tbmodel);


                                                                }

                                                                SharedPreferences appSharedPrefs = PreferenceManager
                                                                        .getDefaultSharedPreferences(getApplicationContext());
                                                                SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                                                                Gson gson = new Gson();

                                                                prefsEditor.putString("TimeBlockList", gson.toJson(listTbmodel));
                                                                prefsEditor.apply();
                                                                prefsEditor.commit();

                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            Intent intent = new Intent(ActivityMain.this, HomePage.class);
                                                            ActivityMain.this.startActivity(intent);
                                                        } else {
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };


                                            GetTimeBlockDAO gtbdao = new GetTimeBlockDAO(times_r2JSON, responseListener3);
                                            RequestQueue queue = Volley.newRequestQueue(ActivityMain.this);
                                            queue.add(gtbdao);


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        GetTimeDAO gtdao = new GetTimeDAO(FirebaseAuth.getInstance().getCurrentUser().getUid(), responseListener2);
                        RequestQueue queue = Volley.newRequestQueue(ActivityMain.this);
                        queue.add(gtdao);

                    } else {
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GetTasksDAO gtsksdao = new GetTasksDAO(FirebaseAuth.getInstance().getCurrentUser().getUid(), responseListener4);
        RequestQueue queue = Volley.newRequestQueue(ActivityMain.this);
        queue.add(gtsksdao);

    }

}