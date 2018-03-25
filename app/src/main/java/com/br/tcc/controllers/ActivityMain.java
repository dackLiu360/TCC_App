package com.br.tcc.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.br.tcc.database.local.DataDAO;
import com.br.tcc.database.local.TimeBlockDAO;
import com.br.tcc.database.local.UserDAO;
import com.br.tcc.database.remote.GetTimeBlockDAO;
import com.br.tcc.database.remote.GetTimeDAO;
import com.br.tcc.database.remote.LoginDAO;
import com.example.victor.tcc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityMain extends Activity {
    LottieAnimationView animationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        final UserDAO udao = new UserDAO(this);
        final DataDAO dataDAO = new DataDAO(this);
        dataDAO.create();
        final TimeBlockDAO timeBlockDAO = new TimeBlockDAO(this);
        timeBlockDAO.create();
        udao.create();
        final EditText userLogin = (EditText) findViewById(R.id.userLogin);
        final EditText passwordLogin = (EditText) findViewById(R.id.passwordLogin);
        final Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        final TextView forgot = (TextView) findViewById(R.id.forgot);
        final TextView buttonGoToRegister = (TextView) findViewById(R.id.buttonGoToRegister);
        final RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.layout1);
        final RelativeLayout layout2 = (RelativeLayout) findViewById(R.id.layout2);
        final ArrayList idTimes = new ArrayList();

        // buttonTest.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //     public void onClick(View view) {
        //        Intent testIntent = new Intent(ActivityMain.this, Test.class);
        //       ActivityMain.this.startActivity(testIntent);
        //   }
        // });
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
                System.out.println("LOGIN");
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
                        System.out.println("RESPONSE");

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){

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
                                                    for (int i = 0; i < jArray.length(); i++)
                                                    {
                                                        JSONObject json_data = jArray.getJSONObject(i);
                                                        dataDAO.addData(json_data.getString("id_time"),json_data.getString("id_user"),json_data.getString("day"));




                                                        Response.Listener<String> responseListener3 = new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response23) {
                                                                try {
                                                                    JSONObject jsonResponse = new JSONObject(response23);
                                                                    boolean success = jsonResponse.getBoolean("success");
                                                                    if(success){
                                                                        try
                                                                        {
                                                                            JSONArray jArray2 = jsonResponse.getJSONArray("timesBlockArray");
                                                                            for (int i = 0; i < jArray2.length(); i++)
                                                                            {
                                                                                JSONObject json_data = jArray2.getJSONObject(i);
                                                                                timeBlockDAO.addData(json_data.getString("id_time_block"),json_data.getString("id_time"),json_data.getString("time_start"),json_data.getString("time_end"),json_data.getString("part"), json_data.getString("availability"));
                                                                                System.out.println("AQUI");
                                                                            }
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





                                                        GetTimeBlockDAO gtbdao = new GetTimeBlockDAO(json_data.getString("id_time"),responseListener3);
                                                        RequestQueue queue = Volley.newRequestQueue(ActivityMain.this);
                                                        queue.add(gtbdao);
                                                    }


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

                                String username = jsonResponse.getString("username");
                                int user_id = jsonResponse.getInt("user_id");
                                String email = jsonResponse.getString("email");
                                String name = jsonResponse.getString("name");
                                String password = jsonResponse.getString("password");
                                udao.addData(Integer.toString(user_id),name,username,email,password);

                                GetTimeDAO gtdao = new GetTimeDAO(Integer.toString(user_id),responseListener2);
                                RequestQueue queue = Volley.newRequestQueue(ActivityMain.this);
                                queue.add(gtdao);


                                Intent intent = new Intent(ActivityMain.this, HomePage.class);

                                ActivityMain.this.startActivity(intent);
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