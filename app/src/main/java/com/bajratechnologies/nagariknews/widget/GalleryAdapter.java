package com.bajratechnologies.nagariknews.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bajratechnologies.nagariknews.R;
import com.bajratechnologies.nagariknews.Utils.BasicUtilMethods;
import com.bajratechnologies.nagariknews.Utils.StaticStorage;
import com.bajratechnologies.nagariknews.model.Multimedias;

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

        View view = TYPE == StaticStorage.VIDEOS ? LayoutInflater.from(context).inflate(R.layout.single_row_video, parent, false) :
                LayoutInflater.from(context).inflate(R.layout.single_row_gallery, parent, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalleryViewHolder holder, int position) {
        Multimedias multimedias = multimediaList.get(position);
        holder.galleryItemTitleTextView.setText(multimedias.getTitle());

        /**
         * getting thumbnail path from the server accordingly
         * if the {@value TYPE} is of VIDEO, concatenate the url with the
         * {@link StaticStorage.VIDEO_THUMBNAIL_PREFIX} and
         * {@link StaticStorage.VIDEO_THUMBNAIL_POSTFIX}
         * else leave the url as it is
         */
        String url;
        if (TYPE == StaticStorage.VIDEOS) {
            url = StaticStorage.VIDEO_THUMBNAIL_PREFIX + multimedias.getMultimediaPath() + StaticStorage.VIDEO_THUMBNAIL_POSTFIX;
            holder.galleryItempublishDate.setText(multimedias.getDate());
        } else {
            url = multimedias.getMultimediaPath();

        }


        /**
         * calling the function to load the image in {@link holder.galleryThumbnail}
         * along with the disk cache enabled
         */
        BasicUtilMethods.loadImage(context, url, holder.galleryThumbnail);
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
        @Bind(R.id.gallery_item_date)
        TextView galleryItempublishDate;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
