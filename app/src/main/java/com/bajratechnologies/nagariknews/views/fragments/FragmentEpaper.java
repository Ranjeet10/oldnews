package com.bajratechnologies.nagariknews.views.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Build;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bajratechnologies.nagariknews.BuildConfig;
import com.bajratechnologies.nagariknews.R;
import com.bajratechnologies.nagariknews.Utils.BasicUtilMethods;
import com.bajratechnologies.nagariknews.Utils.StaticStorage;
import com.bajratechnologies.nagariknews.Utils.ToggleRefresh;
import com.bajratechnologies.nagariknews.controller.server_request.ServerConfig;
import com.bajratechnologies.nagariknews.controller.server_request.WebService;
import com.bajratechnologies.nagariknews.controller.sqlite.SqliteDatabase;
import com.bajratechnologies.nagariknews.model.epaper.Epaper;
import com.bajratechnologies.nagariknews.views.activities.BaseThemeActivity;
import com.bajratechnologies.nagariknews.views.activities.Dashboard;
import com.bajratechnologies.nagariknews.views.activities.GalleryViewActivity;
import com.bajratechnologies.nagariknews.views.activities.PDFViewer;
import com.bajratechnologies.nagariknews.views.customviews.ControllableAppBarLayout;
import com.bajratechnologies.nagariknews.views.customviews.MySnackbar;
import com.bajratechnologies.nagariknews.widget.EpapersListAdapter;
import com.bajratechnologies.nagariknews.widget.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

    private int SELECTED_POSITION = 2;
    private ArrayList<Epaper> epapers;
    private ArrayList<Epaper> epapersSearched;
    private String gallery = "epaper";
    //    ControllableAppBarLayout appBarLayout;
    EpapersListAdapter epapersListAdapter;
    GridLayoutManager gridLayoutManager;

    private String EPAPER_URL = "";
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

        //disable the nestedScrollview to forbade appbarLayout's collapsing toolbar to collapse and expand
        epaperRecyclerView.setNestedScrollingEnabled(false);
        Log.i(TAG, "onViewCreated called");
//        /**
//         * accessing the views of the parent activity {@link Dashboard}
//         */
//        appBarLayout = (ControllableAppBarLayout) (getActivity().findViewById(R.id.app_bar_layout));

        searchCardView.setVisibility(View.GONE);

        //setting the color of text of searchview
        SearchView.SearchAutoComplete theTextArea = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        theTextArea.setTextColor(getResources().getColor(R.color.colorPrimary));


//        gridLayoutManager = (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ?
//                new GridLayoutManager(getActivity(), 2) :
//                new GridLayoutManager(getActivity(), 3);

//        gridLayoutManager = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ?
//                new GridLayoutManager(getActivity(), 2) :
//                new GridLayoutManager(getActivity(), 2);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        epaperRecyclerView.setLayoutManager(gridLayoutManager);
        epaperRecyclerView.setHasFixedSize(true);
        epaperRecyclerView.setItemAnimator(new DefaultItemAnimator());
        epaperRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), 0, this));

        handleServerResponse();
        fetchEpaper();

        searchView.setOnQueryTextListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ToggleRefresh.hideRefreshDialog(swipeRefreshLayout);
