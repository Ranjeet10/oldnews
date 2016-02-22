package com.bidhee.nagariknews.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.model.BreakingAndLatestNews;
import com.bidhee.nagariknews.model.BreakingAndLatestNewsListModel;
import com.bidhee.nagariknews.model.NewsObj;
import com.bidhee.nagariknews.views.activities.NewsDetailActivity;

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


    public BreakingAndLatestnewsParentAdapter(ArrayList<BreakingAndLatestNews> dataModels) {
        this.dataModels = dataModels;
    }

    @Override
    public ParentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemVIew = LayoutInflater.from(context).inflate(R.layout.single_row_parent_breaking_and_latest_news, parent, false);
        return new ParentViewHolder(itemVIew);
    }

    @Override
    public void onBindViewHolder(ParentViewHolder holder, int position) {

        holder.title.setText(dataModels.get(position).getTopic());


        holder.childRecyclerView.setLayoutManager(new CustomLinearLayoutManager(context));
        holder.childRecyclerView.setHasFixedSize(true);
        holder.childRecyclerView.setItemAnimator(new DefaultItemAnimator());

        listModels = dataModels.get(position).getBreakingAndLatestNewsListModels();
        childAdapter = new BreakingAndLatestNewsChildAdapter(listModels);
        holder.childRecyclerView.setAdapter(childAdapter);
        holder.childRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, position, this));

    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }


    public static class ParentViewHolder extends RecyclerView.ViewHolder {
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

        newsObj.setNewsCategory(newsCategory);

        newsDetailIntent.putExtra(NewsDetailActivity.NEWS_TITLE_EXTRA_STRING, newsObj);
        context.startActivity(newsDetailIntent);

        Toast.makeText(context, dataModels.get(parentPosition).getBreakingAndLatestNewsListModels().get(childPosition).getTitle(), Toast.LENGTH_SHORT).show();
    }
}
