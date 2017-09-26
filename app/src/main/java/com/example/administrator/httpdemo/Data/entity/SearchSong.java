package com.example.administrator.httpdemo.Data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Administrator on 2017/9/11.
 */
/*
{
      "bitrate_fee": "{\"0\":\"0|0\",\"1\":\"0|0\"}",
      "weight": "51390",
      "songname": "泡沫",
      "songid": "14945107",
      "has_mv": "0",
      "yyr_artist": "0",
      "resource_type_ext": "0",
      "artistname": "G.E.M.邓紫棋",
      "info": "泡沫-G.E.M.邓紫棋",
      "resource_provider": "1",
      "control": "0000000000",
      "encrypted_songid": "6706e40b530956c6e620L"
    }
 */
public class SearchSong implements Parcelable{
    private String songid;
    private String songname;
    private String artistname;

    public SearchSong(String songid, String songname, String artistname) {
        this.songid = songid;
        this.songname = songname;
        this.artistname = artistname;
    }

    protected SearchSong(Parcel in) {
        songid = in.readString();
        songname = in.readString();
        artistname = in.readString();
    }

    public static final Creator<SearchSong> CREATOR = new Creator<SearchSong>() {
        @Override
        public SearchSong createFromParcel(Parcel in) {
            return new SearchSong(in);
        }

        @Override
        public SearchSong[] newArray(int size) {
            return new SearchSong[size];
        }
    };

    public String getSongid() {
        return songid;
    }

    public void setSongid(String songid) {
        this.songid = songid;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getArtistname() {
        return artistname;
    }

    public void setArtistname(String artistname) {
        this.artistname = artistname;
    }

    @Override
    public String toString() {
        return "SearchSong{" +
                "songid='" + songid + '\'' +
                ", songname='" + songname + '\'' +
                ", artistname='" + artistname + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(songid);
        dest.writeString(songname);
        dest.writeString(artistname);
    }
}
