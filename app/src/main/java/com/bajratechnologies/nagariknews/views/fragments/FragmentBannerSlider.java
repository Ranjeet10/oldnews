package com.bajratechnologies.nagariknews.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bajratechnologies.nagariknews.R;
import com.bajratechnologies.nagariknews.Utils.BasicUtilMethods;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 5/20/16.
 */
public class FragmentBannerSlider extends Fragment {
    @Bind(R.id.banner_image_view)
    ImageView bannerImageView;

    public static FragmentBannerSlider newInstance(String imageUrl) {
        FragmentBannerSlider fragmentBannerSlider = new FragmentBannerSlider();
        Bundle box = new Bundle();
        box.putString("imgurl", imageUrl);
        fragmentBannerSlider.setArguments(box);
        return fragmentBannerSlider;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_banner_slider, container, false);
        ButterKnife.bind(this, rootview);
        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String imageUrl = getArguments().getString("imgurl");
        BasicUtilMethods.loadImage(getActivity(), imageUrl, bannerImageView);
    }
}
