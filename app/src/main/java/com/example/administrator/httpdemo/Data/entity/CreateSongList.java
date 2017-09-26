package com.example.administrator.httpdemo.Data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/7/25.
 */

public class CreateSongList extends BmobObject{
    private String userID;
    private String name;
    private List<String> netSongIds;
    private List<String> localSongIds;
    private List<String> bmobSongIds;
    private String imageURL;

    public CreateSongList() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<String> getBmobSongIds() {
        return bmobSongIds;
    }

    public void setBmobSongIds(List<String> bmobSongIds) {
        this.bmobSongIds = bmobSongIds;
    }

    public List<String> getLocalSongIds() {
        return localSongIds;
    }

    public void setLocalSongIds(List<String> localSongIds) {
        this.localSongIds = localSongIds;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getNetSongIds() {
        return netSongIds;
    }

    public void setNetSongIds(List<String> netSongIds) {
        this.netSongIds = netSongIds;
    }

    @Override
    public String toString() {
        return "CreateSongList{" +
                "userID='" + userID + '\'' +
                ", name='" + name + '\'' +
                ", netSongIds=" + netSongIds +
                ", localSongIds=" + localSongIds +
                ", bmobSongIds=" + bmobSongIds +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }

}
