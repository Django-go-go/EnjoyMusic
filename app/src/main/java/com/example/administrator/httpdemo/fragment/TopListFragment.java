package com.example.administrator.httpdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.administrator.httpdemo.CustomView.LoadView;
import com.example.administrator.httpdemo.Data.entity.SongList;
import com.example.administrator.httpdemo.Data.entity.TopList;
import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.Other.Constant;
import com.example.administrator.httpdemo.activity.SongListActivity;
import com.example.administrator.httpdemo.adapter.TopListAdapter2;
import com.example.administrator.httpdemo.fragment.base.BaseFragment;
import com.example.administrator.httpdemo.fragment.presenter.TopListFraPresenter;
import com.example.administrator.httpdemo.fragment.view.TopListFraView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/3.
 */

public class TopListFragment extends BaseFragment implements TopListFraView {

    private TopListFraPresenter mPresenter;

//    private ListView mListView;
//    private Map<Integer, List<SongList>> allSongList;
    private List<TopList> mList;

    private RelativeLayout contentView;
    private GridView mGridView;
    private LoadView mLoadView;

//    private TopListAdapter topListAdapter;
    private TopListAdapter2 mListAdapter2;

    public TopListFragment() {
    }

    @Override
    protected void onLazyLoadOnce() {
        if (mList == null){
            Log.i(MusicApp.TAG, "onLazyLoadOnce: TopListFragment");
//            getData(Constant.getTopListIDFor1(), 1);
//            getData(Constant.getTopListIDFor2(), 2);
            getData();
            contentView.addView(mLoadView);
            mLoadView.setVisibility(View.VISIBLE);
            mLoadView.start();
        }

    }

    @Override
    protected void onRestoreState(Bundle savedInstanceState) {
        super.onRestoreState(savedInstanceState);
            if (savedInstanceState != null){
                List<TopList> list = savedInstanceState.getParcelableArrayList("toplist");
                if (list != null){
                    if (mList == null){
                        mList = new ArrayList<>();
                    }
                    mList.addAll(list);
                    mListAdapter2 = new TopListAdapter2(getContext(), mList);
                    mGridView.setAdapter(mListAdapter2);
                    mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getContext(), SongListActivity.class);
                            intent.putExtra("toplist", mList.get(position));
                            startActivity(intent);
                            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    });
                    contentView.setVisibility(View.VISIBLE);
                }
//                List<SongList> songLists1 = savedInstanceState.getParcelableArrayList("songlists1");
//                List<SongList> songLists2 = savedInstanceState.getParcelableArrayList("songlists2");
//                if (songLists1 != null && songLists2 != null){
//                    allSongList.put(1, songLists1);
//                    allSongList.put(2, songLists2);
//                    topListAdapter = new TopListAdapter(getContext(), allSongList);
//                    topListAdapter.setOnClickedListener(new TopListAdapter.onClickedListener() {
//                        @Override
//                        public void onClicked(SongList songList) {
//                            Intent intent = new Intent(getContext(), SongListActivity.class);
//                            intent.putExtra("songlist", songList);
//                            startActivity(intent);
//                            getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                        }
//                    });
//                    mListView.setAdapter(topListAdapter);
//                    contentView.setVisibility(View.VISIBLE);
//                }
            }
    }

    @Override
    protected void onSaveState(Bundle outState) {
        super.onSaveState(outState);
//        if (allSongList != null){
//            outState.putParcelableArrayList("songlists1", (ArrayList<? extends Parcelable>) allSongList.get(1));
//            outState.putParcelableArrayList("songlists2", (ArrayList<? extends Parcelable>) allSongList.get(2));
//        }
        if (mList != null){
            outState.putParcelableArrayList("toplist", (ArrayList<? extends Parcelable>) mList);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter = new TopListFraPresenter(this, getContext());

        mLoadView = new LoadView(getContext());
        mLoadView.setVisibility(View.GONE);
        mLoadView.setMinimumHeight(100);
        mLoadView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));

        contentView = new RelativeLayout(getContext());
//        mListView = new ListView(getContext());
        mGridView = new GridView(getContext());
        mGridView.setNumColumns(3);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), SongListActivity.class);
                intent.putExtra("toplist", mList.get(position));
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        return contentView;
    }

    private void getData(){
        mPresenter.getData();
//        mPresenter.getData(ids, type);
    }

    @Override
    public void showView(List<TopList> topLists) {
        if (mList == null){
            mList = new ArrayList<>();
        }else {
            mList.clear();
        }
        for (TopList topList : topLists){
            if (!topList.getType().equals("105")){
                mList.add(topList);
            }
        }
        mListAdapter2 = new TopListAdapter2(getContext(), mList);
        mLoadView.stop();
        mLoadView.setVisibility(View.GONE);

        contentView.removeAllViews();
//        mListView.setAdapter(topListAdapter);
//        contentView.addView(mListView);
        mGridView.setAdapter(mListAdapter2);
        contentView.addView(mGridView);
    }

//    @Override
//    public void showListView(Map<Integer, List<SongList>> all) {
//        allSongList = all;
//        topListAdapter = new TopListAdapter(getContext(), all);
//        topListAdapter.setOnClickedListener(new TopListAdapter.onClickedListener() {
//            @Override
//            public void onClicked(SongList songList) {
//                Intent intent = new Intent(getContext(), SongListActivity.class);
//                intent.putExtra("songlist", songList);
//                startActivity(intent);
//                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//            }
//        });
//
//        mLoadView.stop();
//        mLoadView.setVisibility(View.GONE);
//
//        contentView.removeAllViews();
//        mListView.setAdapter(topListAdapter);
//        contentView.addView(mListView);
//    }
}
