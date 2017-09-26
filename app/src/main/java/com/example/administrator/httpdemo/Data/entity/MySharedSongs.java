package com.example.administrator.httpdemo.Data.entity;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/7/20.
 */

public class MySharedSongs extends BmobObject {
    private Integer language;
    private Integer style;
    private Integer mood;
    private Integer theme;

    private String name;//song name;
    private String lrc;//url
    private String album;//url
    private String artist;//singer
    private String url;//path
    private String fromId;

    public MySharedSongs() {
    }

    public MySharedSongs(String name, String artist) {
        this.name = name;
        this.artist = artist;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Integer getStyle() {
        return style;
    }

    public void setStyle(Integer style) {
        this.style = style;
    }

    public Integer getMood() {
        return mood;
    }

    public void setMood(Integer mood) {
        this.mood = mood;
    }

    public Integer getTheme() {
        return theme;
    }

    public void setTheme(Integer theme) {
        this.theme = theme;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "MySharedSongs{" +
                "language=" + language +
                ", style=" + style +
                ", mood=" + mood +
                ", theme=" + theme +
                ", name='" + name + '\'' +
                ", lrc='" + lrc + '\'' +
                ", album='" + album + '\'' +
                ", artist='" + artist + '\'' +
                ", url='" + url + '\'' +
                ", fromId='" + fromId + '\'' +
                '}';
    }
}
