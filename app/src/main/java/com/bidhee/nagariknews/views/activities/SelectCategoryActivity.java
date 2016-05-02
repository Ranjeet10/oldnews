package com.bidhee.nagariknews.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bidhee.nagariknews.BuildConfig;
import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.controller.SessionManager;
import com.bidhee.nagariknews.controller.server_request.ServerConfig;
import com.bidhee.nagariknews.controller.server_request.WebService;
import com.bidhee.nagariknews.model.MyCheckBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ronem on 4/19/16.
 */
public class SelectCategoryActivity extends AppCompatActivity {
    private String TAG = getClass().getSimpleName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.select_category_layout)
    LinearLayout selectCategoryLayout;
    @Bind(R.id.btn_done)
    Button btnDone;
    @Bind(R.id.result)
    TextView resultView;

    ArrayList<MyCheckBox> listOfCheckedItem;


    private Response.Listener<String> serverResponse;
    private Response.ErrorListener errorListener;
    private ProgressDialog dialog;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.RepublicaTheme);
        setContentView(R.layout.select_category_layout);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("loading categories...");
        dialog.setCancelable(false);

        setUpToolBar();
        attachCheckBoxes();
    }

    @OnClick(R.id.btn_done)
    void onButtonDoneClick() {

        /**
         * first get the number of check items
         * for the category
         */
        int checkedCount = 0;
        for (int i = 0; i < listOfCheckedItem.size(); i++) {
            if (listOfCheckedItem.get(i).getIsChecked()) {
                checkedCount++;
            }
        }

        /**
         * if the checkedCount is greater than 0
         * make the array of the category id
         */
        if (checkedCount > 0) {
            String[] checkedArray = new String[checkedCount];
            checkedCount = 0;
            for (int i = 0; i < listOfCheckedItem.size(); i++) {
                if (listOfCheckedItem.get(i).getIsChecked()) {
                    checkedArray[checkedCount] = listOfCheckedItem.get(i).getId();
                    checkedCount++;
                }
            }
            String media = "nagarik";
            String categories = Arrays.toString(checkedArray);
            Log.i(TAG, categories);

            handlePostServerResponse();

            //header
            HashMap<String, String> header = new HashMap<>();
            header.put("apikey", sessionManager.getToken().toString());

            Log.i(TAG, "apikey: " + sessionManager.getToken());

            //params
            HashMap<String, String> params = new HashMap<>();
            params.put("media", media);
            params.put("categories", categories);

            dialog.setMessage("please wait...");
            dialog.show();
            WebService.saveCategoryList(ServerConfig.getCategoryListSaveurl(BuildConfig.BASE_URL_NAGARIK), header, params, serverResponse, errorListener);

        } else {
            Toast.makeText(getApplicationContext(), "select atleast one category", Toast.LENGTH_SHORT).show();
        }


    }

    private void handlePostServerResponse() {

        serverResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, response);
                dialog.dismiss();
                try {
                    JSONObject nodeObject = new JSONObject(response);
                    String status = nodeObject.getString("status");
                    if (status.equals("success")) {
//                        JSONObject dataObject = nodeObject.getJSONObject("data");
//                        String categories = dataObject.getString("categories");

                        Intent intent = new Intent(SelectCategoryActivity.this, Dashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        };
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    private void attachCheckBoxes() {

        listOfCheckedItem = new ArrayList<>();

        handleServerResponse();
        dialog.show();
        WebService.getServerData(ServerConfig.getCategoryListUrl(BuildConfig.BASE_URL_NAGARIK), serverResponse, errorListener);

    }

    private void handleServerResponse() {
        serverResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                try {
                    JSONObject nodeObject = new JSONObject(response);
                    String status = nodeObject.getString("status");

                    if (status.equals("success")) {
                        JSONArray dataArray = nodeObject.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject catObject = dataArray.getJSONObject(i);
                            String id = catObject.getString("id");
                            String name = catObject.getString("name");
                            listOfCheckedItem.add(new MyCheckBox(id, false, name));
                        }
                        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        for (int i = 0; i < listOfCheckedItem.size(); i++) {
                            final MyCheckBox myCheckBox = listOfCheckedItem.get(i);
                            CheckBox checkBox = new CheckBox(SelectCategoryActivity.this);
//            checkBox.setButtonDrawable(R.drawable.checkbox_selector_background);
                            checkBox.setChecked(myCheckBox.getIsChecked());
                            checkBox.setText(myCheckBox.getText());
                            selectCategoryLayout.addView(checkBox, params);
                            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    myCheckBox.setIsChecked(isChecked);

                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        ButterKnife.unbind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
