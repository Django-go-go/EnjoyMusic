package com.example.administrator.httpdemo.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/8/11.
 */

public class WordView extends View {
    Typeface typeFace =Typeface.createFromAsset(getContext().getAssets(),"fonts/girl.ttf");
    Paint paint = new Paint();
    Paint pathPaint = new Paint();
    Path path = new Path();
    String text = "我是分割线";
    String leftText = "<";
    String rightText = ">";
    PathEffect pathEffect = new DashPathEffect(new float[]{10, 5}, 0);
    Rect bounds1 = new Rect();
    Rect bounds2 = new Rect();
    Rect bounds3 = new Rect();

    public WordView(Context context) {
        this(context, null);
    }

    public WordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth()/2;
        int centerY = getHeight()/2;

        paint.reset();
        paint.getTextBounds(text, 0, text.length(), bounds1);
        paint.getTextBounds(leftText, 0, leftText.length(), bounds2);
        paint.getTextBounds(rightText, 0, rightText.length(), bounds3);

        paint.setAntiAlias(true);
        paint.setTextSize(50);
        paint.setTextSkewX(-0.33f);
        paint.setTypeface(typeFace);
        paint.setColor(Color.RED);
        paint.setLetterSpacing(0.1f);


        float h1 = bounds1.bottom - bounds1.top;
        float h2 = bounds1.bottom - bounds1.top;
        float h3 = bounds1.bottom - bounds1.top;
        float w1 = paint.measureText(text);
        float w2 = paint.measureText(leftText);
        float w3 = paint.measureText(rightText);

        float startX = centerX - w1/2;
        float startY = centerY + h1/2;
        canvas.drawText(text, startX, startY, paint);

        float sX2 = getLeft();
        float sY2 = centerY + h2/2;
        canvas.drawText(leftText, sX2, sY2, paint);

        float sX3 = getRight() - w3 - getPaddingEnd() - w3;
        float sY3 = centerY + h3/2;
        canvas.drawText(rightText, sX3, sY3, paint);

        pathPaint.reset();
        pathPaint.setStrokeWidth(5);
        pathPaint.setAntiAlias(true);
        pathPaint.setPathEffect(pathEffect);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setColor(Color.RED);

        path.reset();
        path.moveTo(getLeft()+w2, sY3-h3);
        path.lineTo(startX, sY3-h3);
        canvas.drawPath(path, pathPaint);
//        canvas.drawLine(getLeft()+w2, sY3-h3/3, startX, sY3-h3/3, paint);
        path.reset();
        path.moveTo(startX+w1, sY3-h3);
        path.lineTo(sX3, sY3-h3);
//        canvas.drawLine(startX+w1, sY3-h3/3, sX3, sY3-h3/3, paint);
        canvas.drawPath(path, pathPaint);
    }
}

