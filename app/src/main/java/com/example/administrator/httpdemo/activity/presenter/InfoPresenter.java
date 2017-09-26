package com.example.administrator.httpdemo.activity.presenter;

import android.content.Context;

import com.example.administrator.httpdemo.Data.entity.MyUser;
import com.example.administrator.httpdemo.activity.base.BasePresenter;
import com.example.administrator.httpdemo.activity.view.InfoView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Administrator on 2017/9/4.
 */

public class InfoPresenter extends BasePresenter<InfoView> {

    private InfoView mInfoView;
    private Context mContext;

    public InfoPresenter(InfoView infoView, Context context) {
        mInfoView = infoView;
        mContext = context;
    }

    public void getMyUser(){
        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        BmobQuery<MyUser> query = new BmobQuery<>();
        query.getObject(myUser.getObjectId(), new QueryListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                mInfoView.getMyUser(myUser);
            }
        });
    }
}
