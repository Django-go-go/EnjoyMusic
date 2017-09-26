package com.example.administrator.httpdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.httpdemo.CustomView.LoadView;
import com.example.administrator.httpdemo.Data.entity.HotWord;
import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.Data.entity.SearchSong;
import com.example.administrator.httpdemo.Data.entity.Song;
import com.example.administrator.httpdemo.Data.entity.Song2;
import com.example.administrator.httpdemo.Data.remote.BaseNetData;
import com.example.administrator.httpdemo.Listener.HttpListener;
import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.DensityUtils;
import com.example.administrator.httpdemo.Utils.OtherUtils;
import com.example.administrator.httpdemo.Utils.ToastUtils;
import com.example.administrator.httpdemo.activity.base.BaseActivity;
import com.example.administrator.httpdemo.activity.presenter.SearchPresenter;
import com.example.administrator.httpdemo.activity.view.SearchActivityView;
import com.example.administrator.httpdemo.adapter.SearchSongAdapter;
import com.example.administrator.httpdemo.adapter.SongsAdapter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/21.
 */

public class SearchActivity extends BaseActivity<SearchActivityView, SearchPresenter> implements SearchActivityView {

    private MaterialSearchView searchView;
//    private LoadView mLoadView;
//    private LoadView mLoadView_footer;
    private ListView mListView;
    private TagFlowLayout mFlowLayout;

    private SearchSongAdapter mAdapter;
    private List<SearchSong> mLists;
    private List<String> hotWords;

//    private int offset = 0;
//    private int total = 0;
//    private boolean isLoading = false;
//    private String text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mPresenter = createPresenter();

        initSearchView();

        mListView = (ListView) findViewById(R.id.search_listview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new BaseNetData(SearchActivity.this).getSong2(mLists.get(position).getSongid(), new HttpListener<Song2>() {
                    @Override
                    public void onResponse(Call<Song2> call, Response<Song2> response) {
                        Mp3Info mp3 = OtherUtils.Song2ToMp3Info(response.body());
                        if (!playService.getPlaylist().contains(mp3)){
                            playService.getPlaylist().add(0, mp3);
                            playService.play(0);
                        }
                    }

                    @Override
                    public void onFailure(Call<Song2> call, Throwable t) {

                    }
                });
            }
        });
        mFlowLayout = (TagFlowLayout) findViewById(R.id.flowlayout);
//        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
//            @Override
//            public void onSelected(Set<Integer> selectPosSet) {
//                ToastUtils.showShort(SearchActivity.this, " " + selectPosSet.toString());
//            }
//        });
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                searchView.setQuery(hotWords.get(position), true);
                return false;
            }
        });
//        mLoadView_footer = new LoadView(this);
//        mLoadView_footer.setVisibility(View.GONE);
//        mLoadView_footer.setMinimumHeight(100);
//        mLoadView_footer.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
//
//        mListView.addFooterView(mLoadView_footer);
//        mListView.setAdapter(null);

//        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
//                        && total-1 == view.getLastVisiblePosition() && !isLoading){
//                    isLoading = true;
//                    mPresenter.getSearchSong(offset, text);
//                    mLoadView_footer.setVisibility(View.VISIBLE);
//                    mLoadView_footer.start();
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                total = totalItemCount;
//            }
//        });
//        mLoadView = (LoadView) findViewById(R.id.search_load);
        mPresenter.getHotWord();
        setPlayFragment(R.id.search_frame);
    }

    private void initSearchView() {
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setEllipsize(true);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
//        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(MusicApp.TAG, "onQueryTextSubmit: " + query);
                if (query != null && query.length() > 0){
//                    text = query;
                    mPresenter.getSearchSong(query);
//                    mLoadView.start();
//                    mLoadView.setVisibility(View.VISIBLE);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(MusicApp.TAG, "onQueryTextChange: " + newText);
                return false;
            }
        });
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
    public SearchPresenter createPresenter() {
        return new SearchPresenter(this, this);
    }

    @Override
    public void showListView(List<SearchSong> songs) {

        if (mLists == null){
            mLists = new ArrayList<>();
        }else {
            mLists.clear();
        }
        mLists.addAll(songs);
        mAdapter = new SearchSongAdapter(this, mLists);
//        if (offset == 0){
//            mAdapter = new SongsAdapter(this, mLists);
//            mListView.setAdapter(mAdapter);
//        mLoadView.stop();
//        mLoadView.setVisibility(View.GONE);
        mFlowLayout.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        mListView.setAdapter(mAdapter);
//        }else {
//            mAdapter.notifyDataSetChanged();
//        }
//        offset += 20;
//        isLoading = false;
//
//        mLoadView_footer.setVisibility(View.GONE);
//        mLoadView_footer.stop();

    }

    @Override
    public void showHotWord(final List<HotWord> words) {

        if (hotWords == null){
            hotWords = new ArrayList<>();
        }else {
            hotWords.clear();
        }
        for (HotWord word : words){
            hotWords.add(word.getWord());
        }
        mFlowLayout.setAdapter(new TagAdapter<String>(hotWords) {
            @Override
            public View getView(FlowLayout parent, int position, String hotWord) {
                TextView textView = (TextView) LayoutInflater.from(SearchActivity.this)
                        .inflate(R.layout.flowlayout_tv, parent, false);
                textView.setTextSize(DensityUtils.px2sp(SearchActivity.this, 40));
                textView.setText(hotWord);
                return textView;
            }
        });
    }
}
