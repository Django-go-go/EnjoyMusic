package com.example.administrator.httpdemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.httpdemo.Data.entity.CollectSongList;
import com.example.administrator.httpdemo.Data.entity.CreateSongList;
import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.Data.entity.MySharedSongList;
import com.example.administrator.httpdemo.Data.entity.MySharedSongs;
import com.example.administrator.httpdemo.Data.entity.MyUser;
import com.example.administrator.httpdemo.Data.entity.NetSong;
import com.example.administrator.httpdemo.Data.entity.Song;
import com.example.administrator.httpdemo.Data.entity.Song2;
import com.example.administrator.httpdemo.Data.entity.SongList;
import com.example.administrator.httpdemo.Data.entity.SongList2;
import com.example.administrator.httpdemo.Data.entity.TopList;
import com.example.administrator.httpdemo.Data.remote.BaseNetData;
import com.example.administrator.httpdemo.Event.UpdateEvent;
import com.example.administrator.httpdemo.Listener.DeleteSongListener;
import com.example.administrator.httpdemo.Listener.HttpCallBack;
import com.example.administrator.httpdemo.Listener.HttpListener;
import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.Other.Constant;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Service.UpLoadIntentService;
import com.example.administrator.httpdemo.activity.base.BaseActivity;
import com.example.administrator.httpdemo.activity.presenter.SongListPresenter;
import com.example.administrator.httpdemo.activity.view.SongListView;
import com.example.administrator.httpdemo.adapter.CreateSongAdapter;
import com.example.administrator.httpdemo.adapter.SongsAdapter;
import com.example.administrator.httpdemo.Utils.OtherUtils;
import com.example.administrator.httpdemo.Utils.ToastUtils;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import retrofit2.Call;
import retrofit2.Response;

