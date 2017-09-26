package com.example.administrator.httpdemo.fragment.presenter;

import android.content.Context;
import android.util.Log;

import com.example.administrator.httpdemo.Data.entity.SongList;
import com.example.administrator.httpdemo.Data.entity.TopList;
import com.example.administrator.httpdemo.Data.remote.BaseNetData;
import com.example.administrator.httpdemo.Listener.HttpListener;
import com.example.administrator.httpdemo.activity.SongListActivity;
import com.example.administrator.httpdemo.fragment.view.TopListFraView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/3.
 */

public class TopListFraPresenter {
    private TopListFraView mView;
    private Context mContext;

//    private List<SongList> mList_official;
//    private List<SongList> mList_allWord;
//    private Map<Integer, List<SongList>> all;

    public TopListFraPresenter(TopListFraView topListFraView, Context context) {
        mView = topListFraView;
        mContext = context;
//        mList_allWord = new ArrayList<>(15);
//        mList_official = new ArrayList<>();
//        all = new HashMap<>();
    }

    public void getData(){
        BaseNetData netData = new BaseNetData(mContext);
        netData.getTopList(new HttpListener<List<TopList>>() {
            @Override
            public void onResponse(Call<List<TopList>> call, Response<List<TopList>> response) {
                mView.showView(response.body());
            }

            @Override
            public void onFailure(Call<List<TopList>> call, Throwable t) {
                Log.e(BaseNetData.TAG, "onFailure: ", t);
            }
        });
    }

//    public void getData(final List<Long> ids, final int type) {
//        BaseNetData netData = new BaseNetData(mContext);
//        if (type == 1) {
//            for (long id : ids) {
//                netData.getSongList(id, new HttpListener<SongList>() {
//                    @Override
//                    public void onResponse(Call<SongList> call, Response<SongList> response) {
//                        mList_official.add(response.body());
//                        if (mList_official.size() == ids.size()){
//                            all.put(1, mList_official);
//                            all.put(2, mList_allWord);
//                            mView.showListView(all);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<SongList> call, Throwable t) {
//
//                    }
//                });
//            }
//        } else {
//            for (int i = 0; i < ids.size(); i++) {
//                netData.getSongList(ids.get(i), new HttpListener<SongList>() {
//                    @Override
//                    public void onResponse(Call<SongList> call, Response<SongList> response) {
//                        mList_allWord.add(response.body());
//                        if (mList_allWord.size() == ids.size()){
//                            all.put(1, mList_official);
//                            all.put(2, mList_allWord);
//                            mView.showListView(all);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<SongList> call, Throwable t) {
//
//                    }
//                });
//            }
//        }
//    }

}
