package com.bidhee.nagariknews.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.model.Multimedias;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/17/16.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    ArrayList<Multimedias> multimediaList;
    Context context;
    int TYPE;

    public GalleryAdapter(ArrayList<Multimedias> multimediaList, int type) {
        this.multimediaList = multimediaList;
        this.TYPE = type;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.single_row_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        holder.galleryItemTitleTextView.setText(multimediaList.get(position).getTitle());


        Picasso.with(context)
                .load(TYPE == StaticStorage.VIDEOS ?
                        StaticStorage.VIDEO_THUMBNAIL_PREFIX + multimediaList.get(position).getMultimediaPath() + StaticStorage.VIDEO_THUMBNAIL_POSTFIX :
                        multimediaList.get(position).getMultimediaPath())
                .error(R.drawable.nagariknews)
                .placeholder(R.drawable.nagariknews)
                .into(holder.galleryThumbnail);
        Log.d("url",multimediaList.get(position).getMultimediaPath());

    }

    @Override
    public int getItemCount() {
        return multimediaList.size();
    }


    public static class GalleryViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.gallery_thumbnail)
        ImageView galleryThumbnail;
        @Bind(R.id.gallery_item_title)
        TextView galleryItemTitleTextView;


        public GalleryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
