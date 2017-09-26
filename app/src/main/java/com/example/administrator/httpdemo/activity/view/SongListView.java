package com.example.administrator.httpdemo.activity.view;

import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.Data.entity.Song;
import com.example.administrator.httpdemo.Data.entity.Song2;

import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 */

public interface SongListView {
    void showListView(List<Song2> song2s);
//    void showListView(List<Song> songs);
    void showListView(Mp3Info mp3Info);
}
