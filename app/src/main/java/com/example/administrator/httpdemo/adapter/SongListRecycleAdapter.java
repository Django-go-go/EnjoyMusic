package com.example.administrator.httpdemo.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.httpdemo.CustomView.LoadView;
import com.example.administrator.httpdemo.Data.entity.SongList2;
import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.DensityUtils;
import com.example.administrator.httpdemo.Utils.ScreenUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/9/13.
 */

public class SongListRecycleAdapter extends Adapter {

    private Typeface mTypeface;

    private LayoutInflater mInflater;
    private Context mContext;
    private List<SongList2> mList;

    private static final int TYPE_HEAD = 1;
    private static final int TYPE_CONTENT = 2;
    private static final int TYPE_FOOT = 3;

    int col = 2;
    int w;
    int s;

    public SongListRecycleAdapter(Context context, List<SongList2> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
        mTypeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/girl.ttf");

        w = ScreenUtils.getScreenWidth(mContext);
        s = DensityUtils.sp2px(mContext, 15) + DensityUtils.dp2px(mContext, 8);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        switch (viewType){
            case TYPE_HEAD:
                viewHolder = new HeadViewHolder(mInflater.inflate(R.layout.recycle_songlist, parent, false));
                break;
            case TYPE_CONTENT:
                View convertView = mInflater.inflate(R.layout.item_grid, parent, false);
                RecyclerView.LayoutParams param = new RecyclerView.LayoutParams(w/col, w/col+s*2);
                convertView.setLayoutParams(param);
                viewHolder = new ContentViewHolder(convertView);
                break;
            case TYPE_FOOT:
                viewHolder = new FootViewHolder(new LoadView(mContext));
                break;
            default:
                viewHolder = null;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeadViewHolder){
            HeadViewHolder headViewHolder = (HeadViewHolder) holder;
            headViewHolder.mTextView.setTypeface(mTypeface);
            headViewHolder.mTextView.setText("来呀,来呀!");
            headViewHolder.mTextView.setTextColor(Color.RED);
            headViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mHeadOnClickListener != null){
                        mHeadOnClickListener.headOnClick(v);
                    }
                }
            });

        }
        if (holder instanceof ContentViewHolder){
            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            contentViewHolder.mTextView.setText(mList.get(position-1).getTitle());

            if (mList.get(position-1).getPic_300() != null){
                Picasso.with(mContext)
                        .load(mList.get(position-1).getPic_300())
                        .placeholder(R.drawable.bg)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .tag(mContext)
                        .resize(w/col, w/col)
                        .centerCrop()
                        .into(contentViewHolder.mImageView);
            }
            if (mList.get(position-1).getPic() != null){
                Picasso.with(mContext).load(mList.get(position-1).getPic())
                        .placeholder(R.drawable.bg)
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                        .tag(mContext)
                        .resize(w/col, w/col)
                        .centerCrop()
                        .into(contentViewHolder.mImageView);
            }

            contentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContentOnClickListener != null){
                        mContentOnClickListener.contentOnClick(position - 1);
                    }
                }
            });

        }

        if (holder instanceof FootViewHolder){
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
            GridLayoutManager manager = (GridLayoutManager) layoutManager;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == TYPE_HEAD
                            || getItemViewType(position) == TYPE_FOOT){
                        return 2;
                    }
                    return 1;
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_HEAD;
        }else if (position == mList.size() + 1){
            return TYPE_FOOT;
        }else {
            return TYPE_CONTENT;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1 + 1;
    }

    private HeadOnClickListener mHeadOnClickListener;

    private ContentOnClickListener mContentOnClickListener;

    public void setContentOnClickListener(ContentOnClickListener contentOnClickListener) {
        mContentOnClickListener = contentOnClickListener;
    }

    public void setHeadOnClickListener(HeadOnClickListener headOnClickListener) {
        mHeadOnClickListener = headOnClickListener;
    }

    public interface HeadOnClickListener{
        void headOnClick(View view);
    }

    public interface ContentOnClickListener{
        void contentOnClick(int pos);
    }

    private class HeadViewHolder extends RecyclerView.ViewHolder{

        TextView mTextView;

        public HeadViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.classfiy_tv);
            final LinearLayout layout = (LinearLayout) itemView.findViewById(R.id.new_linear);
            Palette.from(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.classfiybg))
                    .generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            int population = 0;
                            float[] hsv = new float[3];
                            List<Palette.Swatch> swatches = palette.getSwatches();
                            for (Palette.Swatch swatch : swatches) {
                                if (swatch != null) {
                                    if (swatch.getPopulation() > population) {
                                        population = swatch.getPopulation();
                                        hsv = swatch.getHsl();
                                    }
                                }
                            }

                            layout.setBackgroundColor(Color.HSVToColor(hsv));
                        }
                    });
        }
    }

    private class ContentViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mTextView;

        public ContentViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.grid_iv);
            mImageView.setLayoutParams(new LinearLayout.LayoutParams(w/col, w/col));
            mTextView = (TextView) itemView.findViewById(R.id.grid_tv);
        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder{
        LoadView mLoadView;
        public FootViewHolder(View itemView) {
            super(itemView);
            mLoadView = (LoadView) itemView;
            mLoadView.setMinimumHeight(100);
            mLoadView.setVisibility(View.VISIBLE);
            mLoadView.start();
            mLoadView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 100));
        }
    }
}
