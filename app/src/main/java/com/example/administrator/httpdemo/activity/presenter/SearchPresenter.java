package com.example.administrator.httpdemo.activity.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.administrator.httpdemo.Data.entity.HotWord;
import com.example.administrator.httpdemo.Data.entity.SearchSong;
import com.example.administrator.httpdemo.Data.entity.Song;
import com.example.administrator.httpdemo.Data.remote.BaseNetData;
import com.example.administrator.httpdemo.Listener.HttpListener;
import com.example.administrator.httpdemo.activity.base.BasePresenter;
import com.example.administrator.httpdemo.activity.view.SearchActivityView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/21.
 */

public class SearchPresenter extends BasePresenter<SearchActivityView> {
    private SearchActivityView mView;
    private Context mContext;

    public SearchPresenter(SearchActivityView view, Context context) {
        mView = view;
        mContext = context;
    }

    public void getSearchSong(String query){
        new BaseNetData(mContext).getSearchSong(query, new HttpListener<List<SearchSong>>() {
            @Override
            public void onResponse(Call<List<SearchSong>> call, Response<List<SearchSong>> response) {
                mView.showListView(response.body());
            }

            @Override
            public void onFailure(Call<List<SearchSong>> call, Throwable t) {

            }
        });

    }

    public void getHotWord(){
        new BaseNetData(mContext).getHotWord(new HttpListener<List<HotWord>>() {
            @Override
            public void onResponse(Call<List<HotWord>> call, Response<List<HotWord>> response) {
                mView.showHotWord(response.body());
            }

            @Override
            public void onFailure(Call<List<HotWord>> call, Throwable t) {

            }
        });
    }
}
