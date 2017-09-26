package com.example.administrator.httpdemo.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.fragment.dialogfragment.DeleteSongFragment;

import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */

public class CreateSongAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Mp3Info> mSongs;

    public CreateSongAdapter(Context context, List<Mp3Info> songs) {
        mContext = context;
        mSongs = songs;
        mInflater = LayoutInflater.from(mContext);
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
        String name = mSongs.get(position).getTitle();
        final String singer = mSongs.get(position).getArtist();
        viewHolder.tv_singer.setText(singer);
        viewHolder.tv_songName.setText(name);
        viewHolder.tv_position.setText(String.valueOf(position+1));
        viewHolder.tv_position.setVisibility(View.VISIBLE);
        viewHolder.iv_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = (FragmentActivity) mContext;
                DeleteSongFragment songFragment = new DeleteSongFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("pos", position);
                songFragment.setArguments(bundle);
                songFragment.show(activity.getSupportFragmentManager(), "DeleteSongFragment");
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
