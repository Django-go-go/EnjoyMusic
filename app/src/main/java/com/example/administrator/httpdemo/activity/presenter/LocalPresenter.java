package com.example.administrator.httpdemo.activity.presenter;

import android.content.Context;
import android.util.Log;

import com.example.administrator.httpdemo.Data.Local.ContentHelper;
import com.example.administrator.httpdemo.Data.entity.SongRecorder;
import com.example.administrator.httpdemo.activity.base.BasePresenter;
import com.example.administrator.httpdemo.activity.view.LocalView;
import com.example.administrator.httpdemo.Utils.GreenDaoUtils;

import java.util.List;

import static com.example.administrator.httpdemo.MusicApp.TAG;

/**
 * Created by Administrator on 2017/6/17.
 */

public class LocalPresenter extends BasePresenter<LocalView> {

    private LocalView mLocalView;
    private Context mContext;

    public LocalPresenter(LocalView localView, Context context) {
        mLocalView = localView;
        mContext = context;
    }

    public void getLocalData(){
        ContentHelper mContentHelper = new ContentHelper(mContext);
        mLocalView.showLocalData(mContentHelper.getMusic());
    }

    public void getRecorder(){
        List<SongRecorder> recorders = GreenDaoUtils.querySongRecorderDesc(mContext);
        mLocalView.showRecorderData(recorders);
    }
}
