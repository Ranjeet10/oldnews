package com.bidhee.nagariknews.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.model.BreakingAndLatestNewsListModel;
import com.bidhee.nagariknews.model.NewsObj;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/19/16.
 */
public class BreakingAndLatestNewsChildAdapter extends RecyclerView.Adapter<BreakingAndLatestNewsChildAdapter.ChildViewHolder> {
    ArrayList<NewsObj> listModels;
    Context context;

    public BreakingAndLatestNewsChildAdapter(ArrayList<NewsObj> listModels) {
        this.listModels = listModels;
    }

    @Override
    public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.news_title_layout, parent, false);
        return new ChildViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChildViewHolder holder, int position) {
        holder.newsSourceTextView.setText(listModels.get(position).getReportedBy());
        holder.newsDateTextView.setText(listModels.get(position).getDate());
        holder.titleTextView.setText(listModels.get(position).getTitle());
        String desc = listModels.get(position).getIntroText();
        Picasso.with(context)
                .load(listModels.get(position).getImg())
                .placeholder(R.drawable.nagariknews)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.news_title_text_view)
        TextView titleTextView;
        @Bind(R.id.news_title_thumbnail)
        ImageView thumbnail;
        @Bind(R.id.news_source_text_view)
        TextView newsSourceTextView;
        @Bind(R.id.news_date_text_view)
        TextView newsDateTextView;


        public ChildViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
