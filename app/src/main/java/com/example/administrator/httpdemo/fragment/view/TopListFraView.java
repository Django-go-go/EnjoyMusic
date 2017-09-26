package com.example.administrator.httpdemo.fragment.view;

import com.example.administrator.httpdemo.Data.entity.SongList;
import com.example.administrator.httpdemo.Data.entity.TopList;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/3.
 */

public interface TopListFraView {
//    void showListView(Map<Integer, List<SongList>> all);
    void showView(List<TopList> topLists);
}
