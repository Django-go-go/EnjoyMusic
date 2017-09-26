package com.example.administrator.httpdemo.fragment.view;

import com.example.administrator.httpdemo.Data.entity.CreateSongList;
import com.example.administrator.httpdemo.Data.entity.MySharedSongList;
import com.example.administrator.httpdemo.Data.entity.Song;
import com.example.administrator.httpdemo.Data.entity.SongList;
import com.example.administrator.httpdemo.Data.entity.SongList2;

import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */

public interface RecomFraView {
//    void showGridView_Recommend(List<SongList> lists);
    void showGridView_Recommend(List<SongList2> lists);
    void showGridView_Share(List<MySharedSongList> lists);
}
