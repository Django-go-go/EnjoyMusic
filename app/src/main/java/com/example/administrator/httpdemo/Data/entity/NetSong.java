package com.example.administrator.httpdemo.Data.entity;

/**
 * Created by Administrator on 2017/9/13.
 */

public class NetSong {
    private String pic_huge;
    private String file_link;
    private String album_500_500;
    private String pic_radio;
    private String artist_500_500;
    private String pic_big;
    private String pic_small;
    private String artist_1000_1000;

    public NetSong() {
    }

    public String getAlbum_500_500() {
        return album_500_500;
    }

    public void setAlbum_500_500(String album_500_500) {
        this.album_500_500 = album_500_500;
    }

    public String getPic_radio() {
        return pic_radio;
    }

    public void setPic_radio(String pic_radio) {
        this.pic_radio = pic_radio;
    }

    public String getArtist_500_500() {
        return artist_500_500;
    }

    public void setArtist_500_500(String artist_500_500) {
        this.artist_500_500 = artist_500_500;
    }

    public String getPic_big() {
        return pic_big;
    }

    public void setPic_big(String pic_big) {
        this.pic_big = pic_big;
    }

    public String getPic_small() {
        return pic_small;
    }

    public void setPic_small(String pic_small) {
        this.pic_small = pic_small;
    }

    public String getArtist_1000_1000() {
        return artist_1000_1000;
    }

    public void setArtist_1000_1000(String artist_1000_1000) {
        this.artist_1000_1000 = artist_1000_1000;
    }

    public String getFile_link() {
        return file_link;
    }

    public void setFile_link(String file_link) {
        this.file_link = file_link;
    }

    public String getPic_huge() {
        return pic_huge;
    }

    public void setPic_huge(String pic_huge) {
        this.pic_huge = pic_huge;
    }

    @Override
    public String toString() {
        return "NetSong{" +
                "pic_huge='" + pic_huge + '\'' +
                ", file_link='" + file_link + '\'' +
                ", album_500_500='" + album_500_500 + '\'' +
                ", pic_radio='" + pic_radio + '\'' +
                ", artist_500_500='" + artist_500_500 + '\'' +
                ", pic_big='" + pic_big + '\'' +
                ", pic_small='" + pic_small + '\'' +
                ", artist_1000_1000='" + artist_1000_1000 + '\'' +
                '}';
    }
}
