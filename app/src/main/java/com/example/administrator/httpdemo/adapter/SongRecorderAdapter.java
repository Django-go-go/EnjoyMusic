package com.example.administrator.httpdemo.adapter;

import android.content.Context;
import android.net.Uri;
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
import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.Data.entity.MySharedSongs;
import com.example.administrator.httpdemo.Data.entity.Song;
import com.example.administrator.httpdemo.Data.entity.SongRecorder;
import com.example.administrator.httpdemo.Event.UpdateEvent;
import com.example.administrator.httpdemo.Listener.ListDialogListener;
import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.Other.Constant;
import com.example.administrator.httpdemo.R;

import java.util.List;


/**
 * Created by Administrator on 2017/9/3.
 */

public class SongRecorderAdapter extends BaseAdapter{
    private List<SongRecorder> mSongRecorders;
    private Context mContext;
    private LayoutInflater mInflater;
    private CustomDialog mDialog_main;
    private CustomDialog mDialog_collect;

    public SongRecorderAdapter(List<SongRecorder> songRecorders, Context context) {
        mSongRecorders = songRecorders;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mSongRecorders.size();
    }

    @Override
    public Object getItem(int position) {
        return mSongRecorders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_song, parent, false);
            viewHolder.iv_pos = (TextView) convertView.findViewById(R.id.position);
            viewHolder.tv_singer = (TextView) convertView.findViewById(R.id.singer);
            viewHolder.tv_songName = (TextView) convertView.findViewById(R.id.songName);
            viewHolder.iv_action = (ImageView) convertView.findViewById(R.id.action);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final SongRecorder songRecorder = (SongRecorder) getItem(position);
        viewHolder.tv_songName.setText(songRecorder.getTitle());
        viewHolder.tv_singer.setText(songRecorder.getArtist());
        viewHolder.iv_pos.setText("" + position);
        viewHolder.iv_pos.setVisibility(View.VISIBLE);
        viewHolder.iv_action.setVisibility(View.INVISIBLE);

//        viewHolder.iv_action.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDialog_main = CustomDialogAdapter.createDialog(mContext, mp3Info.getTitle(), new ListDialogListener() {
//                    @Override
//                    public void createListDialog(ListView listView) {
//                        List<Integer> icons = new ArrayList<>();
//                        icons.add(R.drawable.ic_queue_music_black_24dp);
//                        icons.add(R.drawable.ic_queue_music_black_24dp);
//                        icons.add(R.drawable.ic_queue_music_black_24dp);
//                        List<String> titles = new ArrayList<>();
//                        titles.add("收藏到歌单");
//                        titles.add("分享");
//                        titles.add("删除");
//                        listView.setAdapter(new ItemListDialogAdapter(mContext, icons, titles, ItemListDialogAdapter.DIALOG_1));
//                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                if (position == 0) {
//                                    mDialog_collect = CustomDialogAdapter.createDialog(mContext, "收藏到歌单", new ListDialogListener() {
//                                        @Override
//                                        public void createListDialog(ListView listView) {
//                                            listView.setAdapter(new CreateSongListAdapter(mContext, Constant.sCreateSongLists));
//                                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                                @Override
//                                                public void onItemClick(AdapterView<?> parent, View view, final int pos, final long id) {
//                                                    CreateSongList createSongList = Constant.sCreateSongLists.get(pos);
//                                                    BmobQuery<CreateSongList> bmobQuery = new BmobQuery<>();
//                                                    bmobQuery.getObject(createSongList.getObjectId(), new QueryListener<CreateSongList>() {
//                                                        @Override
//                                                        public void done(CreateSongList createSongList, BmobException e) {
//                                                            if (e == null){
//                                                                List<String> ids = createSongList.getLocalSongIds();
//                                                                if (ids == null){
//                                                                    ids = new ArrayList<>();
//                                                                }
//                                                                ids.add(String.valueOf(mp3Info.getId()));
//                                                                createSongList.setLocalSongIds(ids);
//                                                                createSongList.update(createSongList.getObjectId(), new UpdateListener() {
//                                                                    @Override
//                                                                    public void done(BmobException e) {
//                                                                        if (e != null){
//                                                                            ToastUtils.showShort(mContext, "收藏失败");
//                                                                        }else {
//                                                                            ToastUtils.showShort(mContext, "收藏成功");
//                                                                            EventBus.getDefault().postSticky(new UpdateEvent(1));
//                                                                        }
//                                                                    }
//                                                                });
//                                                            }
//                                                        }
//                                                    });
//                                                    mDialog_collect.dismiss();
//                                                }
//                                            });
//                                        }
//                                    });
//
//                                    mDialog_main.dismiss();
//                                    mDialog_collect.setGravity(Gravity.CENTER).setDefaultContentHeight(mDialog_collect.getDefaultContentHeight()).build().show();
//                                } else if (position == 1) {
//                                    final MySharedSongs sharedSongs = new MySharedSongs();
//                                    sharedSongs.setName(mp3Info.getTitle());
//                                    sharedSongs.setArtist(mp3Info.getArtist());
//                                    sharedSongs.setAlbum(ContentUriFToFilePathUtils.getPath(mContext,
//                                            Uri.parse("content://media/external/audio/albumart" + "/"
//                                                    + mp3Info.getAlbumId())));
//                                    sharedSongs.setUrl(mp3Info.getUrl());
//                                    sharedSongs.save(new SaveListener<String>() {
//                                        @Override
//                                        public void done(String s, BmobException e) {
//                                            if (e != null) {
//                                                Log.i(MusicApp.TAG, "save===========> " + e.toString());
//                                            } else {
//                                                final BmobFile pathFile = new BmobFile(new File(sharedSongs.getUrl()));
//                                                final BmobFile albumFile = new BmobFile(new File(sharedSongs.getAlbum()));
//                                                pathFile.upload(new UploadFileListener() {
//                                                    @Override
//                                                    public void done(BmobException e) {
//                                                        if (e != null) {
//                                                            Log.i(MusicApp.TAG, "upload===========> " + e.toString());
//                                                        } else {
//                                                            sharedSongs.setUrl(pathFile.getFileUrl());
//                                                            sharedSongs.update(sharedSongs.getObjectId(), new UpdateListener() {
//                                                                @Override
//                                                                public void done(BmobException e) {
//                                                                    if (e != null) {
//                                                                        Log.i(MusicApp.TAG, "update===========> " + e.toString());
//                                                                    }
//                                                                }
//                                                            });
//                                                        }
//                                                    }
//                                                });
//                                                albumFile.upload(new UploadFileListener() {
//                                                    @Override
//                                                    public void done(BmobException e) {
//                                                        if (e != null) {
//                                                            LogUtils.i(e.toString());
//                                                        } else {
//                                                            sharedSongs.setAlbum(albumFile.getFileUrl());
//                                                            sharedSongs.update(sharedSongs.getObjectId(), new UpdateListener() {
//                                                                @Override
//                                                                public void done(BmobException e) {
//                                                                    if (e != null) {
//                                                                        Log.i(MusicApp.TAG, "albumFile update===========> " + e.toString());
//                                                                    }
//                                                                }
//                                                            });
//                                                        }
//                                                    }
//                                                });
//                                            }
//                                        }
//                                    });
//                                    mDialog_main.dismiss();
//                                } else {
//                                    String path = ContentUriFToFilePathUtils.getPath(mContext,
//                                            Uri.parse("content://media/external/audio/albumart" + "/" + mp3Info.getAlbumId()));
//                                    ToastUtils.showShort(mContext, mp3Info.getUrl());
//                                    if (FileUtils.deleteFile(new File(path)) && FileUtils.deleteFile(new File(mp3Info.getUrl()))){
//                                        ToastUtils.showShort(mContext, "删除成功");
//                                    }
//                                    mDialog_main.dismiss();
//                                }
//                            }
//                        });
//                    }
//                });
//                mDialog_main.build().show();
//            }
//        });
        return convertView;
    }

    private class ViewHolder{
        TextView tv_songName;
        TextView tv_singer;
        TextView iv_pos;
        ImageView iv_action;
    }
}
