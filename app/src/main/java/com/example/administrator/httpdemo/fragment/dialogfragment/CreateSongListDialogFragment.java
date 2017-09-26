package com.example.administrator.httpdemo.fragment.dialogfragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.httpdemo.Listener.DialogFragmentListener;
import com.example.administrator.httpdemo.Utils.DensityUtils;
import com.example.administrator.httpdemo.Utils.ToastUtils;

/**
 * Created by Administrator on 2017/8/14.
 */

public class CreateSongListDialogFragment extends DialogFragment {

    private DialogFragmentListener mListener;

    public void setListener(DialogFragmentListener listener) {
        mListener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DialogFragmentListener) {
            mListener = (DialogFragmentListener) context;
        } else {
            System.out.println("================> must implement MyListener ");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int dp = DensityUtils.dp2px(getContext(), 10);
        int sp = DensityUtils.sp2px(getContext(), 7);
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp, dp, dp, dp);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        TextView textView = new TextView(getContext());
        textView.setText("创建歌单");
        textView.setTextSize(sp);
        textView.setPadding(0, 0, 0, dp);
        final EditText editText = new EditText(getContext());
        editText.setHint("请输入歌单标题");
        layout.addView(textView);
        layout.addView(editText);

        builder.setView(layout).setPositiveButton("提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener != null){
                    if (editText.getText() != null){
                        mListener.createSongList(editText.getText().toString());
                    }else {
                        ToastUtils.showShort(getContext(), "歌单名不能为空");
                    }
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }
}
