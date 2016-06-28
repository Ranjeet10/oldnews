package com.bajratechnologies.nagariknews.views.fragments;

/**
 * Created by ronem on 5/18/16.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bajratechnologies.nagariknews.R;
import com.bajratechnologies.nagariknews.Utils.StaticStorage;
import com.bajratechnologies.nagariknews.model.TabModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ronem on 2/9/16.
 */
public class FragmentMeroRuchi extends Fragment{
    @Bind(R.id.recycler_view_mero_ruchi)
    RecyclerView recyclerView;
    @Bind(R.id.content_not_found_parent_layout)
    LinearLayout contentNotFoundLayout;

    private String TAG = getClass().getSimpleName();


    public static FragmentMeroRuchi createNewInstance(TabModel tab) {

        FragmentMeroRuchi swipableFragment = new FragmentMeroRuchi();
        Bundle box = new Bundle();
        box.putString(StaticStorage.NEWS_CATEGORY_ID, tab.cat_id);
        box.putString(StaticStorage.NEWS_CATEGORY, tab.cat_name);
        swipableFragment.setArguments(box);
        return swipableFragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mero_ruchi, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

    }

    @OnClick(R.id.content_not_found_parent_layout)
    void onContentNotFoundLayoutClicked(){
        Toast.makeText(getActivity(),"clicked",Toast.LENGTH_SHORT).show();
    }
}
