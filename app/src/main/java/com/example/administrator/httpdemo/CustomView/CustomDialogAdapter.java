package com.example.administrator.httpdemo.CustomView;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.httpdemo.Listener.ListDialogListener;
import com.example.administrator.httpdemo.Utils.DensityUtils;

/**
 * Created by Administrator on 2017/7/25.
 */

public class CustomDialogAdapter {
    private static ListView dialogForListView(Context context){
        ListView listView = new ListView(context);
        listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        return listView;
    }

    private static LinearLayout dialogForLinearLayout(Context context){
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

//        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT,
//                ViewGroup.MarginLayoutParams.WRAP_CONTENT));

//        ViewGroup.MarginLayoutParams tv_margin = getLayoutParams(textView);
//        int margin = DensityUtils.dp2px(context, 10);
//        System.out.println("==========> tv_margin1 " + tv_margin.topMargin);
//        tv_margin.setMargins(margin, margin, margin, 0);
//        System.out.println("==========> tv_margin1 " + tv_margin.topMargin);
//        textView.setLayoutParams(tv_margin);
//        System.out.println("==========> setLayoutParams " + textView.getLayoutParams());
        return linearLayout;
    }

    public static CustomDialog createDialog(Context context, String title, ListDialogListener listener){
        LinearLayout layout = CustomDialogAdapter.dialogForLinearLayout(context);
        ListView listView = CustomDialogAdapter.dialogForListView(context);
        if (title != null){
            TextView textView = new TextView(context);
            textView.setText(title);
            textView.setClickable(true);
            textView.setFocusable(true);
            textView.setTextColor(Color.GRAY);
            textView.setTextSize(DensityUtils.sp2px(context, 6));
            int padding = DensityUtils.dp2px(context, 10);
            textView.setPadding(padding, padding, padding, padding);
            layout.addView(textView);
        }

        listener.createListDialog(listView);

        layout.addView(listView);
        return new CustomDialog(context)
                .setContentView(layout)
                .setGravity(Gravity.BOTTOM);
    }

}
