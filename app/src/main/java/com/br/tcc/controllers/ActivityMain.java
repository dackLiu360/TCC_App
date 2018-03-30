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
import com.br.tcc.database.remote.GetTasksDAO;
import com.br.tcc.database.remote.GetTimeBlockDAO;
import com.br.tcc.database.remote.GetTimeDAO;
import com.br.tcc.database.remote.LoginDAO;
import com.example.victor.tcc.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class ActivityMain extends Activity {
    LottieAnimationView animationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final ConstraintLayout cl = (ConstraintLayout) findViewById(R.id.consLayout1);
        final EditText userLogin = (EditText) findViewById(R.id.userLogin);
        final EditText passwordLogin = (EditText) findViewById(R.id.passwordLogin);
        final Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        final TextView forgot = (TextView) findViewById(R.id.forgot);
        final TextView buttonGoToRegister = (TextView) findViewById(R.id.buttonGoToRegister);
        final RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.layout1);
        final RelativeLayout layout2 = (RelativeLayout) findViewById(R.id.layout2);
        final ArrayList idTimes = new ArrayList();

        buttonGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(ActivityMain.this, Register.class);
                ActivityMain.this.startActivity(registerIntent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username = userLogin.getText().toString();
                final String password = passwordLogin.getText().toString();
                if (username.trim().length() != 0) {
                    userLogin.setBackgroundResource(R.drawable.edtnormal);
                } else {
                    userLogin.setError("error");
                    userLogin.setBackgroundResource(R.drawable.edterr);
                }

                if (password.trim().length() != 0) {
                    passwordLogin.setBackgroundResource(R.drawable.edtnormal);
                } else {
                    passwordLogin.setError("error");
                    passwordLogin.setBackgroundResource(R.drawable.edterr);
                }




                if (username.trim().length() != 0) {
                    if (password.trim().length() != 0) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(userLogin.getWindowToken(), 0);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){



                                String username = jsonResponse.getString("username");
                                final int user_id = jsonResponse.getInt("user_id");
                                String email = jsonResponse.getString("email");
                                String name = jsonResponse.getString("name");
                                String password = jsonResponse.getString("password");


                                SharedPreferences appSharedPrefs = PreferenceManager
                                        .getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                                Gson gson = new Gson();
                                prefsEditor.putString("username", gson.toJson(username));
                                prefsEditor.putString("userId", gson.toJson(Integer.toString(user_id)));
                                prefsEditor.putString("email", gson.toJson(email));
                                prefsEditor.putString("name", gson.toJson(name));
                                prefsEditor.putString("password", gson.toJson(password));
                                prefsEditor.apply();
                                prefsEditor.commit();

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
                                                        TaskModel tmodel = new TaskModel(json_data.getString("id_task"),json_data.getString("id_user"),json_data.getString("title"),json_data.getString("subject"),json_data.getString("description"),json_data.getString("estimated_time"),json_data.getString("deadline"),json_data.getString("progress"));
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
                                                                                    Intent intent = new Intent(ActivityMain.this, HomePage.class);
                                                                                    ActivityMain.this.startActivity(intent);
                                                                                }else{
                                                                                }

                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    };



                                                                    GetTimeBlockDAO gtbdao = new GetTimeBlockDAO(times_r2JSON,responseListener3);
                                                                    RequestQueue queue = Volley.newRequestQueue(ActivityMain.this);
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

                                                GetTimeDAO gtdao = new GetTimeDAO(Integer.toString(user_id),responseListener2);
                                                RequestQueue queue = Volley.newRequestQueue(ActivityMain.this);
                                                queue.add(gtdao);

                                            }else{
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                GetTasksDAO gtsksdao = new GetTasksDAO(Integer.toString(user_id),responseListener4);
                                RequestQueue queue = Volley.newRequestQueue(ActivityMain.this);
                                queue.add(gtsksdao);






                            }else{
                                layout1.setVisibility(View.VISIBLE);
                                layout2.setVisibility(View.VISIBLE);
                                animationView.setVisibility(View.INVISIBLE);
                                userLogin.setError("error");
                                userLogin.setBackgroundResource(R.drawable.edterr);
                                passwordLogin.setError("error");
                                passwordLogin.setBackgroundResource(R.drawable.edterr);
                                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this);
                                builder.setMessage("O ActivityMain falhou").setNegativeButton("Tentar novamente", null).create().show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                layout1.setVisibility(View.INVISIBLE);
                layout2.setVisibility(View.INVISIBLE);
                animationView = findViewById(R.id.loadAnimation);
                animationView.setAnimation("loading.json");

                animationView.loop(true);
                animationView.setVisibility(View.VISIBLE);
                animationView.bringToFront();
                animationView.playAnimation();





                LoginDAO ldao = new LoginDAO(username,password,responseListener);
                RequestQueue queue = Volley.newRequestQueue(ActivityMain.this);
                queue.add(ldao);
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this);
                        builder.setMessage("Digite todos os campos").setNegativeButton("Tentar novamente", null).create().show();
                    }
                } else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMain.this);
                    builder.setMessage("Digite todos os campos").setNegativeButton("Tentar novamente", null).create().show();
                }


            }
        });

    }
}