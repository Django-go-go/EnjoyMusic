package com.example.administrator.httpdemo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.httpdemo.CustomView.CustomDialog;
import com.example.administrator.httpdemo.CustomView.CustomDialogAdapter;
import com.example.administrator.httpdemo.Data.entity.CreateSongList;
import com.example.administrator.httpdemo.Data.entity.MySharedSongs;
import com.example.administrator.httpdemo.Listener.ListDialogListener;
import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.Other.Constant;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Service.DownloadIntentService;
import com.example.administrator.httpdemo.Utils.FileUtils;
import com.example.administrator.httpdemo.Utils.StorageUtils;
import com.example.administrator.httpdemo.Utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/8/21.
 */

public class MyShareSongAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<MySharedSongs> mSongs;

    private CustomDialog mDialog_collect;
    private CustomDialog mDialog_main;

    private int type;

    public MyShareSongAdapter(Context context, List<MySharedSongs> songs, int type) {
        mContext = context;
        mSongs = songs;
        mInflater = LayoutInflater.from(mContext);
        this.type = type;
    }

    @Override
    public int getCount() {
        return mSongs.size();
    }

    @Override
    public Object getItem(int position) {
        return mSongs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_song, null);
            viewHolder.iv_action = (ImageView) convertView.findViewById(R.id.action);
            viewHolder.iv_trumpet = (ImageView) convertView.findViewById(R.id.trumpet);
            viewHolder.tv_singer = (TextView) convertView.findViewById(R.id.singer);
            viewHolder.tv_position = (TextView) convertView.findViewById(R.id.position);
            viewHolder.tv_songName = (TextView) convertView.findViewById(R.id.songName);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final MySharedSongs song = mSongs.get(position);
        String name = mSongs.get(position).getName();
        String singer = mSongs.get(position).getArtist();
        viewHolder.tv_singer.setText(singer);
        viewHolder.tv_songName.setText(name);
        viewHolder.tv_position.setText(String.valueOf(position+1));
        viewHolder.tv_position.setVisibility(View.VISIBLE);
        if (type == 1){
            viewHolder.iv_action.setVisibility(View.GONE);
        }else {
            viewHolder.iv_action.setVisibility(View.VISIBLE);
        }
        viewHolder.iv_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog_main = CustomDialogAdapter.createDialog(mContext, mSongs.get(position).getName(), new ListDialogListener() {
                    @Override
                    public void createListDialog(ListView listView) {
                        List<Integer> icons = new ArrayList<>();
                        icons.add(R.drawable.ic_collect);
                        icons.add(R.drawable.ic_download);
                        List<String> titles = new ArrayList<>();
                        titles.add("收藏到歌单");
                        titles.add("下载");
                        listView.setAdapter(new ItemListDialogAdapter(mContext, icons, titles));
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                                if (pos == 0){
                                    mDialog_main.dismiss();
                                    mDialog_collect = CustomDialogAdapter.createDialog(mContext, "收藏到歌单", new ListDialogListener() {
                                        @Override
                                        public void createListDialog(ListView listView) {
                                            listView.setAdapter(new CreateSongListAdapter(mContext, Constant.sCreateSongLists));
                                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, final int pos, final long id) {
                                                    CreateSongList createSongList = Constant.sCreateSongLists.get(pos);
                                                    BmobQuery<CreateSongList> bmobQuery = new BmobQuery<>();
                                                    bmobQuery.getObject(createSongList.getObjectId(), new QueryListener<CreateSongList>() {
                                                        @Override
                                                        public void done(CreateSongList createSongList, BmobException e) {
                                                            if (e == null){
                                                                List<String> ids = createSongList.getBmobSongIds();
                                                                if (ids == null){
                                                                    ids = new ArrayList<>();
                                                                }
                                                                ids.add(String.valueOf(mSongs.get(position).getObjectId()));
                                                                createSongList.setBmobSongIds(ids);
                                                                createSongList.update(createSongList.getObjectId(), new UpdateListener() {
                                                                    @Override
                                                                    public void done(BmobException e) {
                                                                        if (e != null){
                                                                            ToastUtils.showShort(mContext, "收藏失败");
                                                                        }else {
                                                                            ToastUtils.showShort(mContext, "收藏成功");
                                                                            EventBus.getDefault().postSticky(1);
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });
                                                    mDialog_collect.dismiss();
                                                }
                                            });
                                        }
                                    });
                                    mDialog_collect.setGravity(Gravity.CENTER).setDefaultContentHeight(mDialog_collect.getDefaultContentHeight()).build().show();
                                }else {
                                    mDialog_main.dismiss();
                                    DownloadIntentService.startDownloadBmob(mContext, song.getUrl(), song.getAlbum());
                                }
                            }
                        });
                    }
                });
                mDialog_main.setGravity(Gravity.BOTTOM).build().show();
            }
        });
        return convertView;
    }

    private class ViewHolder{
        TextView tv_songName;
        TextView tv_singer;
        ImageView iv_action;
        TextView tv_position;
        ImageView iv_trumpet;
    }
}
