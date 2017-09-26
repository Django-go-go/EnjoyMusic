/*
 *
 *  * Copyright (C) 2015 Eason.Lai (easonline7@gmail.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.example.administrator.httpdemo.imagePicker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.ScreenUtils;
import com.example.administrator.httpdemo.activity.InfoActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.administrator.httpdemo.activity.InfoActivity.RESULT_BACKGROUND;
import static com.example.administrator.httpdemo.activity.InfoActivity.RESULT_PHOTO;

public class ImagePickerActivity extends AppCompatActivity implements OnImagesLoadedListener {

    private GridView mGridView;
    private Toolbar mToolbar;
    private ListPopupWindow mFolderPopupWindow;

    private List<String> pathList = new ArrayList<>();
    private List<ImageSet> mImageSets = new ArrayList<>();

    private MyAdapter mMyAdapter;

    private DataSource mDataSource;

    private String res = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_grid);

        if (getIntent().getStringExtra("photo") != null){
            res = getIntent().getStringExtra("photo");
        }
        if (getIntent().getStringExtra("background") != null){
            res = getIntent().getStringExtra("background");
        }

        initToolbar();

        mGridView = (GridView) findViewById(R.id.imagePicker_gridview);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("path", (String) mMyAdapter.getItem(position));
                if (res.equals("photo")){
                    ImagePickerActivity.this.setResult(RESULT_PHOTO, intent);
                    finish();
                }
                if (res.equals("background")){
                    ImagePickerActivity.this.setResult(RESULT_BACKGROUND, intent);
                    finish();
                }
            }
        });

        mDataSource = new LocalDataSource(this);
        mDataSource.provideMediaItems(this);

    }

    private void initToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.imagePicker_toolbar);
        mToolbar.setTitle("全部图片");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_choose:
                        createPopupFolderList(ScreenUtils.getScreenWidth(ImagePickerActivity.this),
                                ScreenUtils.getScreenHeight(ImagePickerActivity.this)*3/5);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onImagesLoaded(List<ImageSet> imageSetList) {
        mImageSets.clear();
        mImageSets = imageSetList;

        pathList.clear();
        for (ImageSet imageSet : imageSetList){
            for (ImageItem imageItem : imageSet.imageItems){
                pathList.add(imageItem.path);
            }
        }
        mMyAdapter = new MyAdapter();
        mGridView.setAdapter(mMyAdapter);
    }

    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return pathList.size();
        }

        @Override
        public Object getItem(int position) {
            return pathList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = LayoutInflater.from(ImagePickerActivity.this).inflate(R.layout.item_imagepick_grid, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Picasso.with(ImagePickerActivity.this)
                    .load(new File(pathList.get(position)))
                    .centerCrop()
                    .resize(ScreenUtils.getScreenWidth(ImagePickerActivity.this)/3, ScreenUtils.getScreenWidth(ImagePickerActivity.this)/3)
                    .placeholder(R.drawable.default_img)
                    .error(R.drawable.default_img)
                    .into(viewHolder.mImageView);

            return convertView;
        }

        class ViewHolder{
            ImageView mImageView;
            CheckBox mCheckBox;

            ViewHolder(View view) {
                mImageView = (ImageView) view.findViewById(R.id.image);
                mCheckBox = (CheckBox) view.findViewById(R.id.checkbox);
            }
        }
    }

    private void createPopupFolderList(int width, int height) {
        mFolderPopupWindow = new ListPopupWindow(this);
        mFolderPopupWindow.setAdapter(new ImageSetAdapter());
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setWidth(width);
        mFolderPopupWindow.setHeight(height);
//        mFolderPopupWindow.setAnimationStyle(R.style.popupwindow_anim_style);
        mFolderPopupWindow.setAnchorView(mToolbar);
        mFolderPopupWindow.setModal(true);

        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pathList.clear();
                for (ImageItem item : mImageSets.get(i).imageItems){
                    pathList.add(item.path);
                }
                mMyAdapter.notifyDataSetChanged();
                mFolderPopupWindow.dismiss();
                mToolbar.setTitle(mImageSets.get(i).name);
            }
        });
        mFolderPopupWindow.show();
    }

    private class ImageSetAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mImageSets.size();
        }
        @Override
        public Object getItem(int i) {
            return mImageSets.get(i);
        }
        @Override
        public long getItemId(int i) {
            return i;
        }
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if(view == null){
                view = LayoutInflater.from(ImagePickerActivity.this).inflate(R.layout.item_list_folder, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }

            holder.name.setText(mImageSets.get(i).name);
            holder.size.setText(String.valueOf(mImageSets.get(i).imageItems.size()));

            Picasso.with(ImagePickerActivity.this)
                    .load(new File(mImageSets.get(i).cover.path))
                    .centerCrop()
                    .resize(50, 50)
                    .placeholder(R.drawable.default_img)
                    .error(R.drawable.default_img)
                    .into(holder.cover);
            return view;
        }

        class ViewHolder{
            ImageView cover;
            TextView name;
            TextView size;

            ViewHolder(View view){
                cover = (ImageView)view.findViewById(R.id.cover);
                name = (TextView) view.findViewById(R.id.name);
                size = (TextView) view.findViewById(R.id.size);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my, menu);
        menu.getItem(1).setVisible(true);
        return true;
    }
}
