package com.example.administrator.httpdemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.httpdemo.Data.entity.SongList;
import com.example.administrator.httpdemo.Data.entity.SongList2;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.DensityUtils;
import com.example.administrator.httpdemo.Utils.ScreenUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/8/6.
 */

public class SongListGridAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<SongList2> mList;

    public SongListGridAdapter(Context context, List<SongList2> list) {
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
        VH vh ;
        int col = ((GridView)parent).getNumColumns();
        int w = ScreenUtils.getScreenWidth(mContext);
        int s = DensityUtils.sp2px(mContext, 15) + DensityUtils.dp2px(mContext, 8);

        if(convertView == null){
            vh = new VH();
            convertView = mInflater.inflate(R.layout.item_grid, parent, false);
            vh.imageView = (ImageView) convertView.findViewById(R.id.grid_iv);
            vh.imageView.setLayoutParams(new LinearLayout.LayoutParams(w/col, w/col));
            vh.textView = (TextView) convertView.findViewById(R.id.grid_tv);
            convertView.setTag(vh);
        }else {
            vh = (VH) convertView.getTag();
        }

        vh.textView.setText(mList.get(position).getTitle());
        if (mList.get(position).getPic_300() != null){
            Picasso.with(mContext)
                    .load(mList.get(position).getPic_300())
                    .placeholder(R.drawable.bg)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .tag(mContext)
                    .resize(w/col, w/col)
                    .centerCrop()
                    .into(vh.imageView);
        }
        if (mList.get(position).getPic() != null){
            Picasso.with(mContext).load(mList.get(position).getPic())
                    .placeholder(R.drawable.bg)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .tag(mContext)
                    .resize(w/col, w/col)
                    .centerCrop()
                    .into(vh.imageView);
        }


        AbsListView.LayoutParams param = new AbsListView.LayoutParams(w/col, w/col+s*2);
        convertView.setLayoutParams(param);
        return convertView;
    }

    private class VH {
        ImageView imageView;
        TextView textView;
    }
}
