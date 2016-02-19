package com.bidhee.nagariknews.views.customviews;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.model.Multimedias;
import com.bidhee.nagariknews.widget.ImageSliderAdapter;

import java.util.ArrayList;

/**
 * Created by ronem on 2/17/16.
 */
public class ImageSliderDialog {
    ViewPager galleryImageSilderPager;
    ImageSliderAdapter adapter;

    public void showDialog(Context context, ArrayList<Multimedias> galleryItemList,int selected_position) {
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.gallery_image_slider_dialog);

        galleryImageSilderPager = (ViewPager) dialog.findViewById(R.id.image_slider_pager);
        adapter = new ImageSliderAdapter(context, galleryItemList);
        galleryImageSilderPager.setAdapter(adapter);

        galleryImageSilderPager.setCurrentItem(selected_position);
        dialog.show();
    }
}
