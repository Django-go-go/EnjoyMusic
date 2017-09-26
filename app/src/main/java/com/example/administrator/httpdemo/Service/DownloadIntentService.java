package com.example.administrator.httpdemo.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.example.administrator.httpdemo.MusicApp;
import com.example.administrator.httpdemo.Utils.FileUtils;
import com.example.administrator.httpdemo.Utils.IoUtils;
import com.example.administrator.httpdemo.Utils.StorageUtils;
import com.example.administrator.httpdemo.Utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DownloadIntentService extends IntentService {
    private static final String ACTION_DOWNLOAD_NETFILE = "com.example.administrator.httpdemo.Service.action.DOWNLOAD_NETFILE";
    private static final String ACTION_DOWNLOAD_BMOBFILE = "com.example.administrator.httpdemo.Service.action.DOWNLOAD_BMOBFILE";

    private static final String EXTRA_PARAM1 = "com.example.administrator.httpdemo.Service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.administrator.httpdemo.Service.extra.PARAM2";

    private static final String TAG = "DownloadIntentService";

    public DownloadIntentService() {
        super("DownloadIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startDownloadNet(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DownloadIntentService.class);
        intent.setAction(ACTION_DOWNLOAD_NETFILE);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startDownloadBmob(Context context, String param1, String param2) {
        Intent intent = new Intent(context, DownloadIntentService.class);
        intent.setAction(ACTION_DOWNLOAD_BMOBFILE);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD_NETFILE.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleDownloadNet(param1, param2);
            } else if (ACTION_DOWNLOAD_BMOBFILE.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleDownloadBmob(param1, param2);
            }
        }
    }

    private void handleDownloadNet(String param1, String param2) {
        if (param1 != null && param1.length() > 0) {
            if (param2 == null) {
                downloadFile(param1, null, "image");
            }else {
                downloadFile(param1, param2 + ".mp3", "music");
            }
        } else {
            ToastUtils.showShort(this, "资源无法获取,抱歉!");
        }
    }

    private void downloadFile(final String path, final String extra, final String dir){
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(1000, TimeUnit.MINUTES)
                .readTimeout(1000, TimeUnit.MINUTES).writeTimeout(1000, TimeUnit.MINUTES).build();
        Request request = new Request.Builder().url(path).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    ResponseBody responseBody = response.body();
                    if (responseBody != null){
                        InputStream inputStream = responseBody.byteStream();
                        FileOutputStream fileOutputStream = null;
                        File musicfile = new File(StorageUtils.getDownloadDirectory(DownloadIntentService.this), dir);
                        if (extra == null){
                            fileOutputStream = new FileOutputStream(new File(musicfile, FileUtils.getFullNameFromFilepath(path)));
                        }else {
                            fileOutputStream = new FileOutputStream(new File(musicfile, extra));
                        }

                        try{
                            IoUtils.downStream(responseBody, inputStream, fileOutputStream, new IoUtils.CopyListener() {
                                @Override
                                public boolean onBytesCopied(int current, int total) {
                                    Log.i(TAG, "onBytesCopied: ====> " + current + " / " + total);
                                    return true;
                                }
                            });
                        }catch (IOException e){
                            IoUtils.closeQuietly(inputStream);
                            IoUtils.closeQuietly(fileOutputStream);
                        }finally {
                            IoUtils.closeQuietly(inputStream);
                            IoUtils.closeQuietly(fileOutputStream);
                        }
                    }
                }
            }
        });


//        File saveFile = new File(musicfile, file.getFilename());
//        file.download(saveFile, new DownloadFileListener() {
//
//            @Override
//            public void onStart() {
//                ToastUtils.showShort(mContext, "开始下载...");
//            }
//
//            @Override
//            public void done(String savePath,BmobException e) {
//                if(e==null){
//                    ToastUtils.showShort(mContext, "下载成功,保存路径:"+savePath);
//                    Log.i(MusicApp.TAG, "done: " + savePath);
//                }else{
//                    ToastUtils.showShort(mContext,  "下载失败："+e.getErrorCode()+","+e.getMessage());
//                    Log.i(MusicApp.TAG, "下载失败："+e.getErrorCode()+","+e.getMessage());
//                }
//            }
//
//            @Override
//            public void onProgress(Integer value, long newworkSpeed) {
////                Log.i(TAG,"下载进度："+value+","+newworkSpeed);
//            }
//
//        });
    }

    private void handleDownloadBmob(String param1, String param2) {
        BmobFile bmobFile = new BmobFile(
                FileUtils.getFullNameFromFilepath(param1)+".mp3", "", param1);
        downloadFile(bmobFile, "music");

        BmobFile imageFile = new BmobFile(
                FileUtils.getFullNameFromFilepath(param2), "", param2);
        downloadFile(imageFile, "image");
    }

    private void downloadFile(BmobFile file, String dir){
        File musicfile = new File(StorageUtils.getDownloadDirectory(this), dir);
        File saveFile = new File(musicfile, file.getFilename());

        file.download(saveFile, new DownloadFileListener() {
            @Override
            public void onStart() {
                ToastUtils.showShort(DownloadIntentService.this, "开始下载...");
            }

            @Override
            public void done(String savePath,BmobException e) {
                if(e == null){
                    Log.i(MusicApp.TAG, "done: " + savePath);
                }else{
                    Log.i(MusicApp.TAG, "下载失败："+e.getErrorCode()+","+e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i(TAG,"下载进度："+value+","+newworkSpeed);
            }

        });
    }
}
