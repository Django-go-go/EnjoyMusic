package com.example.administrator.httpdemo.Utils;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.example.administrator.httpdemo.Data.Local.ContentHelper;
import com.example.administrator.httpdemo.Data.entity.Artist;
import com.example.administrator.httpdemo.Data.entity.CreateSongList;
import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.Data.entity.MySharedSongList;
import com.example.administrator.httpdemo.Data.entity.MySharedSongs;
import com.example.administrator.httpdemo.Data.entity.MyUser;
import com.example.administrator.httpdemo.Data.entity.NetSong;
import com.example.administrator.httpdemo.Data.entity.Song;
import com.example.administrator.httpdemo.Data.entity.Song2;
import com.example.administrator.httpdemo.Data.entity.SongRecorder;
import com.example.administrator.httpdemo.Other.Constant;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Administrator on 2017/8/24.
 */

public class OtherUtils {

    public static String getUserId(){
        return BmobUser.getCurrentUser(MyUser.class).getObjectId();
    }

    public static String getImageUrl(NetSong netSong){
        if (netSong.getPic_huge() != null && netSong.getPic_huge().length() > 0){
            return netSong.getPic_huge();
        }
        if (netSong.getAlbum_500_500() != null && netSong.getAlbum_500_500().length() > 0){
            return netSong.getAlbum_500_500();
        }
        if (netSong.getArtist_500_500() != null && netSong.getArtist_500_500().length() > 0){
            return netSong.getArtist_500_500();
        }
        if (netSong.getArtist_1000_1000() != null && netSong.getArtist_1000_1000().length() > 0){
            return netSong.getArtist_1000_1000();
        }
        if (netSong.getPic_big() != null && netSong.getPic_big().length() > 0){
            return netSong.getPic_huge();
        }
        if (netSong.getPic_small() != null && netSong.getPic_small().length() > 0){
            return netSong.getPic_small();
        }
        return " ";
    }

    public static boolean bitmapIsHave(String path){
        if (BitmapFactory.decodeFile(path) == null){
            return false;
        }else {
            return true;
        }
    }

    public static int getNumber(MySharedSongList songList){
        int i = 0;
        int j = 0;
        if (songList.getBmobSongIDs() == null){
            i = 0;
        }else {
            i = songList.getBmobSongIDs().size();
        }
        if (songList.getNetSongIDs() == null){
            j = 0;
        }else {
            j = songList.getNetSongIDs().size();
        }
        return i + j;
    }

    public static int getNumber(CreateSongList createSongList){
        int i = 0;
        int j = 0;
        int k = 0;
        if (createSongList.getBmobSongIds() == null){
            i = 0;
        }else {
            i = createSongList.getBmobSongIds().size();
        }
        if (createSongList.getNetSongIds() == null){
            j = 0;
        }else {
            j = createSongList.getNetSongIds().size();
        }
        if (createSongList.getLocalSongIds() == null){
            k = 0;
        }else {
            k = createSongList.getLocalSongIds().size();
        }
        return i + j + k;
    }

    public static int getNumber2(CreateSongList createSongList){
        int i = 0;
        int j = 0;
        if (createSongList.getBmobSongIds() == null){
            i = 0;
        }else {
            i = createSongList.getBmobSongIds().size();
        }
        if (createSongList.getNetSongIds() == null){
            j = 0;
        }else {
            j = createSongList.getNetSongIds().size();
        }

        return i + j;
    }

    public static Long getAlumIdForID(Context context, String id){
        ContentHelper helper = new ContentHelper(context);
        List<Mp3Info> mp3Infos = helper.getMusic();

        for (Mp3Info mp3Info : mp3Infos) {
            if (mp3Info.getId() == Long.parseLong(id)) {
                return mp3Info.getAlbumId();
            }
        }
        return null;
    }

    public static Mp3Info SongToMp3Info(Song song){
        Mp3Info mp3Info = new Mp3Info();
        mp3Info.setId(song.getId());
        mp3Info.setPicUrl(song.getArtists().get(0).getPicUrl());
        mp3Info.setArtist(song.getArtists().get(0).getName());
        mp3Info.setArtists(song.getArtists());
        mp3Info.setLyric(song.getLyric());
        mp3Info.setTitle(song.getName());
        mp3Info.setUrl(song.getAudio());
        mp3Info.setType(Constant.TYPE_NET);
        return mp3Info;
    }

