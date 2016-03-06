package com.bidhee.nagariknews.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ronem on 2/29/16.
 */
public class FragmentEpaperSwipable extends Fragment {
    @Bind(R.id.gallery_item_image_view)
    ImageView imageView;
    @Bind(R.id.image_loading_progress)
    ProgressBar progressBar;
    @Bind(R.id.reload_image_view)
    ImageView reloadImage;

    private String pageUrl = "";

    public static FragmentEpaperSwipable createNewInstance(String pageUrl) {
        FragmentEpaperSwipable fragmentEpaperSwipable = new FragmentEpaperSwipable();
        Bundle box = new Bundle();
        box.putString(StaticStorage.KEY_EPAPER_PAGE, pageUrl);
        fragmentEpaperSwipable.setArguments(box);

        return fragmentEpaperSwipable;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            pageUrl = args.getString(StaticStorage.KEY_EPAPER_PAGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_image_item_for_slider, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadImage(pageUrl);
    }

    private void loadImage(final String pageUrl) {
        progressBar.setVisibility(View.VISIBLE);
        Picasso.with(getActivity())
                .load(pageUrl)
                .error(R.drawable.nagariknews)
                .placeholder(R.drawable.nagariknews)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        try {
                            progressBar.setVisibility(View.GONE);
                            reloadImage.setVisibility(View.GONE);
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.GONE);
                        reloadImage.setVisibility(View.VISIBLE);
                        loadImage(pageUrl);
                    }
                });
    }

    @OnClick(R.id.reload_image_view)
    public void reloadImageOnClick() {
        loadImage(pageUrl);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
