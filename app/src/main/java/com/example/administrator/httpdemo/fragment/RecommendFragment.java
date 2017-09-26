package com.example.administrator.httpdemo.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.httpdemo.CustomView.MyGridView;
import com.example.administrator.httpdemo.Data.Local.ContentHelper;
import com.example.administrator.httpdemo.Data.entity.CreateSongList;
import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.Data.entity.MySharedSongList;
import com.example.administrator.httpdemo.Data.entity.NetSong;
import com.example.administrator.httpdemo.Data.entity.SongList;
import com.example.administrator.httpdemo.Data.entity.SongList2;
import com.example.administrator.httpdemo.Data.entity.TopList;
import com.example.administrator.httpdemo.Data.remote.BaseNetData;
import com.example.administrator.httpdemo.Data.remote.UrlString;
import com.example.administrator.httpdemo.Listener.HttpCallBack;
import com.example.administrator.httpdemo.Listener.HttpListener;
import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.Other.Constant;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.ScreenUtils;
import com.example.administrator.httpdemo.activity.ShareActivity;
import com.example.administrator.httpdemo.activity.SongListActivity;
import com.example.administrator.httpdemo.adapter.ShareListAdapter;
import com.example.administrator.httpdemo.adapter.SongListGridAdapter;
import com.example.administrator.httpdemo.fragment.base.BaseFragment;
import com.example.administrator.httpdemo.fragment.presenter.RecomFraPresenter;
import com.example.administrator.httpdemo.fragment.view.RecomFraView;
import com.example.administrator.httpdemo.Utils.ContentUriFToFilePathUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class RecommendFragment extends BaseFragment implements RecomFraView {

    private TextView share_tv, recom_tv, hotSong_tv, line_tv;
    private MyGridView mGridView_reco;
    private MyGridView mGridView_share;

    public Typeface typeface;

    private RecomFraPresenter mPresenter;

    private List<SongList2> mSongList2s = new ArrayList<>();
    private List<MySharedSongList> mMySharedSongLists = new ArrayList<>();

    public RecommendFragment() {
    }

    @Override
    protected void onLazyLoadOnce() {
        if (mSongList2s.size() == 0 && mMySharedSongLists.size() == 0){
            mPresenter.getShareSongList();
            mPresenter.getData(6);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPresenter = new RecomFraPresenter(this, getContext());

        typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/girl.ttf");
        LinearLayout contentView = new LinearLayout(getContext());
        contentView.setOrientation(LinearLayout.VERTICAL);

        ListView listView = new ListView(getContext());

        View headView1 = inflater.inflate(R.layout.fragment_recommend, null);
        View headView2 = inflater.inflate(R.layout.item_list_grid, null);
        View headView3 = inflater.inflate(R.layout.item_list_grid, null);

        ((TextView)headView2.findViewById(R.id.tv_list_grid)).setText("推荐歌单");
        ((TextView)headView3.findViewById(R.id.tv_list_grid)).setText("分享歌单");
        mGridView_reco = (MyGridView) headView2.findViewById(R.id.grid_list_grid);
        mGridView_share = (MyGridView) headView3.findViewById(R.id.grid_list_grid);

        initTextView(headView1);

        listView.addHeaderView(headView1);
        listView.addHeaderView(headView2);
        listView.addHeaderView(headView3);
        listView.setAdapter(null);
        listView.setDividerHeight(0);
        contentView.addView(listView);

        mGridView_share.setNumColumns(3);
        mGridView_reco.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), SongListActivity.class);
                SongList2 songList2 = mSongList2s.get(position);
                songList2.setPic_300(songList2.getPic());
                intent.putExtra("songlist2", songList2);
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        mGridView_share.setNumColumns(2);
        mGridView_share.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), SongListActivity.class);
                intent.putExtra("mySharedSongList", mMySharedSongLists.get(position));
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        return contentView;
    }

    @Override
    protected void onSaveState(Bundle outState) {
        super.onSaveState(outState);
        if (mSongList2s != null)
            outState.putParcelableArrayList("songlists", (ArrayList<? extends Parcelable>) mSongList2s);
        if (mMySharedSongLists != null){
            outState.putSerializable("createSongLists", (Serializable) mMySharedSongLists);
        }
    }

    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
            if (savedInstanceState != null){
                mSongList2s = savedInstanceState.getParcelableArrayList("songlists");
                mMySharedSongLists = (List<MySharedSongList>) savedInstanceState.getSerializable("createSongLists");
                if (mSongList2s != null){
                    mGridView_reco.setAdapter(new SongListGridAdapter(getContext(), mSongList2s));
                }
                if (mMySharedSongLists != null){
                    mGridView_share.setAdapter(new ShareListAdapter(getContext(), mMySharedSongLists));
                }
            }else {
                mPresenter.getData(6);
                mPresenter.getShareSongList();
            }
    }

    private void initTextView(View view){
        share_tv = (TextView) view.findViewById(R.id.everyday_share);
        recom_tv = (TextView) view.findViewById(R.id.recommend);
        hotSong_tv = (TextView) view.findViewById(R.id.hot_song);
        line_tv = (TextView) view.findViewById(R.id.line_tv);


        line_tv.setText("快来戳我");
        line_tv.setTypeface(typeface);
//        Animation animation = new TranslateAnimation(-ScreenUtils.getScreenWidth(getContext()),
//                ScreenUtils.getScreenWidth(getContext()),
//                line_tv.getTop(), line_tv.getTop());
//        animation.setDuration(8000);
//        animation.setRepeatCount(-1);
//        animation.start();
//        line_tv.setAnimation(animation);

        share_tv.setText("黑");
        share_tv.setTypeface(typeface);
        recom_tv.setText("风");
        recom_tv.setTypeface(typeface);
        hotSong_tv.setText("梨");
        hotSong_tv.setTypeface(typeface);
        share_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShareActivity.class);
                intent.putExtra("shareSong", "我的分享");
                startActivity(intent);
