package com.example.administrator.httpdemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.administrator.httpdemo.CustomView.CustomDialog;
import com.example.administrator.httpdemo.Other.Constant;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.DensityUtils;
import com.example.administrator.httpdemo.Utils.ScreenUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */

public class ClassifyActivity extends AppCompatActivity {

    private ScrollView mScrollView;
    private int dp20;
    private Typeface typeface;

    //语种 风格 情感 场景 主题
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dp20 = DensityUtils.dp2px(this, 20);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/girl.ttf");

        LinearLayout linearlayout = new LinearLayout(this);
        linearlayout.setOrientation(LinearLayout.VERTICAL);

        mScrollView = new ScrollView(this);
        mScrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mScrollView.setPadding(dp20*2, dp20, dp20*2, dp20);
        mScrollView.addView(createLayout());
        mScrollView.setBackgroundResource(R.drawable.classify_bg);
        mScrollView.setVerticalScrollBarEnabled(false);

        linearlayout.addView(initToolbar());
        linearlayout.addView(mScrollView);

        setContentView(linearlayout);
    }

    private Toolbar initToolbar(){
        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, null);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextView textView = (TextView) toolbar.findViewById(R.id.toolbar_tv1);
        textView.setText("选择分类");
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.toolbar_iv);
        imageView.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return toolbar;
    }

    private GridLayout createLayout(){
        GridLayout gridlayout = new GridLayout(this);
        gridlayout.setOrientation(GridLayout.VERTICAL);
        gridlayout.addView(createGridLayout("语种", Constant.getTagForLanguage()));
        gridlayout.addView(createGridLayout("风格", Constant.getTagForStyle()));
        gridlayout.addView(createGridLayout("情感", Constant.getTagForMood()));
        gridlayout.addView(createGridLayout("场景", Constant.getTagForPlace()));
        gridlayout.addView(createGridLayout("主题", Constant.getTagForTheme()));
        gridlayout.setBackgroundResource(R.drawable.tag_tv);
        return gridlayout;
    }

    private GridLayout createGridLayout(String tag, final List<String> tags){
        int size = ScreenUtils.getScreenWidth(this) - DensityUtils.dp2px(this, 20)*4;
        int total = tags.size();
        int row = total/4 + 1;
        int col = 4;

        GridLayout gridlayout = new GridLayout(this);
        gridlayout.setColumnCount(col);
        gridlayout.setRowCount(row);

        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                final int k = j + col*i;
                if (k <= total - 1){
                    TextView view = new TextView(this);
                    view.setText(tags.get(k));
                    view.setTypeface(typeface);
                    view.setGravity(Gravity.CENTER);
                    view.setBackgroundResource(R.drawable.tag_tv);
                    view.setClickable(true);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.putExtra("tag", tags.get(k));
                            setResult(2, intent);
                            ClassifyActivity.this.finish();
                        }
                    });
                    GridLayout.Spec rowSpec = GridLayout.spec(i);
                    GridLayout.Spec columnSpec = GridLayout.spec(j);
                    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
                    layoutParams.height = size/8;
                    layoutParams.width = size/4;
                    gridlayout.addView(view, layoutParams);
                }
            }
        }

        TextView view = new TextView(this);
        view.setText(tag);
        view.setTextSize(DensityUtils.sp2px(this, 10));
        view.setTypeface(typeface);
        view.setGravity(Gravity.CENTER);
        view.setTextColor(Color.RED);
        view.setBackgroundResource(R.drawable.tag_tv);

        GridLayout layout = new GridLayout(this);
        layout.setOrientation(GridLayout.VERTICAL);
        layout.setPadding(0, dp20, 0, dp20);

        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.height = size/8;
        layoutParams.width = size;
        layout.addView(view, layoutParams);

        layout.addView(gridlayout);

        return layout;
    }
}
