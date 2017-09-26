package com.example.administrator.httpdemo.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.httpdemo.CustomView.LoadView;
import com.example.administrator.httpdemo.CustomView.MyGridView;
import com.example.administrator.httpdemo.Data.entity.SongList2;
import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.ToastUtils;
import com.example.administrator.httpdemo.activity.NewSongListActivity;
import com.example.administrator.httpdemo.activity.SongListActivity;
import com.example.administrator.httpdemo.adapter.SongListGridAdapter;
import com.example.administrator.httpdemo.adapter.SongListRecycleAdapter;
import com.example.administrator.httpdemo.fragment.base.BaseFragment;
import com.example.administrator.httpdemo.fragment.presenter.SongListFraPresenter;
import com.example.administrator.httpdemo.fragment.view.SongListFraView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/6.
 */

public class SongListFragment extends BaseFragment implements SongListFraView{
    private RecyclerView mRecyclerView;
    private SongListRecycleAdapter mRecycleAdapter;

    private List<SongList2> mLists;

    private SongListFraPresenter mPresenter;

    private int offset = 1;

    public SongListFragment() {
    }

    @Override
    protected void onLazyLoadOnce() {
        if (mLists.size() == 0){
            mPresenter.getSongList(20, offset);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLists = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songlist, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_songlist);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    int y = manager.findLastCompletelyVisibleItemPosition();
                    if (y == mLists.size() + 1){
                        mPresenter.getSongList(20, offset);
                    }
                }
            }
        });

        mPresenter = new SongListFraPresenter(getContext(), this);

        return view;
    }

    @Override
    public void showGridView(List<SongList2> lists) {
        for (SongList2 list : lists){
            mLists.add(list);
        }

        if (offset == 1){
            mRecycleAdapter = new SongListRecycleAdapter(getContext(), mLists);
            mRecyclerView.setAdapter(mRecycleAdapter);
        }else {
            mRecycleAdapter.notifyDataSetChanged();
        }

        offset++;

        mRecycleAdapter.setContentOnClickListener(new SongListRecycleAdapter.ContentOnClickListener() {
            @Override
            public void contentOnClick(int pos) {
                if (mLists.get(pos) != null){
                    Intent intent = new Intent(getContext(), SongListActivity.class);
                    intent.putExtra("songlist2", mLists.get(pos));
                    startActivity(intent);
                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
        mRecycleAdapter.setHeadOnClickListener(new SongListRecycleAdapter.HeadOnClickListener() {
            @Override
            public void headOnClick(View view) {
                startActivity(new Intent(getContext(), NewSongListActivity.class));
            }
        });
    }

//    private void getData(String cat){
//        try {
//            this.cat = cat;
//            mPresenter.getSongList(cat, "hot", offset, true, 10);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void getNewData(){
//        try {
//            mPresenter.getSongList("all", "new", 0, true, 1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void showGridView(List<SongList> lists) {
//        for (SongList list : lists){
//            mLists.add(list);
//        }
//        if (offset == 0){
//            mAdapter = new SongListGridAdapter(getContext(), mLists);
//        }else {
//            mAdapter.notifyDataSetChanged();
//        }
//        offset += 10;
//        isLoading = false;
//        mGridView.setAdapter(mAdapter);
//        mLoadView.setVisibility(View.GONE);
//        mLoadView.stop();
//        contentView.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void showNewData(final List<SongList> lists) {
//        mList = lists.get(0);
//        mTextView2.setText(mList.getName());
//
//        Picasso.with(getContext()).load(mList.getCoverImgUrl()).into(mImageView);
//        mExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    bitmap = Picasso.with(getContext()).load(mList.getCoverImgUrl()).get();
//                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                        @Override
//                        public void onGenerated(Palette palette) {
//                            int population = 0;
//                            hsv = new float[3];
//                            List<Palette.Swatch> swatches = palette.getSwatches();
//                            for (Palette.Swatch swatch : swatches){
//                                if (swatch != null){
//                                    if (swatch.getPopulation() > population){
//                                        population = swatch.getPopulation();
//                                        hsv = swatch.getHsl();
//                                    }
//                                }
//                            }
//
//                            mLayout.setBackgroundColor(Color.HSVToColor(hsv));
//                        }
//                    });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
}
