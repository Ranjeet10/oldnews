package com.bidhee.nagariknews.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.Utils.StaticStorage;
import com.bidhee.nagariknews.model.ExtraModel;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 2/21/16.
 */
public class ExtraAdapter extends RecyclerView.Adapter<ExtraAdapter.ExtraViewHolder> {
    ArrayList<ExtraModel> list;
    Context context;

    public ExtraAdapter(ArrayList<ExtraModel> list) {
        this.list = list;
    }

    @Override
    public ExtraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.single_row_extra_item, parent, false);
        return new ExtraViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExtraViewHolder holder, int position) {
        holder.titleTextView.setText(list.get(position).getTitle());
//        holder.cardLayout.setBackgroundColor(StaticStorage.colors[position]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ExtraViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.title)
        TextView titleTextView;
        @Bind(R.id.card_layout)
        RelativeLayout cardLayout;

        public ExtraViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
