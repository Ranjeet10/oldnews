package com.bidhee.nagariknews.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.bidhee.nagariknews.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ronem on 2/4/16.
 */
public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.btn_facebook_login)
    Button btnFacebookLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_login)
    void onLoginClick() {
        startActivity(new Intent(this, Dashboard.class));
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
