package com.example.administrator.httpdemo.CustomView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.ImageUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static b.focused.w;

/**
 * Created by Administrator on 2017/8/16.
 */

public class MusicView extends View implements Target{

    Paint paint = new Paint();
    int radius = 400;
    int strokeWidth = 140;
    Shader shader;
    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.music);

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public MusicView(Context context) {
        this(context, null);
    }

    public MusicView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = Math.max(radius*2+getPaddingStart()+getPaddingEnd(), getSuggestedMinimumWidth());
        if (heightMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(width, width);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth()/2;
        int centerY = getHeight()/2;

        float w = (float) (radius/Math.sqrt(2));
        float left = centerX - w;
        float top = centerY - w;
        bitmap = ImageUtils.zoomBitmap(bitmap, (int)(w*2), (int)(w*2));

        paint.reset();
        paint.setAntiAlias(true);
        canvas.drawBitmap(bitmap, left, top, paint);

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        shader = new LinearGradient(0, 0, getMeasuredWidth()/4, getMeasuredHeight()/4, Color.parseColor("#757171"), Color.BLACK, Shader.TileMode.MIRROR);
        paint.setShader(shader);
        canvas.drawCircle(centerX, centerY, radius-strokeWidth/2, paint);

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth/20/4*3);
        paint.setColor(Color.BLACK);
        for (int i = 0; i <= strokeWidth; i+=(strokeWidth/20)){
            canvas.drawCircle(centerX, centerY, radius-i, paint);
        }

    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        this.bitmap = bitmap;
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}

