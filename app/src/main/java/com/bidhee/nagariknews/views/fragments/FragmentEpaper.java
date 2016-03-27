package com.bidhee.nagariknews.views.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.NewsData;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.model.NewsObj;
import com.bidhee.nagariknews.model.epaper.Epaper;
import com.bidhee.nagariknews.model.epaper.EpaperBundle;
import com.bidhee.nagariknews.views.activities.EpaperActivity;
import com.bidhee.nagariknews.widget.EndlessScrollListener;
import com.bidhee.nagariknews.widget.EpapersListAdapter;
import com.bidhee.nagariknews.widget.RecyclerItemClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/29/16.
 */
public class FragmentEpaper extends Fragment implements RecyclerItemClickListener.OnItemClickListener, SearchView.OnQueryTextListener {

    @Bind(R.id.gallery_recycler_view)
    RecyclerView epaperRecyclerView;
    @Bind(R.id.search_card_view)
    CardView searchCardView;
    @Bind(R.id.search_view)
    SearchView searchView;


    private int TYPE = 0;
    private EpaperBundle epaperBundle;
    private ArrayList<Epaper> epapers;
    private ArrayList<Epaper> epapersSearched;
    EpapersListAdapter epapersListAdapter;
    GridLayoutManager gridLayoutManager;


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
        Bundle args = getArguments();
        if (args != null) {
            TYPE = args.getInt(StaticStorage.KEY_EPAPER_TYPE);
        }

        epaperBundle = StaticStorage.getEpaperBundle(1);

        epapers = (ArrayList<Epaper>) epaperBundle.getEpapers();
        epapersSearched = epapers;

        Log.d("size", epapers.size() + "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchCardView.setVisibility(View.VISIBLE);

        //setting the color of text of searchview
        SearchView.SearchAutoComplete theTextArea = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        theTextArea.setTextColor(getResources().getColor(R.color.colorPrimary));


        gridLayoutManager = (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ?
                new GridLayoutManager(getActivity(), 2) :
                new GridLayoutManager(getActivity(), 4);

        epaperRecyclerView.setLayoutManager(gridLayoutManager);

        epaperRecyclerView.setHasFixedSize(true);
        epaperRecyclerView.setItemAnimator(new DefaultItemAnimator());

        epapersListAdapter = new EpapersListAdapter(epapersSearched, R.layout.single_row_gallery);
        epaperRecyclerView.setAdapter(epapersListAdapter);
        epaperRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), 0, this));

        searchView.setOnQueryTextListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(View view, int parentPosition, int position) {
        Intent epaperIntent = new Intent(getActivity(), EpaperActivity.class);

        epaperIntent.putExtra(StaticStorage.KEY_EPAPER, epapersSearched.get(position));

        startActivity(epaperIntent);
        Log.i("info", "clicked");
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
}
