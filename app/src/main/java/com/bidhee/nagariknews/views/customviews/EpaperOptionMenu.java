package com.bidhee.nagariknews.views.customviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.controller.interfaces.ListPositionListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ronem on 5/27/16.
 */
public class EpaperOptionMenu extends Dialog {
    @Bind(R.id.saveLayout)
    RelativeLayout saveLayout;
    @Bind(R.id.shareLayout)
    RelativeLayout shareLayout;
    @Bind(R.id.epaper_option_menu_layout)
    LinearLayout epaperOptionMenuLayout;
    @Bind(R.id.save_share_layout)
    LinearLayout saveShareLayout;

    ListPositionListener listPositionListener;

    public void setListPositionListener(ListPositionListener listPositionListener) {
        this.listPositionListener = listPositionListener;
    }

    public EpaperOptionMenu(Context context) {
        super(context, R.style.right_top_corner_menu);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_list);
        ButterKnife.bind(this);

        epaperOptionMenuLayout.setVisibility(View.VISIBLE);
        saveShareLayout.setVisibility(View.GONE);
    }

    /**
     * onclick only for epaperotion menu layout
     */
    @OnClick(R.id.menu_purweli)
    public void onMenuPurweliClicked() {
        listPositionListener.tappedPosition(0);
        dismiss();
    }

    @OnClick(R.id.menu_paschimi)
    public void onMenuPaschimiClicked() {
        listPositionListener.tappedPosition(1);
        dismiss();
    }
    @OnClick(R.id.menu_all_epaper_nagarik)
    public void onMenuNagarikClicked() {
        listPositionListener.tappedPosition(2);
        dismiss();
    }

}
