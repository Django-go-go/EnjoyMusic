package com.example.administrator.httpdemo.Event;

/**
 * Created by Administrator on 2017/8/26.
 */

public class UpdateEvent {
    private int updatePos;

    public UpdateEvent(int updatePos) {
        this.updatePos = updatePos;
    }

    public int getUpdatePos() {
        return updatePos;
    }
}
