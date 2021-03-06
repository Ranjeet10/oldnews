package com.bajratechnologies.nagariknews.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bajratechnologies.nagariknews.R;
import com.bajratechnologies.nagariknews.model.NewsObj;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/9/16.
 */
public class RelatedNewsTitleAdapter extends RecyclerView.Adapter<RelatedNewsTitleAdapter.ViewHolder> {
    ArrayList<NewsObj> newsObjs;
    private int categoryId;
    private Context context;
    private Boolean isFromDetail;

    RecyclerPositionListener recyclerPositionListener;

    public interface RecyclerPositionListener {

        void onChildItemPositionListen(int position, View view);
    }

    public void setOnRecyclerPositionListener(RecyclerPositionListener recyclerPositionListener) {
        this.recyclerPositionListener = recyclerPositionListener;
    }


    public RelatedNewsTitleAdapter(Boolean isFromDetail, int categoryId, ArrayList<NewsObj> newsObjs) {
        this.newsObjs = newsObjs;
        this.categoryId = categoryId;
        this.isFromDetail = isFromDetail;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.single_row_related_news, parent, false);

//        if (isFromDetail) {
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    StaticStorage.ROW_HEIGHT);
//            view.setLayoutParams(params);
//        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NewsObj no = newsObjs.get(position);


//        //setting margin to the cardview since cardview doesnot show shadow above 21
//        //so we set margin to get shadow
//        if (Build.VERSION.SDK_INT > 21) {
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            params.setMargins(0, 0, 0, (int) context.getResources().getDimension(R.dimen.screen_padding_lr));
//            holder.cardView.setLayoutParams(params);
//        }


        holder.newsTitleTv.setText(no.getTitle());
        holder.newsDateTv.setText(no.getDate());
        Log.i("dateD", no.getDate());


        View.OnClickListener myClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerPositionListener.onChildItemPositionListen(position, v);
            }
        };

        holder.newsDateTv.getRootView().setOnClickListener(myClickListener);


//        if (no.isToShow() == 0) {
//            holder.cardView.setVisibility(View.GONE);
//        } else {
//            holder.cardView.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public int getItemCount() {
        return newsObjs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.news_title_text_view)
        TextView newsTitleTv;
        @Bind(R.id.news_date_text_view)
        TextView newsDateTv;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
