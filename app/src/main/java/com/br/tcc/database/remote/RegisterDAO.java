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

    public RegisterDAO(String email, String id_firebase, Response.Listener<String> listener){
        super(Method.POST, REQUISTER_REQUEST_URL, listener, null);
        System.out.println("EMAIL "+email);
        System.out.println("IDFIREBASE "+id_firebase);
        params = new HashMap<>();
        params.put("email", email);
        params.put("id_firebase", id_firebase);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
