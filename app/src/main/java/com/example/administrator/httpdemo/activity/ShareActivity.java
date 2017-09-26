package com.example.administrator.httpdemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.RenderScript;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.httpdemo.Data.entity.Banner;
import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.Data.entity.MySharedSongs;
import com.example.administrator.httpdemo.Data.entity.Song2;
import com.example.administrator.httpdemo.Data.remote.BaseNetData;
import com.example.administrator.httpdemo.Listener.HttpListener;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.FileUtils;
import com.example.administrator.httpdemo.Utils.ScreenUtils;
import com.example.administrator.httpdemo.Utils.StorageUtils;
import com.example.administrator.httpdemo.Utils.ToastUtils;
import com.example.administrator.httpdemo.activity.base.BaseActivity;
import com.example.administrator.httpdemo.activity.presenter.SharePresenter;
import com.example.administrator.httpdemo.activity.view.InfoView;
import com.example.administrator.httpdemo.activity.view.ShareView;
import com.example.administrator.httpdemo.adapter.HotSongAdapter;
import com.example.administrator.httpdemo.adapter.MyShareSongAdapter;
import com.example.administrator.httpdemo.Utils.DensityUtils;
import com.example.administrator.httpdemo.Utils.OtherUtils;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.jude.rollviewpager.hintview.IconHintView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/21.
 */

public class ShareActivity extends BaseActivity<ShareView, SharePresenter> implements ShareView {

    private static final String TAG = "ShareActivity";

    private ListView mListView;
    private List<MySharedSongs> mSongsList;
    private List<Song2> mSong2s;

    private TextView title_tv;

    private RollPagerView mLoopViewPager;
    private MyLoopAdapter mLoopAdapter;

    private String str1;
    private String str2;
    private String str3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        mPresenter = createPresenter();
        mSongsList = new ArrayList<>();
        initToolbar();
        initView();

        Intent intent = getIntent();
        str1 = intent.getStringExtra("shareSong");
        str2 = intent.getStringExtra("recomSong");
        str3 = intent.getStringExtra("hotSong");
        if (str1 != null){
            title_tv.setText(str1);
            getShareSong();
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Mp3Info mp3Info = OtherUtils.MySharedSongsToMp3Info(mSongsList.get(position-1));
                    Log.i(TAG, "onItemClick: " + mp3Info);
                    if (!playService.getPlaylist().contains(mp3Info)){
                        playService.getPlaylist().add(0, mp3Info);
                        playService.play(0);
                    }
                }
            });
        }
        if (str2 != null){
            title_tv.setText(str2);
            getRecomSong();
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Mp3Info mp3Info = OtherUtils.MySharedSongsToMp3Info(mSongsList.get(position-1));
                    Log.i(TAG, "onItemClick: " + mp3Info);
                    if (!playService.getPlaylist().contains(mp3Info)){
                        playService.getPlaylist().add(0, mp3Info);
                        playService.play(0);
                    }
                }
            });
        }

        if (str3 != null){
            title_tv.setText(str3);
            getHotSong();
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        }

        mPresenter.getBanner();

        setPlayFragment(R.id.share_frame);
    }

    private void initView(){
        mListView = (ListView) findViewById(R.id.share_listview);
//        ImageView imageView = new ImageView(this);
//        imageView.setLayoutParams(new AbsListView.LayoutParams(
//                AbsListView.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(this, 180)));
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageResource(R.drawable.bg);
//        mListView.addHeaderView(imageView);

        mLoopViewPager= new RollPagerView(this);
        mLoopViewPager.setLayoutParams(new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(this, 180)));
        mLoopViewPager.setPlayDelay(2000);
        mLoopViewPager.setHintView(new ColorPointHintView(this, Color.RED, Color.WHITE));
        mListView.addHeaderView(mLoopViewPager);
        mListView.setAdapter(null);
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_share);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        title_tv = (TextView) toolbar.findViewById(R.id.toolbar_tv1);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.toolbar_iv);
        imageView.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public SharePresenter createPresenter() {
        return new SharePresenter(this, this);
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

    private void getShareSong(){
        BmobQuery<MySharedSongs> bmobQuery = new BmobQuery<>();
        bmobQuery.setLimit(100);
        bmobQuery.addWhereEqualTo("fromId", OtherUtils.getUserId()).findObjects(new FindListener<MySharedSongs>() {
            @Override
            public void done(final List<MySharedSongs> list, BmobException e) {
                if (e == null) {
                    mSongsList.clear();
                    mSongsList.addAll(list);
                    mListView.setAdapter(new MyShareSongAdapter(ShareActivity.this, mSongsList, 1));
                }
            }
        });

    }

    private void getRecomSong(){
        BmobQuery<MySharedSongs> bmobQuery = new BmobQuery<>();
        bmobQuery.setLimit(20);
        bmobQuery.findObjects(new FindListener<MySharedSongs>() {
            @Override
            public void done(final List<MySharedSongs> list, BmobException e) {
                if (e == null) {
                    mSongsList.clear();
                    mSongsList.addAll(list);
                    mListView.setAdapter(new MyShareSongAdapter(ShareActivity.this, mSongsList, 2));

                }
            }
        });

    }

    private void getHotSong(){
        new BaseNetData(this).getHotSong(new HttpListener<List<Song2>>() {
            @Override
            public void onResponse(Call<List<Song2>> call, Response<List<Song2>> response) {
                mSong2s = response.body();
                Log.i(TAG, "onResponse: " + mSong2s);
                mListView.setAdapter(new HotSongAdapter(ShareActivity.this, mSong2s));
            }

            @Override
            public void onFailure(Call<List<Song2>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    public void getBanner(List<String> banners) {
        if (str1 != null){
            mLoopAdapter = new MyLoopAdapter(banners.subList(0, 3), mLoopViewPager);
        }
        if (str2 != null){
            mLoopAdapter = new MyLoopAdapter(banners.subList(3, 6), mLoopViewPager);
        }
        if (str3 != null){
            mLoopAdapter = new MyLoopAdapter(banners.subList(6, 9), mLoopViewPager);
        }
        mLoopViewPager.setAdapter(mLoopAdapter);
    }

    private class MyLoopAdapter extends LoopPagerAdapter {
        private List<String> imgs;

        public MyLoopAdapter(List<String> imgs, RollPagerView view) {
            super(view);
            this.imgs = imgs;
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));
            Picasso.with(ShareActivity.this)
                    .load(imgs.get(position))
                    .resize(ScreenUtils.getScreenWidth(ShareActivity.this), DensityUtils.dp2px(ShareActivity.this, 180))
                    .centerCrop()
                    .placeholder(R.drawable.bg)
                    .into(view);
            return view;
        }

        @Override
        public int getRealCount() {
            return imgs.size();
        }

    }
}
