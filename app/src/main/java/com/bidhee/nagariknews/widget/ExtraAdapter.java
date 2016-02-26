package com.bidhee.nagariknews.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    int[] colors;

    public ExtraAdapter(ArrayList<ExtraModel> list, Context context) {
        this.list = list;
        this.context = context;
        colors = new int[]{context.getResources().getColor(R.color.grid_1),
                context.getResources().getColor(R.color.grid_6),
                context.getResources().getColor(R.color.grid_3),
                context.getResources().getColor(R.color.grid_4),
                context.getResources().getColor(R.color.grid_5),
                context.getResources().getColor(R.color.grid_2)};
    }

    @Override
    public ExtraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.single_row_extra_item, parent, false);
        return new ExtraViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExtraViewHolder holder, int position) {
        holder.extraImageView.setImageResource(list.get(position).getImage());
        holder.titleTextView.setText(list.get(position).getTitle());
        holder.descriptionTextView.setText(list.get(position).getDescription());
        try {
            holder.cardLayout.setBackgroundColor(colors[position]);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ExtraViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.extra_image_view)
        ImageView extraImageView;
        @Bind(R.id.title)
        TextView titleTextView;
        @Bind(R.id.description)
        TextView descriptionTextView;
        @Bind(R.id.card_layout)
        RelativeLayout cardLayout;

        public ExtraViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
