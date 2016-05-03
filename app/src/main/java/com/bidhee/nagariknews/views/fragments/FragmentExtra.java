
package com.bidhee.nagariknews.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.BasicUtilMethods;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.Utils.ToggleRefresh;
import com.bidhee.nagariknews.model.ExtraModel;
import com.bidhee.nagariknews.views.activities.Dashboard;
import com.bidhee.nagariknews.views.customviews.ControllableAppBarLayout;
import com.bidhee.nagariknews.widget.ExtraAdapter;
import com.bidhee.nagariknews.widget.RecyclerItemClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/21/16.
 */
public class FragmentExtra extends Fragment implements RecyclerItemClickListener.OnItemClickListener {
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.gallery_recycler_view)
    RecyclerView recyclerView;
    ArrayList<ExtraModel> list;
    ExtraAdapter adapter;

    ControllableAppBarLayout appBarLayout;


    public static FragmentExtra createNewInstance() {
        return new FragmentExtra();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = Dashboard.sessionManager.getSwitchedNewsValue() == 1 ?
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

        /**
         * accessing the views of the parent activity {@link Dashboard}
         */
        appBarLayout = (ControllableAppBarLayout) (getActivity().findViewById(R.id.app_bar_layout));

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), 0, this));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ToggleRefresh.hideRefreshDialog(swipeRefreshLayout);
                BasicUtilMethods.collapseAppbar(appBarLayout, null);

            }
        });
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