package com.example.administrator.httpdemo.Data.remote.API;

import com.example.administrator.httpdemo.Data.entity.Song2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/9/11.
 */

public interface Song2Api {
    @GET("ting?method=baidu.ting.song.baseInfos")
    Call<Song2> getSong2(@Query("song_id") String id);
}
