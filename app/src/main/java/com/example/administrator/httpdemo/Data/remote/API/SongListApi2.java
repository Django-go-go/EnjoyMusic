package com.example.administrator.httpdemo.Data.remote.API;

import com.example.administrator.httpdemo.Data.entity.SongList2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/9/10.
 */

public interface SongListApi2 {
    @GET("ting?method=baidu.ting.diy.gedan")
    Call<List<SongList2>> getSongList(@Query("page_size") int page_size, @Query("page_no") int page_no);
}
