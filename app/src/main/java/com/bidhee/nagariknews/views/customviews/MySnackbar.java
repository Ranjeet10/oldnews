package com.bidhee.nagariknews.views.customviews;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.views.activities.Dashboard;

/**
 * Created by ronem on 5/10/16.
 */
public class MySnackbar {
    public static Snackbar showSnackBar(Context context, View view, String title) {
        Snackbar snackbar = Snackbar.make(view, title, Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

        int bgcolor;
        switch (Dashboard.sessionManager.getSwitchedNewsValue()) {
            case 1:
                bgcolor = context.getResources().getColor(R.color.republicaColorPrimary);
                break;
            case 2:
                bgcolor = context.getResources().getColor(R.color.nagarikColorPrimary);
                break;
            case 3:
                bgcolor = context.getResources().getColor(R.color.sukrabarColorPrimary);
                break;
            default:
                bgcolor = context.getResources().getColor(R.color.colorPrimary);
                break;
        }

        snackbarLayout.setBackgroundColor(bgcolor);

        return snackbar;
    }
}
