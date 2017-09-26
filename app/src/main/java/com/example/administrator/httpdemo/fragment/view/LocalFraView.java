package com.example.administrator.httpdemo.fragment.view;

import com.example.administrator.httpdemo.Data.entity.CollectSongList;
import com.example.administrator.httpdemo.Data.entity.CreateSongList;
import com.example.administrator.httpdemo.Data.entity.Song;
import com.example.administrator.httpdemo.Data.entity.SongList;

import java.util.List;

/**
 * Created by Administrator on 2017/8/13.
 */

public interface LocalFraView {
//    void showExpandableView(List<CreateSongList> list1, List<SongList> list2);
    void showExpandableView(List<CreateSongList> list1, List<CollectSongList> list2);
}
