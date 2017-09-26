package com.example.administrator.httpdemo.fragment.dialogfragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.httpdemo.Listener.DeleteSongListener;
import com.example.administrator.httpdemo.Listener.DialogFragmentListener;
import com.example.administrator.httpdemo.Utils.DensityUtils;

/**
 * Created by Administrator on 2017/8/24.
 */

public class DeleteSongFragment extends DialogFragment {
    private DeleteSongListener mListener;

    public DeleteSongFragment() {
    }

    public void setListener(DeleteSongListener listener) {
        mListener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DeleteSongListener) {
            mListener = (DeleteSongListener) context;
        } else {
            System.out.println("================> must implement MyListener ");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int dp = DensityUtils.dp2px(getContext(), 10);
        int sp = DensityUtils.sp2px(getContext(), 6);
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp, dp, dp, dp);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        TextView textView1 = new TextView(getContext());
        textView1.setText("删除歌曲");
        textView1.setTextSize(sp);
        textView1.setTextColor(Color.RED);
        textView1.setPadding(0, 0, 0, dp);
        TextView textView2 = new TextView(getContext());
        textView2.setText("真的要删除此歌曲吗?");
        textView2.setTextSize(sp);
        textView2.setTextColor(Color.GRAY);
        textView2.setPadding(0, 0, 0, dp/5*3);
        layout.addView(textView1);
        layout.addView(textView2);

        builder.setView(layout).setPositiveButton("提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener != null){
                    Bundle bundle = getArguments();
                    mListener.delete(bundle.getInt("pos"));
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
