package com.bajratechnologies.nagariknews.Utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by ronem on 2/14/16.
 */
public class MyAnimation {


    private ValueAnimator slideAnimator(int start, int end, final View view) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                // Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view
                        .getLayoutParams();
                layoutParams.width = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public void expand(View v) {
        v.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        v.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(0, v.getMeasuredWidth(), v);
        mAnimator.start();
    }


    public void collapse(final View v) {
        int finalWwidth = v.getWidth();

        ValueAnimator mAnimator = slideAnimator(finalWwidth, 0, v);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                // Height=0, but it set visibility to GONE
                v.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

        });
        mAnimator.start();
    }


    public void fadeIn(Context context, View view) {
        Animation fadeInAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        view.setVisibility(View.VISIBLE);
        view.startAnimation(fadeInAnimation);
    }

    public void fadeOut(Context context, View view) {
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        view.setVisibility(View.GONE);
        view.startAnimation(fadeOutAnimation);
    }
}
