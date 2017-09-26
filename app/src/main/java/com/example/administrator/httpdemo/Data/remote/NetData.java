package com.example.administrator.httpdemo.Data.remote;

import com.example.administrator.httpdemo.Data.entity.MySharedSongs;
import com.example.administrator.httpdemo.Data.entity.MyUser;
import com.example.administrator.httpdemo.Data.entity.CreateSongList;

import java.util.List;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2017/7/22.
 */

public interface NetData {
    void share(MySharedSongs sharedSong);
    void share(List<MySharedSongs> sharedSongs);

    void upload(CreateSongList list);
    void upload(MySharedSongs song);

    List<String> query(MyUser myUser);
    List<CreateSongList> query(List<String> ids);

    void update(MyUser myUser);
    void update(MySharedSongs song);
    void updateSongFromFile(MySharedSongs song, BmobFile file);

    MySharedSongs appreciate();
}
