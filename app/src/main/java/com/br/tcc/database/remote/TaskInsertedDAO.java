package com.br.tcc.database.remote;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor on 1/8/2018.
 */

public class TaskInsertedDAO extends StringRequest{

    private static final String REQUISTER_REQUEST_URL="http://tcctarefas.xyz/getlasttaskinserted.php";
    private Map<String, String> params;

    public TaskInsertedDAO(String id_task, Response.Listener<String> listener){
        super(Method.POST, REQUISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_task", id_task);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
