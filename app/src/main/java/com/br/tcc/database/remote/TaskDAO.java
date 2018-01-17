package com.br.tcc.database.remote;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor on 1/17/2018.
 */

public class TaskDAO extends StringRequest {

    private static final String REQUISTER_REQUEST_URL="https://vicmc14.000webhostapp.com/activities.php";
    private Map<String, String> params;

    public TaskDAO(String title, String subject,  String description, String difficulty_level, String day_end,Response.Listener<String> listener){
        super(Method.POST, REQUISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("title", title);
        params.put("subject", subject);
        params.put("description", description);
        params.put("difficulty_level", difficulty_level);
        params.put("day_end", day_end);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}