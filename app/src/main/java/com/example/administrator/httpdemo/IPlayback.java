package com.example.administrator.httpdemo;

import android.content.Context;

import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.Data.entity.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 */

public interface IPlayback {
    void setPlaylist(List<Mp3Info> playlist);
    void play(int position);
    void playNext();
    void playLast();
    void pause();
    void seekTo(int msec);
    void start();
    void setPlayMode(PlayMode mode);
    boolean isRunning();
    void unRegisterCallBack(CallBack callBack);
    void registerCallBack(CallBack callBack);
    void unRegisterUICallBack(UICallBack uiCallBack);
    void registerUICallBack(UICallBack uiCallBack);
    int getCurrPosition();
    void setCurrPosition(int pos);
    int getLastPosition();
    void setLastPosition(int pos);
    int getCurrProgress();
    int getDuration();
    List<Mp3Info> getPlaylist();
    PlayMode getPlayMode();

    interface CallBack{
        void onSwitchLast();
        void onSwitchNext();
        void onComplete();
        void onPlayStatusChange(boolean isRunning);
    }

    interface UICallBack{
        void updateUI(int position, List<Mp3Info> mp3Infos);
    }
}
