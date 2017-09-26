package com.example.administrator.httpdemo;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;

import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.Other.Constant;
import com.example.administrator.httpdemo.Utils.GreenDaoUtils;
import com.example.administrator.httpdemo.Utils.OtherUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/6/5.
 */

public class PlayBack implements IPlayback, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener{

    private static final String TAG = "PlayBack";

    private List<CallBack> mCallBacks;
    private List<UICallBack> mUICallBacks;

    private MediaPlayer mPlayer;

    private List<Mp3Info> mPlaylist;
    private PlayMode mMode = PlayMode.LOOP;

    private boolean isPreparing = false;
    private int currentPosition = 0;
    private int lastPosition = 0;

    private static volatile PlayBack instance = null;

    private PlayBack() {
        mCallBacks = new ArrayList<>(2);
        mUICallBacks = new ArrayList<>(2);
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(this);
        mPlaylist = new ArrayList<>();
    }

    @Override
    public int getLastPosition() {
        return lastPosition;
    }

    @Override
    public void setLastPosition(int pos) {
        lastPosition = pos;
    }

    public static PlayBack getInstance(){
        if(instance == null){
            synchronized (PlayBack.class){
                if(instance == null)
                    instance = new PlayBack();
            }
        }
        return instance;
    }

    public void play(String str){
        try {
            mPlayer.reset();
            mPlayer.setDataSource(str);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPreparing() {
        return isPreparing;
    }

    @Override
    public void setPlaylist(List<Mp3Info> playlist) {
        if(playlist == null)
            playlist = new ArrayList<>();
        mPlaylist.clear();
        mPlaylist.addAll(playlist);
    }

    @Override
    public void play(final int position) {
        if(position < 0 || position > mPlaylist.size() - 1){
            return;
        }

        Mp3Info song = mPlaylist.get(position);

        mPlayer.reset();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(song.getUrl());
            mPlayer.prepareAsync();
            isPreparing = true;
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    isPreparing = false;
                    currentPosition = position;
                    notifyUI(currentPosition, mPlaylist);
                }
            });
        }catch (IOException e) {
            Log.e(TAG, "IOException: ", e);
        }

    }

    @Override
    public void playNext() {
        if(currentPosition + 1 == mPlaylist.size())
            currentPosition = 0;
        lastPosition = currentPosition;
        currentPosition++;
        play(currentPosition);
        notifyUI(currentPosition, mPlaylist);
    }

    @Override
    public void playLast() {
        if(currentPosition - 1 < 0)
            currentPosition = mPlaylist.size() - 1;
        lastPosition = currentPosition;
        currentPosition--;
        play(currentPosition);
        notifyUI(currentPosition, mPlaylist);
    }

    @Override
    public void pause() {
        if(mPlayer.isPlaying())
            mPlayer.pause();
        notifyUI(currentPosition, mPlaylist);
    }

    @Override
    public void seekTo(int msec) {
        mPlayer.seekTo(msec);
    }

    @Override
    public void start() {
        if(mPlayer != null && !mPlayer.isPlaying()){
            mPlayer.start();
            notifyUI(currentPosition, mPlaylist);
        }
    }

    @Override
    public void setPlayMode(PlayMode mode) {
        mMode = mode;
    }

    @Override
    public boolean isRunning() {
        return mPlayer.isPlaying();
    }

    @Override
    public void unRegisterCallBack(CallBack callBack) {
        mCallBacks.remove(callBack);
    }

    @Override
    public void registerCallBack(CallBack callBack) {
        mCallBacks.add(callBack);
    }

    @Override
    public void unRegisterUICallBack(UICallBack uiCallBack) {
        mUICallBacks.remove(uiCallBack);
    }

    @Override
    public void registerUICallBack(UICallBack uiCallBack) {
        mUICallBacks.add(uiCallBack);
    }

    @Override
    public int getCurrPosition() {
        return currentPosition;
    }

    @Override
    public void setCurrPosition(int pos) {
        lastPosition = currentPosition;
        currentPosition = pos;
    }

    @Override
    public int getCurrProgress() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public List<Mp3Info> getPlaylist() {
        return mPlaylist;
    }

    @Override
    public PlayMode getPlayMode() {
        return mMode;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i(TAG, "onCompletion: ");
        if(mMode == PlayMode.LOOP){
            lastPosition = currentPosition;
            currentPosition++;
            play(currentPosition);
        }else if(mMode == PlayMode.RANDOM){
            lastPosition = currentPosition;
            currentPosition = new Random().nextInt(mPlaylist.size());
            play(currentPosition);
        }else if(mMode == PlayMode.SINGLE){
            play(currentPosition);
        }
    }


    public void notifyUI(int pos, List<Mp3Info> mp3Infos){
        for(UICallBack uiCallBack : mUICallBacks){
            uiCallBack.updateUI(pos, mp3Infos);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.i(TAG, "onError: what " + what);
        Log.i(TAG, "onError: extra " + extra);
        return false;
    }
}
