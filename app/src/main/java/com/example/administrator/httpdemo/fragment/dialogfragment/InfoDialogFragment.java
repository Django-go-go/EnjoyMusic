package com.example.administrator.httpdemo.fragment.dialogfragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.administrator.httpdemo.Listener.InfoActivityListener;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.DensityUtils;
import com.example.administrator.httpdemo.Utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/4.
 */

public class InfoDialogFragment extends DialogFragment {

    private InfoActivityListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InfoActivityListener){
            mListener = (InfoActivityListener) context;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getString("nickname") != null) {
                return setNickname();
            }
            if (bundle.getString("sex") != null) {
                return chooseSex();
            }
        }

        throw new IllegalArgumentException("null");
    }

    private Dialog setNickname(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("设置性别");
        FrameLayout frameLayout = new FrameLayout(getContext());
        final EditText editText = new EditText(getContext());
        editText.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        frameLayout.addView(editText);
        frameLayout.setPadding(DensityUtils.dp2px(getContext(), 20),
                DensityUtils.dp2px(getContext(), 12),
                DensityUtils.dp2px(getContext(), 20),
                DensityUtils.dp2px(getContext(), 10));
        builder.setView(frameLayout);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(editText.getText().toString())){
                    if (mListener != null){
                        mListener.setNickname(editText.getText().toString());
                    }
                }else {
                    ToastUtils.showShort(getContext(), "不能为空!");
                }
            }
        });
        return builder.create();
    }

    private Dialog chooseSex(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        ListView listView = new ListView(getContext());
        List<String> list = new ArrayList<>();
        list.add("男");
        list.add("女");
        listView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListener != null){
                    mListener.chooseSex(position);
                }
            }
        });
        return builder.setView(listView).create();
    }
}
