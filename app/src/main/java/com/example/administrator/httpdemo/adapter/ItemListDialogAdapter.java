package com.example.administrator.httpdemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/25.
 */

public class ItemListDialogAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Integer> icons = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    public ItemListDialogAdapter(Context context, List<Integer> icons, List<String> titles) {
        mContext = context;
        this.icons = icons;
        this.titles = titles;
        mInflater = LayoutInflater.from(mContext);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_list_dialog, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView_icon.setVisibility(View.VISIBLE);
        viewHolder.imageView_icon.setImageResource(icons.get(position));
        viewHolder.textView.setText(titles.get(position));

        return convertView;
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
        }
    }
}
