package com.example.administrator.httpdemo.Data.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/8/22.
 */
@Entity
public class SongRecorder {

    @Id(autoincrement = true)
    private Long id;
    @Index(unique = true)
    private String specialID; // 歌曲ID 3
    private String title; // 歌曲名称 0
    private String artist; // 歌手名称 2
    private String url; // 歌曲路径 5
    private String picUrl;
    @NotNull
    private int type;
    @Generated(hash = 818591280)
    public SongRecorder(Long id, String specialID, String title, String artist,
            String url, String picUrl, int type) {
        this.id = id;
        this.specialID = specialID;
        this.title = title;
        this.artist = artist;
        this.url = url;
        this.picUrl = picUrl;
        this.type = type;
    }
    @Generated(hash = 1522171774)
    public SongRecorder() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSpecialID() {
        return this.specialID;
    }
    public void setSpecialID(String specialID) {
        this.specialID = specialID;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getArtist() {
        return this.artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getPicUrl() {
        return this.picUrl;
    }
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SongRecorder{" +
                "id=" + id +
                ", specialID='" + specialID + '\'' +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", url='" + url + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", type=" + type +
                '}';
    }
}
