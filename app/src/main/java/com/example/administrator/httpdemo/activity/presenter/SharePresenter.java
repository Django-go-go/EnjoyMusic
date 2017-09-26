package com.example.administrator.httpdemo.activity.presenter;

import android.content.Context;

import com.example.administrator.httpdemo.Data.entity.Banner;
import com.example.administrator.httpdemo.Data.remote.BaseNetData;
import com.example.administrator.httpdemo.Listener.HttpListener;
import com.example.administrator.httpdemo.activity.base.BasePresenter;
import com.example.administrator.httpdemo.activity.view.ShareView;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/21.
 */

public class SharePresenter extends BasePresenter<ShareView> {
    private ShareView mShareView;
    private Context mContext;

    public SharePresenter(ShareView shareView, Context context) {
        mShareView = shareView;
        mContext = context;
    }

    public void getBanner(){
        new BaseNetData(mContext).getBanner(new HttpListener<List<Banner>>() {
            @Override
            public void onResponse(Call<List<Banner>> call, Response<List<Banner>> response) {
                List<String> urls = new ArrayList<>();
                List<Banner> banners = response.body();
                for (Banner banner : banners){
                    urls.add(banner.getUrl());
                }
                mShareView.getBanner(urls);
            }

            @Override
            public void onFailure(Call<List<Banner>> call, Throwable t) {

            }
        });
    }
}
