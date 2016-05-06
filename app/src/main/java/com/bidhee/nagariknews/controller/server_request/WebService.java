package com.bidhee.nagariknews.controller.server_request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.bidhee.nagariknews.controller.AppController;

import java.io.UnsupportedEncodingException;
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

    public static void hitServerWithParams(String url, final HashMap<String, String> params, Response.Listener<String> response, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }


    public static void getCategoryList(String url, final HashMap<String, String> header, Response.Listener<String> response, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return header;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public static void saveCategoryList(String url, final String apikey, final String body, Response.Listener<String> response, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //header
                HashMap<String, String> header = new HashMap<>();
//                header.put("apikey", apikey);
                header.put("apikey", "6scWbK6F9k4aEv7NM_bXfIKbtEX7Xqdg-whbxuJ-8dk");
                header.put("Content-Type", "application/json; charset=utf-8");
                return header;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return body.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public static void authRequest(String url, final String body, Response.Listener<String> response, Response.ErrorListener errorListener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response, errorListener) {
//            @Override
//            public String getBodyContentType() {
//                return "application/json";
//            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json; charset=utf-8");
                return header;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                byte[] byteBody = null;
                try {
                    byteBody = body.getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return byteBody;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
