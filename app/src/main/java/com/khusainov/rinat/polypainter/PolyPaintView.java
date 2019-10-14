package com.khusainov.rinat.polypainter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PolyPaintView extends View {

    private Paint mPaint;
    private List<PointF> mPoints = new ArrayList<>();

    public PolyPaintView(Context context) {
        this(context, null);
    }

    public PolyPaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public PolyPaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //ActionMasked под мультитач
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mPoints.clear();
                mPoints.add(new PointF(event.getX(), event.getY()));
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                int pointerId = event.getPointerId(event.getActionIndex());
                if (mPoints.size() == pointerId) {
                    mPoints.add(new PointF(
                            event.getX(event.getActionIndex()),
                            event.getY(event.getActionIndex())));
                } else {
                    PointF point = mPoints.get(pointerId);
                    point.x = event.getX(event.getActionIndex());
                    point.y = event.getY(event.getActionIndex());
                }
                break;

            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int id = event.getPointerId(i);
                    PointF point = mPoints.get(id);
                    point.x = event.getX(i);
                    point.y = event.getY(i);
                }

                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                break;
        }

        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {


        if (mPoints.isEmpty()) {
            return;
        }

        if (mPoints.size() == 1) {
            canvas.drawPoint(mPoints.get(0).x, mPoints.get(0).y, mPaint);
        } else {
            for (int i = 1; i < mPoints.size(); i++) {
                PointF one = mPoints.get(i - 1);
                PointF two = mPoints.get(i);

                canvas.drawLine(one.x, one.y, two.x, two.y, mPaint);
            }
        }
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(getContext().getResources().getDimension(R.dimen.line_width));
    }
}
