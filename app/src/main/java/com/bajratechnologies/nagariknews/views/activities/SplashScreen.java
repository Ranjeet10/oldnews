package com.bajratechnologies.nagariknews.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bajratechnologies.nagariknews.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ronem on 5/20/16.
 */
public class SplashScreen extends AppCompatActivity implements Animation.AnimationListener {

    @Bind(R.id.logo_image)
    ImageView logoImage;

    private Thread splashTimer;
    Animation fromCenterToBottomtAnim;
    Animation logoAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        ButterKnife.bind(this);

        splashTimer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent dashboardIntent = new Intent(SplashScreen.this, Dashboard.class);
                    dashboardIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dashboardIntent);
                }
            }
        });

        animatedSplash();

    }


    private void animatedSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadAnimations();
            }
        }, 200);
    }

    private void loadAnimations() {
        logoImage.setVisibility(View.VISIBLE);

        logoAnimation = AnimationUtils.loadAnimation(this, R.anim.center_zoom_in);

        logoAnimation.setAnimationListener(this);
        logoImage.startAnimation(logoAnimation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        splashTimer.start();


    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
