package com.br.tcc.database.remote;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor on 1/8/2018.
 */

public class TimeDAO extends StringRequest{

    private static final String TIME_REQUEST_URL="http://tcctarefas.xyz/time.php";
    private Map<String, String> params;

    public TimeDAO(String id_user,String day,String time_start,String time_end,Response.Listener<String> listener){
        super(Method.POST, TIME_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_user", id_user);
        params.put("day", day);
        params.put("time_start", time_start);
        params.put("time_end", time_end);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
