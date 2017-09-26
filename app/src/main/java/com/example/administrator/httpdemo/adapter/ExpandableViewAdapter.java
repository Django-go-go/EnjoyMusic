package com.example.administrator.httpdemo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.httpdemo.CustomView.CustomDialog;
import com.example.administrator.httpdemo.CustomView.CustomDialogAdapter;
import com.example.administrator.httpdemo.Data.entity.CollectSongList;
import com.example.administrator.httpdemo.Data.entity.CreateSongList;
import com.example.administrator.httpdemo.Data.entity.MySharedSongs;
import com.example.administrator.httpdemo.Data.entity.Song2;
import com.example.administrator.httpdemo.Data.entity.SongList;
import com.example.administrator.httpdemo.Data.remote.BaseNetData;
import com.example.administrator.httpdemo.Listener.HttpListener;
import com.example.administrator.httpdemo.Listener.ListDialogListener;
import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.fragment.dialogfragment.CreateSongListDialogFragment;
import com.example.administrator.httpdemo.fragment.dialogfragment.DeleteFragment;
import com.example.administrator.httpdemo.Utils.OtherUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/7/25.
 */

public class ExpandableViewAdapter extends BaseExpandableListAdapter {

    private String[] group = new String[]{"我的歌单", "收藏的歌单"};

    private List<CreateSongList> child1 = new ArrayList<>();
    private List<CollectSongList> child2 = new ArrayList<>();

    private Context context;
    private LayoutInflater mInflater;

    private ListDialogListener mListener = new ListDialogListener() {
        @Override
        public void createListDialog(ListView listView) {

        }
    };

    public ExpandableViewAdapter(Context context, List<CreateSongList> list1, List<CollectSongList> list2) {
        this.context = context;
        child1 = list1;
        child2 = list2;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return group.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 0){
            return child1.size();
        }
        if (groupPosition == 1){
            return child2.size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if(groupPosition == 0){
            return child1.get(childPosition);
        }
        if(groupPosition == 1){
            return child2.get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return (groupPosition+1) * (childPosition + 1);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {
        ParentViewHolder pVH;
        if(convertView == null){
            pVH = new ParentViewHolder();
            convertView = mInflater.inflate(R.layout.expand_group, parent, false);
            pVH.tv_title = (TextView) convertView.findViewById(R.id.expand_tv_title);
            pVH.iv_action = (ImageView) convertView.findViewById(R.id.expand_iv);
            convertView.setTag(pVH);
        }else {
            pVH = (ParentViewHolder) convertView.getTag();
        }

        pVH.tv_title.setText(group[groupPosition]);
        if (groupPosition == 1){
            pVH.iv_action.setVisibility(View.GONE);
        }else {
            pVH.iv_action.setVisibility(View.VISIBLE);
        }
        pVH.iv_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (groupPosition == 0){
                    CustomDialog dialog = CustomDialogAdapter.createDialog(context, group[groupPosition], new ListDialogListener() {
                        @Override
                        public void createListDialog(ListView listView) {
                            final FragmentActivity activity = (FragmentActivity) context;
                            List<Integer> icons = new ArrayList<>();
                            icons.add(R.drawable.ic_create);
                            List<String> titles = new ArrayList<>();
                            titles.add("新建歌单");
                            listView.setAdapter(new ItemListDialogAdapter(context, icons, titles));
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (position == 0) {
                                        CreateSongListDialogFragment dialogFragment = new CreateSongListDialogFragment();
                                        dialogFragment.show(activity.getSupportFragmentManager(), "CreateSongListDialogFragment");
                                    }
                                }
                            });
                        }
                    });
                    dialog.build().show();
                }
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder cVH;
        if(convertView == null){
            cVH = new ChildViewHolder();
            convertView = mInflater.inflate(R.layout.expand_child, parent, false);
            cVH.iv_action = (ImageView) convertView.findViewById(R.id.iv_action);
            cVH.iv_album = (ImageView) convertView.findViewById(R.id.iv_album);
            cVH.tv_name = (TextView) convertView.findViewById(R.id.tv_songlistName);
            cVH.tv_num = (TextView) convertView.findViewById(R.id.tv_songlistNum);
            convertView.setTag(cVH);
        }else {
            cVH = (ChildViewHolder) convertView.getTag();
        }

        cVH.iv_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentActivity activity = (FragmentActivity) context;
                DeleteFragment deleteFragment = new DeleteFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("group", groupPosition);
                bundle.putInt("child", childPosition);
                deleteFragment.setArguments(bundle);
                deleteFragment.show(activity.getSupportFragmentManager(), "deleteFragment");
            }
        });

        if (groupPosition == 0){
            final CreateSongList songList = child1.get(childPosition);
            cVH.tv_name.setText(songList.getName());

            cVH.tv_num.setText(String.valueOf(OtherUtils.getNumber(songList)) + " 首");
            cVH.iv_action.setVisibility(View.VISIBLE);

            if (songList.getImageURL() != null){
                Picasso.with(context).load(songList.getImageURL()).into(cVH.iv_album);
            }else if (songList.getLocalSongIds() != null && songList.getLocalSongIds().size() != 0){
                Picasso.with(context).load("content://media/external/audio/albumart" + "/"
                        + OtherUtils.getAlumIdForID(context, songList.getLocalSongIds().get(0)))
                        .into(cVH.iv_album);
            }else if (songList.getNetSongIds() != null && songList.getNetSongIds().size() != 0){
                new BaseNetData(context).getSong2(songList.getNetSongIds().get(0), new HttpListener<Song2>() {
                    @Override
                    public void onResponse(Call<Song2> call, Response<Song2> response) {
                        String url = response.body().getPic_big();
                        Picasso.with(context).load(url).into(cVH.iv_album);
                        CreateSongList createSongList = new CreateSongList();
                        createSongList.setImageURL(url);
                        createSongList.update(songList.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e != null){
                                    Log.e(MusicApp.TAG, "done: ", e);
                                }
                            }
                        });
                    }
                    @Override
                    public void onFailure(Call<Song2> call, Throwable t) {
                    }
                });
            } else if (songList.getBmobSongIds() != null && songList.getBmobSongIds().size() != 0){
                new BmobQuery<MySharedSongs>().getObject(songList.getBmobSongIds().get(0), new QueryListener<MySharedSongs>() {
                    @Override
                    public void done(MySharedSongs sharedSongs, BmobException e) {
                        if (e == null){
                            Picasso.with(context).load(sharedSongs.getAlbum()).into(cVH.iv_album);
                            CreateSongList createSongList = new CreateSongList();
                            createSongList.setImageURL(sharedSongs.getAlbum());
                            createSongList.update(songList.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e != null){
                                        Log.e(MusicApp.TAG, "done: ", e);
                                    }
                                }
                            });
                        }
                    }
                });
            }else {
                cVH.iv_album.setImageResource(R.drawable.music);
            }

        }
        if(groupPosition == 1){
            CollectSongList songList = child2.get(childPosition);
            cVH.tv_name.setText(songList.getName());
            cVH.tv_num.setText(String.valueOf(songList.getCount()) + " 首");
            Picasso.with(context).load(songList.getImageCoverUrl()).into(cVH.iv_album);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ParentViewHolder{
        TextView tv_title;
        ImageView iv_action;
    }

    private class ChildViewHolder{
        TextView tv_name;
        TextView tv_num;
        ImageView iv_album;
        ImageView iv_action;
    }
}
