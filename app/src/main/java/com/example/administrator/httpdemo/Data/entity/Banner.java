package com.example.administrator.httpdemo.Data.entity;

/**
 * Created by Administrator on 2017/9/20.
 */

public class Banner {
    private String randpic;

    public String getUrl() {
        return randpic;
    }

    public void setUrl(String url) {
        this.randpic = url;
    }

    @Override
    public String toString() {
        return "Banner{" +
                "url='" + randpic + '\'' +
                '}';
    }
}
