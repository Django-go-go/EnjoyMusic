package com.example.administrator.httpdemo.Data.remote.API;

import com.example.administrator.httpdemo.Data.entity.SongList;

import retrofit2.Call;
import retrofit2.http.HTTP;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/8/3.
 */

public interface SongListApi {
    @HTTP(method = "GET", path = "playlist/detail", hasBody = false)
    Call<SongList> getSongList(@Query("id") long id);
}
