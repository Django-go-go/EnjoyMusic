package com.example.administrator.httpdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.Data.entity.SongRecorder;
import com.example.administrator.httpdemo.Utils.GreenDaoUtils;
import com.example.administrator.httpdemo.Utils.OtherUtils;
import com.example.administrator.httpdemo.Utils.SharedPreferencesUtils;

import java.util.List;

public class MusicService extends Service implements IPlayback, IPlayback.CallBack{
    private PlayBack mPlayBack;

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayBack = PlayBack.getInstance();
        mPlayBack.registerCallBack(this);
    }

    public MusicService() {
    }

    @Override
    public boolean stopService(Intent name) {
        unRegisterCallBack(this);
        return super.stopService(name);
    }

    @Override
    public void setPlaylist(List<Mp3Info> playlist) {
        mPlayBack.setPlaylist(playlist);
    }

    public void play(String str){
        mPlayBack.play(str);
    }

    @Override
    public void play(int position) {
        mPlayBack.play(position);

        GreenDaoUtils.addSongRecorder(this,
                OtherUtils.Mp3InfoToSongRecorder(getPlaylist().get(position)));
    }

    @Override
    public void playNext() {
        mPlayBack.playNext();
    }

    @Override
    public void playLast() {
        mPlayBack.playLast();
    }

    @Override
    public void pause() {
        mPlayBack.pause();
    }

    @Override
    public void seekTo(int msec) {
        mPlayBack.seekTo(msec);
    }

    @Override
    public void start() {
        mPlayBack.start();
    }

    public boolean isPrepared(){
        return mPlayBack.isPreparing();
    }

    @Override
    public void setPlayMode(PlayMode mode) {
        mPlayBack.setPlayMode(mode);
    }

    @Override
    public boolean isRunning() {
        return mPlayBack.isRunning();
    }

    @Override
    public void unRegisterCallBack(CallBack callBack) {
        mPlayBack.unRegisterCallBack(callBack);
    }

    @Override
    public void registerCallBack(CallBack callBack) {
        mPlayBack.registerCallBack(callBack);
    }

    @Override
    public void unRegisterUICallBack(UICallBack uiCallBack) {
        mPlayBack.unRegisterUICallBack(uiCallBack);
    }

    @Override
    public void registerUICallBack(UICallBack uiCallBack) {
        mPlayBack.registerUICallBack(uiCallBack);
    }

    @Override
    public int getCurrPosition() {
        return mPlayBack.getCurrPosition();
    }

    @Override
    public void setCurrPosition(int pos) {
        mPlayBack.setCurrPosition(pos);
    }

    @Override
    public int getLastPosition() {
        return mPlayBack.getLastPosition();
    }

    @Override
    public void setLastPosition(int pos) {
        mPlayBack.setLastPosition(pos);
    }

    @Override
    public int getCurrProgress() {
        return mPlayBack.getCurrProgress();
    }

    @Override
    public int getDuration() {
        return mPlayBack.getDuration();
    }

    @Override
    public List<Mp3Info> getPlaylist() {
        return mPlayBack.getPlaylist();
    }

    @Override
    public PlayMode getPlayMode() {
        return mPlayBack.getPlayMode();
    }

    @Override
    public void onSwitchLast() {

    }

    @Override
    public void onSwitchNext() {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onPlayStatusChange(boolean isRunning) {

    }

    public void notifyUI(int pos, List<Mp3Info> mp3Infos){
        mPlayBack.notifyUI(pos, mp3Infos);
    }

    public class ServiceBind extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        List<Mp3Info> mp3Infos = getPlaylist();
        for (int i = 0; i < mp3Infos.size(); i++) {
//			System.out.println("==================> " + OtherUtils.Mp3InfoToSongRecorder(mp3Infos.get(i)));
            GreenDaoUtils.addSongRecorder2(this, OtherUtils.Mp3InfoToSongRecorder(mp3Infos.get(i)));
        }
        mp3Infos.clear();
        SharedPreferencesUtils.setParam(this, "curpos", getCurrPosition());
    }
}
