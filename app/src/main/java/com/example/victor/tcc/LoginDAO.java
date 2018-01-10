package com.example.victor.tcc;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor on 1/9/2018.
 */

public class LoginDAO extends StringRequest{

    private static final String LOGIN_REQUEST_URL="https://vicmc14.000webhostapp.com/login.php";
    private Map<String, String> params;

    public LoginDAO(String username, String password, Response.Listener<String> listener){
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
