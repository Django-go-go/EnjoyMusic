package com.example.administrator.httpdemo.Data.remote;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.administrator.httpdemo.Data.entity.Banner;
import com.example.administrator.httpdemo.Data.entity.HotWord;
import com.example.administrator.httpdemo.Data.entity.MySharedSongs;
import com.example.administrator.httpdemo.Data.entity.MyUser;
import com.example.administrator.httpdemo.Data.entity.NetSong;
import com.example.administrator.httpdemo.Data.entity.SearchSong;
import com.example.administrator.httpdemo.Data.entity.Song;
import com.example.administrator.httpdemo.Data.entity.Song2;
import com.example.administrator.httpdemo.Data.entity.SongList;
import com.example.administrator.httpdemo.Data.entity.CreateSongList;
import com.example.administrator.httpdemo.Data.entity.SongList2;
import com.example.administrator.httpdemo.Data.entity.TopList;
import com.example.administrator.httpdemo.Data.remote.API.BannerApi;
import com.example.administrator.httpdemo.Data.remote.API.HotSongApi;
import com.example.administrator.httpdemo.Data.remote.API.HotSongListApi;
import com.example.administrator.httpdemo.Data.remote.API.HotWordApi;
import com.example.administrator.httpdemo.Data.remote.API.SearchApi;
import com.example.administrator.httpdemo.Data.remote.API.SearchSongApi;
import com.example.administrator.httpdemo.Data.remote.API.Song2Api;
import com.example.administrator.httpdemo.Data.remote.API.Song2FromSongListApi;
import com.example.administrator.httpdemo.Data.remote.API.Song2FromTopListApi;
import com.example.administrator.httpdemo.Data.remote.API.SongApi;
import com.example.administrator.httpdemo.Data.remote.API.SongListApi2;
import com.example.administrator.httpdemo.Data.remote.API.SongListApiForList;
import com.example.administrator.httpdemo.Data.remote.API.SongListApi;
import com.example.administrator.httpdemo.Data.remote.API.TagSongListApi;
import com.example.administrator.httpdemo.Data.remote.API.TopListApi;
import com.example.administrator.httpdemo.Listener.HttpCallBack;
import com.example.administrator.httpdemo.Listener.HttpListener;
import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.Other.Constant;
import com.example.administrator.httpdemo.Utils.LogUtils;
import com.example.administrator.httpdemo.Utils.MyException;
import com.example.administrator.httpdemo.Utils.ToastUtils;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2017/7/22.
 */

public class BaseNetData implements NetData {
    public static final String TAG = "BaseNetData";

    private Context mContext;
    public BaseNetData(Context context) {
        mContext = context;
    }

    @Override
    public void share(final MySharedSongs sharedSong) {
        sharedSong.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e != null){
                    LogUtils.i("save===========> " + e.toString());
                }else{
                    upload(sharedSong);
//                    ToastUtils.showShort(mContext, "save===========>成功");
                }
            }
        });
    }

    @Override
    public void share(List<MySharedSongs> sharedSongs) {
        new BmobBatch().doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if(e != null){
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).getError() != null){
                            LogUtils.i("第 " + i+1 + " 错了");
                        }
                    }
                } else {
                    ToastUtils.showShort(mContext, "成功");
                }
            }
        });
    }

    @Override
    public void upload(CreateSongList list) {
        list.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                MyException.solve("shareSongList ", e, mContext);
                if(e == null){
                    MyUser newUser = new MyUser();
//                    BmobUsercom.example.administrator.httpdemo.Data.entity.MyUser@54cb041
//                    s 8a185db470
//                    ids null
//                    shareSongList ===================>errorCode:9015,errorMsg:java.lang.NullPointerException: Attempt to invoke interface method 'boolean java.util.List.add(java.lang.Object)' on a null object reference
//                    queryId =================>success!
                    List<String> ids = query(BmobUser.getCurrentUser(MyUser.class));
                    if(ids == null) ids = new ArrayList<>();
                    ids.add(s);
//                    newUser.setSongListId(ids);
                    update(newUser);
                }
            }
        });
    }

    @Override
    public void upload(final MySharedSongs song) {
        final BmobFile pathFile = new BmobFile(new File(song.getUrl()));
        final BmobFile albumFile = new BmobFile(new File(song.getAlbum()));
        pathFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e != null){
                    LogUtils.i("upload===========> " + e.toString());
                }else{
                    song.setUrl(pathFile.getFileUrl());
                    song.update(song.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e != null){
                                LogUtils.i("update ===========> " + e.toString());
                            }else{
//                                ToastUtils.showShort(mContext, "update ===========> 成功");
                            }
                        }
                    });
