package com.br.tcc.database.remote;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor on 1/8/2018.
 */

public class RegisterDAO extends StringRequest{

    private static final String REQUISTER_REQUEST_URL="http://tcctarefas.xyz/register.php";
    private Map<String, String> params;

    public RegisterDAO(String name, String username, String email, String password, Response.Listener<String> listener){
        super(Method.POST, REQUISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("username", username);
        params.put("email", email);
        params.put("password", password);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
