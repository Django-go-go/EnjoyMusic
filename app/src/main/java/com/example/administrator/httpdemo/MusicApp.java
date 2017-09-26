package com.example.administrator.httpdemo;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.httpdemo.Data.entity.SongRecorder;
import com.example.administrator.httpdemo.Utils.GreenDaoUtils;
import com.example.administrator.httpdemo.greendao.gen.DaoMaster;
import com.example.administrator.httpdemo.greendao.gen.DaoSession;
import com.example.administrator.httpdemo.greendao.gen.SongRecorderDao;
import com.example.administrator.httpdemo.greendao.gen.DaoMaster.DevOpenHelper;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by Administrator on 2017/6/5.
 */

public class MusicApp extends Application {

    public static final String TAG = "MusicApp";

    private SongRecorderDao mSongRecorderDao;
    private SongRecorderDao mSongRecorderDao2;
    @Override
    public void onCreate() {
        super.onCreate();
        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config =new BmobConfig.Builder(this)
                ////设置appkey
                .setApplicationId("7ac81dd7fb28cee52b3cc2ce33634221")
                ////请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                ////文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024*1024)
                ////文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "Music-db", null);
        DaoMaster.DevOpenHelper helper2 = new DaoMaster.DevOpenHelper(this, "Music2-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        SQLiteDatabase db2 = helper2.getWritableDatabase();
        mSongRecorderDao =  new DaoMaster(db).newSession().getSongRecorderDao();
        mSongRecorderDao2 =  new DaoMaster(db2).newSession().getSongRecorderDao();
    }

    public SongRecorderDao getDao(){
        return mSongRecorderDao;
    }

    public SongRecorderDao getDao2(){
        return mSongRecorderDao2;
    }
}
