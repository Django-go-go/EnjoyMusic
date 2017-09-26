package com.example.administrator.httpdemo.Data.remote.API;

import com.example.administrator.httpdemo.Data.entity.TopList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.HTTP;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/9/8.
 */

public interface TopListSongsApi {
    @HTTP(method = "GET", path = "&method=baidu.ting.billboard.billList&fields=song_id,title,author,album_title,pic_big,pic_small,havehigh,all_rate,charge,has_mv_mobile,learn,song_source,korean_bb_song", hasBody = false)
    Call<List<TopList>> getTopListSong(@Query("type") int type);
}
