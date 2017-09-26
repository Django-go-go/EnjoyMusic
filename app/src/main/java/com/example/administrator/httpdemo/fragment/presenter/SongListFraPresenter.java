package com.example.administrator.httpdemo.fragment.presenter;

import android.content.Context;
import android.util.Log;

import com.example.administrator.httpdemo.Data.entity.SongList;
import com.example.administrator.httpdemo.Data.entity.SongList2;
import com.example.administrator.httpdemo.Data.remote.BaseNetData;
import com.example.administrator.httpdemo.Listener.HttpListener;
import com.example.administrator.httpdemo.fragment.view.SongListFraView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/6.
 */

public class SongListFraPresenter {
    private Context mContext;
    private SongListFraView mView;

    public SongListFraPresenter(Context context, SongListFraView view) {
        mContext = context;
        mView = view;
    }

    public void getSongList(int size, int no){
        BaseNetData netData = new BaseNetData(mContext);
        netData.getSongList(size, no, new HttpListener<List<SongList2>>() {
            @Override
            public void onResponse(Call<List<SongList2>> call, Response<List<SongList2>> response) {
                mView.showGridView(response.body());
            }

            @Override
            public void onFailure(Call<List<SongList2>> call, Throwable t) {
                Log.e(BaseNetData.TAG, "onFailure: ", t);
            }
        });
    }

//    public void getSongList(String cat, String order, int offset, boolean total, int limit) throws Exception {
//        BaseNetData netData = new BaseNetData(mContext);
//        if (order.equals("hot")){
//            netData.getSongListForList(cat, order, offset, total, limit, new HttpListener<List<SongList>>() {
//                @Override
//                public void onResponse(Call<List<SongList>> call, Response<List<SongList>> response) {
//                    mView.showGridView(response.body());
//                }
//
//                @Override
//                public void onFailure(Call<List<SongList>> call, Throwable t) {
//
//                }
//            });
//        }else if (order.equals("new")){
//            netData.getSongListForList(cat, order, offset, total, limit, new HttpListener<List<SongList>>() {
//                @Override
//                public void onResponse(Call<List<SongList>> call, Response<List<SongList>> response) {
//                    mView.showNewData(response.body());
//                }
//
//                @Override
//                public void onFailure(Call<List<SongList>> call, Throwable t) {
//
//                }
//            });
//        }
//
//    }
}
