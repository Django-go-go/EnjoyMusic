package com.example.administrator.httpdemo.Data.remote.API;

import android.widget.ListView;

import com.example.administrator.httpdemo.Data.entity.Song2;
import com.example.administrator.httpdemo.Data.entity.SongList2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017/9/13.
 */

public interface HotSongApi {
    @GET("ting?method=baidu.ting.song.getEditorRecommend&num=20")
    Call<List<Song2>> getHotSong();
}
