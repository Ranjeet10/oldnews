package com.bidhee.nagariknews.controller.server_request;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bidhee.nagariknews.controller.AppController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ronem on 4/20/16.
 */
public class WebService {

    /**
     * Getting String response from the server when hit to the passed url
     *
     * @param url
     * @param response
     * @param errorListener
     */
    public static void getServerData(String url, Response.Listener<String> response, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response, errorListener);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public static void registerUser(String url, final HashMap<String, String> params, Response.Listener<String> response, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
