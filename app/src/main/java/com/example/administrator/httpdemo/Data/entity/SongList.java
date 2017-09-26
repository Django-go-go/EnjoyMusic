package com.example.administrator.httpdemo.Data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */

public class SongList implements Parcelable{
    private String name;
    private long id;
    private String coverImgUrl;
    private List<Song> tracks;
    private int trackCount;
    private String description;
    private Author creator;


    public SongList(String name, long id, String coverImgUrl, List<Song> tracks, int trackCount, String description, Author creator) {
        this.name = name;
        this.id = id;
        this.coverImgUrl = coverImgUrl;
        this.tracks = tracks;
        this.trackCount = trackCount;
        this.description = description;
        this.creator = creator;
    }

    public SongList() {
    }


    protected SongList(Parcel in) {
        name = in.readString();
        id = in.readLong();
        coverImgUrl = in.readString();
        tracks = in.createTypedArrayList(Song.CREATOR);
        trackCount = in.readInt();
        description = in.readString();
        creator = in.readParcelable(Author.class.getClassLoader());
    }

    public static final Creator<SongList> CREATOR = new Creator<SongList>() {
        @Override
        public SongList createFromParcel(Parcel in) {
            return new SongList(in);
        }

        @Override
        public SongList[] newArray(int size) {
            return new SongList[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public List<Song> getTracks() {
        return tracks;
    }

    public void setTracks(List<Song> tracks) {
        this.tracks = tracks;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Author getCreator() {
        return creator;
    }

    public void setCreator(Author creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "SongList{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", coverImgUrl='" + coverImgUrl + '\'' +
                ", tracks=" + tracks +
                ", trackCount=" + trackCount +
                ", description='" + description + '\'' +
                ", creator=" + creator +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeLong(id);
        dest.writeString(coverImgUrl);
        dest.writeTypedList(tracks);
        dest.writeInt(trackCount);
        dest.writeString(description);
        dest.writeParcelable(creator, flags);
    }


}
