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

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        final EditText userRegister = (EditText) findViewById(R.id.userRegister);
        final EditText emailRegister = (EditText) findViewById(R.id.emailRegister);
        final EditText passwordRegister = (EditText) findViewById(R.id.passwordRegister);
        final EditText confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        final Button buttonRegister = (Button) findViewById(R.id.buttonRegister);




        final TextView buttonGoToLogin = (TextView) findViewById(R.id.buttonGoToLogin);

        buttonGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(Register.this, Login.class);
                Register.this.startActivity(loginIntent);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String username = userRegister.getText().toString();
                final String email = emailRegister.getText().toString();
                final String password = passwordRegister.getText().toString();
                final String confirm = confirmPassword.getText().toString();
                if (password.equals(confirm)) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                System.out.println("RESPOSTA "+response.toString());
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    Intent intent = new Intent(Register.this, Login.class);
                                    Register.this.startActivity(intent);

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                    builder.setMessage("O Cadastro falhou").setNegativeButton("Tentar novamente", null).create().show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    RegisterDAO rdao = new RegisterDAO(username, email, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Register.this);
                    queue.add(rdao);

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setMessage("As senhas não conferem").setNegativeButton("Tentar novamente", null).create().show();

                }
            }
        });


    }
}
