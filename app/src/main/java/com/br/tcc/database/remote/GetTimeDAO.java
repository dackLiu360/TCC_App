package com.br.tcc.database.remote;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Victor on 1/8/2018.
 */

public class GetTimeDAO extends StringRequest{

    private static final String TIME_REQUEST_URL="http://tcctarefas.xyz/gettimedao.php";
    private Map<String, String> params;

    public GetTimeDAO(String user_id,Response.Listener<String> listener){
        super(Method.POST, TIME_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("user_id", user_id);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
