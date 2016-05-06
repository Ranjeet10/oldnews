package com.bidhee.nagariknews.views.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ronem on 5/5/16.
 */
public class CustomShape extends View {
    Paint paint;
    PointF point;

    public CustomShape(Context context) {
        super(context);
    }

    public CustomShape(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (point != null) {
            int radius = 50;
            paint.setColor(Color.RED);
            canvas.drawCircle(point.x, point.y, radius, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                point = new PointF(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                point = new PointF(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                point = new PointF(event.getX(), event.getY());
                break;
        }
        invalidate();
        return true;
    }
}
