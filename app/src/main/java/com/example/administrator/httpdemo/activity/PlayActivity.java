package com.example.administrator.httpdemo.activity;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.administrator.httpdemo.CustomView.CustomDialog;
import com.example.administrator.httpdemo.CustomView.CustomDialogAdapter;
import com.example.administrator.httpdemo.CustomView.MusicView;
import com.example.administrator.httpdemo.CustomView.PicassoTransform;
import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.Listener.ListDialogListener;
import com.example.administrator.httpdemo.Other.Constant;
import com.example.administrator.httpdemo.PlayMode;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.activity.base.BaseActivity;
import com.example.administrator.httpdemo.activity.presenter.PlayPresenter;
import com.example.administrator.httpdemo.activity.view.PlayView;
import com.example.administrator.httpdemo.adapter.ItemListAdapter;
import com.example.administrator.httpdemo.adapter.ViewPagerScroller;
import com.example.administrator.httpdemo.Utils.ScreenUtils;
import com.example.administrator.httpdemo.Utils.TimeUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PlayActivity extends BaseActivity<PlayView, PlayPresenter> implements View.OnClickListener, PlayView{

    public static final String TAG = "PlayActivity";

    private ImageView mImageView1, mImageView2, mImageView3,
            mImageView4, mImageView5, mImageView_finish;
    private TextView mTextView_start, mTextView_total;
    private TextView mTextView_songname, mTextView_artist;
    private ViewGroup decorView;
    private SeekBar mSeekBar;
    private Toolbar mToolbar;
    
    private ViewPager mViewPager;
    private MusicView mMusicView;
    private SongPagerAdapter mPagerAdapter;
    private List<String> mViewPagerInfos;


    private Map<Integer, Bitmap> cacheBitmapMap = new HashMap<>();
    private ExecutorService mBitmapExecutorService = Executors.newCachedThreadPool();

    private ObjectAnimator objectAnimator;

    private final Handler mHandler = new Handler();
    private Runnable mUpdateTask = new Runnable() {
        @Override
        public void run() {
                if(playService.isRunning()){
                    final int progress = playService.getCurrProgress();
                    final int total = playService.getDuration();
                    mSeekBar.setMax(total);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextView_start.setText(TimeUtils.formatTime((long) progress));
                            mTextView_total.setText(TimeUtils.formatTime((long) total));
                        }
                    });
                    if(progress >= 0 && progress <= mSeekBar.getMax()){
                        mSeekBar.setProgress(progress);
                    }
                }
        }
    };

    private final ScheduledExecutorService mSeekBarExecutorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mScheduledFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        mPresenter = createPresenter();

        ScreenUtils.setTranslucent(PlayActivity.this);

        initView();
        initToolbar();
        initViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        exeBindService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        exeUnbindService();
    }

    @Override
    public PlayPresenter createPresenter() {
        return new PlayPresenter();
    }

    private void initToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.play_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mToolbar.setBackgroundResource(R.color.white_alpha);
        mTextView_songname = (TextView) mToolbar.findViewById(R.id.toolbar_tv1);
        mTextView_artist = (TextView) mToolbar.findViewById(R.id.toolbar_tv2);
        mTextView_artist.setVisibility(View.VISIBLE);
        mImageView_finish = (ImageView) mToolbar.findViewById(R.id.toolbar_iv);
        mImageView_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView(){
        mImageView1 = (ImageView) findViewById(R.id.mode_play);
        mImageView2 = (ImageView) findViewById(R.id.last_play);
        mImageView3 = (ImageView) findViewById(R.id.play_pause);
        mImageView4 = (ImageView) findViewById(R.id.next_play);
        mImageView5 = (ImageView) findViewById(R.id.menu_play);

        mImageView1.setOnClickListener(this);
        mImageView2.setOnClickListener(this);
        mImageView3.setOnClickListener(this);
        mImageView4.setOnClickListener(this);
        mImageView5.setOnClickListener(this);

        mTextView_start = (TextView) findViewById(R.id.start);
        mTextView_total = (TextView) findViewById(R.id.total);

        mSeekBar = (SeekBar) findViewById(R.id.seek_play);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    playService.seekTo(progress);
                    if (playService.isRunning()){
                        playService.start();
                    }
                    mImageView3.setImageResource(R.drawable.ic_pause);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void initViewPager(){
        decorView = (ViewGroup) findViewById(android.R.id.content);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_play);
        ViewPagerScroller scroller = new ViewPagerScroller(PlayActivity.this);
        scroller.setScrollDuration(1200);
        scroller.initViewPagerScroll(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {

                int cur = playService.getCurrPosition();

                if (position == 0){
                    mViewPager.setCurrentItem(mViewPagerInfos.size()-2, false);
                }else if (position == mViewPagerInfos.size() - 1){
                    mViewPager.setCurrentItem(1, false);
                }else if (position - 1 != cur){
                    cur = position - 1;
                    playService.play(cur);
                }

                if (playService.isPrepared()){
                    animator(true).start();
                }

                mBitmapExecutorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bitmap cacheBitmap = cacheBitmapMap.get(position);
                            if (cacheBitmap == null){
                                cacheBitmap = Picasso
                                        .with(PlayActivity.this)
                                        .load(mViewPagerInfos.get(position))
                                        .transform(new PicassoTransform(PlayActivity.this, 25, 4))
                                        .get();
                                if (cacheBitmap != null){
                                    cacheBitmapMap.put(position, cacheBitmap);
                                }
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (cacheBitmapMap.get(position) == null){
                                        PicassoTransform transform = new PicassoTransform(PlayActivity.this, 25, 4);
                                        Bitmap bt = transform.transform(BitmapFactory.decodeResource(getResources(), R.drawable.music));
                                        decorView.setBackground(new BitmapDrawable(bt));
                                    }else {
                                        decorView.setBackground(new BitmapDrawable(cacheBitmapMap.get(position)));
                                    }
                                }
                            });

                        } catch (IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    PicassoTransform transform = new PicassoTransform(PlayActivity.this, 25, 4);
                                    Bitmap bt = transform.transform(BitmapFactory.decodeResource(getResources(), R.drawable.music));
                                    decorView.setBackground(new BitmapDrawable(bt));
                                }
                            });
                        }
                    }
                });

