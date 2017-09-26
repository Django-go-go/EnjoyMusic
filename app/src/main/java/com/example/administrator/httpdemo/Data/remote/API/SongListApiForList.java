package com.example.administrator.httpdemo.Data.remote.API;

import com.example.administrator.httpdemo.Data.entity.SongList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.HTTP;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/8/6.
 */

public interface SongListApiForList {
//    http://music.163.com/api/playlist/list?cat=%E5%8D%8E%E8%AF%AD&order=&offset=0&total=true&limit=1
    @HTTP(method = "GET", path = "playlist/list", hasBody = false)
    Call<List<SongList>> getSongListForList(@Query("cat") String cat, @Query("order") String order, @Query("offset") int offset, @Query("total") boolean total, @Query("limit") int limit);
}
