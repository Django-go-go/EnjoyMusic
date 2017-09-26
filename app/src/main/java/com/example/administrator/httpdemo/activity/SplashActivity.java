package com.example.administrator.httpdemo.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.administrator.httpdemo.Data.entity.MyUser;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.ScreenUtils;

public class SplashActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ScreenUtils.setTranslucent(SplashActivity.this);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLogin()){
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }else {
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 3000);
    }

    public boolean isLogin(){
        return MyUser.getCurrentUser() != null;
    }
}
