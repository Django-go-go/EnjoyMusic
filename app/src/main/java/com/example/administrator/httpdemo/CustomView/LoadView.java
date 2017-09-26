package com.example.administrator.httpdemo.CustomView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.example.administrator.httpdemo.Utils.DensityUtils;

/**
 * Created by Administrator on 2017/8/10.
 */
public class LoadView extends View {
    Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint pathPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Rect bounds = new Rect();

    int viewHeight1 = 50;
    int viewHeight2 = 50;
    int viewHeight3 = 50;
    int viewHeight4 = 50;

    int textSize = 50;
    int dis = 6;
    int rectWidth = 5;
    String text = "努力加载中....";

    ObjectAnimator[] animators = {
            ObjectAnimator.ofInt(this, "viewHeight1", 65, 30),
            ObjectAnimator.ofInt(this, "viewHeight2", 30, 60),
            ObjectAnimator.ofInt(this, "viewHeight3", 65, 20),
            ObjectAnimator.ofInt(this, "viewHeight4", 40, 60)
    };

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setViewHeight1(int viewHeight1) {
        this.viewHeight1 = viewHeight1;
        invalidate();
    }

    public void setViewHeight2(int viewHeight2) {
        this.viewHeight2 = viewHeight2;
        invalidate();
    }

    public void setViewHeight3(int viewHeight3) {
        this.viewHeight3 = viewHeight3;
        invalidate();
    }

    public void setViewHeight4(int viewHeight4) {
        this.viewHeight4 = viewHeight4;
        invalidate();
    }

    public LoadView(Context context) {
        this(context, null);
    }

    public LoadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        for (ObjectAnimator animator : animators){
            animator.setDuration(1000);
            animator.setInterpolator(new BounceInterpolator());
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setRepeatMode(ValueAnimator.REVERSE);
        }

    }

//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        for (ObjectAnimator animator : animators){
//            animator.start();
//        }
//    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (ObjectAnimator animator : animators){
            animator.end();
        }
    }

    public void start(){
        for (ObjectAnimator animator : animators){
            animator.start();
        }
    }

    public void stop(){
        for (ObjectAnimator animator : animators){
            animator.end();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int min = getSuggestedMinimumHeight();
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

//        if (mode == MeasureSpec.AT_MOST){
//            System.out.println("=======> size " + size);
//            System.out.println("=======> min " + min);
        int height = Math.min(min, size);
        setMeasuredDimension(width, height);
//        }else {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth()/2;
        int centerY = getHeight()/2;
        textPaint.setTextSize(textSize);
        textPaint.getTextBounds(text, 0, text.indexOf("加"), bounds);

        int h = bounds.bottom - bounds.top;
        int w = bounds.right - bounds.left;

        canvas.drawText(text, centerX-w, centerY+h/2, textPaint);

        bounds.left += centerX-w;
        bounds.right += bounds.left;
        bounds.top += centerY+h/2;
        bounds.bottom = bounds.top + h;
        int l = bounds.left - 25;
        int b = bounds.bottom;
        pathPaint.setColor(Color.RED);

        int j = 0;

        for (int i = 0; i < 4; i++){
            if (i == 0){
                canvas.drawRect(l-rectWidth-j, b-viewHeight1, l-j, b, pathPaint);
            }else if (i == 1){
                canvas.drawRect(l-rectWidth-j, b-viewHeight2, l-j, b, pathPaint);
            }else if (i == 2){
                canvas.drawRect(l-rectWidth-j, b-viewHeight3, l-j, b, pathPaint);
            }else {
                canvas.drawRect(l-rectWidth-j, b-viewHeight4, l-j, b, pathPaint);
            }
            j = j + dis + rectWidth;
        }
    }
}
