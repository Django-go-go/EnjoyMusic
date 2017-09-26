package com.example.administrator.httpdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.httpdemo.Data.entity.SearchSong;
import com.example.administrator.httpdemo.R;

import java.util.List;

/**
 * Created by Administrator on 2017/9/11.
 */

public class SearchSongAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private List<SearchSong> mSongs;

    public SearchSongAdapter(Context context, List<SearchSong> songs) {
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

        String name = mSongs.get(position).getSongname();
        String singer = mSongs.get(position).getArtistname();

        viewHolder.tv_singer.setText(singer);
        viewHolder.tv_songName.setText(name);
        viewHolder.tv_position.setText(String.valueOf(position+1));
        viewHolder.tv_position.setVisibility(View.VISIBLE);
        viewHolder.iv_action.setVisibility(View.GONE);
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
