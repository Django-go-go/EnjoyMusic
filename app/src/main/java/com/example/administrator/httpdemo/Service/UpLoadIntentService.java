package com.example.administrator.httpdemo.Service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.administrator.httpdemo.Data.Local.ContentHelper;
import com.example.administrator.httpdemo.Data.entity.CreateSongList;
import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.Data.entity.MySharedSongList;
import com.example.administrator.httpdemo.Data.entity.MySharedSongs;
import com.example.administrator.httpdemo.Data.entity.Song;
import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.Utils.ContentUriFToFilePathUtils;
import com.example.administrator.httpdemo.Utils.OtherUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

import static com.example.administrator.httpdemo.MusicApp.TAG;

/**
 * Created by Administrator on 2017/8/26.
 */

public class UpLoadIntentService extends IntentService {

    private CreateSongList mCreateSongList;
//    private String mSongListId;
    private List<String> shareSongIds;
    private List<Mp3Info> songs;
    private List<MySharedSongs> sharedSongs;
//    private String[] songUrls;
//    private String[] imageUrls;
//    private List<Integer> songInt = new ArrayList<>();
//    private List<Integer> imageInt = new ArrayList<>();

    public UpLoadIntentService() {
        super("UpLoadIntentService");
    }

    public static void startUpload(Context context, CreateSongList createSongList) {
        Intent intent = new Intent(context, UpLoadIntentService.class);
        intent.putExtra("createSongList", createSongList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mCreateSongList = (CreateSongList) intent.getSerializableExtra("createSongList");

        if (mCreateSongList == null) {
            return;
        }

        if (getSongLocal(mCreateSongList.getLocalSongIds())){
            for (final MySharedSongs song : sharedSongs){
                final BmobFile bmobFile = new BmobFile(new File(song.getUrl()));
                bmobFile.upload(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            song.setUrl(bmobFile.getFileUrl());
                            song.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null){
                                        if (shareSongIds == null){
                                            shareSongIds = new ArrayList<>();
                                        }
                                        shareSongIds.add(s);
                                        if (shareSongIds.size() == sharedSongs.size()) {
                                            MySharedSongList mySharedSongList = new MySharedSongList();
                                            mySharedSongList.setName(mCreateSongList.getName());
                                            mySharedSongList.setImageURL(mCreateSongList.getImageURL());
                                            mySharedSongList.setNetSongIDs(mCreateSongList.getNetSongIds());
                                            List<String> ids = new ArrayList<>();
                                            for (String id : shareSongIds){
                                                if (!id.equals("null")){
                                                    ids.add(id);
                                                }
                                            }
                                            mySharedSongList.setBmobSongIDs(ids);
                                            mySharedSongList.save(new SaveListener<String>() {
                                                @Override
                                                public void done(String s, BmobException e) {
                                                    if (e != null) {
                                                        Log.e(TAG, "done: ", e);
                                                    }else {
                                                        for (int i = 0; i < sharedSongs.size(); i++){
                                                            MySharedSongs song = sharedSongs.get(i);
                                                            final String id = shareSongIds.get(i);
                                                            BmobFile bmobFile = new BmobFile(
                                                                    new File(ContentUriFToFilePathUtils.getPath(UpLoadIntentService.this, Uri.parse(song.getAlbum()))));
                                                            bmobFile.upload(new UploadFileListener() {
                                                                @Override
                                                                public void done(BmobException e) {
                                                                    if (e == null){
                                                                        if (!id.equals("null")){
                                                                            MySharedSongs newSong = new MySharedSongs();
                                                                            newSong.setAlbum(id);
                                                                            newSong.update(id, new UpdateListener() {
                                                                                @Override
                                                                                public void done(BmobException e) {
                                                                                    if (e != null){
                                                                                        Log.e(TAG, "图片不存在!: ", e);
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                }
                                                            });

                                                        }
                                                    }
                                                }
                                            });

                                        }
                                    }else {
                                        if (shareSongIds == null){
                                            shareSongIds = new ArrayList<>();
                                        }
                                        shareSongIds.add("null");
                                    }
                                }
                            });
                        }else {
                            if (shareSongIds == null){
                                shareSongIds = new ArrayList<>();
                            }
                            shareSongIds.add("null");
                        }
                    }
                });
            }
        }else {
            MySharedSongList mySharedSongList = new MySharedSongList();
            mySharedSongList.setName(mCreateSongList.getName());
            mySharedSongList.setImageURL(mCreateSongList.getImageURL());
            mySharedSongList.setNetSongIDs(mCreateSongList.getNetSongIds());
            mySharedSongList.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e != null) {
                        Log.e(TAG, "done: ", e);
                    }
                }
            });
        }


