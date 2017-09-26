package com.example.administrator.httpdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.httpdemo.Data.entity.MyUser;
import com.example.administrator.httpdemo.Listener.InfoActivityListener;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.ToastUtils;
import com.example.administrator.httpdemo.activity.base.BaseActivity;
import com.example.administrator.httpdemo.activity.presenter.InfoPresenter;
import com.example.administrator.httpdemo.activity.view.InfoView;
import com.example.administrator.httpdemo.fragment.dialogfragment.InfoDialogFragment;
import com.example.administrator.httpdemo.imagePicker.ImagePickerActivity;
import com.squareup.picasso.Picasso;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class InfoActivity extends BaseActivity<InfoView, InfoPresenter>
        implements View.OnClickListener, InfoActivityListener, InfoView{

    public static final String TAG = "InfoActivity";

    public static final int RESULT_PHOTO = 1;
    public static final int RESULT_BACKGROUND = 2;
    public static final int REQUEST_OK = 3;
    private int count = 0;

    private FrameLayout group_photo, group_background, group_nickname
            , group_sex, group_place;

    private ImageView image_photo, image_background;

    private TextView text_nickname, text_sex, text_place;

    private InfoDialogFragment mInfoDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mPresenter = createPresenter();

        mPresenter.getMyUser();

        initToolbar();

        initView();

    }

    private void initView(){
        group_photo = (FrameLayout) findViewById(R.id.frame_1);
        group_background = (FrameLayout) findViewById(R.id.frame_2);
        group_nickname = (FrameLayout) findViewById(R.id.frame_3);
        group_sex = (FrameLayout) findViewById(R.id.frame_4);
        group_place = (FrameLayout) findViewById(R.id.frame_5);

        image_photo = (ImageView) group_photo.findViewById(R.id.photo_iv);
        image_background = (ImageView) group_background.findViewById(R.id.background_iv);

        text_nickname = (TextView) group_nickname.findViewById(R.id.nickname_tv);
        text_place = (TextView) group_place.findViewById(R.id.place_tv);
        text_sex = (TextView) group_sex.findViewById(R.id.sex_tv);

        group_photo.setOnClickListener(this);
        group_background.setOnClickListener(this);
        group_nickname.setOnClickListener(this);
        group_sex.setOnClickListener(this);
        group_place.setOnClickListener(this);
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.info_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextView textView = (TextView)toolbar.findViewById(R.id.toolbar_tv1);
        textView.setText("个人资料");
        ImageView imageView = (ImageView)toolbar.findViewById(R.id.toolbar_iv);
        imageView.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 0){
                    InfoActivity.this.setResult(MainActivity.RESULT_OK);
                }
                finish();
            }
        });
    }

    @Override
    public InfoPresenter createPresenter() {
        return new InfoPresenter(this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.frame_1:
                startActivityForResult(new Intent(InfoActivity.this, ImagePickerActivity.class).putExtra("photo", "photo"), REQUEST_OK);
                break;
            case R.id.frame_2:
                startActivityForResult(new Intent(InfoActivity.this, ImagePickerActivity.class).putExtra("background", "background"), REQUEST_OK);
                break;
            case R.id.frame_3:{
                if (mInfoDialogFragment == null){
                    mInfoDialogFragment = new InfoDialogFragment();
                }
                Bundle bundle = new Bundle();
                bundle.putString("nickname", "nickname");
                mInfoDialogFragment.setArguments(bundle);
                mInfoDialogFragment.show(getSupportFragmentManager(), "InfoDialogFragment");
                break;
            }
            case R.id.frame_4:{
                if (mInfoDialogFragment == null){
                    mInfoDialogFragment = new InfoDialogFragment();
                }
                Bundle bundle = new Bundle();
                bundle.putString("sex", "sex");
                mInfoDialogFragment.setArguments(bundle);
                mInfoDialogFragment.show(getSupportFragmentManager(), "InfoDialogFragment");
                break;
            }
            case R.id.frame_5:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OK){
            if (RESULT_PHOTO == resultCode){
                String path = data.getStringExtra("path");
                Picasso.with(this).load(new File(path)).into(image_photo);
                uploadPhotoFile(path);
                count++;
            }
            if (RESULT_BACKGROUND == resultCode){
                String path = data.getStringExtra("path");
                Picasso.with(this).load(new File(path)).into(image_background);
                uploadBackgroundFile(path);
                count++;
            }
        }
    }

    private void uploadPhotoFile(String path){
        ToastUtils.showShort(this, "正在上传...");
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    MyUser m = new MyUser();
                    m.setPhotoID(bmobFile.getFileUrl());
                    m.update(BmobUser.getCurrentUser(MyUser.class).getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                ToastUtils.showShort(InfoActivity.this, "上传成功");
                            }
                            Log.e(TAG, "upload: ", e);
                        }
                    });
                }else {
                    Log.e(TAG, "upload: ", e);
                }
            }
        });
    }

    private void uploadBackgroundFile(String path){
        ToastUtils.showShort(this, "正在上传...");
        final BmobFile bmobFile = new BmobFile(new File(path));
        bmobFile.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    MyUser m = new MyUser();
                    m.setBackgroundID(bmobFile.getFileUrl());
                    m.update(BmobUser.getCurrentUser(MyUser.class).getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                ToastUtils.showShort(InfoActivity.this, "上传成功");
                            }
                            Log.e(TAG, "upload: ", e);
                        }
                    });
                }else {
                    Log.e(TAG, "upload: ", e);
                }
            }
        });
    }

    @Override
    public void chooseSex(int pos) {
        MyUser myuser = new MyUser();

        if (pos == 0){
            text_sex.setText("男");
            myuser.setSex(1);
        }
        if (pos == 1){
            text_sex.setText("女");
            myuser.setSex(0);
        }
        mInfoDialogFragment.dismiss();

        myuser.update(BmobUser.getCurrentUser(MyUser.class).getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                Log.e(TAG, "update: ", e);
            }
        });
    }

    @Override
    public void setNickname(String name) {
        text_nickname.setText(name);
        mInfoDialogFragment.dismiss();
        MyUser myuser = new MyUser();
        myuser.setName(name);
        myuser.setUsername(name);
        myuser.update(BmobUser.getCurrentUser(MyUser.class).getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                Log.e(TAG, "update: ", e);
            }
        });
        count++;
    }

    @Override
    public void getMyUser(MyUser myUser) {
        if (myUser.getName() != null){
            text_nickname.setText(myUser.getName());
        }

        if (myUser.getPhotoID() != null){
            Picasso.with(this).load(myUser.getPhotoID()).into(image_photo);
        }

        if (myUser.getBackgroundID() != null){
            Picasso.with(this).load(myUser.getBackgroundID()).into(image_background);
        }

        if (myUser.getSex() != null){
            text_sex.setText(myUser.getSex() == 1 ? "男" : "女");
        }
    }

    @Override
    public void onBackPressed() {
        if (count > 0){
            setResult(MainActivity.RESULT_OK);
        }
        super.onBackPressed();
    }
}
