package com.bidhee.nagariknews.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.model.Multimedias;
import com.bidhee.nagariknews.views.customviews.ImageSliderDialog;
import com.bidhee.nagariknews.widget.GalleryAdapter;
import com.bidhee.nagariknews.widget.RecyclerItemClickListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/17/16.
 */
public class FragmentGallery extends Fragment implements RecyclerItemClickListener.OnItemClickListener {

    @Bind(R.id.gallery_recycler_view)
    RecyclerView galleryRecyclerView;

    ArrayList<Multimedias> multimediaList;
    GalleryAdapter galleryAdapter;
    ImageSliderDialog imageSliderDialog;

    public static FragmentGallery createNewInstance() {
        return new FragmentGallery();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        multimediaList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            multimediaList.add(new Multimedias("id", "title" + i, "http://nagariknews.com/media/k2/items/cache/xaafbf109d9cc513c903b1a05e07fc919_L.jpg.pagespeed.ic.T8f9vg-kZj.webp"));
        }

        imageSliderDialog = new ImageSliderDialog();
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
        galleryRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        galleryRecyclerView.setHasFixedSize(true);
        galleryRecyclerView.setItemAnimator(new DefaultItemAnimator());

        galleryAdapter = new GalleryAdapter(multimediaList);
        galleryRecyclerView.setAdapter(galleryAdapter);
        galleryRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),0, this));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(View view,int parentPosition, int position) {
        imageSliderDialog.showDialog(getActivity(), multimediaList,position);
    }
}