//                    ToastUtils.showShort(mContext, "upload===========> 成功");
                }
            }
        });
        albumFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e != null){
                    LogUtils.i(e.toString());
                }else{
                    song.setAlbum(albumFile.getFileUrl());
//                    System.out.println("albumFile.getFileUrl() " + albumFile.getFileUrl());
//                    System.out.println("albumFile.getUrl() " + albumFile.getUrl());
//                    System.out.println("song.getObjectId() " + song.getObjectId());
                    song.update(song.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e != null){
                                LogUtils.i("update ===========> " + e.toString());
                            }else{
//                                ToastUtils.showShort(mContext, "成功");
                            }
                        }
                    });
//                    ToastUtils.showShort(mContext, "成功");
                }
            }
        });
    }

    private List<String> songsListId;

    @Override
    public List<String> query(MyUser myUser) {
        BmobQuery<MyUser> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(myUser.getObjectId(), new QueryListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                MyException.solve("queryId ", e, mContext);
                if (e == null){
                    songsListId = new ArrayList<>();
                    songsListId.add("wwwwwwww");
//                    songsListId = myUser.getSongListId();
                }
            }
        });
        return songsListId;
    }
    private List<CreateSongList> mCreateSongLists = new ArrayList<>();

    @Override
    public List<CreateSongList> query(List<String> ids) {
        BmobQuery<CreateSongList> bmobQuery = new BmobQuery<>();
        for (String str : ids){
            bmobQuery.getObject(str, new QueryListener<CreateSongList>() {
                @Override
                public void done(CreateSongList list, BmobException e) {
                    MyException.solve("querySongsList", e, mContext);
                    if(e == null){
                        mCreateSongLists.add(list);
                    }
                }
            });
        }
        return mCreateSongLists;
    }

    @Override
    public void update(MyUser myUser) {
        myUser.update(BmobUser.getCurrentUser(MyUser.class).getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                MyException.solve("MyUser Update " , e, mContext);
            }
        });
    }


    @Override
    public void update(MySharedSongs song) {

    }

    @Override
    public void updateSongFromFile(MySharedSongs song, BmobFile file) {
    }

    @Override
    public MySharedSongs appreciate() {
        return null;
    }

    public void getTopList(final HttpListener<List<TopList>> listener){
//        OkHttpClient c = new OkHttpClient.Builder().addInterceptor(new RequestInterceptor()).build();
//        Request r = new Request.Builder().get()
//                .url("http://tingapi.ting.baidu.com/v1/restserver/ting?from=android&version=5.6.5.6&format=json&method=baidu.ting.billboard.billCategory&&kflag=1")
//                .header("User-Agent", "Mozilla")
//                .build();
//        c.newCall(r).enqueue(new okhttp3.Callback() {
//            @Override
//            public void onFailure(okhttp3.Call call, IOException e) {
//                Log.e(TAG, "onFailure: ", e);
//            }
//
//            @Override
//            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
//                Gson gson = new Gson();
//                String s = response.body().string();
//                JsonArray array = new JsonParser().parse(s).getAsJsonObject().getAsJsonArray("content");
//                List<TopList> songs = new ArrayList<>();
//                for(JsonElement song : array) {
//                    if (song != null) {
//                        songs.add(gson.fromJson(song, TopList.class));
//                        Log.i(TAG, "convert: " + song);
//                    } else {
//                        Log.i(TAG, "convert: convertFactoryForTopList null");
//                    }
//                }
//            }
//        });
        Retrofit retrofit = createRetrofit3(convertFactoryForTopList());
        TopListApi api = retrofit.create(TopListApi.class);
        api.getTopList().enqueue(new Callback<List<TopList>>() {
            @Override
            public void onResponse(Call<List<TopList>> call, Response<List<TopList>> response) {
                listener.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<TopList>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                listener.onFailure(call, t);
            }
        });
    }

    public void getSongList(int size, int no, final HttpListener<List<SongList2>> listener){
        Retrofit retrofit = createRetrofit3(convertFactoryForSongList2());
        SongListApi2 api = retrofit.create(SongListApi2.class);
        api.getSongList(size, no).enqueue(new Callback<List<SongList2>>() {
            @Override
            public void onResponse(Call<List<SongList2>> call, Response<List<SongList2>> response) {
                listener.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<SongList2>> call, Throwable t) {
                listener.onFailure(call, t);
            }
        });
    }

    public void getSong2FromSongList(String id, final HttpListener<List<Song2>> listener){
        Retrofit retrofit = createRetrofit3(convertFactoryForSong2FromSongList());
        Song2FromSongListApi api = retrofit.create(Song2FromSongListApi.class);
        api.getSong2FromSongList(id).enqueue(new Callback<List<Song2>>() {
            @Override
            public void onResponse(Call<List<Song2>> call, Response<List<Song2>> response) {
                listener.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<Song2>> call, Throwable t) {
                listener.onFailure(call, t);
            }
        });
    }

    public void getSong2FromTopList(int type, final HttpListener<List<Song2>> listener){
        Retrofit retrofit = createRetrofit3(convertFactoryForSong2FromTopList());
        Song2FromTopListApi api = retrofit.create(Song2FromTopListApi.class);
        api.getSong2FromTopList(type).enqueue(new Callback<List<Song2>>() {
            @Override
            public void onResponse(Call<List<Song2>> call, Response<List<Song2>> response) {
                listener.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<Song2>> call, Throwable t) {
                listener.onFailure(call, t);
            }
        });
    }

    public void getSong2(String id, final HttpListener<Song2> listener){
        Retrofit retrofit = createRetrofit3(convertFactoryForSong2());
        Song2Api api = retrofit.create(Song2Api.class);
        api.getSong2(id).enqueue(new Callback<Song2>() {
            @Override
            public void onResponse(Call<Song2> call, Response<Song2> response) {
                listener.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Song2> call, Throwable t) {
                listener.onFailure(call, t);
            }
        });
    }

    public void getHotSongList(int num, final HttpListener<List<SongList2>> listener){
        Retrofit retrofit = createRetrofit3(convertFactoryForHotSongList());
        HotSongListApi api = retrofit.create(HotSongListApi.class);
        api.getHotSongList(num).enqueue(new Callback<List<SongList2>>() {
            @Override
            public void onResponse(Call<List<SongList2>> call, Response<List<SongList2>> response) {
                listener.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<SongList2>> call, Throwable t) {
                listener.onFailure(call, t);
            }
        });
    }

    public void getTagSongList(String tag, final HttpListener<List<SongList2>> listener){
        Retrofit retrofit = createRetrofit3(convertFactoryForTagSongList());
        TagSongListApi api = retrofit.create(TagSongListApi.class);
        api.getTagSongList(tag).enqueue(new Callback<List<SongList2>>() {
            @Override
            public void onResponse(Call<List<SongList2>> call, Response<List<SongList2>> response) {
                listener.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<SongList2>> call, Throwable t) {
                listener.onFailure(call, t);
            }
        });
    }


    public void getSearchSong(String query, final HttpListener<List<SearchSong>> listener){
        Retrofit retrofit = createRetrofit3(convertFactoryForSearchSong());
        SearchSongApi api = retrofit.create(SearchSongApi.class);
        api.getSearchSong(query).enqueue(new Callback<List<SearchSong>>() {
            @Override
            public void onResponse(Call<List<SearchSong>> call, Response<List<SearchSong>> response) {
                listener.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<SearchSong>> call, Throwable t) {
                listener.onFailure(call, t);
            }
        });
    }

    public void getHotWord(final HttpListener<List<HotWord>> listener){
        Retrofit retrofit = createRetrofit3(convertFactoryForHotWord());
        HotWordApi api = retrofit.create(HotWordApi.class);
        api.getHotWord().enqueue(new Callback<List<HotWord>>() {
            @Override
            public void onResponse(Call<List<HotWord>> call, Response<List<HotWord>> response) {
                listener.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<HotWord>> call, Throwable t) {
                listener.onFailure(call, t);
            }
        });
    }

    public void getNetSong(String id, final HttpCallBack callback){
        OkHttpClient client = new OkHttpClient.Builder().build();
        String url = UrlString.Song.songInfo(id);
        Log.i(MusicApp.TAG, "url: " + url);
        Request request = new Request.Builder()
                .url(url)
                .header("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:55.0) Gecko/20100101 Firefox/55.0")
                .header("accept-encoding", "gzip, deflate")
                .header("accept-language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                .header("connection", "Keep-Alive")
                .header("accept", "text/html,application/json,application/xml;q=0.9,*/*;q=0.8")
                .header("cookie", "BAIDUID=F7A79F9D5675ADFF8FAB6C533BB6AEE3:FG=1; BIDUPSID=993C0ADCD1F62B1EC7DA0FBB107AB73A; PSTM=1491932224; __cfduid=d9ca8f30118bd2c533a960acf916c71611495113898; BDUSS=WtmMEh6UkNHZHI5ejBBU29QWXg0bGczNWlwU1VWNXlKRnd-VTBva0tyWHdScEpaSUFBQUFBJCQAAAAAAAAAAAEAAAB5WqKLyMjH6bXEamluZ2ppMTIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPC5alnwuWpZY; BDORZ=FFFB88E999055A3F8A630C64834BD6D0; BDRCVFR[Fc9oatPmwxn]=srT4swvGNE6uzdhUL68mv3; PSINO=3; H_PS_PSSID=1436_21078_19897_22160; app_vip=show; UM_distinctid=15e78adb978452-003ee4a7517fd3-17387440-104040-15e78adb97947c")
                .header("Host", "tingapi.ting.baidu.com")
                .header("Upgrade-Insecure-Requests", "1")
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()){
                    String s = response.body().string();
                    Log.i(MusicApp.TAG, "onResponse: ================> " + s);
                    if (s != null && s.length() > 0){
                        Gson gson = new Gson();
                        JsonArray array1 = new JsonParser().parse(s).getAsJsonObject().getAsJsonObject("songurl").getAsJsonArray("url");
                        NetSong netSong1 = gson.fromJson(array1.get(0), NetSong.class);
                        JsonObject array2 = new JsonParser().parse(s).getAsJsonObject().getAsJsonObject("songinfo");
                        NetSong netSong = gson.fromJson(array2, NetSong.class);
                        netSong.setFile_link(netSong1.getFile_link());
                        if (callback != null){
                            callback.netSong(netSong);
                        }
                    }else {
                        if (callback != null){
                            callback.netSong(null);
                        }
                    }
                }

            }
        });
    }

    public void getHotSong(final HttpListener<List<Song2>> listener){
        Retrofit retrofit = createRetrofit3(convertFactoryForHotSong());
        HotSongApi api = retrofit.create(HotSongApi.class);
        api.getHotSong().enqueue(new Callback<List<Song2>>() {
            @Override
            public void onResponse(Call<List<Song2>> call, Response<List<Song2>> response) {
                listener.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<Song2>> call, Throwable t) {
                listener.onFailure(call, t);
            }
        });
    }

    public void getBanner(final HttpListener<List<Banner>> listener){
        Retrofit retrofit = createRetrofit3(convertFactoryForBanner());
        BannerApi api = retrofit.create(BannerApi.class);
        api.getBanner(9).enqueue(new Callback<List<Banner>>() {
            @Override
            public void onResponse(Call<List<Banner>> call, Response<List<Banner>> response) {
                listener.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<Banner>> call, Throwable t) {
                listener.onFailure(call, t);
            }
        });
    }

    private class RequestInterceptor implements Interceptor{
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request()
                    .newBuilder()
                    .header("User-Agent", "Mozilla")
                    .build();
            return chain.proceed(request);
        }
    }

    private OkHttpClient getOkHttpClient() {
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        //使用拦截器
        httpClientBuilder.addInterceptor(new RequestInterceptor());
        return httpClientBuilder.build();
    }

    private Retrofit createRetrofit3(Converter.Factory factory){
        return new Retrofit.Builder()
                .baseUrl("http://tingapi.ting.baidu.com/v1/restserver/")
                .client(getOkHttpClient())
                .addConverterFactory(factory)
                .build();
    }

    private Converter.Factory convertFactoryForSongList2(){
        return new Converter.Factory() {
            @Nullable
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, final Annotation[] annotations, Retrofit retrofit) {
                return new Converter<ResponseBody, List<SongList2>>() {
                    @Override
                    public List<SongList2> convert(ResponseBody value) throws IOException {
                        Gson gson = new Gson();
                        String s = value.string();
                        JsonArray array = new JsonParser().parse(s).getAsJsonObject().getAsJsonArray("content");
                        List<SongList2> songs = new ArrayList<>();
                        Log.i(TAG, "convert: " + s);
                        for(JsonElement song : array){
                            if (song != null){
                                songs.add(gson.fromJson(song, SongList2.class));
                                Log.i(TAG, "convert: " + song);
                            }else {
                                Log.i(TAG, "convert: convertFactoryForTopList null");
                            }
                        }
                        return songs;
                    }
                };
            }
        };
    }

    private Converter.Factory convertFactoryForTopList(){
        return new Converter.Factory() {
            @Nullable
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, final Annotation[] annotations, Retrofit retrofit) {
                return new Converter<ResponseBody, List<TopList>>() {
                    @Override
                    public List<TopList> convert(ResponseBody value) throws IOException {
                        Gson gson = new Gson();
                        String s = value.string();
                        JsonArray array = new JsonParser().parse(s).getAsJsonObject().getAsJsonArray("content");
                        List<TopList> songs = new ArrayList<>();
                        Log.i(TAG, "convert: " + s);
                        for(JsonElement song : array){
                            if (song != null){
                                songs.add(gson.fromJson(song, TopList.class));
                                Log.i(TAG, "convert: " + song);
                            }else {
                                Log.i(TAG, "convert: convertFactoryForTopList null");
                            }
                        }
                        return songs;
                    }
                };
            }
        };
    }

    private Converter.Factory convertFactoryForSong2FromSongList(){
        return new Converter.Factory() {
            @Nullable
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, final Annotation[] annotations, Retrofit retrofit) {
                return new Converter<ResponseBody, List<Song2>>() {
                    @Override
                    public List<Song2> convert(ResponseBody value) throws IOException {
                        Gson gson = new Gson();
                        String s = value.string();
                        JsonArray array = new JsonParser().parse(s).getAsJsonObject().getAsJsonArray("content");
                        List<Song2> songs = new ArrayList<>();
                        Log.i(TAG, "convert: " + s);
                        for(JsonElement song : array){
                            if (song != null){
                                songs.add(gson.fromJson(song, Song2.class));
                                Log.i(TAG, "convert: " + song);
                            }else {
                                Log.i(TAG, "convert: convertFactoryForTopList null");
                            }
                        }
                        return songs;
                    }
                };
            }
        };
    }

    private Converter.Factory convertFactoryForSong2FromTopList(){
        return new Converter.Factory() {
            @Nullable
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, final Annotation[] annotations, Retrofit retrofit) {
                return new Converter<ResponseBody, List<Song2>>() {
                    @Override
                    public List<Song2> convert(ResponseBody value) throws IOException {
                        Gson gson = new Gson();
                        String s = value.string();
                        JsonArray array = new JsonParser().parse(s).getAsJsonObject().getAsJsonArray("song_list");
                        List<Song2> songs = new ArrayList<>();
                        Log.i(TAG, "convert: " + s);
                        for(JsonElement song : array){
                            if (song != null){
                                songs.add(gson.fromJson(song, Song2.class));
                                Log.i(TAG, "convert: " + song);
                            }else {
                                Log.i(TAG, "convert: convertFactoryForTopList null");
                            }
                        }
                        return songs;
                    }
                };
            }
        };
    }

    private Converter.Factory convertFactoryForSong2(){
        return new Converter.Factory() {
            @Nullable
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, final Annotation[] annotations, Retrofit retrofit) {
                return new Converter<ResponseBody, Song2>() {
                    @Override
                    public Song2 convert(ResponseBody value) throws IOException {
                        Gson gson = new Gson();
                        String s = value.string();
                        JsonArray array = new JsonParser().parse(s).getAsJsonObject().getAsJsonObject("result").getAsJsonArray("items");
                        List<Song2> songs = new ArrayList<>();
                        Log.i(TAG, "convert: " + s);
                        for(JsonElement song : array){
                            if (song != null){
                                songs.add(gson.fromJson(song, Song2.class));
                                Log.i(TAG, "convert: " + song);
                            }else {
                                Log.i(TAG, "convert: convertFactoryForTopList null");
                            }
                        }
                        return songs.get(0);
                    }
                };
            }
        };
    }

    private Converter.Factory convertFactoryForHotSongList(){
        return new Converter.Factory() {
            @Nullable
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, final Annotation[] annotations, Retrofit retrofit) {
                return new Converter<ResponseBody, List<SongList2>>() {
                    @Override
                    public List<SongList2> convert(ResponseBody value) throws IOException {
                        Gson gson = new Gson();
                        String s = value.string();
                        JsonArray array = new JsonParser().parse(s).getAsJsonObject().getAsJsonObject("content").getAsJsonArray("list");
                        List<SongList2> songs = new ArrayList<>();
                        Log.i(TAG, "convert: convertFactoryForHotSongList " + s);
                        for(JsonElement song : array){
                            if (song != null){
                                songs.add(gson.fromJson(song, SongList2.class));
                                Log.i(TAG, "convert: " + song);
                            }else {
                                Log.i(TAG, "convert: convertFactoryForTopList null");
                            }
                        }
                        return songs;
                    }
                };
            }
        };
    }

    private Converter.Factory convertFactoryForTagSongList(){
        return new Converter.Factory() {
            @Nullable
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, final Annotation[] annotations, Retrofit retrofit) {
                return new Converter<ResponseBody, List<SongList2>>() {
                    @Override
                    public List<SongList2> convert(ResponseBody value) throws IOException {
                        Gson gson = new Gson();
                        String s = value.string();
                        JsonArray array = new JsonParser().parse(s).getAsJsonObject().getAsJsonArray("content");
                        List<SongList2> songs = new ArrayList<>();
                        Log.i(TAG, "convert: convertFactoryForHotSongList " + s);
                        for(JsonElement song : array){
                            if (song != null){
                                songs.add(gson.fromJson(song, SongList2.class));
                                Log.i(TAG, "convert: " + song);
                            }else {
                                Log.i(TAG, "convert: convertFactoryForTopList null");
                            }
                        }
                        return songs;
                    }
                };
            }
        };
    }

    private Converter.Factory convertFactoryForSearchSong(){
        return new Converter.Factory() {
            @Nullable
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, final Annotation[] annotations, Retrofit retrofit) {
                return new Converter<ResponseBody, List<SearchSong>>() {
                    @Override
                    public List<SearchSong> convert(ResponseBody value) throws IOException {
                        Gson gson = new Gson();
                        String s = value.string();
                        JsonArray array = new JsonParser().parse(s).getAsJsonObject().getAsJsonArray("song");
                        List<SearchSong> songs = new ArrayList<>();
                        Log.i(TAG, "convert: convertFactoryForHotSongList " + s);
                        for(JsonElement song : array){
                            if (song != null){
                                songs.add(gson.fromJson(song, SearchSong.class));
                                Log.i(TAG, "convert: " + song);
                            }else {
                                Log.i(TAG, "convert: convertFactoryForTopList null");
                            }
                        }
                        return songs;
                    }
                };
            }
        };
    }

    private Converter.Factory convertFactoryForHotWord(){
        return new Converter.Factory() {
            @Nullable
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, final Annotation[] annotations, Retrofit retrofit) {
                return new Converter<ResponseBody, List<HotWord>>() {
                    @Override
                    public List<HotWord> convert(ResponseBody value) throws IOException {
                        Gson gson = new Gson();
                        String s = value.string();
                        JsonArray array = new JsonParser().parse(s).getAsJsonObject().getAsJsonArray("result");
                        List<HotWord> songs = new ArrayList<>();
                        Log.i(TAG, "convert: convertFactoryForHotSongList " + s);
                        for(JsonElement song : array){
                            if (song != null){
                                songs.add(gson.fromJson(song, HotWord.class));
                                Log.i(TAG, "convert: " + song);
                            }else {
                                Log.i(TAG, "convert: convertFactoryForTopList null");
                            }
                        }
                        return songs;
                    }
                };
            }
        };
    }

    private Converter.Factory convertFactoryForHotSong(){
        return new Converter.Factory() {
            @Nullable
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, final Annotation[] annotations, Retrofit retrofit) {
                return new Converter<ResponseBody, List<Song2>>() {
                    @Override
                    public List<Song2> convert(ResponseBody value) throws IOException {
                        Gson gson = new Gson();
                        String s = value.string();
                        JsonArray array = new JsonParser().parse(s).getAsJsonObject().
                                getAsJsonArray("content").get(0).getAsJsonObject().getAsJsonArray("song_list");
                        List<Song2> songs = new ArrayList<>();
                        Log.i(TAG, "convert: " + s);
                        for(JsonElement song : array){
                            if (song != null){
                                songs.add(gson.fromJson(song, Song2.class));
                                Log.i(TAG, "convert: " + song);
                            }else {
                                Log.i(TAG, "convert: convertFactoryForTopList null");
                            }
                        }
                        return songs;
                    }
                };
            }
        };
    }

    private Converter.Factory convertFactoryForBanner(){
        return new Converter.Factory() {
            @Nullable
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, final Annotation[] annotations, Retrofit retrofit) {
                return new Converter<ResponseBody, List<Banner>>() {
                    @Override
                    public List<Banner> convert(ResponseBody value) throws IOException {
                        Gson gson = new Gson();
                        String s = value.string();
                        JsonArray array = new JsonParser().parse(s).getAsJsonObject().getAsJsonArray("pic");
                        List<Banner> banners = new ArrayList<>();
                        for(JsonElement song : array){
                            if (song != null){
                                banners.add(gson.fromJson(song, Banner.class));
                            }
                        }
                        return banners;
                    }
                };
            }
        };
    }

    //===========================================================================================
    //===========================================================================================
    public void getSearchSong(int type, int limit, int offset, String key, final HttpListener<List<Song>> listener){
        Retrofit retrofit = createRetrofit2(convertFactoryForSearch());
        SearchApi service = retrofit.create(SearchApi.class);
        service.getSearchSong(type, limit, offset, key).enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                listener.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                listener.onFailure(call, t);
            }
        });
    }

    public void getSong(long id, final HttpListener<Song> listener){
        Retrofit retrofit = createRetrofit2(convertFactoryForSong());
        SongApi service = retrofit.create(SongApi.class);
        service.getSong(id).enqueue(new Callback<Song>() {
            @Override
            public void onResponse(Call<Song> call, Response<Song> response) {
                listener.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<Song> call, Throwable t) {
                listener.onFailure(call, t);
            }
        });
    }

    public void getSongs(long id, final HttpListener<SongList> listener){
        Retrofit retrofit = createRetrofit(convertFactoryForSongArray());
        SongListApi service = retrofit.create(SongListApi.class);
        service.getSongList(id).enqueue(new Callback<SongList>() {
            @Override
            public void onResponse(Call<SongList> call, Response<SongList> response) {
                listener.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<SongList> call, Throwable t) {
                listener.onFailure(call, t);
            }
        });
    }

    public void getSongList(long id, final HttpListener<SongList> listener) {
        Retrofit retrofit = createRetrofit(convertFactoryForSongList());
        SongListApi service = retrofit.create(SongListApi.class);
        service.getSongList(id).enqueue(new Callback<SongList>() {
            @Override
            public void onResponse(Call<SongList> call, Response<SongList> response) {
                listener.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<SongList> call, Throwable t) {
                listener.onFailure(call, t);
            }
        });
    }

    public SongList getSongList(long id) {
        SongList songList = null;
        Retrofit retrofit = createRetrofit(convertFactoryForSongList());
        SongListApi service = retrofit.create(SongListApi.class);
        try {
            songList =  service.getSongList(id).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return songList;
    }

    public void getSongListForList(String cat, String order,
                                   int offset, boolean total, int limit,
                                   final HttpListener<List<SongList>> listener)
            throws Exception {
        Retrofit retrofit = createRetrofit(convertFactoryForList());
        SongListApiForList service = retrofit.create(SongListApiForList.class);
        service.getSongListForList(URLDecoder.decode(cat, "UTF-8"), order, offset, total, limit).enqueue(new Callback<List<SongList>>() {
            @Override
            public void onResponse(Call<List<SongList>> call, Response<List<SongList>> response) {
                listener.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<List<SongList>> call, Throwable t) {
                listener.onFailure(call, t);
            }
        });
    }

    private Retrofit createRetrofit(Converter.Factory factory){
        return new Retrofit.Builder().baseUrl(Constant.BASE_URL)
                .addConverterFactory(factory).build();
    }

    private Retrofit createRetrofit2(Converter.Factory factory){
        return new Retrofit.Builder().baseUrl(Constant.BASE_URL2)
                .addConverterFactory(factory).build();
    }

    private Converter.Factory convertFactoryForSongArray(){
        return new Converter.Factory() {
            @Nullable
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, final Annotation[] annotations, Retrofit retrofit) {
                return new Converter<ResponseBody, List<Song>>() {
                    @Override
                    public List<Song> convert(ResponseBody value) throws IOException {
                        Gson gson = new Gson();
                        String s = value.string();
                        JsonObject object = new JsonParser().parse(s).getAsJsonObject().getAsJsonObject("result");
                        JsonArray array = object.getAsJsonArray("tracks");
                        List<Song> songs = new ArrayList<>();
                        for(JsonElement song : array){
                            if (song != null){
                                songs.add(gson.fromJson(song, Song.class));
                            }else {
                                System.out.println("==========================> songs null ");
                            }
                        }
                        return songs;
                    }
                };
            }
        };
    }

    private Converter.Factory convertFactoryForSong(){
        return new Converter.Factory() {
            @Nullable
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
                return new Converter<ResponseBody, Song>() {
                    @Override
                    public Song convert(ResponseBody value) throws IOException {
                        Gson gson = new Gson();
                        String s = value.string();
                        JsonObject object = new JsonParser().parse(s).getAsJsonObject().getAsJsonObject("result");
                        object = object.getAsJsonObject("song");
                        return gson.fromJson(object, Song.class);
                    }
                };
            }
        };
    }

    private Converter.Factory convertFactoryForSongList(){
        return new Converter.Factory() {
            @Nullable
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
                return new Converter<ResponseBody, SongList>() {
                    @Override
                    public SongList convert(ResponseBody value) throws IOException {
                        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
                            @Override
                            public boolean shouldSkipField(FieldAttributes f) {
                                return f.getName().contains("tracks");
                            }

                            @Override
                            public boolean shouldSkipClass(Class<?> clazz) {
                                return false;
                            }
                        }).create();
                        String s = value.string();
                        JsonObject object = new JsonParser().parse(s).getAsJsonObject().getAsJsonObject("result");
//                        JsonArray array = object.getAsJsonArray("tracks");
                        return gson.fromJson(object, SongList.class);
                    }
                };
            }
        };
    }

    private Converter.Factory convertFactoryForSearch(){
        return new Converter.Factory(){
            @Nullable
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
                return new Converter<ResponseBody, List<Song>>() {
                    @Override
                    public List<Song> convert(ResponseBody value) throws IOException {
                        Gson gson = new Gson();
                        String s = value.string();
                        JsonObject object = new JsonParser().parse(s).getAsJsonObject().getAsJsonObject("result");
                        JsonArray array = object.getAsJsonArray("songs");
                        List<Song> songs = new ArrayList<>();
                        for(JsonElement song : array){
                            if (song != null){
                                songs.add(gson.fromJson(song, Song.class));
                            }else {
                                System.out.println("==========================> songs null ");
                            }
                        }
                        return songs;
                    }
                };
            }
        };
    }

    private Converter.Factory convertFactoryForList(){
        return new Converter.Factory() {
            @Nullable
            @Override
            public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
                return new Converter<ResponseBody, List<SongList>>() {
                    @Override
                    public List<SongList> convert(ResponseBody value) throws IOException {
                        Gson gson = new Gson();
                        String s = value.string();
                        int x = "{\"playlists\":".length();
                        int y = s.indexOf(",\"code\":200");
                        String str = s.substring(x, y);
//                                System.out.println("x====>" + x + "y====>" + y);
//                                System.out.println("str====>" + str);
                        Type type = new TypeToken<ArrayList<SongList>>(){}.getType();
//                        List<SongList> bean = gson.fromJson(str, type);
                        return gson.fromJson(str, type);
//                                System.out.println("=============> bean " + bean.toString());
//                        return bean;
                    }
                };
            }
        };
    }

}
