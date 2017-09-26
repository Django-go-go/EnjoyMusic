package com.example.administrator.httpdemo.Data.entity;

import com.example.administrator.httpdemo.Other.Constant;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/1/22.
 */
public class Mp3Info extends BmobObject{
    private long id; // 歌曲ID 3
    private String id_String;
    private String title; // 歌曲名称 0
    private String album; // 专辑 7
    private long albumId;//专辑ID 6
    private String artist; // 歌手名称 2
    private long duration; // 歌曲时长 1
    private long size; // 歌曲大小 8
    private String url; // 歌曲路径 5
    private String picUrl;
    private List<String> lyric;
    private List<Artist> artists;
    private int type = Constant.TYPE_LOCAL;

    public Mp3Info() {
        super();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public List<String> getLyric() {
        return lyric;
    }

    public void setLyric(List<String> lyric) {
        this.lyric = lyric;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId_String() {
        return id_String;
    }

    public void setId_String(String id_String) {
        this.id_String = id_String;
    }

    @Override
    public String toString() {
        return "Mp3Info{" +
                "id=" + id +
                ", id_String='" + id_String + '\'' +
                ", title='" + title + '\'' +
                ", album='" + album + '\'' +
                ", albumId=" + albumId +
                ", artist='" + artist + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", url='" + url + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", lyric=" + lyric +
                ", artists=" + artists +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null){
            return false;
        }
        if (this == obj){
            return true;
        }
        if (obj instanceof Mp3Info){
            Mp3Info mp3Info = (Mp3Info) obj;
            if (this.getId() != 0 && mp3Info.getId() != 0)
                return this.getId() == mp3Info.getId();
            if (this.getId_String() != null && mp3Info.getId_String() != null)
                return this.getId_String().equals(mp3Info.getId_String());
        }
        return false;
    }
}
