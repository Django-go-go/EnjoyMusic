package com.example.administrator.httpdemo.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.administrator.httpdemo.CustomView.MusicView;
import com.example.administrator.httpdemo.Data.entity.Song;
import com.example.administrator.httpdemo.Data.entity.SongRecorder;
import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.MusicService;
import com.example.administrator.httpdemo.greendao.gen.DaoMaster;
import com.example.administrator.httpdemo.greendao.gen.DaoSession;
import com.example.administrator.httpdemo.greendao.gen.SongRecorderDao;

import java.util.List;

/**
 * Created by Administrator on 2017/9/1.
 */

public class GreenDaoUtils {

    private static final String TAG = "GreenDaoUtils";

    private static SongRecorderDao getDao(Context context){
        MusicApp app = null;
        if (context instanceof MusicService){
            app = (MusicApp) ((MusicService)context).getApplication();
        }
        if (context instanceof FragmentActivity){
            app = (MusicApp) ((FragmentActivity)context).getApplication();
        }
        if (app == null){
            throw new IllegalArgumentException("app null");
        }
        return app.getDao();
    }

    private static SongRecorderDao getDao2(Context context){
        MusicApp app = null;
        if (context instanceof MusicService){
            app = (MusicApp) ((MusicService)context).getApplication();
        }
        if (context instanceof FragmentActivity){
            app = (MusicApp) ((FragmentActivity)context).getApplication();
        }
        if (app == null){
            throw new IllegalArgumentException("app null");
        }
        return app.getDao2();
    }

    public static boolean isHave(Context context, SongRecorder songRecorder){
        List<SongRecorder> list = getDao(context).queryBuilder()
                .where(SongRecorderDao.Properties.SpecialID.eq(songRecorder.getSpecialID()))
                .build()
                .list();
        if (list == null || list.size() == 0){
            return false;
        }
//        Log.i(TAG, "isHave: " + list.get(0));
        return true;
    }

    public static void addSongRecorder(Context context, SongRecorder songRecorder){
        if (isHave(context, songRecorder)){
            getDao(context).deleteByKey(querySongRecorder(context, songRecorder).getId());
            getDao(context).insert(songRecorder);
        }else {
            getDao(context).insert(songRecorder);
        }

//        Log.i(TAG, "addSongRecorder: " + getDao(context).loadAll());
    }

    public static void addSongRecorder2(Context context, SongRecorder songRecorder){
        getDao2(context).insert(songRecorder);
    }

    public static List<SongRecorder> queryAll2(Context context){
        return getDao2(context).loadAll();
    }

    public static void deleteAll2(Context context){
        getDao2(context).deleteAll();
    }


    public static SongRecorder querySongRecorder(Context context, SongRecorder songRecorder){
        return getDao(context).queryBuilder()
                .where(SongRecorderDao.Properties.SpecialID.eq(songRecorder.getSpecialID()))
                .build()
                .list()
                .get(0);
    }

    public static List<SongRecorder> queryAll(Context context){
        return getDao(context).loadAll();
    }

    public static List<SongRecorder> querySongRecorderDesc(Context context){
        return getDao(context).queryBuilder()
                .orderDesc(SongRecorderDao.Properties.Id)
                .limit(100)
                .build()
                .list();
    }

    public static void deleteAll(Context context){
        getDao(context).deleteAll();
    }
}
