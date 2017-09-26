package com.example.administrator.httpdemo.Listener;

import com.example.administrator.httpdemo.Data.entity.Mp3Info;

import java.util.List;

/**
 * Created by Administrator on 2017/8/15.
 */

public interface PlayListener {
    void update(int pos, boolean isRunning, List<Mp3Info> mp3Infos);
}
