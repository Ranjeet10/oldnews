package com.bidhee.nagariknews.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.model.NewsObj;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/9/16.
 */
public class NewsTitlesAdapter extends RecyclerView.Adapter<NewsTitlesAdapter.ViewHolder> {
    ArrayList<NewsObj> newsObjs;
    private int categoryId;
    private Context context;
//    private Boolean isToShowBreakingNewsTitle = true;
//    private Boolean isToShowsLatestNewsTitle = true;

    RecyclerPositionListener recyclerPositionListener;

    public interface RecyclerPositionListener {

        void onChildItemPositionListen(int position, View view);
    }

    public void setOnRecyclerPositionListener(RecyclerPositionListener recyclerPositionListener) {
        this.recyclerPositionListener = recyclerPositionListener;
    }


    public NewsTitlesAdapter(int categoryId, ArrayList<NewsObj> newsObjs) {
        this.newsObjs = newsObjs;
        this.categoryId = categoryId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.news_title_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        NewsObj no = newsObjs.get(position);

        if (categoryId == 0) {
            if (no.isToShow()) {
                holder.categoryTextView.setVisibility(View.VISIBLE);
            } else {
                holder.categoryTextView.setVisibility(View.GONE);
            }

        } else {
            holder.categoryTextView.setVisibility(View.GONE);
        }

        holder.categoryTextView.setText(no.getNewsCategory());

        Picasso.with(context)
                .load(no.getImg())
                .error(R.drawable.andyrubin)
                .placeholder(R.drawable.nagariknews)
                .into(holder.thumbnail);

        holder.newsTitleTv.setText(no.getTitle());
        holder.newsSemiDetailTv.setText(no.getDesc());
        holder.newsSourceTv.setText(no.getReportedBy());
        holder.newsDateTv.setText(no.getDate());

        View.OnClickListener myClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerPositionListener.onChildItemPositionListen(position, v);
            }
        };

        holder.newsShareTv.setOnClickListener(myClickListener);
        holder.showDetailTv.setOnClickListener(myClickListener);
        holder.newsShareTv.getRootView().setOnClickListener(myClickListener);
    }

    @Override
    public int getItemCount() {
        return newsObjs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.parent_topic_text_view)
        TextView categoryTextView;
        @Bind(R.id.news_title_thumbnail)
        ImageView thumbnail;
        @Bind(R.id.news_title_text_view)
        TextView newsTitleTv;
        @Bind(R.id.news_source_text_view)
        TextView newsSourceTv;
        @Bind(R.id.news_date_text_view)
        TextView newsDateTv;
        @Bind(R.id.news_semi_detail_text_view)
        TextView newsSemiDetailTv;
        @Bind(R.id.news_share_text_view)
        TextView newsShareTv;
        @Bind(R.id.news_show_detail_text_view)
        TextView showDetailTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
