package com.br.tcc.controllers;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.victor.tcc.R;
import com.br.tcc.database.remote.RegisterDAO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final EditText nameRegister = (EditText) findViewById(R.id.nameRegister);
        final EditText userRegister = (EditText) findViewById(R.id.userRegister);
        final EditText emailRegister = (EditText) findViewById(R.id.emailRegister);
        final EditText passwordRegister = (EditText) findViewById(R.id.passwordRegister);
        final EditText confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        final Button buttonRegister = (Button) findViewById(R.id.buttonRegister);
        final TextView buttonGoToLogin = (TextView) findViewById(R.id.buttonGoToLogin);

        buttonGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(Register.this, ActivityMain.class);
                Register.this.startActivity(loginIntent);
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name = nameRegister.getText().toString();
                final String username = userRegister.getText().toString();
                final String email = emailRegister.getText().toString();
                final String password = passwordRegister.getText().toString();
                final String confirm = confirmPassword.getText().toString();
                boolean teste = isValidEmail(email);
                CharSequence inputStr = name;
                Pattern pattern = Pattern.compile(new String("^[a-zA-Z\\s]*$"));
                Matcher matcher = pattern.matcher(inputStr);


                if (username.trim().length() != 0) {
                    userRegister.setBackgroundResource(R.drawable.edtnormal);
                } else {
                    userRegister.setError("error");
                    userRegister.setBackgroundResource(R.drawable.edterr);
                }

                if (email.trim().length() != 0) {
                    emailRegister.setBackgroundResource(R.drawable.edtnormal);
                } else {
                    emailRegister.setError("error");
                    emailRegister.setBackgroundResource(R.drawable.edterr);
                }
                if (name.trim().length() != 0) {
                    nameRegister.setBackgroundResource(R.drawable.edtnormal);
                } else {
                    nameRegister.setError("error");
                    nameRegister.setBackgroundResource(R.drawable.edterr);
                }

                if (password.trim().length() != 0) {
                    passwordRegister.setBackgroundResource(R.drawable.edtnormal);
                } else {
                    passwordRegister.setError("error");
                    passwordRegister.setBackgroundResource(R.drawable.edterr);
                }

                if (confirm.trim().length() != 0) {
                    confirmPassword.setBackgroundResource(R.drawable.edtnormal);
                } else {
                    confirmPassword.setError("error");
                    confirmPassword.setBackgroundResource(R.drawable.edterr);
                }
                if (matcher.matches() && name.trim().length() != 0) {
                    nameRegister.setBackgroundResource(R.drawable.edtnormal);
                } else {
                    nameRegister.setError("error");
                    nameRegister.setBackgroundResource(R.drawable.edterr);
                }
                if (password.trim().length() >= 8) {
                    passwordRegister.setBackgroundResource(R.drawable.edtnormal);
                } else {
                    passwordRegister.setError("error");
                    passwordRegister.setBackgroundResource(R.drawable.edterr);
                }
                if (password.equals(confirm) && password.trim().length() != 0 && confirm.trim().length() != 0) {
                    passwordRegister.setBackgroundResource(R.drawable.edtnormal);
                    confirmPassword.setBackgroundResource(R.drawable.edtnormal);
                } else {
                    passwordRegister.setError("error");
                    passwordRegister.setBackgroundResource(R.drawable.edterr);
                    confirmPassword.setError("error");
                    confirmPassword.setBackgroundResource(R.drawable.edterr);
                }
                if (teste == true) {
                    emailRegister.setBackgroundResource(R.drawable.edtnormal);
                } else {
                    emailRegister.setError("error");
                    emailRegister.setBackgroundResource(R.drawable.edterr);
                }


                if (username.trim().length() != 0) {
                    if (email.trim().length() != 0) {
                        if (name.trim().length() != 0) {
                            if (password.trim().length() != 0) {
                                if (confirm.trim().length() != 0) {
                                    if (matcher.matches()) {
                                        if (teste == true) {

                                            if (password.trim().length() >= 8) {
                                                if (password.equals(confirm)) {

                                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {

                                                                JSONObject jsonResponse = new JSONObject(response);
                                                                boolean success = jsonResponse.getBoolean("success");
                                                                if (success) {

                                                                    Intent intent = new Intent(Register.this, ActivityMain.class);
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
                                                    RegisterDAO rdao = new RegisterDAO(name, username, email, password, responseListener);
                                                    RequestQueue queue = Volley.newRequestQueue(Register.this);
                                                    queue.add(rdao);


                                                } else {

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                                    builder.setMessage("As senhas não conferem").setNegativeButton("Tentar novamente", null).create().show();
                                                }
                                            } else {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                                builder.setMessage
                                                        ("A senha deve conter 8 digitos").setNegativeButton
                                                        ("Tentar novamente", null).create().show();

                                            }
                                        } else {

                                            AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                            builder.setMessage("Digite um email valido").setNegativeButton("Tentar novamente", null).create().show();


                                        }
                                    } else {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                        builder.setMessage("Digite um nome valido").setNegativeButton("Tentar novamente", null).create().show();

                                    }
                                } else {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                    builder.setMessage("Digite todos os campos").setNegativeButton("Tentar novamente", null).create().show();

                                }
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                builder.setMessage("Digite todos os campos").setNegativeButton("Tentar novamente", null).create().show();


                            }
                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                            builder.setMessage("Digite todos os campos").setNegativeButton("Tentar novamente", null).create().show();


                        }
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                        builder.setMessage("Digite todos os campos").setNegativeButton("Tentar novamente", null).create().show();

                    }
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setMessage("Digite todos os campos").setNegativeButton("Tentar novamente", null).create().show();
                }


            }
        });


    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


}



    //public static boolean isValidPassword(String s) {
    //  Pattern PASSWORD_PATTERN = Pattern.compile(
    //    "[a-zA-Z0-9\\!\\@\\#\\$]{8,24}"
    //);
    //return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    //}

