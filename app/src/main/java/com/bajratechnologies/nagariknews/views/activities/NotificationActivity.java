package com.bajratechnologies.nagariknews.views.activities;

import android.os.Bundle;

import com.bajratechnologies.nagariknews.R;

import butterknife.ButterKnife;

/**
 * Created by ronem on 5/12/16.
 */
public class NotificationActivity extends BaseThemeActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_layout);
        ButterKnife.bind(this);
    }
}
