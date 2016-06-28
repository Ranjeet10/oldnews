package com.bajratechnologies.nagariknews.Utils;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

import com.bajratechnologies.nagariknews.R;

/**
 * Created by ronem on 2/26/16.
 */
public class ToggleRefresh {
    public static void showRefreshDialog(Context context, final SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeColors(context.getResources().getColor(R.color.colorAccent),
                context.getResources().getColor(R.color.colorPrimary),
                context.getResources().getColor(R.color.colorPrimaryDark),
                context.getResources().getColor(R.color.grid_4));

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    public static void hideRefreshDialog(final SwipeRefreshLayout swipeRefreshLayout) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 100);
    }

}
