package com.example.administrator.httpdemo.Data.entity;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/7/23.
 */

public class MyUser extends BmobUser {
    private String name;
    private Integer sex;
    private Integer age;
    private String place;
    private String photoID;
    private String backgroundID;

    public MyUser() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPhotoID() {
        return photoID;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    public String getBackgroundID() {
        return backgroundID;
    }

    public void setBackgroundID(String backgroundID) {
        this.backgroundID = backgroundID;
    }

    @Override
    public String toString() {
        return "MyUser{" +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                ", place='" + place + '\'' +
                ", photoID='" + photoID + '\'' +
                ", backgroundID='" + backgroundID + '\'' +
                '}';
    }
}
