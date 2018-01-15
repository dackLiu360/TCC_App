package com.br.tcc.controllers;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.br.tcc.database.local.UserDAO;
import com.br.tcc.database.remote.LoginDAO;
import com.example.victor.tcc.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    LottieAnimationView animationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final UserDAO udao = new UserDAO(this);
        udao.create();
        final EditText userLogin = (EditText) findViewById(R.id.userLogin);
        final EditText passwordLogin = (EditText) findViewById(R.id.passwordLogin);
        final Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        final Button buttonTest = (Button) findViewById(R.id.buttonTest);
        final TextView buttonGoToRegister = (TextView) findViewById(R.id.buttonGoToRegister);


        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent testIntent = new Intent(Login.this, Test.class);
                Login.this.startActivity(testIntent);
            }
        });
        buttonGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(Login.this, Register.class);
                Login.this.startActivity(registerIntent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("LOGIN");
                final String username = userLogin.getText().toString();
                final String password = passwordLogin.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("RESPONSE");
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){;

                                String username = jsonResponse.getString("username");
                                int user_id = jsonResponse.getInt("user_id");
                                String email = jsonResponse.getString("email");

                                udao.addData(Integer.toString(user_id),username,email);
                                Intent intent = new Intent(Login.this, HomePage.class);

                                Login.this.startActivity(intent);
                            }else{
                                userLogin.setVisibility(View.VISIBLE);
                                passwordLogin.setVisibility(View.VISIBLE);
                                buttonLogin.setVisibility(View.VISIBLE);
                                buttonTest.setVisibility(View.VISIBLE);
                                buttonGoToRegister.setVisibility(View.VISIBLE);
                                animationView.setVisibility(View.INVISIBLE);
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                builder.setMessage("O Login falhou").setNegativeButton("Tentar novamente", null).create().show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                userLogin.setVisibility(View.INVISIBLE);
                passwordLogin.setVisibility(View.INVISIBLE);
                buttonLogin.setVisibility(View.INVISIBLE);
                buttonTest.setVisibility(View.INVISIBLE);
                buttonGoToRegister.setVisibility(View.INVISIBLE);
                animationView = (LottieAnimationView) findViewById(R.id.loadAnimation);
                animationView.setAnimation("loading.json");

                animationView.loop(true);
                animationView.setVisibility(View.VISIBLE);
                animationView.bringToFront();
                animationView.playAnimation();





                LoginDAO ldao = new LoginDAO(username,password,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Login.this);
                queue.add(ldao);

            }
        });

    }
}
