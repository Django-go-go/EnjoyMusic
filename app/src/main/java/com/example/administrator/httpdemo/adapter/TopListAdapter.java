package com.example.administrator.httpdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.administrator.httpdemo.Data.entity.SongList;
import com.example.administrator.httpdemo.Data.entity.TopList;
import com.example.administrator.httpdemo.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/5.
 */

public class TopListAdapter extends BaseAdapter {
    private onClickedListener mOnClickedListener;
    private Context mContext;
    private LayoutInflater mInflater;
    private String[] name = {"官方榜", "全球榜"};
    private Map<Integer, List<SongList>> all;

    public TopListAdapter(Context context, Map<Integer, List<SongList>> all) {
        mContext = context;
        this.all = all;
        mInflater = LayoutInflater.from(mContext);
    }

    public TopListAdapter() {
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return name[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final int pos = position;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_list_grid, parent, false);
            viewHolder.mGridView = (GridView) convertView.findViewById(R.id.grid_list_grid);
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv_list_grid);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTextView.setText(name[position]);
//        viewHolder.mGridView.setAdapter(new SongListGridAdapter(mContext, all.get(position+1)));
//        viewHolder.mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                SongList list = all.get(pos+1).get(position);
//                if (mOnClickedListener != null){
//                    mOnClickedListener.onClicked(list);
//                }
//            }
//        });
        return convertView;
    }

    public void setOnClickedListener(onClickedListener onClickedListener) {
        mOnClickedListener = onClickedListener;
    }

    private class ViewHolder{
        TextView mTextView;
        GridView mGridView;
    }

    public interface onClickedListener{
        void onClicked(SongList songList);
    }
}
