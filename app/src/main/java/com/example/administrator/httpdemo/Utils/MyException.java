package com.example.administrator.httpdemo.Utils;

import android.content.Context;

/**
 * Created by Administrator on 2017/7/23.
 */

public class MyException {
    public static void solve(String msg, Exception e, Context context){
        if(e == null){
            System.out.println(msg + ":  =================> Success!");
        }else {
            System.out.println(msg + ":  =================> " + e.toString());
//            ToastUtils.showShort(context, msg + "failed");
        }
    }
}