    public static Mp3Info Song2ToMp3Info(Song2 song){
        Mp3Info mp3Info = new Mp3Info();
        mp3Info.setId_String(song.getSong_id());
        mp3Info.setPicUrl(song.getPic_big());
        mp3Info.setArtist(song.getAuthor());
        mp3Info.setTitle(song.getTitle());
        mp3Info.setType(Constant.TYPE_NET);
        mp3Info.setUrl(song.getUrl());
        return mp3Info;
    }

    public static Song Mp3InfoToSong(Mp3Info mp3Info){
        Song song = new Song();
        song.setId(mp3Info.getId());
        song.setPicUrl(mp3Info.getPicUrl());
        song.setArtists(mp3Info.getArtists());
        song.setLyric(mp3Info.getLyric());
        song.setName(mp3Info.getTitle());
        song.setAudio(mp3Info.getUrl());
        return song;
    }

    public static Mp3Info MySharedSongsToMp3Info(MySharedSongs mySharedSongs){
        Mp3Info mp3Info = new Mp3Info();
        mp3Info.setUrl(mySharedSongs.getUrl());
        mp3Info.setArtist(mySharedSongs.getArtist());
        mp3Info.setPicUrl(mySharedSongs.getAlbum());
        mp3Info.setId_String(mySharedSongs.getObjectId());
        mp3Info.setType(Constant.TYPE_BMOB);
        mp3Info.setTitle(mySharedSongs.getName());
        return mp3Info;
    }

    public static MySharedSongs Mp3InfoToMySharedSongs(Mp3Info mp3Info){
        MySharedSongs mySharedSongs = new MySharedSongs();
        mySharedSongs.setName(mp3Info.getTitle());
        mySharedSongs.setAlbum(mp3Info.getPicUrl());
        mySharedSongs.setUrl(mp3Info.getUrl());
        mySharedSongs.setArtist(mp3Info.getArtist());
        mySharedSongs.setFromId(getUserId());
        return mySharedSongs;
    }

    public static Mp3Info SongRecorderToMp3Info(SongRecorder songRecorder){
        Mp3Info mp3Info = new Mp3Info();
        mp3Info.setUrl(songRecorder.getUrl());
        mp3Info.setType(songRecorder.getType());
        mp3Info.setArtist(songRecorder.getArtist());
        mp3Info.setPicUrl(songRecorder.getPicUrl());
        mp3Info.setTitle(songRecorder.getTitle());

        if (songRecorder.getType() == Constant.TYPE_BMOB){
            mp3Info.setId_String(songRecorder.getSpecialID());
        }
        if (songRecorder.getType() == Constant.TYPE_LOCAL){
            mp3Info.setId(Long.parseLong(songRecorder.getSpecialID()));
        }
        if (songRecorder.getType() == Constant.TYPE_NET){
            mp3Info.setId_String(songRecorder.getSpecialID());
        }

        return mp3Info;
    }

    public static SongRecorder Mp3InfoToSongRecorder(Mp3Info mp3Info){
        SongRecorder songRecorder = new SongRecorder();
        songRecorder.setUrl(mp3Info.getUrl());
        songRecorder.setArtist(mp3Info.getArtist());
        songRecorder.setTitle(mp3Info.getTitle());
        songRecorder.setType(mp3Info.getType());
        songRecorder.setPicUrl(mp3Info.getPicUrl());

        if (mp3Info.getType() == Constant.TYPE_BMOB){
            songRecorder.setSpecialID(mp3Info.getId_String());
        }
        if (mp3Info.getType() == Constant.TYPE_LOCAL){
            songRecorder.setSpecialID(String.valueOf(mp3Info.getId()));
        }
        if (mp3Info.getType() == Constant.TYPE_NET){
            songRecorder.setSpecialID(mp3Info.getId_String());
        }

        return songRecorder;
    }
}
