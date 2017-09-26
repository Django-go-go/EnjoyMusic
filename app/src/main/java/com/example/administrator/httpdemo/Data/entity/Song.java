package com.example.administrator.httpdemo.Data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 */

//{
//        "result": {
//        "song": {
//        "id": 485612504,
//        "name": "说",
//        "artists": [
//        {
//        "id": 6454,
//        "name": "张信哲",
//        "picUrl": "http://p1.music.126.net/P8A2-QG6XvISE5b8ytL9_Q==/1426066581780644.jpg?param=25x25x1"
//        }
//        ],
//        "album": {
//        "id": 35644806,
//        "name": "说",
//        "artist": {
//        "id": 0,
//        "name": "",
//        "picUrl": null
//        },
//        "picUrl": "http://p1.music.126.net/ZgUs9Zz5DQAbPHYMAWRIqg==/19145795974660791.jpg"
//        },
//        "audio": "http://m2.music.126.net/hmZoNQaqzZALvVp0rE7faA==/0.mp3",
//        "lyric": [
//        "真的就要这样 半天了不说一句话",
//        "沉默 承诺 看谁先崩塌",
//        "从哪里来的爱 停在了这里怎样",
//        "痛得就好像  爱的 一样烫",
//        "寂寞不缺  体谅  ",
//        "每个人都有过的疯狂 ",
//        "放手也是有趣的希望",
//        "至少 你我都一样",
//        "你说  孤独是装傻",
//        "我说  放手是做假",
//        "他说  算了吧",
//        "没有爱的爱情 就像个笑话",
//        "我说  去你的坚强",
//        "你说  给我个肩膀  ",
//        "那些吻过的伤",
//        "别再找过去 隐藏 ",
//        "真的就要这样 半天了不说一句话",
//        "沉默 承诺 看谁先崩塌",
//        "从哪里来的爱 停在了这里怎样",
//        "痛得就好像  爱的 一样烫",
//        "寂寞不缺  体谅  ",
//        "每个人都有过的疯狂 ",
//        "放手也是有趣的希望",
//        "至少 你我都一样",
//        "你说  孤独是装傻",
//        "我说  放手是做假",
//        "他说  算了吧",
//        "没有爱的爱情 就像个笑话",
//        "我说  去你的坚强",
//        "你说  给我个肩膀  ",
//        "那些吻过的伤",
//        "别再找过去 隐藏"
//        ],
//        "djProgramId": 0
//        }
//        },
//        "code": 200
//        }
public class Song implements Parcelable {
    private long id;
    private String name;
    private String picUrl;
    private String audio;
    private List<String> lyric;
    private List<Artist> artists;

    public Song() {
    }

    public Song(long id, String name, String picUrl, String audio, List<String> lyric, List<Artist> artists) {
        this.id = id;
        this.name = name;
        this.picUrl = picUrl;
        this.audio = audio;
        this.lyric = lyric;
        this.artists = artists;
    }


    protected Song(Parcel in) {
        id = in.readLong();
        name = in.readString();
        picUrl = in.readString();
        audio = in.readString();
        lyric = in.createStringArrayList();
        artists = in.createTypedArrayList(Artist.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(picUrl);
        dest.writeString(audio);
        dest.writeStringList(lyric);
        dest.writeTypedList(artists);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        if (picUrl == null){
            return null;
        }
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public List<String> getLyric() {
        return lyric;
    }

    public void setLyric(List<String> lyric) {
        this.lyric = lyric;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", audio='" + audio + '\'' +
                ", lyric=" + lyric +
                ", artists=" + artists +
                '}';
    }

}