//                BitmapTask task = new BitmapTask(mMp3Infos.get(position).getAlbumId(), position);
//                if (!mTasks.contains(task)){
//                    mTasks.add(task);
//                    mExecutorService.execute(task);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();
                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0);
                } else if (position <= 0) { // [-1,0]
                    // Use the default slide transition when
                    // moving to the left page
                    view.setAlpha(1);
                    view.setTranslationX(0);
                    view.setScaleX(1);
                    view.setScaleY(1);
                } else if (position <= 1) { // (0,1]
                    // Fade the page out.
                    view.setAlpha(1 - position);
                    // Counteract the default slide transition
                    view.setTranslationX(pageWidth * -position);
                    // Scale the page down (between MIN_SCALE and 1)
                    float scaleFactor = 0.75f + (1 - 0.75f)
                            * (1 - Math.abs(position));
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);
                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0);

                }
            }
        });
    }

    private ObjectAnimator animator(boolean change){
        if (change){
            objectAnimator = ObjectAnimator.ofFloat(mPagerAdapter.getPrimaryItem(), "rotation", 0, 360);
            objectAnimator.setDuration(8000);
            objectAnimator.setInterpolator(new LinearInterpolator());
            objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            objectAnimator.setRepeatMode(ObjectAnimator.RESTART);
        }else {
            if (objectAnimator == null){
                objectAnimator = ObjectAnimator.ofFloat(mPagerAdapter.getPrimaryItem(), "rotation", 0, 360);
                objectAnimator.setDuration(8000);
                objectAnimator.setInterpolator(new LinearInterpolator());
                objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                objectAnimator.setRepeatMode(ObjectAnimator.RESTART);
            }
        }
//        Log.i(TAG, "animator: " + mPagerAdapter.getPrimaryItem());

        return objectAnimator;
    }

    private void scheduleSeekBarUpdate(){
        if (!mSeekBarExecutorService.isShutdown()){
            mScheduledFuture = mSeekBarExecutorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    mHandler.post(mUpdateTask);
                }
            }, 100, 1000, TimeUnit.MILLISECONDS);
        }
    }

    private void stopSeekBarUpdate(){
        if (mScheduledFuture != null){
            mScheduledFuture.cancel(false);
        }
    }

    @Override
    protected void updatePlayActivity(int position, List<Mp3Info> mp3Infos, boolean isOne) {
        if (isOne){
            cacheBitmapMap.clear();

            updateMusicViews(mp3Infos);

        }

        if(mp3Infos.size() > 0){
            if(playService.isRunning()){
                mImageView3.setImageResource(R.drawable.ic_pause);
                scheduleSeekBarUpdate();
            }else {
                mImageView3.setImageResource(R.drawable.ic_play);
                stopSeekBarUpdate();
            }

            mTextView_artist.setText(mp3Infos.get(position).getArtist());
            mTextView_songname.setText(mp3Infos.get(position).getTitle());

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cacheBitmapMap.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_play:
                CustomDialog dialog = CustomDialogAdapter.createDialog(this, null, new ListDialogListener() {
                    @Override
                    public void createListDialog(ListView listView) {
                        final List<String> list = new ArrayList<>();
                        for (Mp3Info mp3Info : playService.getPlaylist()){
                            list.add(mp3Info.getTitle());
                        }

                        final ItemListAdapter adapter = new ItemListAdapter(PlayActivity.this, list, playService.getCurrPosition());

                        adapter.setListener(new ItemListAdapter.DeleteSongListener() {
                            @Override
                            public void deleteSong(int pos) {
                                if (pos != playService.getCurrPosition()){
                                    Mp3Info currentMp3 = playService.getPlaylist().get(playService.getCurrPosition());
                                    playService.getPlaylist().remove(pos);
                                    playService.setCurrPosition(playService.getPlaylist().indexOf(currentMp3));
                                }else {
                                    playService.pause();
                                    playService.getPlaylist().remove(pos);
                                    if (playService.getPlaylist().size() > 0){
                                        playService.play(pos);
                                        mViewPager.setCurrentItem(pos+1, true);
                                    }
                                }
                                list.remove(pos);
                                adapter.setIsPlayPos(playService.getCurrPosition());
                                adapter.notifyDataSetChanged();
                            }
                        });

                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                playService.play(position);
                                mViewPager.setCurrentItem(position+1, true);
                                adapter.setIsPlayPos(position);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
                dialog.setDefaultContentHeight(ScreenUtils.getScreenHeight(this)*3/5).build().show();
                break;
            case R.id.last_play:
                if (playService.getPlaylist().size() > 0){
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
                }
                break;
            case R.id.next_play:
                if (playService.getPlaylist().size() > 0){
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
                }
                break;
            case R.id.play_pause:
                if(playService.isRunning()){
                    mImageView3.setImageResource(R.drawable.ic_play);
                    playService.pause();
                    animator(false).pause();
                }else {
                    if (playService.getPlaylist().size() > 0){
                        mImageView3.setImageResource(R.drawable.ic_pause);
                        playService.seekTo(playService.getCurrProgress());
                        playService.start();
                        animator(false).start();
                    }
                }
                break;
            case R.id.mode_play:{
                PlayMode mode = playService.getPlayMode();
                if(mode == PlayMode.LOOP){
                    mImageView1.setImageResource(R.drawable.ic_play_loop);
                    playService.setPlayMode(PlayMode.RANDOM);
                } else if(mode == PlayMode.RANDOM) {
                    mImageView1.setImageResource(R.drawable.ic_play_random);
                    playService.setPlayMode(PlayMode.SINGLE);
                } else {
                    mImageView1.setImageResource(R.drawable.ic_play_one);
                    playService.setPlayMode(PlayMode.LOOP);
                }
                break;
            }

        }
    }

    private class SongPagerAdapter extends PagerAdapter {

        private List<String> mList;
        private MusicView currView;

        public SongPagerAdapter(List<String> list){
            mList = list;
        }

        @Override
        public int getCount() {
            if (mList.size() == 0){
                return 1;
            }
            return mList.size();
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            currView = (MusicView) object;
        }

        public MusicView getPrimaryItem(){
            return currView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            mMusicView = new MusicView(PlayActivity.this);
            mMusicView.setRadius(ScreenUtils.getScreenWidth(PlayActivity.this)/2-100);
            mMusicView.setPadding(100, 100, 100, 100);

            if (mList.size() > 0){
                Picasso.with(PlayActivity.this)
                        .load(mList.get(position))
                        .into(mMusicView);
            }

            container.addView(mMusicView);
            return mMusicView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    private void updateMusicViews(List<Mp3Info> mp3Infos){
        mViewPagerInfos = new ArrayList<>();

        int size = mp3Infos.size();

        if (size > 0){
            mViewPagerInfos.add(mp3Infos.get(size-1).getPicUrl());

            for (Mp3Info mp3Info : mp3Infos){
                mViewPagerInfos.add(mp3Info.getPicUrl());
            }

            mViewPagerInfos.add(mp3Infos.get(0).getPicUrl());
        }

        mPagerAdapter = new SongPagerAdapter(mViewPagerInfos);
        mViewPager.setAdapter(mPagerAdapter);
        if (mp3Infos.size() > 0) {
            mViewPager.setCurrentItem(playService.getCurrPosition() + 1, false);
        } else {
            PicassoTransform transform = new PicassoTransform(PlayActivity.this, 25, 4);
            Bitmap bt = transform.transform(BitmapFactory.decodeResource(getResources(), R.drawable.music));
            decorView.setBackground(new BitmapDrawable(bt));
        }
    }

//    private ViewPagerHandler vHandler = new ViewPagerHandler(new WeakReference<>(this));

//    private static class ViewPagerHandler extends Handler{
//        /**
//         * 请求更新显示的View。
//         */
//        protected static final int MSG_UPDATE_IMAGE  = 1;
//        /**
//         * 请求暂停轮播。
//         */
//        protected static final int MSG_KEEP_SILENT   = 2;
//        /**
//         * 请求恢复轮播。
//         */
//        protected static final int MSG_BREAK_SILENT  = 3;
//        /**
//         * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
//         * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
//         * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
//         */
//        protected static final int MSG_PAGE_CHANGED  = 4;
//
//        //轮播间隔时间
//        protected static final long MSG_DELAY = 3000;
//
//        private WeakReference<PlayActivity> mWeakReference;
//        private int currentItem = 0;
//
//        public ViewPagerHandler(WeakReference<PlayActivity> weakReference) {
//            mWeakReference = weakReference;
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            PlayActivity activity = mWeakReference.get();
//            if(activity == null){
//                return;
//            }
//            if(activity.vHandler.hasMessages(MSG_UPDATE_IMAGE)){
//                activity.vHandler.removeMessages(MSG_UPDATE_IMAGE);
//            }
//            switch (msg.what){
//                case MSG_UPDATE_IMAGE:
//                    currentItem++;
//                    activity.mViewPager.setCurrentItem(currentItem);
//                    //准备下次播放
//                    activity.vHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
//                    break;
//                case MSG_KEEP_SILENT:
//                    //只要不发送消息就暂停了
//                    break;
//                case MSG_BREAK_SILENT:
//                    activity.vHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
//                    break;
//                case MSG_PAGE_CHANGED:
//                    //记录当前的页号，避免播放的时候页面显示不正确。
//                    currentItem = msg.arg1;
//                    break;
//                default:
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    }

//    private static Map<Integer, Bitmap> mBitmapTaskMap = new Hashtable<>(8);
//    private List<BitmapTask> mTasks = new LinkedList<>();
//    private class BitmapTask implements Runnable{
//        private long posID;
//        private int pos;
//
//        public BitmapTask(long posID, int pos) {
//            this.posID = posID;
//            this.pos = pos;
//        }
//
//        public long getId() {
//            return posID;
//        }
//
//        @Override
//        public void run() {
//            try {
//                final Bitmap bitmap = Picasso
//                        .with(PlayActivity.this)
//                        .load("content://media/external/audio/albumart" + "/" + posID)
//                        .transform(new PicassoTransform(PlayActivity.this, 20, 1)).get();
//                if (bitmap != null){
//                    mBitmapTaskMap.put(pos, bitmap);
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mBitmapTaskMap.containsKey(mViewPager.getCurrentItem())){
//                            System.out.println("=========================================================>" + mViewPager.getCurrentItem());
//                            decorView.setBackground(new BitmapDrawable(mBitmapTaskMap.get(mViewPager.getCurrentItem())));
//                        }
//                    }
//                });
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            if (obj == null){
//                return false;
//            }
//
//            if (this == obj){
//                return true;
//            }
//
//            if (obj instanceof BitmapTask){
//                BitmapTask task = (BitmapTask) obj;
//                if (posID == task.getId()){
//                    return true;
//                }
//            }
//            return false;
//        }
//    }

}
