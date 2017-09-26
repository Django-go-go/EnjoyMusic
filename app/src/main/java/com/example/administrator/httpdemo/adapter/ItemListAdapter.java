package com.example.administrator.httpdemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.httpdemo.Data.remote.BaseNetData;
import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */

public class ItemListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> titles = new ArrayList<>();

    private int isPlayPos = -1;

    public ItemListAdapter(Context context, List<String> titles, int isPlayPos) {
        mContext = context;
        this.titles = titles;
        mInflater = LayoutInflater.from(mContext);
        this.isPlayPos = isPlayPos;
    }

    public void setIsPlayPos(int isPlayPos) {
        this.isPlayPos = isPlayPos;
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return titles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_list_dialog, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(titles.get(position));
        viewHolder.imageView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.deleteSong(position);
                }
            }
        });
        if (isPlayPos == position){
            viewHolder.imageView_icon.setVisibility(View.VISIBLE);
            viewHolder.textView.setTextColor(Color.RED);
        }else {
            viewHolder.imageView_icon.setVisibility(View.GONE);
            viewHolder.textView.setTextColor(Color.BLACK);
        }
        return convertView;
    }

    private DeleteSongListener mListener;

    public void setListener(DeleteSongListener listener) {
        mListener = listener;
    }

    public interface DeleteSongListener{
        void deleteSong(int pos);
    }

    private class ViewHolder{
        ImageView imageView_icon;
        TextView textView;
        ImageView imageView_delete;

        public ViewHolder(View convertView) {
            imageView_icon = (ImageView) convertView.findViewById(R.id.item_dialog_icon);
            textView = (TextView) convertView.findViewById(R.id.item_dialog_titles);
            textView.setTextSize(DensityUtils.sp2px(mContext, 6));
            imageView_delete = (ImageView) convertView.findViewById(R.id.item_dialog_delete);

            imageView_icon.setImageResource(R.drawable.song_play_icon);
            imageView_delete.setImageResource(R.drawable.ic_close_black_24dp);
            imageView_delete.setVisibility(View.VISIBLE);
        }
    }
}
