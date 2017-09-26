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
import com.example.administrator.httpdemo.Listener.ListDialogListener;
import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.ContentUriFToFilePathUtils;
import com.example.administrator.httpdemo.Utils.LogUtils;
import com.example.administrator.httpdemo.Utils.OtherUtils;
import com.example.administrator.httpdemo.Utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2017/7/20.
 */

public class LocalListAdapter extends BaseAdapter {

    private List<Mp3Info> mMp3Infos;
    private Context mContext;
    private LayoutInflater mInflater;
    private CustomDialog mDialog_main;
    private CustomDialog mDialog_collect;

    public LocalListAdapter(List<Mp3Info> mp3Infos, Context context) {
        mMp3Infos = mp3Infos;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mMp3Infos.size();
    }

    @Override
    public Object getItem(int position) {
        return mMp3Infos.get(position);
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
            convertView = mInflater.inflate(R.layout.item_song, null);
            viewHolder.iv_action = (ImageView) convertView.findViewById(R.id.action);
            viewHolder.tv_singer = (TextView) convertView.findViewById(R.id.singer);
            viewHolder.tv_songName = (TextView) convertView.findViewById(R.id.songName);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Mp3Info mp3Info = (Mp3Info) getItem(position);
        viewHolder.tv_songName.setText(mp3Info.getTitle());
        viewHolder.tv_singer.setText(mp3Info.getArtist());

        viewHolder.iv_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog_main = CustomDialogAdapter.createDialog(mContext, mp3Info.getTitle(), new ListDialogListener() {
                    @Override
                    public void createListDialog(ListView listView) {
                        List<Integer> icons = new ArrayList<>();
                        icons.add(R.drawable.ic_collect);
                        icons.add(R.drawable.ic_share);
                        icons.add(R.drawable.ic_delete);
                        List<String> titles = new ArrayList<>();
                        titles.add("收藏到歌单");
                        titles.add("分享");
                        titles.add("删除");
                        listView.setAdapter(new ItemListDialogAdapter(mContext, icons, titles));
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (position == 0) {
                                    mDialog_collect = CustomDialogAdapter.createDialog(mContext, "收藏到歌单", new ListDialogListener() {
                                        @Override
                                        public void createListDialog(final ListView listView) {
                                            new BmobQuery<CreateSongList>().addWhereEqualTo("userID", OtherUtils.getUserId()).findObjects(new FindListener<CreateSongList>() {
                                                @Override
                                                public void done(final List<CreateSongList> list, BmobException e) {
                                                    if (e == null) {
                                                        listView.setAdapter(new CreateSongListAdapter(mContext, list));
                                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                            @Override
                                                            public void onItemClick(AdapterView<?> parent, View view, final int pos, final long id) {
                                                                CreateSongList createSongList = list.get(pos);
                                                                BmobQuery<CreateSongList> bmobQuery = new BmobQuery<>();
                                                                bmobQuery.getObject(createSongList.getObjectId(), new QueryListener<CreateSongList>() {
                                                                    @Override
                                                                    public void done(CreateSongList createSongList, BmobException e) {
                                                                        if (e == null) {
                                                                            List<String> ids = createSongList.getLocalSongIds();
                                                                            if (ids == null) {
                                                                                ids = new ArrayList<>();
                                                                            }
                                                                            ids.add(String.valueOf(mp3Info.getId()));
                                                                            createSongList.setLocalSongIds(ids);
                                                                            createSongList.update(createSongList.getObjectId(), new UpdateListener() {
                                                                                @Override
                                                                                public void done(BmobException e) {
                                                                                    if (e != null) {
                                                                                        ToastUtils.showShort(mContext, "收藏失败");
                                                                                    } else {
                                                                                        ToastUtils.showShort(mContext, "收藏成功");
                                                                                        EventBus.getDefault().postSticky(1);
                                                                                    }
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                        }
                                    });

                                    mDialog_main.dismiss();
                                    mDialog_collect.setGravity(Gravity.CENTER).setDefaultContentHeight(mDialog_collect.getDefaultContentHeight()).build().show();
                                } else if (position == 1) {
                                    final MySharedSongs sharedSongs = OtherUtils.Mp3InfoToMySharedSongs(mp3Info);
                                    sharedSongs.save(new SaveListener<String>() {
                                        @Override
                                        public void done(final String s, BmobException e) {
                                            if (e != null) {
                                                Log.i(MusicApp.TAG, "save===========> " + e.toString());
                                            } else {

                                                final BmobFile pathFile = new BmobFile(new File(mp3Info.getUrl()));
                                                final BmobFile albumFile = new BmobFile(new File(ContentUriFToFilePathUtils.getPath(mContext, Uri.parse("content://media/external/audio/albumart" + "/" + mp3Info.getAlbumId()))));
                                                pathFile.upload(new UploadFileListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        if (e != null) {
                                                            Log.i(MusicApp.TAG, "upload===========> " + e.toString());
                                                        } else {
                                                            sharedSongs.setUrl(pathFile.getFileUrl());
                                                            sharedSongs.update(s, new UpdateListener() {
                                                                @Override
                                                                public void done(BmobException e) {
                                                                    if (e != null) {
                                                                        Log.i(MusicApp.TAG, "update===========> " + e.toString());
                                                                    }else {
                                                                        ToastUtils.showShort(mContext, "分享成功啦!");
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                                albumFile.upload(new UploadFileListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        if (e != null) {
                                                            LogUtils.i(e.toString());
                                                        } else {
                                                            sharedSongs.setAlbum(albumFile.getFileUrl());
                                                            sharedSongs.update(s, new UpdateListener() {
                                                                @Override
                                                                public void done(BmobException e) {
                                                                    if (e != null) {
                                                                        Log.i(MusicApp.TAG, "albumFile update===========> " + e.toString());
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                    mDialog_main.dismiss();
                                } else {
//                                    String path = ContentUriFToFilePathUtils.getPath(mContext,
//                                            Uri.parse("content://media/external/audio/albumart" + "/" + mp3Info.getAlbumId()));
//                                    ToastUtils.showShort(mContext, mp3Info.getUrl());
//                                    if (FileUtils.deleteFile(new File(path)) && FileUtils.deleteFile(new File(mp3Info.getUrl()))){
//                                        ToastUtils.showShort(mContext, "删除成功");
//                                    }
//                                    mDialog_main.dismiss();
                                }
                            }
                        });
                    }
                });
                mDialog_main.build().show();
            }
        });
        return convertView;
    }

    private class ViewHolder{
        TextView tv_songName;
        TextView tv_singer;
        ImageView iv_action;
    }
}
