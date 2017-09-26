package com.example.administrator.httpdemo.Data.remote.API;

import com.example.administrator.httpdemo.Data.entity.TopList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

/**
 * Created by Administrator on 2017/9/8.
 */
public interface TopListApi {
    @GET("ting?method=baidu.ting.billboard.billCategory&kflag=1")
    Call<List<TopList>> getTopList();
}
