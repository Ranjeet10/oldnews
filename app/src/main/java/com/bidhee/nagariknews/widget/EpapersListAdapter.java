package com.bidhee.nagariknews.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.model.epaper.Epaper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/29/16.
 */
public class EpapersListAdapter extends RecyclerView.Adapter<EpapersListAdapter.EpaperViewHolder> {
    ArrayList<Epaper> epapers;
    int singleItemRowId;
    Context context;

    public EpapersListAdapter(ArrayList<Epaper> epapers, int singleItemRowId) {
        this.epapers = epapers;
        this.singleItemRowId = singleItemRowId;
    }

    @Override
    public EpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(singleItemRowId, parent, false);
        return new EpaperViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EpaperViewHolder holder, int position) {

        holder.galleryItemTitleTextView.setText(epapers.get(position).getShowDate());
        holder.galleryThumbnail.setVisibility(View.GONE);
        holder.epaperThumbnail.setVisibility(View.VISIBLE);
        Picasso.with(context)
                .load(epapers.get(position).getCoverImage())
                .error(R.drawable.nagariknews)
                .placeholder(R.drawable.nagariknews)
                .resize(200, 280)
                .into(holder.epaperThumbnail);

    }

    @Override
    public int getItemCount() {
        return epapers.size();
    }

    public static class EpaperViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.gallery_thumbnail)
        ImageView galleryThumbnail;
        @Bind(R.id.epaper_thumbnail)
        ImageView epaperThumbnail;
        @Bind(R.id.gallery_item_title)
        TextView galleryItemTitleTextView;

        public EpaperViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