//        BmobFile.uploadBatch(songUrls, new UploadBatchListener() {
//            @Override
//            public void onSuccess(List<BmobFile> list1, List<String> list2) {
//                if (list1.size() == list2.size() && list2.size() == songUrls.length) {
//                    new BmobBatch().insertBatch(sharedSongs).doBatch(new QueryListListener<BatchResult>() {
//                        @Override
//                        public void done(List<BatchResult> list, BmobException e) {
//                            if (e == null) {
//                                for (int i = 0; i < list.size(); i++) {
//                                    if (list.get(i).getError() == null) {
//                                        if (shareSongIds == null) {
//                                            shareSongIds = new ArrayList<>();
//                                        }
//                                        shareSongIds.add(list.get(i).getObjectId());
//                                    }
//                                }
//                                if (shareSongIds.size() == sharedSongs.size()) {
//                                    MySharedSongList mySharedSongList = new MySharedSongList();
//                                    mySharedSongList.setImageURL(mCreateSongList.getImageURL());
//                                    mySharedSongList.setNetSongIDs(mCreateSongList.getNetSongIds());
//                                    mySharedSongList.setBmobSongIDs(shareSongIds);
//                                    mySharedSongList.save(new SaveListener<String>() {
//                                        @Override
//                                        public void done(String s, BmobException e) {
//                                            if (e != null){
//                                                Log.e(TAG, "done: ", e);
//                                            }
//                                        }
//                                    });
//                                } else {
//                                    Log.i(TAG, "done: 不相等");
//                                }
//                            } else {
//                                Log.e(TAG, "done: ", e);
//                            }
//                        }
//                    });
//
//                    BmobFile.uploadBatch(imageUrls, new UploadBatchListener() {
//                        @Override
//                        public void onSuccess(List<BmobFile> list3, List<String> list4) {
//                            if (list3.size() == list4.size() && list4.size() == imageUrls.length) {
//                                List<BmobObject> list = new ArrayList<>();
//                                int j = 0, k = 0;
//                                for (int i = 0; i < shareSongIds.size(); i++) {
//                                    MySharedSongs mysong = new MySharedSongs();
//                                    mysong.setObjectId(shareSongIds.get(i));
//                                    if (songInt.get(i) == 1) {
//                                        mysong.setUrl(list2.get(j));
//                                        j++;
//                                    }
//                                    if (imageInt.get(i) == 1) {
//                                        mysong.setAlbum(list4.get(k));
//                                        k++;
//                                    }
//
//                                    list.add(mysong);
//                                }
//                                new BmobBatch().updateBatch(list).doBatch(new QueryListListener<BatchResult>() {
//                                    @Override
//                                    public void done(List<BatchResult> list, BmobException e) {
//                                        if (e != null) {
//                                            Log.e(TAG, "done: updateBatch ", e);
//                                        }
//                                    }
//                                });
//                            }
//
//                        }
//
//                        @Override
//                        public void onProgress(int i, int i1, int i2, int i3) {
//                            Log.i(TAG, "onProgress: " + i);
//                        }
//
//                        @Override
//                        public void onError(int i, String s) {
//                            Log.i(TAG, "onError: imageUrls " + s);
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onProgress(int i, int i1, int i2, int i3) {
//                Log.i(TAG, "songUrls onProgress: " + i);
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                Log.i(TAG, "songUrls onError: " + s);
//            }
//        });


    }

//    public void getUrls() {
//        List<String> songUrls = new ArrayList<>();
//        List<String> imageUrls = new ArrayList<>();
//        String path;
//        for (Mp3Info song : songs) {
//            if (song.getUrl() != null) {
//                songUrls.add(song.getUrl());
//                songInt.add(1);
//            } else {
//                songInt.add(0);
//            }
//            path = ContentUriFToFilePathUtils.getPath(this, Uri.parse(song.getPicUrl()));
//            if (path != null && OtherUtils.bitmapIsHave(path)) {
//                imageUrls.add(path);
//                imageInt.add(1);
//            } else {
//                imageInt.add(0);
//            }
//        }
//
//        Log.i(TAG, "getUrls: " + imageUrls);
//
//        this.songUrls = songUrls.toArray(new String[songUrls.size()]);
//        this.imageUrls = imageUrls.toArray(new String[imageUrls.size()]);
//    }
//
    private boolean getSongLocal(List<String> ids) {
        if (ids == null){
            return false;
        }
        ContentHelper helper = new ContentHelper(UpLoadIntentService.this);
        List<Mp3Info> mp3Infos = helper.getMusic();
        for (String id : ids) {
            for (Mp3Info mp3Info : mp3Infos) {
                if (mp3Info.getId() == Long.parseLong(id)) {
                    if (songs == null) {
                        songs = new ArrayList<>();
                    }
                    songs.add(mp3Info);
                }
            }
        }
        if (sharedSongs == null) {
            sharedSongs = new ArrayList<>();
        } else {
            sharedSongs.clear();
        }
        for (Mp3Info song : songs) {
            sharedSongs.add(OtherUtils.Mp3InfoToMySharedSongs(song));
        }
        return true;
    }
}
