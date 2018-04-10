package com.br.tcc.database.remote;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor on 1/8/2018.
 */

public class GetTasksDAO extends StringRequest{

    private static final String TIME_REQUEST_URL="http://tcctarefas.xyz/tasks.php";
    private Map<String, String> params;

    public GetTasksDAO(String id_user,Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST, TIME_REQUEST_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("id_user", id_user);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
