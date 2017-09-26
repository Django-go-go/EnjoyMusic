package com.example.administrator.httpdemo.activity.presenter;

import com.example.administrator.httpdemo.Data.entity.MyUser;
import com.example.administrator.httpdemo.activity.base.BasePresenter;
import com.example.administrator.httpdemo.activity.view.MainView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Administrator on 2017/6/17.
 */

public class MainPresenter extends BasePresenter<MainView> {
    private MainView mMainView;

    public MainPresenter(MainView mainView){
        mMainView = mainView;
    }

    public void getMyUser(){
        MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
        BmobQuery<MyUser> query = new BmobQuery<>();
        query.getObject(myUser.getObjectId(), new QueryListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                mMainView.getMyUser(myUser);
            }
        });
    }
}
