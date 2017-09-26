package com.example.administrator.httpdemo.Data.entity;

/**
 * Created by Administrator on 2017/9/11.
 */

public class HotWord {
    private String word;

    public HotWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "HotWord{" +
                "word='" + word + '\'' +
                '}';
    }
}
