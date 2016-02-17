package com.bidhee.nagariknews.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.model.Multimedias;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ronem on 2/17/16.
 */
public class ImageSliderAdapter extends PagerAdapter {
    ArrayList<Multimedias> multimediaList;
    Context context;

    public ImageSliderAdapter(Context context, ArrayList<Multimedias> multimediaList) {
        this.context = context;
        this.multimediaList = multimediaList;
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

        ImageView imageView;

        View itemView = LayoutInflater.from(context).inflate(R.layout.gallery_image_item_for_slider, container, false);
        imageView = (ImageView) itemView.findViewById(R.id.gallery_item_image_view);

        Picasso.with(context).load(multimediaList.get(position).getMultimediaPath()).into(imageView);

        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);

        return itemView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((ViewGroup) object);
    }
}