public class SongListActivity extends BaseActivity<SongListView, SongListPresenter>
        implements SongListView, DeleteSongListener{

    private static final String TAG = "SongListActivity";

    private ListView mListView;
    private Toolbar mToolbar;
    private TextView mTextView1, mTextView2, mTextView3,
            mTextView4, mTextView5, mTextView6, mTextView7;
    private ImageView mImageView;
    private View headView;

    private CreateSongList mCreateSongList;
    private MySharedSongList mMySharedSongList;
    private CollectSongList mCollectSongList;
//    private List<Song> mSongs;
//    private SongList mSongList;
    private List<Mp3Info> mMp3Infos = new ArrayList<>();
    private TopList mTopList;
    private SongList2 mSongList2;
    private List<Song2> mSong2s;

    private CreateSongAdapter mAdapter;

    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    private int songlistType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mPresenter = createPresenter();

        setContentView(R.layout.activity_songlist);

        initToolbar();
        initHeadView();

//        mSongList = intent.getParcelableExtra("collectSongList");
//
//        if (mSongList != null){
//            songlistType = Constant.TYPE_COLLECT;
//            initSongList();
//            setColor();
//        } else {
//            if (intent.getStringExtra("hotSongList") != null){
//                new BaseNetData(this).getSongList(3778678, new HttpListener<SongList>() {
//                    @Override
//                    public void onResponse(Call<SongList> call, Response<SongList> response) {
//                        mSongList = response.body();
//                        songlistType = Constant.TYPE_NETSONGLIST;
//                        initSongList();
//                        setColor();
//                    }
//                    @Override
//                    public void onFailure(Call<SongList> call, Throwable t) {
//                        Log.e(TAG, "onFailure: ===========> ", t);
//                    }
//                });
//            }else {
//                mSongList = intent.getParcelableExtra("toplist");
//                if (mSongList != null){
//                    songlistType = Constant.TYPE_NETSONGLIST;
//                    initSongList();
//                    setColor();
//                }
//            }
//        }
        mCollectSongList = (CollectSongList) intent.getSerializableExtra("collectSongList");
        if (mCollectSongList != null){
            songlistType = Constant.TYPE_COLLECT;
            initCollectSongList();
        }

        mSongList2 = intent.getParcelableExtra("songlist2");
        if (mSongList2 != null){
            songlistType = Constant.TYPE_SONGLIST;
            initSongList();
        }

        mMySharedSongList = (MySharedSongList) intent.getSerializableExtra("mySharedSongList");

        if (mMySharedSongList != null){
            songlistType = Constant.TYPE_SHARE;
            initShareSongList();
        }

        mTopList = intent.getParcelableExtra("toplist");
        if (mTopList != null){
            songlistType = Constant.TYPE_TOPLIST;
            initTopList();
        }

        mCreateSongList = (CreateSongList) intent.getSerializableExtra("createSongList");
        if (mCreateSongList != null){
            songlistType = Constant.TYPE_CREATE;
            initCreateSongList();
        }

        setColor();

        setPlayFragment(R.id.songlist_frame);
    }

    private void initToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.songlist_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mTextView3 = (TextView)mToolbar.findViewById(R.id.toolbar_tv1);
        mTextView3.setText("歌单");
        ImageView imageView = (ImageView)mToolbar.findViewById(R.id.toolbar_iv);
        imageView.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initHeadView(){
        headView = LayoutInflater.from(this).inflate(R.layout.item_list_head, null);
        mTextView1 = (TextView) headView.findViewById(R.id.songlist_name);
        mTextView2 = (TextView) headView.findViewById(R.id.nickname);
        mImageView = (ImageView) headView.findViewById(R.id.coverImage);
        mTextView4 = (TextView) headView.findViewById(R.id.collect_tv);
        mTextView5 = (TextView) headView.findViewById(R.id.chat_tv);
        mTextView6 = (TextView) headView.findViewById(R.id.share_tv);
        mTextView7 = (TextView) headView.findViewById(R.id.download_tv);

        mListView = (ListView) findViewById(R.id.songlist_listview);
        mListView.addHeaderView(headView);
        mListView.setAdapter(null);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position != 0){
                    if (songlistType == Constant.TYPE_COLLECT || songlistType == Constant.TYPE_SONGLIST
                            || songlistType == Constant.TYPE_TOPLIST){
                        new BaseNetData(SongListActivity.this).getNetSong(mSong2s.get(position - 1).getSong_id(), new HttpCallBack() {
                            @Override
                            public void netSong(NetSong netSong) {
                                Song2 song2 = mSong2s.get(position-1);
                                if (netSong.getFile_link() != null && netSong.getFile_link().length() > 0){
                                    song2.setPic_big(OtherUtils.getImageUrl(netSong));
                                    song2.setUrl(netSong.getFile_link());
                                    Mp3Info mp3 = OtherUtils.Song2ToMp3Info(song2);
                                    Log.i(MusicApp.TAG, "netSong: ======================> " + mp3);
                                    if (!playService.getPlaylist().contains(mp3)){
                                        playService.getPlaylist().add(0, mp3);
                                        playService.play(0);
                                    }
                                } else {
                                    ToastUtils.showShort(SongListActivity.this, "资源无法获取,抱歉!");
                                }
                            }
                        });
                    }

                    if (songlistType == Constant.TYPE_CREATE){
                        Mp3Info mp3Info = mMp3Infos.get(position-1);
                        if (!playService.getPlaylist().contains(mp3Info)) {
                            playService.getPlaylist().add(0, mp3Info);
                            playService.play(0);
                        }
                    }
                }
            }
        });
    }

    private void initTopList(){
        mTextView1.setText(mTopList.getName());
        mTextView2.setText(mTopList.getComment());

        if (songlistType == Constant.TYPE_TOPLIST){
            mTextView4.setAlpha(0.5f);
            mTextView5.setAlpha(0.5f);
            mTextView6.setAlpha(0.5f);
            mTextView7.setAlpha(0.5f);
        }

        Picasso.with(this).load(mTopList.getPic_s260()).error(R.drawable.backgroung).into(mImageView);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 1){
                    mTextView3.setText(mTopList.getName());
                    mTextView3.setTranslationY(mTextView3.getHeight());
                    mTextView3.animate().translationY(0).setDuration(500);
                }else if (firstVisibleItem == 0){
                    mTextView3.setText("榜单");
                }
            }
        });

        mPresenter.getSong2FromTopList(Integer.parseInt(mTopList.getType()));
    }

    private void initSongList(){

        mTextView1.setText(mSongList2.getTitle());
        mTextView2.setText(mSongList2.getTag());

        if (songlistType == Constant.TYPE_SONGLIST){
            mTextView4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showShort(SongListActivity.this, "正在收藏...");
                    final String id = BmobUser.getCurrentUser(MyUser.class).getObjectId();
                    CollectSongList collectSongList = new CollectSongList(id, mSongList2.getPic_300(),
                            mSongList2.getListid(), mSongList2.getTitle(), mSong2s.size(), mSongList2.getTag());
                    collectSongList.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e != null){
                                Log.e(MusicApp.TAG, "collectSongList: ==========>", e);
                            }else {
                                EventBus.getDefault().postSticky(new UpdateEvent(2));
                            }
                        }
                    });

                }
            });
            mTextView5.setAlpha(0.5f);
            mTextView6.setAlpha(0.5f);
            mTextView7.setAlpha(0.5f);
        }

        Picasso.with(this).load(mSongList2.getPic_300()).error(R.drawable.backgroung).into(mImageView);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 1){
                    mTextView3.setText(mSongList2.getTitle());
                    mTextView3.setTranslationY(mTextView3.getHeight());
                    mTextView3.animate().translationY(0).setDuration(500);
                }else if (firstVisibleItem == 0){
                    mTextView3.setText("歌单");
                }
            }
        });

        mPresenter.getSong2FromSongList(mSongList2.getListid());
    }

    private void initCollectSongList(){

        mTextView1.setText(mCollectSongList.getName());
        mTextView2.setText(mCollectSongList.getTag());

        if (songlistType == Constant.TYPE_COLLECT){
            mTextView4.setAlpha(0.5f);
            mTextView5.setAlpha(0.5f);
            mTextView6.setAlpha(0.5f);
            mTextView7.setAlpha(0.5f);
        }

        Picasso.with(this).load(mCollectSongList.getImageCoverUrl()).error(R.drawable.backgroung).into(mImageView);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 1){
                    mTextView3.setText(mCollectSongList.getName());
                    mTextView3.setTranslationY(mTextView3.getHeight());
                    mTextView3.animate().translationY(0).setDuration(500);
                }else if (firstVisibleItem == 0){
                    mTextView3.setText("收藏歌单");
                }
            }
        });

        mPresenter.getSong2FromSongList(mCollectSongList.getId());
    }

    private void initCreateSongList(){
        mTextView1.setText(mCreateSongList.getName());

        getCoverImage();

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 1){
                    mTextView3.setText(mCreateSongList.getName());
                    mTextView3.setTranslationY(mTextView3.getHeight());
                    mTextView3.animate().translationY(0).setDuration(500);
                }else if (firstVisibleItem == 0){
                    mTextView3.setText("创建的歌单");
                }
            }
        });

        if (OtherUtils.getNumber(mCreateSongList) == 0){
//            TextView textView = new TextView(SongListActivity.this);
//            textView.setText("请添加歌曲");
//            textView.setTextColor(Color.RED);
//            textView.setTextSize(20);
//            textView.setPadding(0, DensityUtils.dp2px(SongListActivity.this, 30), 0, 0);
//            textView.setGravity(Gravity.CENTER);
//            contentView.addView(textView);
        }

        if (mCreateSongList.getLocalSongIds() != null && mCreateSongList.getLocalSongIds().size() != 0){
            mPresenter.getCreateSongLocal(mCreateSongList.getLocalSongIds());
        }

        if (mCreateSongList.getNetSongIds() != null && mCreateSongList.getNetSongIds().size() != 0){
            mPresenter.getCreateSongNet(mCreateSongList.getNetSongIds());
        }
        if (mCreateSongList.getBmobSongIds() != null && mCreateSongList.getBmobSongIds().size() != 0){
            mPresenter.getCreateSongBmob(mCreateSongList.getBmobSongIds());
        }

        mTextView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort(SongListActivity.this, "正在后台分享");
                UpLoadIntentService.startUpload(SongListActivity.this, mCreateSongList);
            }
        });
    }

    private void initShareSongList(){
        mTextView1.setText(mMySharedSongList.getName());

        if (mMySharedSongList.getImageURL() != null){
            Picasso.with(this).load(mMySharedSongList.getImageURL()).into(mImageView);
        } else {
            mImageView.setImageResource(R.drawable.music);
        }

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 1){
                    mTextView3.setText(mMySharedSongList.getName());
                    mTextView3.setTranslationY(mTextView3.getHeight());
                    mTextView3.animate().translationY(0).setDuration(500);
                }else if (firstVisibleItem == 0){
                    mTextView3.setText("分享歌单");
                }
            }
        });

        if (mMySharedSongList.getNetSongIDs() != null && mMySharedSongList.getNetSongIDs().size() != 0){
            mPresenter.getCreateSongNet(mMySharedSongList.getNetSongIDs());
        }
        if (mMySharedSongList.getBmobSongIDs() != null && mMySharedSongList.getBmobSongIDs().size() != 0){
            mPresenter.getCreateSongBmob(mMySharedSongList.getBmobSongIDs());
        }

        mTextView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort(SongListActivity.this, "正在收藏...");
                final String id = BmobUser.getCurrentUser(MyUser.class).getObjectId();
                CollectSongList collectSongList = new CollectSongList(
                        id, mMySharedSongList.getImageURL(),
                        mMySharedSongList.getObjectId(), mMySharedSongList.getName(),
                        OtherUtils.getNumber(mMySharedSongList), "来自分享的歌单");
                collectSongList.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e != null){
                            Log.e(MusicApp.TAG, "collectSongList: ==========>", e);
                        }else {
                            EventBus.getDefault().postSticky(new UpdateEvent(2));
                        }
                    }
                });

            }
        });
        mTextView5.setAlpha(0.5f);
        mTextView6.setAlpha(0.5f);
        mTextView7.setAlpha(0.5f);
    }

    private void getCoverImage(){
        if (mCreateSongList.getImageURL() != null){
            Picasso.with(this).load(mCreateSongList.getImageURL()).into(mImageView);
        } else {
            new BmobQuery<CreateSongList>().getObject(mCreateSongList.getObjectId(), new QueryListener<CreateSongList>() {
                @Override
                public void done(CreateSongList createSongList, BmobException e) {
                    if (e == null){
                        if (createSongList.getImageURL() != null){
                            Picasso.with(SongListActivity.this).load(createSongList.getImageURL()).into(mImageView);
                        }else {
                            if (mCreateSongList.getLocalSongIds() != null && mCreateSongList.getLocalSongIds().size() != 0){
                                Picasso.with(SongListActivity.this).load("content://media/external/audio/albumart" + "/"
                                        + OtherUtils.getAlumIdForID(SongListActivity.this, mCreateSongList.getLocalSongIds().get(0)))
                                        .into(mImageView);
                            }else {
                                mImageView.setImageResource(R.drawable.music);
                            }
                        }
                    }
                }
            });
        }
    }

    private void setColor(){
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    if (mSongList2 != null){
                        bitmap = Picasso.with(SongListActivity.this).load(mSongList2.getPic_300()).get();
                    }
                    if (mTopList != null){
                        bitmap = Picasso.with(SongListActivity.this).load(mTopList.getPic_s260()).get();
                    }
                    if (mMySharedSongList != null){
                        bitmap = Picasso.with(SongListActivity.this).load(mMySharedSongList.getImageURL()).get();
                    }
                    if (mCollectSongList != null){
                        bitmap = Picasso.with(SongListActivity.this).load(mCollectSongList.getImageCoverUrl()).get();
                    }
                    if (mCreateSongList != null){
                        if (mCreateSongList.getImageURL() == null){
                            new BmobQuery<CreateSongList>().getObject(mCreateSongList.getObjectId(), new QueryListener<CreateSongList>() {
                                @Override
                                public void done(CreateSongList createSongList, BmobException e) {
                                    if (e == null){
                                        Bitmap bitmap;
                                        try {
                                            bitmap = Picasso.with(SongListActivity.this).load(mCreateSongList.getImageURL()).error(R.drawable.music).get();
                                            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                                @Override
                                                public void onGenerated(Palette palette) {
                                                    int population = 0;
                                                    int rgb = 0;
                                                    List<Palette.Swatch> swatches = palette.getSwatches();
                                                    for (Palette.Swatch swatch : swatches){
                                                        if (swatch != null){
                                                            if (swatch.getPopulation() > population){
                                                                population = swatch.getPopulation();
                                                                rgb = swatch.getRgb();
                                                            }
                                                        }
                                                    }
                                                    headView.setBackgroundColor(rgb);
                                                    mToolbar.setBackgroundColor(rgb);
                                                }
                                            });
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }else {
                            bitmap = Picasso.with(SongListActivity.this).load(mCreateSongList.getImageURL()).error(R.drawable.music).get();
                        }
                    }
                    if (bitmap != null){
                        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                int population = 0;
                                int rgb = 0;
                                List<Palette.Swatch> swatches = palette.getSwatches();
                                for (Palette.Swatch swatch : swatches){
                                    if (swatch != null){
                                        if (swatch.getPopulation() > population){
                                            population = swatch.getPopulation();
                                            rgb = swatch.getRgb();
                                        }
                                    }
                                }
                                headView.setBackgroundColor(rgb);
                                mToolbar.setBackgroundColor(rgb);
                            }
                        });
                    }else {
                        headView.setBackgroundColor(Color.RED);
                        mToolbar.setBackgroundColor(Color.RED);
                    }

                } catch (IOException e) {
                    mExecutor.shutdown();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public SongListPresenter createPresenter() {
        return new SongListPresenter(this, SongListActivity.this);
    }
    
//    @Override
//    public void showListView(List<Song> songs) {
//        mSongs = songs;
//        mListView.setAdapter(new SongsAdapter(this, songs));
//    }

    @Override
    public void showListView(List<Song2> song2s) {
        mSong2s = song2s;
        mListView.setAdapter(new SongsAdapter(this, mSong2s));
    }

    @Override
    public void showListView(Mp3Info mp3Info) {
        mMp3Infos.add(mp3Info);
        if (songlistType == Constant.TYPE_CREATE &&
                (mMp3Infos.size() == OtherUtils.getNumber(mCreateSongList) ||
                        mMp3Infos.size() == OtherUtils.getNumber2(mCreateSongList))){
            mAdapter = new CreateSongAdapter(this, mMp3Infos);
            mListView.setAdapter(mAdapter);
        }

        if (songlistType == Constant.TYPE_SHARE &&
                mMp3Infos.size() == OtherUtils.getNumber(mMySharedSongList)){
            mAdapter = new CreateSongAdapter(this, mMp3Infos);
            mListView.setAdapter(mAdapter);
        }
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

    @Override
    public void delete(final int pos) {
        final Mp3Info mp3Info = mMp3Infos.get(pos);
        if (mp3Info.getType() == Constant.TYPE_BMOB){
            new BmobQuery<CreateSongList>().getObject(mCreateSongList.getObjectId(), new QueryListener<CreateSongList>() {
                @Override
                public void done(CreateSongList createSongList, BmobException e) {
                    if (e == null){
                        List<String> ids = createSongList.getBmobSongIds();
                        ids.remove(mp3Info.getId_String());
                        CreateSongList songList = new CreateSongList();
                        songList.setBmobSongIds(ids);
                        songList.update(mCreateSongList.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null){
                                    mMp3Infos.remove(mMp3Infos.get(pos));
                                    mAdapter.notifyDataSetChanged();
                                    ToastUtils.showShort(SongListActivity.this, "已删除");
                                    EventBus.getDefault().postSticky(new UpdateEvent(1));
                                }else {
                                    Log.e(MusicApp.TAG, "done: ========================>", e);
                                    ToastUtils.showShort(SongListActivity.this, "删除失败");
                                }
                            }
                        });
                    }
                }
            });
        }else if (mp3Info.getType() == Constant.TYPE_LOCAL){
            new BmobQuery<CreateSongList>().getObject(mCreateSongList.getObjectId(), new QueryListener<CreateSongList>() {
                @Override
                public void done(CreateSongList createSongList, BmobException e) {
                    if (e == null){
                        List<String> ids = createSongList.getLocalSongIds();
                        ids.remove(String.valueOf(mp3Info.getId()));
                        CreateSongList songList = new CreateSongList();
                        songList.setLocalSongIds(ids);
                        songList.update(mCreateSongList.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null){
                                    mMp3Infos.remove(mMp3Infos.get(pos));
                                    mAdapter.notifyDataSetChanged();
                                    EventBus.getDefault().postSticky(new UpdateEvent(1));
                                    ToastUtils.showShort(SongListActivity.this, "已删除");
                                }else {
                                    Log.e(MusicApp.TAG, "done: ========================>", e);
                                    ToastUtils.showShort(SongListActivity.this, "删除失败");
                                }
                            }
                        });
                    }
                }
            });
        }else {
            new BmobQuery<CreateSongList>().getObject(mCreateSongList.getObjectId(), new QueryListener<CreateSongList>() {
                @Override
                public void done(CreateSongList createSongList, BmobException e) {
                    if (e == null){
                        List<String> ids = createSongList.getNetSongIds();
                        ids.remove(mp3Info.getId_String());
                        CreateSongList songList = new CreateSongList();
                        songList.setNetSongIds(ids);
                        songList.update(mCreateSongList.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null){
                                    mMp3Infos.remove(mMp3Infos.get(pos));
                                    mAdapter.notifyDataSetChanged();
                                    EventBus.getDefault().postSticky(new UpdateEvent(1));
                                    ToastUtils.showShort(SongListActivity.this, "已删除");
                                }else {
                                    Log.e(MusicApp.TAG, "done: ========================>", e);
                                    ToastUtils.showShort(SongListActivity.this, "删除失败");
                                }
                            }
                        });
                    }
                }
            });
        }
    }
}
