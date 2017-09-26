package com.example.administrator.httpdemo.Data.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/9/12.
 */

public class CollectSongList extends BmobObject {
    private String userID;
    private String imageCoverUrl;
    private String id;
    private String name;
    private Integer count;
    private String tag;

    public CollectSongList() {
    }

    public CollectSongList(String userID, String imageCoverUrl, String id, String name, Integer count, String tag) {
        this.userID = userID;
        this.imageCoverUrl = imageCoverUrl;
        this.id = id;
        this.name = name;
        this.count = count;
        this.tag = tag;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getImageCoverUrl() {
        return imageCoverUrl;
    }

    public void setImageCoverUrl(String imageCoverUrl) {
        this.imageCoverUrl = imageCoverUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "CollectSongList{" +
                "userID='" + userID + '\'' +
                ", imageCoverUrl='" + imageCoverUrl + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", tag='" + tag + '\'' +
                '}';
    }
}
