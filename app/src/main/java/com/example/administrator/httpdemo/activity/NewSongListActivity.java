package com.example.administrator.httpdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.httpdemo.CustomView.CustomDialog;
import com.example.administrator.httpdemo.CustomView.LoadView;
import com.example.administrator.httpdemo.Data.entity.SongList;
import com.example.administrator.httpdemo.Data.entity.SongList2;
import com.example.administrator.httpdemo.Data.remote.BaseNetData;
import com.example.administrator.httpdemo.Listener.HttpListener;
import com.example.administrator.httpdemo.Other.Constant;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.DensityUtils;
import com.example.administrator.httpdemo.Utils.ScreenUtils;
import com.example.administrator.httpdemo.Utils.ToastUtils;
import com.example.administrator.httpdemo.adapter.NewSongListAdapter;
import com.example.administrator.httpdemo.imagePicker.ImagePickerActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/24.
 */

public class NewSongListActivity extends AppCompatActivity {

    private ListView mListView;
//    private LoadView mLoadView;
//    private boolean isLoading = false;
//    private int total = 0;
//    private int offset = 0;

    private String tag = "华语";

    private TextView mTextView;
    private NewSongListAdapter mAdapter;
    private List<SongList2> mLists = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());

        getData("华语");

//        getData();
//        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
//                        && total-1 == view.getLastVisiblePosition() && !isLoading){
//                    isLoading = true;
//                    getData();
//                    mLoadView.setVisibility(View.VISIBLE);
//                    mLoadView.start();
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                total = totalItemCount;
//            }
//        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mLists.get(position) != null){
                    Intent intent = new Intent(NewSongListActivity.this, SongListActivity.class);
                    intent.putExtra("songlist2", mLists.get(position));
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    private LinearLayout initLayout(){
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        mListView = new ListView(this);
        mListView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

//        mLoadView = new LoadView(this);
//        mLoadView.setVisibility(View.GONE);
//        mLoadView.setMinimumHeight(100);
//        mLoadView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
//
//        mListView.addFooterView(mLoadView);
//        mListView.setAdapter(null);

        layout.addView(initToolbar());
        layout.addView(mListView);
        return layout;
    }

    private Toolbar initToolbar(){
        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar, null);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        mTextView = (TextView) toolbar.findViewById(R.id.toolbar_tv1);
        mTextView.setText("分类歌单*" + tag);
        ImageView imageView = (ImageView) toolbar.findViewById(R.id.toolbar_iv);
        imageView.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_classify:
                        startActivityForResult(new Intent(NewSongListActivity.this, ClassifyActivity.class), 1);
                        break;
                }
                return true;
            }
        });
        return toolbar;
    }

    private void getData(String tag){
        this.tag = tag;
        new BaseNetData(this).getTagSongList(tag, new HttpListener<List<SongList2>>() {
            @Override
            public void onResponse(Call<List<SongList2>> call, Response<List<SongList2>> response) {
                if (mLists == null){
                    mLists = new ArrayList<>();
                }else {
                    mLists.clear();
                }
                mLists.addAll(response.body());
                mAdapter = new NewSongListAdapter(NewSongListActivity.this, mLists);
                mListView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<List<SongList2>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my, menu);
        menu.getItem(2).setVisible(true);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == 2){
                tag = data.getStringExtra("tag");
                getData(tag);
                mTextView.setText("分类歌单*" + tag);
            }
        }
    }

    //    public void getData(){
//        try {
//            new BaseNetData(this).getSongListForList("all", "new", offset, true, 15, new HttpListener<List<SongList>>() {
//                @Override
//                public void onResponse(Call<List<SongList>> call, Response<List<SongList>> response) {
//                    mLists.addAll(response.body());
//                    if (offset == 0){
//                        mAdapter = new NewSongListAdapter(NewSongListActivity.this, mLists);
//                        mListView.setAdapter(mAdapter);
//                    }else {
//                        mAdapter.notifyDataSetChanged();
//                    }
//                    offset += 15;
//                    isLoading = false;
//                    mLoadView.stop();
//                    mLoadView.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onFailure(Call<List<SongList>> call, Throwable t) {
//
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
