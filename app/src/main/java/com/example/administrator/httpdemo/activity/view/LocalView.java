package com.example.administrator.httpdemo.activity.view;

import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.Data.entity.SongRecorder;

import java.util.List;

/**
 * Created by Administrator on 2017/6/17.
 */

public interface LocalView {

    void showLocalData(List<Mp3Info> mp3Infos);
    void showRecorderData(List<SongRecorder> recorders);
}
