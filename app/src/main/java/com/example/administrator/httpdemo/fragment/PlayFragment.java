package com.example.administrator.httpdemo.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.Listener.PlayListener;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.activity.PlayActivity;
import com.example.administrator.httpdemo.activity.base.BaseActivity;
import com.example.administrator.httpdemo.fragment.base.BaseFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/8/15.
 */

public class PlayFragment extends BaseFragment implements View.OnClickListener{
    private ImageView mImageView_album, mImageView_play, mImageView_menu;
    private TextView mTextView_songName, mTextView_singer;
    private View view;

    private Context mContext;

    private ToActivityListener mListener;

    public static final int CLICK_PLAY = 1;
    public static final int CLICK_MENU = 2;

    public PlayFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof ToActivityListener){
            mListener = (ToActivityListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.play, container, false);
        mImageView_album = (ImageView) view.findViewById(R.id.tail_pic);
        mImageView_menu = (ImageView) view.findViewById(R.id.tail_menu);
        mImageView_play = (ImageView) view.findViewById(R.id.tail_play);
        mTextView_singer = (TextView) view.findViewById(R.id.tail_singer);
        mTextView_songName = (TextView) view.findViewById(R.id.tail_songName);
        mImageView_play.setOnClickListener(this);
        mImageView_menu.setOnClickListener(this);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), PlayActivity.class), 1000);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        if (mContext instanceof BaseActivity){
            BaseActivity activity = (BaseActivity) mContext;
            activity.setPlayListener(new PlayListener() {
                @Override
                public void update(int pos, boolean isRunning, List<Mp3Info> mp3Infos) {
                    if (isRunning){
                        mImageView_play.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
                    }else {
                        mImageView_play.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);
                    }

                    mTextView_singer.setText(mp3Infos.get(pos).getArtist());
                    mTextView_songName.setText(mp3Infos.get(pos).getTitle());

                    Picasso.with(getContext())
                            .load(mp3Infos.get(pos).getPicUrl())
                            .error(R.drawable.music)
                            .into(mImageView_album);

                }
            });
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tail_menu:
                if (mListener != null){
                    mListener.onActivityClick(CLICK_MENU);
                }
                break;
            case R.id.tail_play:
                if (mListener != null){
                    mListener.onActivityClick(CLICK_PLAY);
                }
                break;
        }
    }

    public interface ToActivityListener{
        void onActivityClick(int click);
    }
}
