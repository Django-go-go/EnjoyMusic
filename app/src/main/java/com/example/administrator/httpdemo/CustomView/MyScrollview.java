package com.example.administrator.httpdemo.CustomView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2017/8/6.
 */

public class MyScrollview extends ScrollView {
    // 是否在触摸状态
    private boolean inTouch = false;

    public MyScrollview(Context context) {
        this(context, null);
    }

    public MyScrollview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private OnScrollListener mOnScrollListener;

    public void setOnScrollListener(OnScrollListener onScrollListener){
        mOnScrollListener = onScrollListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                inTouch = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                inTouch = false;
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (t == oldt){
            System.out.println("========================>");
        }
        if(getChildAt(0).getHeight() == getScrollY() + getHeight()){
                if (mOnScrollListener != null && !inTouch){
                    mOnScrollListener.onScroll(true);
            }
        }else {
            if (mOnScrollListener != null){
                mOnScrollListener.onScroll(false);
            }
        }
    }

    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }


    public interface OnScrollListener{
        void onScroll(boolean isBottom);
    }
}
