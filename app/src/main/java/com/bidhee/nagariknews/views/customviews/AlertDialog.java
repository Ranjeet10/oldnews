package com.bidhee.nagariknews.views.customviews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.bidhee.nagariknews.R;
import com.bidhee.nagariknews.controller.interfaces.AlertDialogListener;
import com.bidhee.nagariknews.views.activities.Dashboard;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ronem on 5/4/16.
 */
public class AlertDialog extends Dialog {
    AlertDialogListener alertDialogListener;

    @Bind(R.id.alert_title)
    TextView titleView;
    @Bind(R.id.alert_desc)
    TextView descView;
    @Bind(R.id.btn_positive)
    Button btnPositive;
    @Bind(R.id.btn_negative)
    Button btnNegative;

    public AlertDialog(Context context, String title, String message) {
        super(context, R.style.SlideAnimation);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.alert_dialog_layout);
        ButterKnife.bind(this);

        titleView.setText(title);
        descView.setText(message);

        if (Dashboard.sessionManager.getSwitchedNewsValue() == 1) {
            renderAlertButton(R.drawable.alert_button_republica);

        } else if (Dashboard.sessionManager.getSwitchedNewsValue() == 2) {
            renderAlertButton(R.drawable.alert_button_nagarik);

        } else if (Dashboard.sessionManager.getSwitchedNewsValue() == 3) {
            renderAlertButton(R.drawable.alert_button_sukrabar);
        }
    }

    private void renderAlertButton(int bg) {
        btnPositive.setBackgroundResource(bg);
        btnNegative.setBackgroundResource(bg);
    }

    @OnClick(R.id.btn_positive)
    void onPositiveButtonClicked() {
        alertDialogListener.alertPositiveButtonClicked();
    }

    @OnClick(R.id.btn_negative)
    void onNegativeButtonClicked() {
        alertDialogListener.alertNegativeButtonClicked();
    }

    public void setOnAlertDialogListener(AlertDialogListener alertDialogListener) {
        this.alertDialogListener = alertDialogListener;
    }
}
