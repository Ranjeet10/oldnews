package com.bajratechnologies.nagariknews.views.customviews;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.bajratechnologies.nagariknews.BuildConfig;
import com.bajratechnologies.nagariknews.R;
import com.bajratechnologies.nagariknews.Utils.StaticStorage;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ronem on 7/1/16.
 */
public class AboutDialog extends DialogFragment {
    @Bind(R.id.tv_ver)
    TextView textViewVersion;
    @Bind(R.id.tv_about)
    TextView textViewAbout;
    @Bind(R.id.tv_developed_by_tv)
    TextView textViewDevelopedBy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textViewVersion.setText("Version : " + BuildConfig.VERSION_NAME);
        textViewAbout.setText(Html.fromHtml(StaticStorage.aboutString));
        textViewDevelopedBy.setText(Html.fromHtml(StaticStorage.developedByString));
    }

    @OnClick(R.id.btn_close_dialog)
    public void onBtnCloseClicked() {
        dismiss();
    }
}
