package com.example.administrator.httpdemo.Data.remote.API;

import com.example.administrator.httpdemo.Data.entity.Song;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/8/8.
 */

//http://music.163.com/#/m/song?id=67844
public interface SongApi {
    @GET("song/get/")
    Call<Song> getSong(@Query("id") long id);
}
