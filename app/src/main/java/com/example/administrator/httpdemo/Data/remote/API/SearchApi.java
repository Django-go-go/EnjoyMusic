package com.example.administrator.httpdemo.Data.remote.API;

import com.example.administrator.httpdemo.Data.entity.Song;
import com.example.administrator.httpdemo.Data.entity.SongList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/8/21.
 */
//http://s.music.163.com/search/get/?type=1&limit=2&s=
public interface SearchApi {
    @GET("search/get/")
    Call<List<Song>> getSearchSong(
            @Query("type") int type, @Query("limit") int limit, @Query("offset") int offset, @Query("s") String key);
}
