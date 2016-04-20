package com.bidhee.nagariknews.controller.server_request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bidhee.nagariknews.controller.AppController;

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
    public static void getNewsTitle(String url, Response.Listener<String> response, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response, errorListener);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
