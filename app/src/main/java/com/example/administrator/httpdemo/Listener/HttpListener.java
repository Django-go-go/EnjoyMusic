package com.example.administrator.httpdemo.Listener;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/3.
 */

public interface HttpListener<T> {
    void onResponse(Call<T> call, Response<T> response);
    void onFailure(Call<T> call, Throwable t);
}