//                BasicUtilMethods.collapseAppbar(appBarLayout, null);
            }
        });
    }

    private void fetchEpaper() {
        if (BasicUtilMethods.isNetworkOnline(getActivity())) {
            ToggleRefresh.showRefreshDialog(getActivity(), swipeRefreshLayout);
//            handleServerResponse();
//            if (BaseThemeActivity.CURRENT_MEDIA.equals(BaseThemeActivity.NAGARIK)) {
//
//                if (SELECTED_POSITION == 0) {
//                    EPAPER_URL = ServerConfig.getEpaperListUrl(BaseThemeActivity.PURBELI);
//
//                } else if (SELECTED_POSITION == 1) {
//                    EPAPER_URL = ServerConfig.getEpaperListUrl(BaseThemeActivity.PASCHIMELI);
//
//                } else if (SELECTED_POSITION == 2) {
//                    EPAPER_URL = ServerConfig.getEpaperListUrl(BaseThemeActivity.CURRENT_MEDIA);
//                }
//
//                WebService.getServerData(EPAPER_URL, response, errorListener);
//            } else {
            EPAPER_URL = ServerConfig.getEpaperListUrl(BaseThemeActivity.CURRENT_MEDIA);
//            }

            Log.i("EpaperURL:", EPAPER_URL);
            WebService.getServerData(EPAPER_URL, response, errorListener);
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
//                    if (BaseThemeActivity.CURRENT_MEDIA.equals(BaseThemeActivity.NAGARIK)) {
//                        if (SELECTED_POSITION == 0) {
//
//                            db.deleteLocalGallery(BaseThemeActivity.PURBELI, gallery);
//                            db.saveGallery(BaseThemeActivity.PURBELI, gallery, response);
//
//                        } else if (SELECTED_POSITION == 1) {
//
//                            db.deleteLocalGallery(BaseThemeActivity.PASCHIMELI, gallery);
//                            db.saveGallery(BaseThemeActivity.PASCHIMELI, gallery, response);
//
//                        } else {
//
//                            db.deleteLocalGallery(BaseThemeActivity.CURRENT_MEDIA, gallery);
//                            db.saveGallery(BaseThemeActivity.CURRENT_MEDIA, gallery, response);
//
//                        }
//                    } else {
                    db.deleteLocalGallery(BaseThemeActivity.CURRENT_MEDIA, gallery);
                    db.saveGallery(BaseThemeActivity.CURRENT_MEDIA, gallery, response);
//                    }
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
        epapers = new ArrayList<>();
        Log.i("RESPONSE", response);
        try {
            JSONObject nodeObject = new JSONObject(response);
            String status = nodeObject.getString("status");

            if (status.equalsIgnoreCase("success")) {
                JSONArray dataArray = nodeObject.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dObject = dataArray.getJSONObject(i);
                    int id = dObject.getInt("id");
                    String media = dObject.getString("media");
                    String engDate = dObject.getString("publishOnDate");
                    String nepDate = dObject.getString("nepaliDate");
                    String coverImage = dObject.getString("coverImage");
                    String pdf = dObject.getString("pdfFile");

                    if (Dashboard.sessionManager.getSwitchedNewsValue() == 1) {
                        epapers.add(new Epaper(id, media, engDate, nepDate, engDate, coverImage, pdf));
                    } else {
                        epapers.add(new Epaper(id, media, engDate, nepDate, nepDate, coverImage, pdf));
                    }
                }

                /**
                 * sorting {@link epapers} in ascending order
                 * so that the latest {@link epapers} will be displayed on top of the list
                 */
                Comparator<Epaper> epaperComparator = new Comparator<Epaper>() {
                    @Override
                    public int compare(Epaper lhs, Epaper rhs) {
                        return rhs.getEngDate().compareTo(lhs.getEngDate());
                    }
                };

                Collections.sort(epapers, epaperComparator);

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
        Log.i(TAG, "loading from cache");
        try {
            String response;
            if (BaseThemeActivity.CURRENT_MEDIA.equals(BaseThemeActivity.NAGARIK)) {

                if (SELECTED_POSITION == 0) {
                    response = db.getLocalGalleryResponse(BaseThemeActivity.PURBELI, gallery);
                } else if (SELECTED_POSITION == 1) {
                    response = db.getLocalGalleryResponse(BaseThemeActivity.PASCHIMELI, gallery);
                } else {
                    response = db.getLocalGalleryResponse(BaseThemeActivity.CURRENT_MEDIA, gallery);
                }

            } else {
                response = db.getLocalGalleryResponse(BaseThemeActivity.CURRENT_MEDIA, gallery);
            }

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
//        try {
//            isEpaperPagesPresent = db.isEpaperPagePresent(BaseThemeActivity.CURRENT_MEDIA, String.valueOf(epapersSearched.get(position).getId()));
//        } catch (CursorIndexOutOfBoundsException e) {
//            e.printStackTrace();
//        }
//
//        if (BasicUtilMethods.isNetworkOnline(getActivity()) || isEpaperPagesPresent) {
//            Intent epaperIntent = new Intent(getActivity(), GalleryViewActivity.class);
//            epaperIntent.putExtra(StaticStorage.KEY_GALLERY_TYPE, StaticStorage.KEY_EPAPER);
//            epaperIntent.putExtra("id", epapersSearched.get(position).getId());
//            epaperIntent.putExtra("date", epapersSearched.get(position).getEngDate());
//            epaperIntent.putExtra(StaticStorage.FOLDER_TYPE, StaticStorage.EPAPER);
//
//            startActivity(epaperIntent);
//            Log.i("info", "clicked");
//        } else {
//            MySnackbar.showSnackBar(getActivity(), epaperRecyclerView, BaseThemeActivity.NO_NETWORK).show();
//        }

        Intent epaperIntent = new Intent(getActivity(), PDFViewer.class);
        epaperIntent.putExtra("pdf", epapersSearched.get(position).getPdf());
        startActivity(epaperIntent);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        epapersSearched = new ArrayList<>();
        for (int i = 0; i < epapers.size(); i++) {

            if (epapers.get(i).getEngDate().contains(newText)) {

                epapersSearched.add(epapers.get(i));

            }
        }
        epapersListAdapter = new EpapersListAdapter(epapersSearched, R.layout.single_row_gallery);
        epaperRecyclerView.setAdapter(epapersListAdapter);

        return true;
    }

    public void getSelectedEpaperFor(int position) {
        SELECTED_POSITION = position;
        fetchEpaper();
    }
}
