package com.example.administrator.httpdemo.fragment.presenter;

import android.content.Context;
import android.util.Log;

import com.example.administrator.httpdemo.Data.entity.CreateSongList;
import com.example.administrator.httpdemo.Data.entity.MySharedSongList;
import com.example.administrator.httpdemo.Data.entity.MySharedSongs;
import com.example.administrator.httpdemo.Data.entity.Song2;
import com.example.administrator.httpdemo.Data.entity.SongList;
import com.example.administrator.httpdemo.Data.entity.SongList2;
import com.example.administrator.httpdemo.Data.remote.BaseNetData;
import com.example.administrator.httpdemo.Listener.HttpListener;
import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.Utils.OtherUtils;
import com.example.administrator.httpdemo.fragment.view.RecomFraView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/3.
 */

public class RecomFraPresenter  {
    private RecomFraView mView;
    private Context mContext;

    public RecomFraPresenter(RecomFraView view, Context context) {
        mView = view;
        mContext = context;
    }
//    public void getData(String order) throws Exception{
//        BaseNetData netData = new BaseNetData(mContext);
//        netData.getSongListForList("all", order, 0, true, 6, new HttpListener<List<SongList>>() {
//            @Override
//            public void onResponse(Call<List<SongList>> call, Response<List<SongList>> response) {
//                mView.showGridView_Recommend(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<List<SongList>> call, Throwable t) {
//
//            }
//        });
//    }

    public void getData(int num){
        new BaseNetData(mContext).getHotSongList(num, new HttpListener<List<SongList2>>() {
            @Override
            public void onResponse(Call<List<SongList2>> call, Response<List<SongList2>> response) {
                mView.showGridView_Recommend(response.body());
            }

            @Override
            public void onFailure(Call<List<SongList2>> call, Throwable t) {

            }
        });
    }

    public void getShareSongList(){
        BmobQuery<MySharedSongList> bmobQuery = new BmobQuery<>();
        bmobQuery.setLimit(6).order("-createdAt").findObjects(new FindListener<MySharedSongList>() {
            @Override
            public void done(List<MySharedSongList> list, BmobException e) {
                if (e == null){
                    mView.showGridView_Share(list);
                }else {
                    Log.e(MusicApp.TAG, "getShareSongList: =======>", e);
                }
            }
        });
    }
}
