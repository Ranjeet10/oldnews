package com.bidhee.nagariknews.views.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.BasicUtilMethods;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.Utils.ToggleRefresh;
import com.bidhee.nagariknews.controller.server_request.ServerConfig;
import com.bidhee.nagariknews.controller.server_request.WebService;
import com.bidhee.nagariknews.controller.sqlite.SqliteDatabase;
import com.bidhee.nagariknews.model.epaper.Epaper;
import com.bidhee.nagariknews.model.epaper.EpaperBundle;
import com.bidhee.nagariknews.views.activities.BaseThemeActivity;
import com.bidhee.nagariknews.views.activities.Dashboard;
import com.bidhee.nagariknews.views.activities.GalleryViewActivity;
import com.bidhee.nagariknews.views.customviews.ControllableAppBarLayout;
import com.bidhee.nagariknews.views.customviews.MySnackbar;
import com.bidhee.nagariknews.widget.EpapersListAdapter;
import com.bidhee.nagariknews.widget.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/29/16.
 */
public class FragmentEpaper extends Fragment implements RecyclerItemClickListener.OnItemClickListener, SearchView.OnQueryTextListener {

    private String TAG = getClass().getSimpleName();

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.gallery_recycler_view)
    RecyclerView epaperRecyclerView;
    @Bind(R.id.search_card_view)
    CardView searchCardView;
    @Bind(R.id.search_view)
    SearchView searchView;


    //    private int TYPE = 0;

    private ArrayList<Epaper> epapers;
    private ArrayList<Epaper> epapersSearched;
    private String gallery = "epaper";
    ControllableAppBarLayout appBarLayout;
    EpapersListAdapter epapersListAdapter;
    GridLayoutManager gridLayoutManager;

    Response.Listener<String> response;
    Response.ErrorListener errorListener;

    private SqliteDatabase db;
    private Boolean isEpaperPagesPresent = false;


    // static factory method pattern
    public static FragmentEpaper createNewInstance(int type) {
        FragmentEpaper fragmentEpaper = new FragmentEpaper();
        Bundle box = new Bundle();
        box.putInt(StaticStorage.KEY_EPAPER_TYPE, type);
        fragmentEpaper.setArguments(box);
        return fragmentEpaper;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        epapers = new ArrayList<>();
        db = new SqliteDatabase(getActivity());
        db.open();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);

        Log.i(TAG, "onCreateView called");

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(TAG, "onViewCreated called");
        /**
         * accessing the views of the parent activity {@link Dashboard}
         */
        appBarLayout = (ControllableAppBarLayout) (getActivity().findViewById(R.id.app_bar_layout));

        searchCardView.setVisibility(View.GONE);

        //setting the color of text of searchview
        SearchView.SearchAutoComplete theTextArea = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        theTextArea.setTextColor(getResources().getColor(R.color.colorPrimary));


        gridLayoutManager = (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ?
                new GridLayoutManager(getActivity(), 2) :
                new GridLayoutManager(getActivity(), 4);

        epaperRecyclerView.setLayoutManager(gridLayoutManager);
        epaperRecyclerView.setHasFixedSize(true);
        epaperRecyclerView.setItemAnimator(new DefaultItemAnimator());
        epaperRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), 0, this));

        fetchEpaper();

        searchView.setOnQueryTextListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ToggleRefresh.hideRefreshDialog(swipeRefreshLayout);
                BasicUtilMethods.collapseAppbar(appBarLayout, null);
            }
        });
    }

    private void fetchEpaper() {
        if (BasicUtilMethods.isNetworkOnline(getActivity())) {
            ToggleRefresh.showRefreshDialog(getActivity(), swipeRefreshLayout);
            handleServerResponse();
            WebService.getServerData(ServerConfig.getEpaperListUrl(BaseThemeActivity.CURRENT_MEDIA), response, errorListener);
        } else {
            loadFromCache();
            MySnackbar.showSnackBar(getActivity(), epaperRecyclerView, BaseThemeActivity.NO_NETWORK).show();
        }
    }

    private void handleServerResponse() {
        response = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ToggleRefresh.hideRefreshDialog(swipeRefreshLayout);
                try {
                    db.deleteLocalGallery(BaseThemeActivity.CURRENT_MEDIA, gallery);
                    db.saveGallery(BaseThemeActivity.CURRENT_MEDIA, gallery, response);
                } catch (CursorIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                parseResponse(response);
            }

        };

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ToggleRefresh.hideRefreshDialog(swipeRefreshLayout);
                loadFromCache();
            }
        };
    }

    private void parseResponse(String response) {
        try {
            JSONObject nodeObject = new JSONObject(response);
            String status = nodeObject.getString("status");

            if (status.equalsIgnoreCase("success")) {
                JSONArray dataArray = nodeObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dObject = dataArray.getJSONObject(i);
                    int id = dObject.getInt("id");
                    String media = dObject.getString("media");
                    String date = dObject.getString("publishOn");
                    String coverImage = dObject.getString("coverImage");
                    int pages = dObject.getInt("totalFiles");

                    date = date.substring(0, date.lastIndexOf("T"));
                    epapers.add(new Epaper(id, media, date, coverImage, pages));
                }
                epapersSearched = epapers;
                epapersListAdapter = new EpapersListAdapter(epapersSearched, R.layout.single_row_gallery);
                epaperRecyclerView.setAdapter(epapersListAdapter);
            } else {
                MySnackbar.showSnackBar(getActivity(), epaperRecyclerView, StaticStorage.SOMETHING_WENT_WRONG).show();
            }
            if (epapersSearched.size() > 0)
                searchCardView.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
            ToggleRefresh.hideRefreshDialog(swipeRefreshLayout);
        }
    }

    private void loadFromCache() {
        try {
            String response = db.getLocalGalleryResponse(BaseThemeActivity.CURRENT_MEDIA, gallery);
            if (!TextUtils.isEmpty(response)) {
                parseResponse(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(View view, int parentPosition, int position) {
        try {
            isEpaperPagesPresent = db.isEpaperPagePresent(BaseThemeActivity.CURRENT_MEDIA, String.valueOf(epapersSearched.get(position).getId()));
        } catch (CursorIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        if (BasicUtilMethods.isNetworkOnline(getActivity()) || isEpaperPagesPresent) {
            Intent epaperIntent = new Intent(getActivity(), GalleryViewActivity.class);
            epaperIntent.putExtra(StaticStorage.KEY_GALLERY_TYPE, StaticStorage.KEY_EPAPER);
            epaperIntent.putExtra("id", epapersSearched.get(position).getId());
            epaperIntent.putExtra("date", epapersSearched.get(position).getDate());
            epaperIntent.putExtra(StaticStorage.FOLDER_TYPE, StaticStorage.EPAPER);

            startActivity(epaperIntent);
            Log.i("info", "clicked");
        } else {
            MySnackbar.showSnackBar(getActivity(), epaperRecyclerView, BaseThemeActivity.NO_NETWORK).show();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        epapersSearched = new ArrayList<>();
        for (int i = 0; i < epapers.size(); i++) {

            if (epapers.get(i).getDate().contains(newText)) {

                epapersSearched.add(epapers.get(i));

            }
        }
        epapersListAdapter = new EpapersListAdapter(epapersSearched, R.layout.single_row_gallery);
        epaperRecyclerView.setAdapter(epapersListAdapter);

        return true;
    }

    public void getSelectedEpaperFor(int position) {
    }
}
