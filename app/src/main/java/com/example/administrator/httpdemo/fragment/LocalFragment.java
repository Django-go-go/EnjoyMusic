package com.example.administrator.httpdemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.example.administrator.httpdemo.CustomView.MyExpandableView;
import com.example.administrator.httpdemo.Data.entity.CollectSongList;
import com.example.administrator.httpdemo.Data.entity.CreateSongList;
import com.example.administrator.httpdemo.Data.entity.MyUser;
import com.example.administrator.httpdemo.Data.entity.SongList;
import com.example.administrator.httpdemo.Data.entity.SongList2;
import com.example.administrator.httpdemo.Event.UpdateEvent;
import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.Other.Constant;
import com.example.administrator.httpdemo.Utils.ToastUtils;
import com.example.administrator.httpdemo.activity.MainActivity;
import com.example.administrator.httpdemo.activity.SongListActivity;
import com.example.administrator.httpdemo.adapter.ExpandableViewAdapter;
import com.example.administrator.httpdemo.adapter.FolderRecyclerAdapter;
import com.example.administrator.httpdemo.fragment.base.BaseFragment;
import com.example.administrator.httpdemo.fragment.presenter.LocalFraPresenter;
import com.example.administrator.httpdemo.fragment.view.LocalFraView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class LocalFragment extends BaseFragment implements LocalFraView {

    private RecyclerView mRecyclerView;
    private FolderRecyclerAdapter mAdapter;

    private MyExpandableView mExpandableView;
    private ExpandableViewAdapter mExpandAdapter;

    private LocalFraPresenter mPresenter;

    private List<CreateSongList> mCreateSongList = new ArrayList<>();
    private List<CollectSongList> mCollectSongList = new ArrayList<>();

    public LocalFragment() {
    }

    @Override
    protected void onLazyLoadOnce() {
        if (mCollectSongList.size() == 0 || mCreateSongList.size() == 0){
            mPresenter.getCollectSongList();
            mPresenter.getCreateSongList();
            Log.i(MusicApp.TAG, "onLazyLoadOnce: LocalFragment");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        initView();

        mPresenter = new LocalFraPresenter(getContext(), this);

        MainActivity activity = (MainActivity) getActivity();
        activity.setListener(new MainActivity.ToLocalFragmentListener() {
            @Override
            public void setValue(String name) {
                if (name != null){
                    CreateSongList createSongList = new CreateSongList();
                    createSongList.setName(name);
                    createSongList.setUserID(BmobUser.getCurrentUser(MyUser.class).getObjectId());
                    mCreateSongList.add(createSongList);
                    mExpandAdapter = new ExpandableViewAdapter(getContext(), mCreateSongList, mCollectSongList);
                    mAdapter.setExpandableAdapter(mExpandAdapter);
                    expandableView();
                    mPresenter.saveCreateSongList(createSongList);
                }
            }

            @Override
            public void isDelete(int group, int child) {
                if (group == 0){
                    mPresenter.deleteCreateSongList(mCreateSongList.get(child));
                    mCreateSongList.remove(child);
                }else {
                    mPresenter.deleteCollectSongList(mCollectSongList.get(child));
                    mCollectSongList.remove(child);
                }
                mExpandAdapter = new ExpandableViewAdapter(getContext(), mCreateSongList, mCollectSongList);
                mAdapter.setExpandableAdapter(mExpandAdapter);
                expandableView();
            }
        });

        return mRecyclerView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void OnEvent(UpdateEvent event){
        if (mPresenter != null){
            if (event.getUpdatePos() == 1){
                mPresenter.getCreateSongList();
            }else {
                mPresenter.getCollectSongList();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initView(){
        mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));

        mAdapter = new FolderRecyclerAdapter(getContext());
//        mAdapter.setListener(new FolderRecyclerAdapter.RecyclerClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Intent intent = new Intent(getContext(), LocalActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("position", position);
//                bundle.putString("title", mAdapter.getNames()[position]);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });

        mAdapter.setOnChildClickListener(new FolderRecyclerAdapter.ExpandOnChildClickListener() {
            @Override
            public void onChildClick(int groupPos, int childPos) {
                Intent intent = new Intent(getContext(), SongListActivity.class);
                if (groupPos == 0){
                    intent.putExtra("createSongList", mCreateSongList.get(childPos));
                }else if (groupPos == 1){
                    intent.putExtra("collectSongList", mCollectSongList.get(childPos));
                }
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }

    private void expandableView(){
        for(int i = 0; i < 2; i++){
            if(!mExpandableView.isGroupExpanded(i))
                mExpandableView.expandGroup(i);
        }

        mExpandableView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(parent.isGroupExpanded(groupPosition)){
                    parent.collapseGroup(groupPosition);
                }else {
                    parent.expandGroup(groupPosition);
                }
                return true;
            }
        });

        mExpandableView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        mExpandableView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });
    }

    @Override
    public void showExpandableView(List<CreateSongList> list1, List<CollectSongList> list2) {
        if (list1 != null){
            if (mCreateSongList.size() > 0){
                mCreateSongList.clear();
            }
            mCreateSongList.addAll(list1);
        }
        if (list2 != null){
            if (mCollectSongList.size() > 0){
                mCollectSongList.clear();
            }
            mCollectSongList.addAll(list2);
        }

        mExpandAdapter = new ExpandableViewAdapter(getContext(), mCreateSongList, mCollectSongList);

        mAdapter.setExpandableAdapter(mExpandAdapter);
        mExpandableView = mAdapter.getExpandableListView();
        expandableView();
    }

}
