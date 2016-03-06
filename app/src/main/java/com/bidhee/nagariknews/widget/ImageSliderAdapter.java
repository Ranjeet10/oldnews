package com.bidhee.nagariknews.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.BasicUtilMethods;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.model.Multimedias;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ronem on 2/17/16.
 */
public class ImageSliderAdapter extends PagerAdapter {
    ArrayList<Multimedias> multimediaList;
    Context context;
    private String imagePath;
    private String imageName;
    private int TYPE;

    public ImageSliderAdapter(Context context, ArrayList<Multimedias> multimediaList, int TYPE) {
        this.context = context;
        this.multimediaList = multimediaList;
        this.TYPE = TYPE;
    }

    @Override
    public int getCount() {
        return multimediaList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ViewGroup) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        final ImageView imageView, btnSaveImage;

        View itemView = LayoutInflater.from(context).inflate(R.layout.gallery_image_item_for_slider, container, false);
        imageView = (ImageView) itemView.findViewById(R.id.gallery_item_image_view);
        btnSaveImage = (ImageView) itemView.findViewById(R.id.btn_save_image);
        final ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.image_loading_progress);

        imagePath = multimediaList.get(position).getMultimediaPath();
        Picasso.with(context).load(imagePath).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
            }
        });


        btnSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageName = BasicUtilMethods.getImageNameFromImagepath(imagePath);
                String dir = TYPE ==StaticStorage.PHOTOS ?
                        StaticStorage.FOLDER_ROOT + File.separator + StaticStorage.FOLDER_PHOTO :
                        StaticStorage.FOLDER_ROOT + File.separator + StaticStorage.FOLDER_CARTOON;

                BasicUtilMethods.saveFileToGalery(context, dir, imageName, imageView);
            }
        });

        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;

    }


    private Bitmap getBitmap(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        return bitmap;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((ViewGroup) object);
    }
}
