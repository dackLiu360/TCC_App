package com.br.tcc.database.remote;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor on 1/8/2018.
 */

public class CheckLoginDAO extends StringRequest{

    private static final String REQUISTER_REQUEST_URL="http://tcctarefas.xyz/checkuser.php";
    private Map<String, String> params;

    public CheckLoginDAO(String id_firebase, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST, REQUISTER_REQUEST_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("id_firebase", id_firebase);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
