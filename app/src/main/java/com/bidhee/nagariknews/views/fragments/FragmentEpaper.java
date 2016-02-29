package com.bidhee.nagariknews.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.model.epaper.Epaper;
import com.bidhee.nagariknews.model.epaper.EpaperBundle;
import com.bidhee.nagariknews.views.activities.EpaperActivity;
import com.bidhee.nagariknews.widget.EpapersListAdapter;
import com.bidhee.nagariknews.widget.RecyclerItemClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/29/16.
 */
public class FragmentEpaper extends Fragment implements RecyclerItemClickListener.OnItemClickListener {

    @Bind(R.id.gallery_recycler_view)
    RecyclerView epaperRecyclerView;


    private int TYPE = 0;
    private EpaperBundle epaperBundle;
    private ArrayList<Epaper> epapers;
    EpapersListAdapter epapersListAdapter;


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

        epaperRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        epaperRecyclerView.setHasFixedSize(true);
        epaperRecyclerView.setItemAnimator(new DefaultItemAnimator());

        epapersListAdapter = new EpapersListAdapter(epapers, R.layout.single_row_gallery);
        epaperRecyclerView.setAdapter(epapersListAdapter);
        epaperRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), 0, this));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(View view, int parentPosition, int position) {
        Intent epaperIntent = new Intent(getActivity(), EpaperActivity.class);

        epaperIntent.putExtra(StaticStorage.KEY_EPAPER, epapers.get(position));

        startActivity(epaperIntent);
    }
}
