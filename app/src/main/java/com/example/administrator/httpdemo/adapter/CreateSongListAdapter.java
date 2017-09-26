package com.example.administrator.httpdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.httpdemo.Data.entity.CreateSongList;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.OtherUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/8/21.
 */

public class CreateSongListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<CreateSongList> mCreateSongLists;

    public CreateSongListAdapter(Context context, List<CreateSongList> createSongLists) {
        mContext = context;
        mCreateSongLists = createSongLists;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mCreateSongLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mCreateSongLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder cVH;
        if(convertView == null){
            cVH = new ViewHolder();
            convertView = mInflater.inflate(R.layout.expand_child, parent, false);
            cVH.iv_action = (ImageView) convertView.findViewById(R.id.iv_action);
            cVH.iv_album = (ImageView) convertView.findViewById(R.id.iv_album);
            cVH.tv_name = (TextView) convertView.findViewById(R.id.tv_songlistName);
            cVH.tv_num = (TextView) convertView.findViewById(R.id.tv_songlistNum);
            convertView.setTag(cVH);
        }else {
            cVH = (ViewHolder) convertView.getTag();
        }

        cVH.iv_action.setVisibility(View.GONE);

        CreateSongList songList = mCreateSongLists.get(position);
        cVH.tv_name.setText(songList.getName());

        cVH.tv_num.setText(String.valueOf(OtherUtils.getNumber(songList)) + " 首");
        Picasso.with(mContext).load(songList.getImageURL()).error(R.drawable.music).into(cVH.iv_album);

        return convertView;
    }

    private class ViewHolder{
        TextView tv_name;
        TextView tv_num;
        ImageView iv_album;
        ImageView iv_action;
    }
}
