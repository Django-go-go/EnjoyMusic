package com.example.administrator.httpdemo.activity.base;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.httpdemo.CustomView.CustomDialog;
import com.example.administrator.httpdemo.CustomView.CustomDialogAdapter;
import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.Data.entity.SongRecorder;
import com.example.administrator.httpdemo.IPlayback;
import com.example.administrator.httpdemo.Listener.ListDialogListener;
import com.example.administrator.httpdemo.Listener.PlayListener;
import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.MusicService;
import com.example.administrator.httpdemo.Utils.GreenDaoUtils;
import com.example.administrator.httpdemo.Utils.OtherUtils;
import com.example.administrator.httpdemo.Utils.SharedPreferencesUtils;
import com.example.administrator.httpdemo.Utils.ToastUtils;
import com.example.administrator.httpdemo.activity.PlayActivity;
import com.example.administrator.httpdemo.activity.presenter.MainPresenter;
import com.example.administrator.httpdemo.activity.presenter.PlayPresenter;
import com.example.administrator.httpdemo.adapter.ItemListAdapter;
import com.example.administrator.httpdemo.adapter.ItemListDialogAdapter;
import com.example.administrator.httpdemo.fragment.PlayFragment;
import com.example.administrator.httpdemo.Utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity
        implements IPlayback.UICallBack, PlayFragment.ToActivityListener{

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    protected MusicService playService;
    private boolean isBound = false;
    protected T mPresenter;

    private boolean isOne = true;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
        hasPermission();
    }

    @Override
    public void updateUI(int position, List<Mp3Info> mp3Infos) {
        if (mPresenter instanceof PlayPresenter){
            updatePlayActivity(position, mp3Infos, isOne);
            isOne = false;
        }

        if(mp3Infos.size() > 0 && !(mPresenter instanceof PlayPresenter)){
            if (mPlayListener != null) {
                if (playService.isRunning()) {
                    mPlayListener.update(position, true, mp3Infos);
                } else {
                    mPlayListener.update(position, false, mp3Infos);
                }
            }
            isOne = true;
        }


    }

    protected void updatePlayActivity(int position, List<Mp3Info> mp3Infos, boolean isOne){};

    public abstract T createPresenter();

    private ServiceConnection MyServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.ServiceBind mBinder = (MusicService.ServiceBind) service;
            playService = mBinder.getService();
            playService.registerUICallBack(BaseActivity.this);

            if (SharedPreferencesUtils.getParam(BaseActivity.this, "curpos", -1) != null){
                int cur = (int) SharedPreferencesUtils.getParam(BaseActivity.this, "curpos", -1);
                if (cur != -1){
                    List<Mp3Info> mp3Infos = new ArrayList<>();
                    List<SongRecorder> songRecorders = GreenDaoUtils.queryAll2(BaseActivity.this);
                    GreenDaoUtils.deleteAll2(BaseActivity.this);
                    if (songRecorders == null){
                        songRecorders = new ArrayList<>();
                    }
                    for (int i = 0; i < songRecorders.size(); i++){
                        mp3Infos.add(OtherUtils.SongRecorderToMp3Info(songRecorders.get(i)));
                    }

                    if (playService != null){
                        playService.setCurrPosition(cur);
                        playService.setPlaylist(mp3Infos);
                        SharedPreferencesUtils.setParam(BaseActivity.this, "curpos", -1);
                        songRecorders.clear();
                        mp3Infos.clear();
                    }else {
                        System.out.println("==================================>");
                    }
                }

            }

            playService.notifyUI(playService.getCurrPosition(), playService.getPlaylist());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            playService.unRegisterUICallBack(BaseActivity.this);
            playService = null;
            isBound = false;
        }
    };

    public void exeBindService(){
        if(!isBound){
            Intent intent = new Intent(this, MusicService.class);
            bindService(intent, MyServiceConn, Context.BIND_AUTO_CREATE);
            isBound = true;
        }
    }

    public void exeUnbindService(){
        if(isBound){
            unbindService(MyServiceConn);
            isBound = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE){
            if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE)
                    && permissions[1].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }else {
                finish();
            }
        }
    }

    private void hasPermission(){

        String[] PERMISSIONS_STORAGE={
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CONTACTS)) {
            ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    PlayFragment mPlayFragment = new PlayFragment();
    public void setPlayFragment(int id){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(id, mPlayFragment);
        transaction.commit();
    }

    @Override
    public void onActivityClick(int click) {
        if (click == PlayFragment.CLICK_PLAY){
            if (playService.isRunning()){
                playService.pause();
            }else {
                if (playService.getCurrProgress() > 0){
                    playService.seekTo(playService.getCurrProgress());
                    playService.start();
                }else {
                    playService.play(playService.getCurrPosition());
                }
            }
        }else if (click == PlayFragment.CLICK_MENU){
            CustomDialog dialog = CustomDialogAdapter.createDialog(BaseActivity.this, null, new ListDialogListener() {
                @Override
                public void createListDialog(ListView listView) {
                    final List<String> list = new ArrayList<>();
                    for (Mp3Info mp3Info : playService.getPlaylist()){
                        list.add(mp3Info.getTitle());
                    }

                    final ItemListAdapter adapter = new ItemListAdapter(BaseActivity.this, list, playService.getCurrPosition());

                    adapter.setListener(new ItemListAdapter.DeleteSongListener() {
                        @Override
                        public void deleteSong(int pos) {

//                            Mp3Info lastMp3 = playService.getPlaylist().get(playService.getLastPosition());
                            if (pos != playService.getCurrPosition()){
                                Mp3Info currentMp3 = playService.getPlaylist().get(playService.getCurrPosition());
                                playService.getPlaylist().remove(pos);
                                playService.setCurrPosition(playService.getPlaylist().indexOf(currentMp3));
                            }else {
                                playService.pause();
                                playService.getPlaylist().remove(pos);
                                if (playService.getPlaylist().size() > 0){
                                    playService.play(pos);
                                }
                            }
//                            playService.setLastPosition(playService.getPlaylist().indexOf(lastMp3));

                            list.remove(pos);
                            adapter.setIsPlayPos(playService.getCurrPosition());
                            adapter.notifyDataSetChanged();
                        }
                    });

                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            ViewGroup group = (ViewGroup) parent.getChildAt(playService.getLastPosition());
//                            ImageView imageView = (ImageView) group.getChildAt(0);
//                            TextView textView = (TextView) group.getChildAt(1);
//                            textView.setTextColor(Color.BLACK);
//                            imageView.setVisibility(View.GONE);

                            playService.play(position);

//                            int sum = playService.getPlaylist().size();

//                            for (int i = 0; i < sum; i++){
//                                ViewGroup viewGroup = (ViewGroup) parent.getChildAt(i);
//                                ImageView iV = (ImageView) viewGroup.getChildAt(0);
//                                TextView tV = (TextView) viewGroup.getChildAt(1);
//                                if (i == playService.getCurrPosition()){
//                                    tV.setTextColor(Color.RED);
//                                    iV.setVisibility(View.VISIBLE);
//                                }else {
//                                    tV.setTextColor(Color.BLACK);
//                                    iV.setVisibility(View.GONE);
//                                }
//                            }
                            adapter.setIsPlayPos(position);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            });
            dialog.setDefaultContentHeight(ScreenUtils.getScreenHeight(BaseActivity.this)*3/5).build().show();
        }
    }

    protected PlayListener mPlayListener;

    public void setPlayListener(PlayListener playListener) {
        mPlayListener = playListener;
    }
}
