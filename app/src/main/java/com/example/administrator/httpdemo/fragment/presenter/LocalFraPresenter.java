package com.example.administrator.httpdemo.fragment.presenter;

import android.content.Context;
import android.util.Log;

import com.example.administrator.httpdemo.Data.entity.CollectSongList;
import com.example.administrator.httpdemo.Data.entity.CreateSongList;
import com.example.administrator.httpdemo.Data.entity.MyUser;
import com.example.administrator.httpdemo.Data.entity.SongList;
import com.example.administrator.httpdemo.Data.remote.BaseNetData;
import com.example.administrator.httpdemo.Listener.HttpListener;
import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.fragment.view.LocalFraView;
import com.example.administrator.httpdemo.Utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/13.
 */

public class LocalFraPresenter{
    private Context mContext;
    private LocalFraView mView;

    public LocalFraPresenter(Context context, LocalFraView view) {
        mContext = context;
        mView = view;
    }

    public void getCreateSongList(){
        final String id = BmobUser.getCurrentUser(MyUser.class).getObjectId();
        final BmobQuery<CreateSongList> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("userID", id);
        bmobQuery.findObjects(new FindListener<CreateSongList>() {
            @Override
            public void done(List<CreateSongList> list, BmobException e) {
                if (e == null){
                    mView.showExpandableView(list, null);
                }else {
                    Log.e(MusicApp.TAG, "getCreateSongList: =========> ", e);
                }
            }
        });
    }

    public void getCollectSongList(){
        final String id = BmobUser.getCurrentUser(MyUser.class).getObjectId();
        final BmobQuery<CollectSongList> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("userID", id);
        bmobQuery.findObjects(new FindListener<CollectSongList>() {
            @Override
            public void done(List<CollectSongList> list, BmobException e) {
                if (e == null){
                    mView.showExpandableView(null, list);
                }else {
                    Log.e(MusicApp.TAG, "getCollectSongList: =========> ", e);
                }
            }
        });
    }


    public void deleteCollectSongList(CollectSongList songList){
        songList.delete(songList.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e != null){
                    Log.e(MusicApp.TAG, "deleteCollectSongList: ", e);
                }else {
                    ToastUtils.showShort(mContext, "删除成功");
                }
            }
        });
    }

    public void deleteCreateSongList(CreateSongList createSongList){
        createSongList.delete(createSongList.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e != null){
                    Log.e(MusicApp.TAG, "deleteCreateSongList: ", e);
                }else {
                    ToastUtils.showShort(mContext, "删除成功");
                }
            }
        });
    }

    public void saveCreateSongList(CreateSongList createSongList){
        createSongList.save(new SaveListener<String>() {
            @Override
            public void done(final String s, BmobException e) {
                if (e == null) {
                    ToastUtils.showShort(mContext, "创建成功");
                }else {
                    Log.e(MusicApp.TAG, "saveCreateSongList: ", e);
                }
            }
        });
    }
}
