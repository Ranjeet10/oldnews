package com.bidhee.nagariknews.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.model.BreakingAndLatestNews;
import com.bidhee.nagariknews.model.BreakingAndLatestNewsListModel;
import com.bidhee.nagariknews.model.NewsObj;
import com.bidhee.nagariknews.views.activities.NewsDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/19/16.
 */
public class BreakingAndLatestnewsParentAdapter extends RecyclerView.Adapter<BreakingAndLatestnewsParentAdapter.ParentViewHolder> implements RecyclerItemClickListener.OnItemClickListener {
    ArrayList<BreakingAndLatestNews> dataModels;
    ArrayList<NewsObj> listModels;
    Context context;
    BreakingAndLatestNewsChildAdapter childAdapter;
    LayoutInflater inflater;


    public BreakingAndLatestnewsParentAdapter(ArrayList<BreakingAndLatestNews> dataModels) {
        this.dataModels = dataModels;
    }

    @Override
    public ParentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        inflater = LayoutInflater.from(context);
        View itemVIew = inflater.inflate(R.layout.single_row_parent_breaking_and_latest_news, parent, false);
        return new ParentViewHolder(itemVIew);
    }

    @Override
    public void onBindViewHolder(ParentViewHolder holder, int position) {

        holder.title.setText(dataModels.get(position).getTopic());


//        holder.childRecyclerView.setLayoutManager(new CustomLinearLayoutManager(context));
//        holder.childRecyclerView.setHasFixedSize(true);
//        holder.childRecyclerView.setItemAnimator(new DefaultItemAnimator());

        listModels = dataModels.get(position).getBreakingAndLatestNewsListModels();
//        childAdapter = new BreakingAndLatestNewsChildAdapter(listModels);
//        holder.childRecyclerView.setAdapter(childAdapter);
//        holder.childRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, position, this));

//        View.OnClickListener myListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String tag = (String) v.getTag();
//                String parent = tag.substring(0, tag.lastIndexOf("#"));
//                String child = tag.substring(parent.length() + 1);
//
//                Toast.makeText(context, listModels.get(Integer.parseInt(tag)).getTitle(), Toast.LENGTH_SHORT).show();
//            }
//        };

        /**
        for (int i = 0; i < listModels.size(); i++) {
//            NewsObj no = listModels.get(i);
            View v = inflater.inflate(R.layout.news_content_layout, holder.containerLayout, false);
//
            v.setTag(position + "#" + i);
            holder.containerLayout.addView(v);
            v.setOnClickListener(myListener);

//            TextView titleTextView = (TextView) v.findViewById(R.id.news_title_text_view);
//            titleTextView.setText("");

//            ImageView thumbnail = (ImageView) v.findViewById(R.id.news_title_thumbnail);
//            Picasso.with(context)
//                    .load(listModels.get(position).getImg())
//                    .placeholder(R.drawable.nagariknews)
//                    .into(thumbnail);
//
//            TextView descriptionTextView = (TextView) v.findViewById(R.id.news_semi_detail_text_view);
//            descriptionTextView.setText(no.getDesc());
//
//            TextView newsSourceTextView = (TextView) v.findViewById(R.id.news_source_text_view);
//            newsSourceTextView.setText(no.getReportedBy());
//
//            TextView newsDateTextView = (TextView) v.findViewById(R.id.news_date_text_view);
//            newsDateTextView.setText(no.getDate());


        }
**/

    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }


    public static class ParentViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.container_layout)
        LinearLayout containerLayout;
        @Bind(R.id.parent_topic_text_view)
        TextView title;
        @Bind(R.id.child_recycler_view_breaking_n_latest_news)
        RecyclerView childRecyclerView;

        public ParentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }


    }

    @Override
    public void onItemClick(View view, int parentPosition, int childPosition) {
        Intent newsDetailIntent = new Intent(context, NewsDetailActivity.class);

        String newsCategory = dataModels.get(parentPosition).getTopic();
        NewsObj newsObj = dataModels.get(parentPosition).getBreakingAndLatestNewsListModels().get(childPosition);

        newsObj.setNewsCategoryName(newsCategory);

//        newsDetailIntent.putExtra(NewsDetailActivity.NEWS_TITLE_EXTRA_STRING, newsObj);
        context.startActivity(newsDetailIntent);

        Toast.makeText(context, dataModels.get(parentPosition).getBreakingAndLatestNewsListModels().get(childPosition).getTitle(), Toast.LENGTH_SHORT).show();
    }
}
