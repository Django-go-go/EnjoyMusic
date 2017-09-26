package com.example.administrator.httpdemo.CustomView;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.ScreenUtils;

/**
 * Created by Administrator on 2017/7/22.
 */

public class CustomDialog{
    private OnCancelListener onCancelListener;
    private OnBackPressListener onBackPressListener;

    private final int[] margin = new int[4];

    private ViewGroup decorView;
    private ViewGroup rootView;
    private ViewGroup frameView;
    private ViewGroup contentView;
    private Context context;
    private Activity activity;

    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.BOTTOM
    );

    private int gravity = Gravity.BOTTOM;

    private int defaultContentHeight;
    private int defaultContentWidth;

    public CustomDialog(Context context) {
        this.context = context;
        this.activity = (Activity)context;
        decorView = (ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content);
//        System.out.println("decorView" + decorView);
        rootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.base_dialog, decorView, false);
        frameView = (ViewGroup) rootView.findViewById(R.id.dialog_root);

        cancel();
    }

    public CustomDialog build(){
        rootView.setLayoutParams(getOutmostLayoutParams());
        frameView.setLayoutParams(getFrameParams());
        frameView.addView(contentView);
        return this;
    }

    private void onBackPressed(CustomDialog dialog){
        if(onCancelListener != null){
            onCancelListener.onCancel(dialog);
        }
        dismiss();
    }

    private void onKey(){
        frameView.setFocusableInTouchMode(true);
        frameView.setFocusable(true);
        frameView.requestFocus();
        frameView.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (event.getAction()) {
                    case KeyEvent.ACTION_UP:
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            if (onBackPressListener != null) {
                                onBackPressListener.onBackPressed(CustomDialog.this);
                            }
                            onBackPressed(CustomDialog.this);

                            return true;
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
//                switch (event.getKeyCode()) {
//                    case KeyEvent.KEYCODE_BACK:
//                        if (onBackPressListener != null) {
//                            onBackPressListener.onBackPressed(CustomDialog.this);
//                        }
//                        onBackPressed(CustomDialog.this);
//                        System.out.println("============>  KEYCODE_BACK");
//                        return true;
//                    case KeyEvent.ACTION_UP:
//                        if (keyCode == KeyEvent.KEYCODE_BACK) {
//                            if (onBackPressListener != null) {
//                                onBackPressListener.onBackPressed(CustomDialog.this);
//                            }
//                            onBackPressed(CustomDialog.this);
//                            return true;
//                        }
//                    default:
//                        System.out.println("==========================> default");
//                        break;
//                }
//
//                return false;
//            }
        });
    }

    public void show(){
        if(isShowing()){
            return;
        }
        decorView.addView(rootView);
        onKey();
    }

    public void dismiss(){
        if(isShowing()){
            decorView.removeView(rootView);
        }
        if (onBackPressListener != null){
            onBackPressListener.onBackPressed(this);
        }
    }

    public boolean isShowing() {
        View view = decorView.findViewById(R.id.dialog_outmost);
        return view != null;
    }

    public ViewGroup getContentView() {
        return contentView;
    }

    public CustomDialog setContentView(ViewGroup contentView) {
        this.contentView = contentView;
        return this;
    }

    public CustomDialog setContentView(int contentViewResId) {
        contentView = (ViewGroup) LayoutInflater.from(context).inflate(contentViewResId, frameView, false);
        return this;
    }


    public int getGravity() {
        return gravity;
    }

    public CustomDialog setGravity(int gravity) {
        this.gravity = gravity;
        params.gravity = gravity;
        return this;
    }

    public int getDefaultContentHeight() {
//        Display display = activity.getWindowManager().getDefaultDisplay();
//        int displayHeight = display.getHeight() - getStatusBarHeight(activity);
        int displayHeight = ScreenUtils.getScreenHeight(context) - ScreenUtils.getStatusBarHeight(activity);
        if (defaultContentHeight == 0) {
            defaultContentHeight = (displayHeight * 3) / 5;
        }
        return defaultContentHeight;
    }

    public CustomDialog setDefaultContentHeight(int defaultContentHeight) {
        params.height = defaultContentHeight;
        this.defaultContentHeight = defaultContentHeight;
        return this;
    }

    public CustomDialog setDefaultContentWidth(int defaultContentWidth){
        params.width = defaultContentWidth;
        this.defaultContentWidth = defaultContentWidth;
        return this;
    }

    public int getDefaultContentWidth(){
//        Display display = activity.getWindowManager().getDefaultDisplay();
        int displayWidth = ScreenUtils.getScreenWidth(context);
        if (defaultContentWidth == 0) {
            defaultContentWidth = (displayWidth * 4) / 5;
        }
        return defaultContentWidth;
    }

    public FrameLayout.LayoutParams getFrameParams() {
        if (params.gravity == Gravity.CENTER){
            params.width = getDefaultContentWidth();
        }
//        params.height = getDefaultContentHeight();
        params.setMargins(margin[0], margin[1], margin[2], margin[3]);
        return params;
    }

    public CustomDialog setContentMargin(int left, int top, int right, int bottom){
        margin[0] = left;
        margin[1] = top;
        margin[2] = right;
        margin[3] = bottom;
        return this;
    }

    public FrameLayout.LayoutParams getOutmostLayoutParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        return params;
    }

    private void cancel(){
        View view = rootView.findViewById(R.id.dialog_outmost);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (onCancelListener != null) {
                        onCancelListener.onCancel(CustomDialog.this);
                    }
                    dismiss();
                }
                return true;
            }
        });
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }

    public void setOnBackPressListener(OnBackPressListener onBackPressListener) {
        this.onBackPressListener = onBackPressListener;
    }

    public interface OnBackPressListener {
        void onBackPressed(CustomDialog dialog);
    }
    public interface OnCancelListener {
        void onCancel(CustomDialog dialog);
    }

    public interface OnDismissListener {
        void onDismiss(CustomDialog dialog);
    }

}
