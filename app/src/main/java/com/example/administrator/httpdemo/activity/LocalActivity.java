package com.example.administrator.httpdemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.httpdemo.Data.Local.ContentHelper;
import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.Data.entity.SongRecorder;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.GreenDaoUtils;
import com.example.administrator.httpdemo.Utils.OtherUtils;
import com.example.administrator.httpdemo.activity.base.BaseActivity;
import com.example.administrator.httpdemo.adapter.LocalListAdapter;
import com.example.administrator.httpdemo.activity.presenter.LocalPresenter;
import com.example.administrator.httpdemo.activity.view.LocalView;
import com.example.administrator.httpdemo.adapter.SongRecorderAdapter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.example.administrator.httpdemo.MusicApp.TAG;

/**
 * Created by Administrator on 2017/6/5.
 */

public class LocalActivity extends BaseActivity<LocalView, LocalPresenter> implements LocalView{

    private Toolbar mToolbar;
    private ListView mListView;

    private LocalListAdapter mLocalListAdapter;
    private SongRecorderAdapter mRecorderAdapter;

    private List<Mp3Info> mp3Infos;
    private List<SongRecorder> mRecorders;

    private int position = 0;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        mPresenter = createPresenter();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        position = bundle.getInt("position");
        title = bundle.getString("title");

        initToolbar();

        mListView = (ListView) findViewById(R.id.local_listview);

        if (position == 0){
            mPresenter.getLocalData();
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    if (playService != null && mp3Infos != null && mp3Infos.size() > 0) {
                        playService.setPlaylist(mp3Infos);
                        playService.play(position);
                    }
                }
            });
        }

        if (position == 1){
            mPresenter.getRecorder();
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    if (playService != null && mRecorders != null && mRecorders.size() > 0) {
                        if (mp3Infos == null){
                            mp3Infos = new ArrayList<>();
                        }
                        for (SongRecorder songRecorder : mRecorders){
                            mp3Infos.add(OtherUtils.SongRecorderToMp3Info(songRecorder));
                        }
                        playService.setPlaylist(mp3Infos);
                        playService.play(position);
                    }
                }
            });
        }

        setPlayFragment(R.id.local_frame);
    }

    @Override
    public LocalPresenter createPresenter() {
        return new LocalPresenter(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        exeBindService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        exeUnbindService();
    }

    private void initToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(title);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_delete:
                        GreenDaoUtils.deleteAll(LocalActivity.this);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {

                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my, menu);

        if(position == 1){
            menu.getItem(0).setVisible(true);
        }
        return true;
    }

    @Override
    public void showLocalData(List<Mp3Info> mp3Infos) {
        this.mp3Infos = mp3Infos;
        mLocalListAdapter = new LocalListAdapter(mp3Infos, this);
        mListView.setAdapter(mLocalListAdapter);
    }

    @Override
    public void showRecorderData(List<SongRecorder> recorders) {
        this.mRecorders = recorders;
        mRecorderAdapter = new SongRecorderAdapter(recorders, this);
        mListView.setAdapter(mRecorderAdapter);
    }
}
