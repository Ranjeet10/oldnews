package com.bidhee.nagariknews.views.customviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.RelativeLayout;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.controller.interfaces.ListPositionListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ronem on 5/11/16.
 */
public class LisDialog extends Dialog {
    @Bind(R.id.saveLayout)
    RelativeLayout saveLayout;
    @Bind(R.id.shareLayout)
    RelativeLayout shareLayout;
    ListPositionListener listPositionListener;

    public void setListPositionListener(ListPositionListener listPositionListener) {
        this.listPositionListener = listPositionListener;
    }

    public LisDialog(Context context) {
        super(context, R.style.slideDownAnimation);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_list);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.saveLayout)
    public void onSaveLayoutClicked() {
        listPositionListener.tappedPosition(1);
        dismiss();
    }

    @OnClick(R.id.shareLayout)
    public void onShareLayoutClicked() {
        listPositionListener.tappedPosition(2);
        dismiss();

    }

}
