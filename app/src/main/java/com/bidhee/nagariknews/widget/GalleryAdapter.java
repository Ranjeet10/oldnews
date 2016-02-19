package com.bidhee.nagariknews.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bidhee.nagariknews.R;
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

    public GalleryAdapter(ArrayList<Multimedias> multimediaList) {
        this.multimediaList = multimediaList;
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
                .load(multimediaList.get(position).getMultimediaPath())
                .error(R.drawable.nagariknews)
                .placeholder(R.drawable.nagariknews)
                .into(holder.galleryThumbnail);
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
