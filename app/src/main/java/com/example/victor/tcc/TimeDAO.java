package com.example.victor.tcc;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor on 1/8/2018.
 */

public class TimeDAO extends StringRequest{

    private static final String TIME_REQUEST_URL="https://vicmc14.000webhostapp.com/horario.php";
    private Map<String, String> params;

    public TimeDAO(String user_id,String day,String initial_hour,String final_hour, Response.Listener<String> listener){
        super(Method.POST, TIME_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("day", day);
        params.put("initial_hour", initial_hour);
        params.put("final_hour", final_hour);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
