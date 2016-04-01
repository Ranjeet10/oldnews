package com.bidhee.nagariknews.views.fragments;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.controller.SessionManager;
import com.bidhee.nagariknews.model.ExtraModel;
import com.bidhee.nagariknews.widget.ExtraAdapter;
import com.bidhee.nagariknews.widget.RecyclerItemClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/21/16.
 */
public class FragmentExtra extends Fragment implements RecyclerItemClickListener.OnItemClickListener {
    @Bind(R.id.gallery_recycler_view)
    RecyclerView recyclerView;
    ArrayList<ExtraModel> list;
    ExtraAdapter adapter;

    SessionManager sessionManager;


    public static FragmentExtra createNewInstance() {
        return new FragmentExtra();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getActivity());

        list = sessionManager.getSwitchedNewsValue() == 1 ?
                StaticStorage.getExtraList() :
                StaticStorage.getAnyaList();

        adapter = new ExtraAdapter(list, getActivity());
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

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), 0, this));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onItemClick(View view, int parentPosition, int position) {
        Toast.makeText(getActivity(), list.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }
}
