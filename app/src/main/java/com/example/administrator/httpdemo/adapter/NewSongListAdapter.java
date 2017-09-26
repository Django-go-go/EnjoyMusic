package com.example.administrator.httpdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.httpdemo.Data.entity.SongList;
import com.example.administrator.httpdemo.Data.entity.SongList2;
import com.example.administrator.httpdemo.Data.remote.BaseNetData;
import com.example.administrator.httpdemo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */

public class NewSongListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<SongList2> mList;

    public NewSongListAdapter(Context context, List<SongList2> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH vh;
        if(convertView == null){
            vh = new VH();
            convertView = mInflater.inflate(R.layout.item_list_new, parent, false);
            vh.imageView = (ImageView) convertView.findViewById(R.id.new_imageView);
            vh.textView_name = (TextView) convertView.findViewById(R.id.new_tv_name);
            vh.textView_title = (TextView) convertView.findViewById(R.id.new_tv_title);
            vh.textView_desc = (TextView) convertView.findViewById(R.id.new_tv_desc);
            convertView.setTag(vh);
        }else {
            vh = (VH) convertView.getTag();
        }

        vh.textView_name.setText("by " + mList.get(position).getTag());
        vh.textView_title.setText(mList.get(position).getTitle());
        vh.textView_desc.setText(mList.get(position).getDesc());
        Picasso.with(mContext)
                .load(mList.get(position).getPic_300())
                .placeholder(R.drawable.bg)
                .into(vh.imageView);
        return convertView;
    }

    private class VH {
        ImageView imageView;
        TextView textView_title;
        TextView textView_name;
        TextView textView_desc;
    }
}
