package com.example.victor.tcc;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText userLogin = (EditText) findViewById(R.id.userLogin);
        final EditText passwordLogin = (EditText) findViewById(R.id.passwordLogin);
        final Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        final TextView buttonGoToRegister = (TextView) findViewById(R.id.buttonGoToRegister);

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
                            if(success){
                                String username = jsonResponse.getString("username");
                                int user_id = jsonResponse.getInt("user_id");
                                System.out.println("AQUI10");
                                System.out.println(user_id);
                                Intent intent = new Intent(Login.this, HomePage.class);
                                intent.putExtra("username", username);
                                intent.putExtra("user_id", Integer.toString(user_id));


                                Login.this.startActivity(intent);
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                builder.setMessage("O Login falhou").setNegativeButton("Tentar novamente", null).create().show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginDAO ldao = new LoginDAO(username,password,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Login.this);
                queue.add(ldao);

            }
        });

    }
}
