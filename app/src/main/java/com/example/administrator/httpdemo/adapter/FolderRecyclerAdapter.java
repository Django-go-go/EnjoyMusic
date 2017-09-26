package com.example.administrator.httpdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.administrator.httpdemo.CustomView.MyExpandableView;
import com.example.administrator.httpdemo.Utils.DensityUtils;

/**
 * Created by Administrator on 2017/6/4.
 */

public class FolderRecyclerAdapter extends Adapter {

    private static final int TYPE_LIST = 1;
    private static final int TYPE_EXPANDABLE = 2;
    private RecyclerClickListener mListener;
    private ExpandOnChildClickListener mOnChildClickListener;
    private Context mContext;
    private RecyclerView.LayoutParams layoutParams =
            new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);

    private ViewGroup.LayoutParams layoutParams2 =
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    private int dp;
    private int sp;
    public FolderRecyclerAdapter(Context context){
        mContext = context;
        dp = DensityUtils.dp2px(mContext, 10);
        sp = DensityUtils.sp2px(mContext, 5);
    }

    private int[] icons = {};
    private String[] names = {"本地音乐", "最近播放", "下载管理"};

    public void setListener(RecyclerClickListener listener){
        mListener = listener;
    }

    public void setOnChildClickListener(ExpandOnChildClickListener listener){
        mOnChildClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if(viewType == TYPE_LIST){
//            TextView textView = new TextView(mContext);
//            textView.setLayoutParams(layoutParams2);
//            textView.setGravity(Gravity.CENTER_VERTICAL);
//            textView.setPadding(dp*2, dp, dp, dp);
//            textView.setTextSize(sp);
//            textView.setTextColor(Color.DKGRAY);
//            Drawable drawable = mContext.getResources().getDrawable(R.drawable.ic_queue_music_black_24dp);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            textView.setCompoundDrawables(drawable, null, null, null);
            holder = new ListViewHolder(null);
        } else if (viewType == TYPE_EXPANDABLE){
            expandableListView = new MyExpandableView(mContext);
            expandableListView.setLayoutParams(layoutParams);
            holder = new ExpandableHolder(expandableListView);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ListViewHolder){
//            ListViewHolder listViewHolder = (ListViewHolder) holder;
//            listViewHolder.mTextView.setText(names[position]);
//            if(mListener != null){
//                if (holder.getLayoutPosition() == 0){
//                    listViewHolder.mTextView.setFocusable(true);
//                    listViewHolder.mTextView.setFocusableInTouchMode(true);
//                }
//                listViewHolder.mTextView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mListener.onItemClick(holder.getLayoutPosition());
//                    }
//                });
//            }
        }else if (holder instanceof ExpandableHolder){
            expandableHolder = (ExpandableHolder) holder;
            expandableHolder.mExpandableView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    if (mOnChildClickListener != null){
                        mOnChildClickListener.onChildClick(groupPosition, childPosition);
                    }
                    return true;
                }
            });

        }
    }

    private ExpandableHolder expandableHolder;
    private MyExpandableView expandableListView;
    public MyExpandableView getExpandableListView(){
            return expandableHolder.mExpandableView;
    }
    public void setExpandableAdapter(ExpandableViewAdapter adapter){
         expandableHolder.mExpandableView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return icons.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (icons.length == position){
            return TYPE_EXPANDABLE;
        }else {
            return TYPE_LIST;
        }
    }

    private class ListViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextView;
        public ListViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }
    }

    private class ExpandableHolder extends RecyclerView.ViewHolder{
        private MyExpandableView mExpandableView;
        public ExpandableHolder(View itemView) {
            super(itemView);
            mExpandableView = (MyExpandableView) itemView;
        }
    }

    public int[] getIcons() {
        return icons;
    }

    public String[] getNames() {
        return names;
    }

    public interface RecyclerClickListener{
        void onItemClick(int position);
    }

    public interface ExpandOnChildClickListener{
        void onChildClick(int groupPos, int childPos);
    }
}
