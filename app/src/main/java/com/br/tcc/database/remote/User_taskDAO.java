package com.br.tcc.database.remote;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor on 1/8/2018.
 */

public class User_taskDAO extends StringRequest{

    private static final String TIME_REQUEST_URL="https://vicmc14.000webhostapp.com/user_task.php";
    private Map<String, String> params;

    public User_taskDAO(String id_user, String id_task, Response.Listener<String> listener){
        super(Method.POST, TIME_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_user", id_user);
        params.put("id_task", id_task);


    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
