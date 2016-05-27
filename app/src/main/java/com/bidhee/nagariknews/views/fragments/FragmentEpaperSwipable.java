package com.bidhee.nagariknews.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.BasicUtilMethods;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.controller.interfaces.ListPositionListener;
import com.bidhee.nagariknews.views.customviews.LisDialog;
import com.bidhee.nagariknews.views.customviews.MySnackbar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

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

    @Bind(R.id.btn_gallery_option)
    ImageView btnOption;

    LisDialog optionDialog;
    private String imageName;
    private Boolean isImageLoaded = false;
    private int TYPE;
    private String pageUrl = "";

    public static FragmentEpaperSwipable createNewInstance(String pageUrl) {
        FragmentEpaperSwipable fragmentEpaperSwipable = new FragmentEpaperSwipable();
        Bundle box = new Bundle();
        box.putString(StaticStorage.KEY_EPAPER_PAGE, pageUrl);
        fragmentEpaperSwipable.setArguments(box);

        return fragmentEpaperSwipable;
    }

    @OnClick(R.id.btn_gallery_option)
    public void onOptionMenuClicked() {
        if (isImageLoaded) {
            optionDialog = new LisDialog(getActivity());
            WindowManager.LayoutParams wmlp = optionDialog.getWindow().getAttributes();

            wmlp.gravity = Gravity.TOP | Gravity.LEFT;
            wmlp.x = 30;   //x position
            wmlp.y = 100;   //y position
            optionDialog.setListPositionListener(new ListPositionListener() {
                @Override
                public void tappedPosition(int position) {
                    switch (position) {
                        case 1:
                            imageName = BasicUtilMethods.getImageNameFromImagepath(pageUrl);
                            String dir = StaticStorage.FOLDER_ROOT + File.separator + StaticStorage.FOLDER_EPAPER;

                            BasicUtilMethods.saveFileToGalery(getActivity(), dir, imageName, imageView);
                            break;
                        case 2:
                            BasicUtilMethods.shareLink(getActivity(), pageUrl);
                            break;
                    }
                }
            });
            optionDialog.show();
        } else {
            MySnackbar.showSnackBar(getActivity(), imageView, "Image not loaded").show();
        }
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
                            isImageLoaded = true;
                            progressBar.setVisibility(View.GONE);
                            reloadImage.setVisibility(View.GONE);
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        }
                    }

                    @Override
                    public void onError() {
                        try {
                            progressBar.setVisibility(View.GONE);
                            reloadImage.setVisibility(View.VISIBLE);
                        } catch (NullPointerException npe) {
                            npe.printStackTrace();
                        }
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
