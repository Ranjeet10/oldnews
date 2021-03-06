package com.bajratechnologies.nagariknews.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bajratechnologies.nagariknews.R;
import com.bajratechnologies.nagariknews.Utils.BasicUtilMethods;
import com.bajratechnologies.nagariknews.controller.SessionManager;
import com.bajratechnologies.nagariknews.controller.server_request.ServerConfig;
import com.bajratechnologies.nagariknews.controller.server_request.WebService;
import com.bajratechnologies.nagariknews.model.MyCheckBox;
import com.bajratechnologies.nagariknews.views.customviews.MySnackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ronem on 4/19/16.
 */
public class SelectCategoryActivity extends BaseThemeActivity {
    private String TAG = getClass().getSimpleName();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.select_category_layout)
    LinearLayout selectCategoryLayout;
    @Bind(R.id.btn_done)
    Button btnDone;
    @Bind(R.id.title_textview)
    TextView titleTextView;

    ArrayList<MyCheckBox> listOfCheckedItem;


    private Response.Listener<String> serverResponse;
    private Response.ErrorListener errorListener;
    private ProgressDialog dialog;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.select_category_layout);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("loading categories...");
        dialog.setCancelable(false);

        setUpToolBar();


        titleTextView.setText(BaseThemeActivity.SELECT_CATEGORY_TITLE);
        titleTextView.setTextColor(BaseThemeActivity.COLOR_PRIMARY_DARK);
        attachCheckBoxes();
    }

    @OnClick(R.id.btn_done)
    void onButtonDoneClick() {

        /**
         * first get the number of check items
         * for the category and add to the body list
         */

        ArrayList<Integer> bodyList = new ArrayList<>();
        for (int i = 0; i < listOfCheckedItem.size(); i++) {
            if (listOfCheckedItem.get(i).getIsPreferred()) {
                bodyList.add(Integer.parseInt(listOfCheckedItem.get(i).getId()));
            }
        }

        if (bodyList.size() > 0) {
            handlePostServerResponse();

            //body
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("categories", new JSONArray(bodyList));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String requestBody = jsonObject.toString();
            Log.i(TAG, "request body :" + requestBody);

            String apiKey = sessionManager.getToken();
            Log.i(TAG, apiKey);
            String url = ServerConfig.getCategoryListSaveurl(sessionManager.getSwitchedNewsValue());
            Log.i(TAG, url);

            dialog.setMessage("please wait...");
            dialog.show();
            WebService.saveCategoryList(url, apiKey, requestBody, serverResponse, errorListener);

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

                        launchDashboard();

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

    private void launchDashboard() {
        Intent intent = new Intent(SelectCategoryActivity.this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setUpToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(BaseThemeActivity.CURRENT_NEWS_TITLE);


    }

    private void attachCheckBoxes() {
        handleServerResponse();
        fetchCategoryFromServer();

    }

    private void fetchCategoryFromServer() {
        if (BasicUtilMethods.isNetworkOnline(this)) {
            HashMap<String, String> header = new HashMap<>();
            header.put("apikey", sessionManager.getToken());
            Log.i(TAG, sessionManager.getToken());

            dialog.show();
            WebService.getCategoryList(ServerConfig.getCategoryListUrl(sessionManager.getSwitchedNewsValue()), header, serverResponse, errorListener);
            btnDone.setBackgroundResource(ALERT_BUTTON_THEME_STYLE);
        } else {
            btnDone.setVisibility(View.GONE);
            MySnackbar.showSnackBar(this, selectCategoryLayout, BaseThemeActivity.NO_NETWORK).show();
        }
    }

    private void handleServerResponse() {
        serverResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                selectCategoryLayout.removeAllViews();
                listOfCheckedItem = new ArrayList<>();
                try {
                    JSONObject nodeObject = new JSONObject(response);
                    Log.i(TAG, response);
                    String status = nodeObject.getString("status");

                    if (status.equals("success")) {
                        JSONArray dataArray = nodeObject.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject catObject = dataArray.getJSONObject(i);
                            String id = catObject.getString("id");
                            String name = catObject.getString("name");
                            String alias = catObject.getString("alias");
                            Boolean isPreferred = catObject.getBoolean("isPreferred");

                            listOfCheckedItem.add(new MyCheckBox(id, name, alias, isPreferred));
                        }

                        //hide submit button if the list of checkboxes are equals to =0
                        if (listOfCheckedItem.size() > 0) {
                            btnDone.setVisibility(View.VISIBLE);
                            titleTextView.setVisibility(View.VISIBLE);
                        }

                        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        for (int i = 0; i < listOfCheckedItem.size(); i++) {
                            final MyCheckBox myCheckBox = listOfCheckedItem.get(i);
                            CheckBox checkBox = new CheckBox(SelectCategoryActivity.this);
                            checkBox.setPadding(20, 0, 0, 0);
                            checkBox.setChecked(myCheckBox.getIsPreferred());

//                            if (BaseThemeActivity.sessionManager.getSwitchedNewsValue() == 1) {
//                                checkBox.setText(myCheckBox.getAlias());
//                            } else {
                                checkBox.setText(myCheckBox.getName());
//                            }

                            selectCategoryLayout.addView(checkBox, params);
                            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    myCheckBox.setIsPreferred(isChecked);

                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    btnDone.setVisibility(View.GONE);
                }
            }
        };

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                btnDone.setVisibility(View.GONE);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.select_category_menu, menu);

        for (int i = 0; i < 2; i++) {
            final Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                final Drawable wrapped = DrawableCompat.wrap(drawable);
                drawable.mutate();
                DrawableCompat.setTint(wrapped, Color.WHITE);
                menu.getItem(i).setIcon(drawable);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refresh:
                fetchCategoryFromServer();
                break;
            case R.id.menu_skip:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ButterKnife.unbind(this);
    }

    @Override
    public void onBackPressed() {
        if (Dashboard.getInstance() != null) {
            finish();
            Log.i(TAG, "not null");
        } else {
            launchDashboard();
            Log.i(TAG, "was null");
        }
    }
}
