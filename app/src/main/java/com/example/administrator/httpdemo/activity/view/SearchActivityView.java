package com.example.administrator.httpdemo.activity.view;

import com.example.administrator.httpdemo.Data.entity.HotWord;
import com.example.administrator.httpdemo.Data.entity.SearchSong;
import com.example.administrator.httpdemo.Data.entity.Song;

import java.util.List;

/**
 * Created by Administrator on 2017/8/21.
 */

public interface SearchActivityView {
    void showListView(List<SearchSong> songs);
    void showHotWord(List<HotWord> words);
}
