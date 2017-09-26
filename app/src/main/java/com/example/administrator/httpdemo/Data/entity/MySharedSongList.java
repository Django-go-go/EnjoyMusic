package com.example.administrator.httpdemo.Data.entity;

import android.widget.ListView;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/8/24.
 */

public class MySharedSongList extends BmobObject {
    private List<String> bmobSongIDs;
    private List<String> netSongIDs;
    private String name;
    private String imageURL;

    public MySharedSongList() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public List<String> getBmobSongIDs() {
        return bmobSongIDs;
    }

    public void setBmobSongIDs(List<String> bmobSongIDs) {
        this.bmobSongIDs = bmobSongIDs;
    }

    public List<String> getNetSongIDs() {
        return netSongIDs;
    }

    public void setNetSongIDs(List<String> netSongIDs) {
        this.netSongIDs = netSongIDs;
    }

    @Override
    public String toString() {
        return "MySharedSongList{" +
                "bmobSongIDs=" + bmobSongIDs +
                ", netSongIDs=" + netSongIDs +
                ", name='" + name + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
