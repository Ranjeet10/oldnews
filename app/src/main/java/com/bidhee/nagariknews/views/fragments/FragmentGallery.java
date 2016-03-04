package com.bidhee.nagariknews.views.fragments;

import android.content.Intent;
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
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.model.Multimedias;
import com.bidhee.nagariknews.views.activities.YoutubePlayerActivity;
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
    private int TYPE;

    public static FragmentGallery createNewInstance(int type) {
        FragmentGallery frag = new FragmentGallery();
        Bundle box = new Bundle();
        box.putInt(StaticStorage.KEY_GALLERY_TYPE, type);
        frag.setArguments(box);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            TYPE = args.getInt(StaticStorage.KEY_GALLERY_TYPE);
        }
        multimediaList = StaticStorage.getGalleryList(TYPE);
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

        galleryAdapter = new GalleryAdapter(multimediaList, TYPE);
        galleryRecyclerView.setAdapter(galleryAdapter);
        galleryRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), 0, this));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(View view, int parentPosition, int position) {
        if (TYPE == StaticStorage.VIDEOS) {

            Intent playerIntent = new Intent(getActivity(), YoutubePlayerActivity.class);

            playerIntent.putExtra(StaticStorage.KEY_VIDEO_BUNDLE, multimediaList.get(position));
            startActivity(playerIntent);
        } else {
            imageSliderDialog.showDialog(getActivity(), multimediaList, position);
        }
    }
}
