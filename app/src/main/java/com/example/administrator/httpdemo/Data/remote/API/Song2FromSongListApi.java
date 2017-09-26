package com.example.administrator.httpdemo.Data.remote.API;

import com.example.administrator.httpdemo.Data.entity.Song2;
import com.example.administrator.httpdemo.Data.entity.SongList2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/9/11.
 */

public interface Song2FromSongListApi {
    @GET("ting?method=baidu.ting.diy.gedanInfo")
    Call<List<Song2>> getSong2FromSongList(@Query("listid") String id);
}
