package com.example.administrator.httpdemo.activity.presenter;

import android.content.Context;
import android.util.Log;

import com.example.administrator.httpdemo.Data.Local.ContentHelper;
import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.Data.entity.MySharedSongs;
import com.example.administrator.httpdemo.Data.entity.Song;
import com.example.administrator.httpdemo.Data.entity.Song2;
import com.example.administrator.httpdemo.Data.entity.SongList;
import com.example.administrator.httpdemo.Data.remote.BaseNetData;
import com.example.administrator.httpdemo.Listener.HttpListener;
import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.Other.Constant;
import com.example.administrator.httpdemo.Utils.OtherUtils;
import com.example.administrator.httpdemo.activity.base.BasePresenter;
import com.example.administrator.httpdemo.activity.view.SongListView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/8.
 */

public class SongListPresenter extends BasePresenter<SongListView> {
    public static final String TAG = "SongListActivity";

    private SongListView mView;
    private Context mContext;

    public SongListPresenter(SongListView view, Context context) {
        mView = view;
        mContext = context;
    }

    public void getSong2FromSongList(String id){
        BaseNetData netData = new BaseNetData(mContext);
        netData.getSong2FromSongList(id, new HttpListener<List<Song2>>() {
            @Override
            public void onResponse(Call<List<Song2>> call, Response<List<Song2>> response) {
                mView.showListView(response.body());
            }

            @Override
            public void onFailure(Call<List<Song2>> call, Throwable t) {
                Log.e(MusicApp.TAG, "getCollectSong onFailure: ", t);
            }
        });
    }

    public void getSong2FromTopList(int type){
        BaseNetData netData = new BaseNetData(mContext);
        netData.getSong2FromTopList(type, new HttpListener<List<Song2>>() {
            @Override
            public void onResponse(Call<List<Song2>> call, Response<List<Song2>> response) {
                Log.i(TAG, "onResponse: " + response.body().toString());
                mView.showListView(response.body());
            }

            @Override
            public void onFailure(Call<List<Song2>> call, Throwable t) {
                Log.e(MusicApp.TAG, "getCollectSong onFailure: ", t);
            }
        });
    }

    public void getCreateSongLocal(List<String> ids){
        ContentHelper helper = new ContentHelper(mContext);
        List<Mp3Info> mp3Infos = helper.getMusic();
        for (String id : ids){
            for (Mp3Info mp3Info : mp3Infos){
                if (mp3Info.getId() == Long.parseLong(id)){
                    mView.showListView(mp3Info);
                }
            }
        }
    }

    public void getCreateSongBmob(List<String> ids){
        BmobQuery<MySharedSongs> query = new BmobQuery<>();
        query.addWhereContainedIn("objectId", ids);
        query.findObjects(new FindListener<MySharedSongs>() {
            @Override
            public void done(List<MySharedSongs> list, BmobException e) {
                if (e == null){
                    for (MySharedSongs song : list){
                        mView.showListView(OtherUtils.MySharedSongsToMp3Info(song));
                    }
                }else {
                    Log.e(MusicApp.TAG, "getCreateSongBmob done: ", e);
                }
            }
        });
    }

    public void getCreateSongNet(List<String> ids){
        for (String id : ids){
            new BaseNetData(mContext).getSong2(id, new HttpListener<Song2>() {
                @Override
                public void onResponse(Call<Song2> call, Response<Song2> response) {
                    Song2 song = response.body();
                    if (song != null){
                        mView.showListView(OtherUtils.Song2ToMp3Info(song));
                    }else {
                        Log.i(MusicApp.TAG, "getCreateSongNet onResponse: ");
                    }
                }

                @Override
                public void onFailure(Call<Song2> call, Throwable t) {
                    Log.e(MusicApp.TAG, "getCreateSongNet onFailure: ", t);
                }
            });
        }
    }
}
