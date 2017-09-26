/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * This file is part of FileExplorer.
 *
 * FileExplorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FileExplorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SwiFTP.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.administrator.httpdemo.Data.Local;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video;
import android.util.Log;

import com.example.administrator.httpdemo.Data.entity.Mp3Info;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.FileUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ContentHelper {

    public static final String TAG = "ContentHelper";

    public static HashSet<String> sDocMimeTypesSet = new HashSet<String>() {
        {
            add("text/plain");
            add("text/plain");
            add("application/pdf");
            add("application/msword");
            add("application/vnd.ms-excel");
            add("application/vnd.ms-excel");
        }
    };

    public enum FileCategory {
        All, Music, Video, Picture, Theme, Doc, Zip, Apk, Other, Favorite
    }

    public static HashMap<FileCategory, Integer> categoryNames = new HashMap<FileCategory, Integer>();

    static {
        categoryNames.put(FileCategory.All, R.string.category_all);
        categoryNames.put(FileCategory.Music, R.string.category_music);
        categoryNames.put(FileCategory.Video, R.string.category_video);
        categoryNames.put(FileCategory.Picture, R.string.category_picture);
        categoryNames.put(FileCategory.Theme, R.string.category_theme);
        categoryNames.put(FileCategory.Doc, R.string.category_document);
        categoryNames.put(FileCategory.Zip, R.string.category_zip);
        categoryNames.put(FileCategory.Apk, R.string.category_apk);
        categoryNames.put(FileCategory.Other, R.string.category_other);
        categoryNames.put(FileCategory.Favorite, R.string.category_favorite);
    }

    private Context mContext;

    public ContentHelper(Context context) {
        mContext = context;
    }

    private String buildDocSelection() {
        StringBuilder selection = new StringBuilder();
        Iterator<String> iter = sDocMimeTypesSet.iterator();
        while(iter.hasNext()) {
            selection.append("(" + FileColumns.MIME_TYPE + "=='" + iter.next() + "') OR ");
        }
        return  selection.substring(0, selection.lastIndexOf(")") + 1);
    }

    private String buildSelectionByCategory(FileCategory cat) {
        String selection;
        switch (cat) {
            case Theme:
                selection = FileColumns.DATA + " LIKE '%.mtz'";
                break;
            case Doc:
                selection = buildDocSelection();
                break;
            case Zip:
                selection = "(" + FileColumns.MIME_TYPE + " == '" + "application/zip" + "')";
                break;
            case Apk:
                selection = FileColumns.DATA + " LIKE '%.apk'";
                break;
            default:
                selection = null;
        }
        return selection;
    }

    private Uri getContentUriByCategory(FileCategory cat) {
        Uri uri;
        String volumeName = "external";
        switch(cat) {
            case Theme:
            case Doc:
            case Zip:
            case Apk:
                uri = Files.getContentUri(volumeName);
                break;
            case Music:
                uri = Audio.Media.getContentUri(volumeName);
                Log.i(TAG, "getContentUriByCategory: " + uri);
                break;
            case Video:
                uri = Video.Media.getContentUri(volumeName);
                break;
            case Picture:
                uri = Images.Media.getContentUri(volumeName);
                break;
           default:
               uri = null;
        }
        return uri;
    }

    private String buildSortOrder(SortMethod sort) {
        String sortOrder = null;
        switch (sort) {
            case name:
                sortOrder = FileColumns.TITLE + " asc";
                break;
            case size:
                sortOrder = FileColumns.SIZE + " asc";
                break;
            case date:
                sortOrder = FileColumns.DATE_MODIFIED + " desc";
                break;
            case type:
                sortOrder = FileColumns.MIME_TYPE + " asc, " + FileColumns.TITLE + " asc";
                break;
        }
        return sortOrder;
    }

    private Cursor query(FileCategory fc, SortMethod sort) {
        Uri uri = getContentUriByCategory(fc);
        String selection = buildSelectionByCategory(fc);
        String sortOrder = buildSortOrder(sort);
        if (uri == null) {
            return null;
        }

        return mContext.getContentResolver().query(uri, null, selection, null, sortOrder);
    }

    public List<Mp3Info> getMusic(){
        ArrayList<Mp3Info> mp3Infos = new ArrayList<>();
        Cursor cursor = null;
        try{
            cursor = query(FileCategory.Music, SortMethod.size);

            for (int i = 0; i < cursor.getCount(); i++) {
                Mp3Info mp3Info = new Mp3Info();
                if(cursor.moveToNext()){
                    long id = cursor.getLong(cursor
                            .getColumnIndex(MediaStore.Audio.Media._ID));   //音乐id
                    long album_id = cursor.getLong(cursor
                            .getColumnIndex(Audio.Media.ALBUM_ID));         //专辑id
                    String title = cursor.getString((cursor
                            .getColumnIndex(MediaStore.Audio.Media.TITLE)));//音乐标题
                    String artist = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ARTIST));//艺术家
                    long duration = cursor.getLong(cursor
                            .getColumnIndex(MediaStore.Audio.Media.DURATION));//时长
                    long size = cursor.getLong(cursor
                            .getColumnIndex(MediaStore.Audio.Media.SIZE));  //文件大小
                    String url = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA)); //文件路径
                    int isMusic = cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));//是否为音乐
                    String album = cursor.getString(cursor
                            .getColumnIndex(Audio.Media.ALBUM));
                    if (isMusic != 0) {     //只把音乐添加到集合当中
                        mp3Info.setId(id);
                        mp3Info.setTitle(title);
                        mp3Info.setArtist(artist);
                        mp3Info.setDuration(duration);
                        mp3Info.setSize(size);
                        mp3Info.setUrl(url);
                        mp3Info.setAlbum(album);
                        mp3Info.setAlbumId(album_id);
                        mp3Info.setPicUrl("content://media/external/audio/albumart" + "/" + mp3Info.getAlbumId());
                        mp3Infos.add(mp3Info);
                    }
                }
            }
        }catch (Exception e){
            System.out.println("exception " + e.toString());
        }finally {
            if (cursor != null)
                cursor.close();
        }
        return mp3Infos;
    }

    public String getUrl(long id){
        String path = null;
        String[] projection = {Audio.Albums.ALBUM_ID, Audio.Albums.ALBUM_ART};
        String selection = "(" + Audio.AlbumColumns.ALBUM_ID + " == " + id + ")";
        Log.i(TAG, "getUrl: " + Audio.Albums.EXTERNAL_CONTENT_URI);
        Cursor cursor = mContext.getContentResolver().
                query(Audio.Albums.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursor != null){
            if (cursor.moveToNext()){
                int columnIndex = cursor.getColumnIndex(projection[1]);
                path = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return path;
    }

    public enum SortMethod {
        name, size, date, type
    }

    private SortMethod mSort = SortMethod.name;

    private boolean mFileFirst;

    private HashMap<SortMethod, Comparator> mComparatorList = new HashMap<SortMethod, Comparator>(){
        {
            put(SortMethod.name, cmpName);
            put(SortMethod.size, cmpSize);
            put(SortMethod.date, cmpDate);
            put(SortMethod.type, cmpType);
        }
    };

    public void setSortMethog(SortMethod s) {
        mSort = s;
    }

    public SortMethod getSortMethod() {
        return mSort;
    }

    public void setFileFirst(boolean f) {
        mFileFirst = f;
    }

    public Comparator getComparator() {
        return mComparatorList.get(mSort);
    }

    private abstract class FileComparator implements Comparator<FileInfo> {

        @Override
        public int compare(FileInfo object1, FileInfo object2) {
            if (object1.IsDir == object2.IsDir) {
                return doCompare(object1, object2);
            }

            if (mFileFirst) {
                // the files are listed before the dirs
                return (object1.IsDir ? 1 : -1);
            } else {
                // the dir-s are listed before the files
                return object1.IsDir ? -1 : 1;
            }
        }

        protected abstract int doCompare(FileInfo object1, FileInfo object2);
    }

    private Comparator cmpName = new FileComparator() {
        @Override
        public int doCompare(FileInfo object1, FileInfo object2) {
            return object1.fileName.compareToIgnoreCase(object2.fileName);
        }
    };

    private Comparator cmpSize = new FileComparator() {
        @Override
        public int doCompare(FileInfo object1, FileInfo object2) {
            return longToCompareInt(object1.fileSize - object2.fileSize);
        }
    };

    private Comparator cmpDate = new FileComparator() {
        @Override
        public int doCompare(FileInfo object1, FileInfo object2) {
            return longToCompareInt(object2.ModifiedDate - object1.ModifiedDate);
        }
    };

    private int longToCompareInt(long result) {
        return result > 0 ? 1 : (result < 0 ? -1 : 0);
    }

    private Comparator cmpType = new FileComparator() {
        @Override
        public int doCompare(FileInfo object1, FileInfo object2) {
            int result = FileUtils.getExtFromFilename(object1.fileName).compareToIgnoreCase(
                    FileUtils.getExtFromFilename(object2.fileName));
            if (result != 0)
                return result;

            return FileUtils.getNameFromFilename(object1.fileName).compareToIgnoreCase(
                    FileUtils.getNameFromFilename(object2.fileName));
        }
    };
}
