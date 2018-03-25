package com.br.tcc.database.remote;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor on 3/25/2018.
 */

public class GetTimeBlockDAO extends StringRequest {

    private static final String TIME_BLOCK_REQUEST_URL="http://tcctarefas.xyz/gettimeblockdao.php";
    private Map<String, String> params;

    public GetTimeBlockDAO(String id_time,Response.Listener<String> listener){
        super(Request.Method.POST, TIME_BLOCK_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_time", id_time);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
