package com.example.administrator.httpdemo.Data.remote.API;

import com.example.administrator.httpdemo.Data.entity.Song;
import com.example.administrator.httpdemo.Data.entity.SongList2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/9/11.
 */

public interface TagSongListApi {
    @GET("ting?&method=baidu.ting.diy.search&page_size=100")
    Call<List<SongList2>> getTagSongList(@Query("query") String tag);
}
