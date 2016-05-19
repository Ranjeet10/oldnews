package com.bidhee.nagariknews.views.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.BasicUtilMethods;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.controller.interfaces.ListPositionListener;
import com.bidhee.nagariknews.model.Multimedias;
import com.bidhee.nagariknews.views.customviews.LisDialog;
import com.bidhee.nagariknews.views.customviews.MySnackbar;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ronem on 2/29/16.
 */
public class FragmentGallerySwipable extends Fragment {
    @Bind(R.id.image_title)
    TextView imageTitleTextView;
    @Bind(R.id.gallery_item_image_view)
    ImageView imageView;
    @Bind(R.id.image_loading_progress)
    ProgressBar progressBar;
    @Bind(R.id.reload_image_view)
    ImageView reloadImage;
    @Bind(R.id.btn_gallery_option)
    ImageView btnOption;

    private String imageTitle;
    LisDialog optionDialog;
    private String imagePath;
    private String imageName;
    private Boolean isImageLoaded = false;
    private int TYPE;

    public static FragmentGallerySwipable createNewInstance(Multimedias multimedias, int type) {
        FragmentGallerySwipable fragmentGallerySwipable = new FragmentGallerySwipable();
        Bundle box = new Bundle();
        box.putString("title", multimedias.getTitle());
        box.putString("image_path", multimedias.getMultimediaPath());
        box.putInt("type", type);
        fragmentGallerySwipable.setArguments(box);

        return fragmentGallerySwipable;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            imageTitle = args.getString("title");
            imagePath = args.getString("image_path");
            TYPE = args.getInt("type");
        }
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
                            imageName = BasicUtilMethods.getImageNameFromImagepath(imagePath);
                            String dir = TYPE == StaticStorage.PHOTOS ?
                                    StaticStorage.FOLDER_ROOT + File.separator + StaticStorage.FOLDER_PHOTO :
                                    StaticStorage.FOLDER_ROOT + File.separator + StaticStorage.FOLDER_CARTOON;

                            try {
                                BasicUtilMethods.saveFileToGalery(getActivity(), dir, imageName, imageView);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), StaticStorage.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 2:
                            BasicUtilMethods.shareLink(getActivity(), imagePath);
                            break;
                    }
                }
            });
            optionDialog.show();
        } else {
            MySnackbar.showSnackBar(getActivity(), imageView, "Image not loaded").show();
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
        try {
//            BasicUtilMethods.loadImage(getActivity(), imagePath, imageView);
//            imageTitleTextView.setVisibility(View.VISIBLE);
//            imageTitleTextView.setText(imageTitle);


            Picasso.with(getActivity())
                    .load(imagePath)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            isImageLoaded = true;
                        }

                        @Override
                        public void onError() {
                            isImageLoaded = false;
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