//                new BaseNetData(getContext()).getNetSong("276867440", new HttpCallBack() {
//                    @Override
//                    public void netSong(NetSong netSong) {
//                        Log.i(MusicApp.TAG, "netSong: " + netSong);
//                    }
//                });

//                MediaPlayer mPlayer = new MediaPlayer();
//                mPlayer.reset();
//                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                try {
//                    mPlayer.setDataSource("http://yinyueshiting.baidu.com/data2/music/c20256f0e41fa58984298f9022119339/522720246/522720246.mp3?xcode=d104bbb93987cff80e36f2067d58dcad");
//                    mPlayer.prepareAsync();
//                    mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                        @Override
//                        public void onPrepared(MediaPlayer mp) {
//                            mp.start();
//                        }
//                    });
//                } catch (IOException e) {
//                    Log.e(MusicApp.TAG, "onClick: ", e);
//                }

//                Mp3Info mp3Info = new ContentHelper(getContext()).getMusic().get(0);
//                long id = new ContentHelper(getContext()).getMusic().get(0).getAlbumId();
//                Log.i(ContentHelper.TAG, "onClick1: " + new ContentHelper(getContext()).getUrl(id));
//                Log.i(ContentHelper.TAG, "onClick2: " + mp3Info.getPicUrl());
//                Log.i(ContentHelper.TAG, "onClick3: " + ContentUriFToFilePathUtils.getPath(getContext(), Uri.parse(mp3Info.getPicUrl())));
            }
        });
        recom_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShareActivity.class);
                intent.putExtra("recomSong", "每日推荐");
                startActivity(intent);
            }
        });
        hotSong_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShareActivity.class);
                intent.putExtra("hotSong", "热歌推荐");
                startActivity(intent);
            }
        });
    }

//    private void getData(){
//        try {
//            mPresenter.getData("new");
//            mPresenter.getShareSongList();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    @Override
//    public void showGridView_Recommend(List<SongList> lists) {
//        mSongList2s.addAll(lists);
//        mGridView_reco.setAdapter(new SongListGridAdapter(getContext(), mSongLists));
//    }

    @Override
    public void showGridView_Recommend(List<SongList2> lists) {
        mSongList2s.addAll(lists);
        mGridView_reco.setAdapter(new SongListGridAdapter(getContext(), mSongList2s));
    }

    @Override
    public void showGridView_Share(List<MySharedSongList> lists) {
        mMySharedSongLists.addAll(lists);
        mGridView_share.setAdapter(new ShareListAdapter(getContext(), mMySharedSongLists));
    }

}
