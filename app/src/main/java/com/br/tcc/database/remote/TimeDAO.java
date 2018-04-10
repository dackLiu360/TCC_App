package com.br.tcc.database.remote;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor on 1/8/2018.
 */

public class TimeDAO extends StringRequest{

    private static final String TIME_REQUEST_URL="http://tcctarefas.xyz/time.php";
    private Map<String, String> params;

    public TimeDAO(JSONArray timesCSV, Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.POST, TIME_REQUEST_URL, listener, errorListener);
        params = new HashMap<>();
        System.out.println("LIST IN TIMEDAO "+timesCSV.toString());
        params.put("timesCSV", timesCSV.toString());
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}