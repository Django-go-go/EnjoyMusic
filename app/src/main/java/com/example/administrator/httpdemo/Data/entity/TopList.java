package com.example.administrator.httpdemo.Data.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/9/8.
 */

public class TopList implements Parcelable{
    private String name;
    private String type;
    private String comment;
    private String pic_s210;
    private String pic_s260;

    public TopList(String name, String type, String comment, String pic_s210, String pic_s260) {
        this.name = name;
        this.type = type;
        this.comment = comment;
        this.pic_s210 = pic_s210;
        this.pic_s260 = pic_s260;
    }

    protected TopList(Parcel in) {
        name = in.readString();
        type = in.readString();
        comment = in.readString();
        pic_s210 = in.readString();
        pic_s260 = in.readString();
    }

    public static final Creator<TopList> CREATOR = new Creator<TopList>() {
        @Override
        public TopList createFromParcel(Parcel in) {
            return new TopList(in);
        }

        @Override
        public TopList[] newArray(int size) {
            return new TopList[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPic_s210() {
        return pic_s210;
    }

    public void setPic_s210(String pic_s210) {
        this.pic_s210 = pic_s210;
    }

    public String getPic_s260() {
        return pic_s260;
    }

    public void setPic_s260(String pic_s260) {
        this.pic_s260 = pic_s260;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(comment);
        dest.writeString(pic_s210);
        dest.writeString(pic_s260);
    }

    @Override
    public String toString() {
        return "TopList{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", comment='" + comment + '\'' +
                ", pic_s210='" + pic_s210 + '\'' +
                ", pic_s260='" + pic_s260 + '\'' +
                '}';
    }
}
