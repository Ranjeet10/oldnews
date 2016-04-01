package com.bidhee.nagariknews.views.customviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.controller.interfaces.FontSizeListener;

/**
 * Created by ronem on 6/14/15.
 */
public class FontDialog {
    FontSizeListener fontSizeListener;

    public void showFontDialog(final Context context) {

        final Dialog dialog = new Dialog(context, R.style.SlideAnimation);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_option_menu);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER | Gravity.RIGHT;

        ImageView mLarger = (ImageView) dialog.findViewById(R.id.menu_larger_font);
        ImageView mLarge = (ImageView) dialog.findViewById(R.id.menu_large_font);
        ImageView mNormal = (ImageView) dialog.findViewById(R.id.menu_normal_font);
        ImageView mSmall = (ImageView) dialog.findViewById(R.id.menu_small_font);

        View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.menu_larger_font:
                        fontSizeListener.onFontChanged(25);
                        break;
                    case R.id.menu_large_font:
                        fontSizeListener.onFontChanged(20);
                        break;
                    case R.id.menu_normal_font:
                        fontSizeListener.onFontChanged(15);
                        break;
                    case R.id.menu_small_font:
                        fontSizeListener.onFontChanged(10);
                        break;
                }
                dialog.dismiss();
            }
        };
        mLarger.setOnClickListener(myListener);
        mLarge.setOnClickListener(myListener);
        mNormal.setOnClickListener(myListener);
        mSmall.setOnClickListener(myListener);
        dialog.show();

    }

    public void setOnFontSizeListener(FontSizeListener fontSizeListener) {
        this.fontSizeListener = fontSizeListener;
    }

}
