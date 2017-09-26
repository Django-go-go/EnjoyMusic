package com.example.administrator.httpdemo.Listener;

/**
 * Created by Administrator on 2017/8/22.
 */

public interface DialogFragmentListener {
    void createSongList(String name);
    void deleteSongList(int group, int child);
}
