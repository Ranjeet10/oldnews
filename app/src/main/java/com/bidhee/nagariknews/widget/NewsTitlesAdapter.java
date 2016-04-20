package com.bidhee.nagariknews.widget;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.model.NewsObj;
import com.bidhee.nagariknews.views.activities.Dashboard;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.bidhee.nagariknews.R.anim.row_item_animation;

/**
 * Created by ronem on 2/9/16.
 */
public class NewsTitlesAdapter extends RecyclerView.Adapter<NewsTitlesAdapter.ViewHolder> {
    ArrayList<NewsObj> newsObjs;
    private int categoryId;
    private Context context;
    private Boolean isFromDetail;

    RecyclerPositionListener recyclerPositionListener;

    public interface RecyclerPositionListener {

        void onChildItemPositionListen(int position, View view, Boolean isShown);
    }

    public void setOnRecyclerPositionListener(RecyclerPositionListener recyclerPositionListener) {
        this.recyclerPositionListener = recyclerPositionListener;
    }


    public NewsTitlesAdapter(Boolean isFromDetail, int categoryId, ArrayList<NewsObj> newsObjs) {
        this.newsObjs = newsObjs;
        this.categoryId = categoryId;
        this.isFromDetail = isFromDetail;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view;
        if (isFromDetail) {
            view = LayoutInflater.from(context).inflate(R.layout.news_title_layout, parent, false);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    StaticStorage.ROW_HEIGHT);
            view.setLayoutParams(params);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.news_title_layout, parent, false);
        }
//        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
//        view.startAnimation(animation);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NewsObj no = newsObjs.get(position);

        //categoryId == 1 means it is from breaking and latest news
        if (categoryId == 1) {
            if (no.isToShow()) {
                holder.categoryTextView.setVisibility(View.VISIBLE);
                switch (Dashboard.sessionManager.getSwitchedNewsValue()) {
                    case 1:
//                        holder.categoryTextView.setBackgroundColor(context.getResources().getColor(R.color.republicaColorPrimary));
                        holder.categoryTextView.setBackgroundResource(R.drawable.corner_republica_background);
                        break;
                    case 2:
                        holder.categoryTextView.setBackgroundResource(R.drawable.corner_nagarik_background);
                        break;
                    case 3:
                        holder.categoryTextView.setBackgroundResource(R.drawable.corner_sukrabar_background);
                        break;
                }
            } else {
                holder.categoryTextView.setVisibility(View.GONE);
            }

        } else {
            holder.categoryTextView.setVisibility(View.GONE);
        }

        //setting margin to the cardview since cardview doesnot show shadow above 21
        //so we set margin to get shadow
        if (Build.VERSION.SDK_INT > 21) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, (int) context.getResources().getDimension(R.dimen.screen_padding_lr));
            holder.cardView.setLayoutParams(params);
        }


        holder.categoryTextView.setText(no.getNewsCategoryName());

        if (!TextUtils.isEmpty(no.getImg())) {
            try {
                Picasso.with(context)
                        .load(no.getImg())
                        .placeholder(R.drawable.nagariknews)
                        .error(R.drawable.nagariknews)
                        .into(holder.thumbnail);
            } catch (IllegalArgumentException iae) {
                iae.printStackTrace();
            }
        }

        holder.newsTitleTv.setText(no.getTitle());

        View.OnClickListener myClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerPositionListener.onChildItemPositionListen(position, v, (no.isToShow() != null) ? no.isToShow() : false);
            }
        };

        holder.newsShareTv.getRootView().setOnClickListener(myClickListener);
    }

    @Override
    public int getItemCount() {
        return newsObjs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.news_card_view)
        CardView cardView;
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
