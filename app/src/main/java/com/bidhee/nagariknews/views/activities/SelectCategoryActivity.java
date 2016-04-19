package com.bidhee.nagariknews.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.bidhee.nagariknews.R;

import butterknife.Bind;

/**
 * Created by ronem on 4/19/16.
 */
public class SelectCategoryActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_category_layout);
        setSupportActionBar(toolbar);
    }
}
