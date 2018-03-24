package com.br.tcc.database.remote;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor on 1/17/2018.
 */

public class TaskDAO extends StringRequest {

    private static final String REQUISTER_REQUEST_URL="http://tcctarefas.xyz/task.php";
    private Map<String, String> params;

    public TaskDAO(String user_id, String title, String subject,  String description, String estimated_time, String deadline,Response.Listener<String> listener){
        super(Method.POST, REQUISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_user", user_id);
        params.put("title", title);
        params.put("subject", subject);
        params.put("description", description);
        params.put("estimated_time", estimated_time);
        params.put("deadline", deadline);
        params.put("progress", "0");

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}