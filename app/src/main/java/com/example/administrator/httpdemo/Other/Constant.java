package com.example.administrator.httpdemo.Other;

import android.widget.ListView;

import com.example.administrator.httpdemo.Data.entity.CreateSongList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/3.
 */

public class Constant {
    public static final int TYPE_LOCAL = 1;
    public static final int TYPE_NET = 2;
    public static final int TYPE_BMOB = 3;

    public static final int TYPE_CREATE = 4;
    public static final int TYPE_COLLECT = 5;
    public static final int TYPE_SONGLIST = 6;
    public static final int TYPE_TOPLIST = 7;
    public static final int TYPE_SHARE = 8;

    public static final String BASE_URL = "http://music.163.com/api/";

    public static final String BASE_URL2 = "http://s.music.163.com/";

    public static List<CreateSongList> sCreateSongLists = new ArrayList<>();

    public static List<Long> getTopListIDFor1(){
        List<Long> topListID = new ArrayList<>();
        topListID.add((long) 19723756);
        topListID.add((long) 3779629);
        topListID.add((long) 2884035);
        topListID.add((long) 3778678);
        return topListID;
    }

    public static List<Long> getTopListIDFor2(){
        List<Long> topListID = new ArrayList<>();
        topListID.add((long) 71385702);
        topListID.add((long) 71384707);
        topListID.add((long) 10520166);
        topListID.add((long) 180106);
        topListID.add((long) 60198);
        topListID.add((long) 3812895);
        topListID.add((long) 27135204);
        topListID.add((long) 21845217);
        topListID.add((long) 11641012);
        topListID.add((long) 60131);
        topListID.add((long) 112463);
        topListID.add((long) 112504);
        topListID.add((long) 64016);
        topListID.add((long) 10169002);
        topListID.add((long) 1899724);
        return topListID;
    }

    //语种 风格 情感 场景 主题

    public static List<String> getTagForTheme(){
        List<String> tags = new ArrayList<>();
        tags.add("经典");
        tags.add("翻唱");tags.add("榜单");tags.add("现场");
        tags.add("KTV");tags.add("DJ");tags.add("网络歌曲");
        tags.add("器乐");
        return tags;
    }

    public static List<String> getTagForPlace(){
        List<String> tags = new ArrayList<>();

        tags.add("运动");tags.add("驾驶");
        tags.add("学习");tags.add("工作");
        tags.add("清晨");tags.add("夜晚");tags.add("午后");
        tags.add("游戏");tags.add("旅行");tags.add("散步");
        tags.add("酒吧");tags.add("夜店");
        tags.add("咖啡厅");tags.add("地铁");
        tags.add("校园");tags.add("婚礼");
        tags.add("约会");tags.add("休息");
        return tags;
    }

    public static List<String> getTagForMood(){
        List<String> tags = new ArrayList<>();

        tags.add("快乐");tags.add("美好");
        tags.add("安静");tags.add("伤感");tags.add("寂寞");
        tags.add("思念");tags.add("孤独");tags.add("怀旧");
        tags.add("悲伤");tags.add("感动");tags.add("治愈");
        tags.add("放松");tags.add("清新");tags.add("浪漫");
        tags.add("兴奋");tags.add("性感");
        tags.add("励志");
        return tags;
    }

    public static List<String> getTagForStyle(){
        List<String> tags = new ArrayList<>();
        tags.add("流行");
        tags.add("摇滚");tags.add("民谣");
        tags.add("电子");tags.add("影视原声");
        tags.add("ACG");tags.add("轻音乐");tags.add("新世纪");
        tags.add("爵士");tags.add("古典");
        tags.add("乡村");tags.add("说唱");tags.add("世界音乐");
        tags.add("古风");tags.add("儿歌");
        tags.add("朋克");tags.add("布鲁斯");
        tags.add("金属");tags.add("雷鬼");tags.add("英伦");
        tags.add("民族");tags.add("后摇");tags.add("拉丁");

        return tags;
    }

    public static List<String> getTagForLanguage(){
        List<String> tags = new ArrayList<>();
        tags.add("华语");tags.add("欧美");tags.add("粤语");
        tags.add("日语");tags.add("韩语");
        tags.add("纯音乐");tags.add("小语种");
        return tags;
    }
}
