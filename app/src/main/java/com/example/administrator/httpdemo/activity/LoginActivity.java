package com.example.administrator.httpdemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.ImageUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView mImageView;
    private Button mbutton1, mbutton2;

    public LoginActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        Bitmap bitmap = ImageUtils.createReflectionImageWithOrigin(ImageUtils.getRoundedCornerBitmap(
                ImageUtils.drawableToBitmap(getDrawable(R.drawable.music)), 90));
        mImageView.setImageBitmap(bitmap);
    }

    void initView(){
//        findViewById(R.id.relative_back).setBackground(getDrawable(R.drawable.login));
        mbutton1 = (Button) findViewById(R.id.login_button);
        mbutton2 = (Button) findViewById(R.id.register_button);
        mImageView = (ImageView) findViewById(R.id.imageView_music);
        mbutton1.setOnClickListener(this);
        mbutton2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, LoginOrRegisterActivity.class);
        if(v.getId() == R.id.login_button){
            intent.putExtra("str", "login");
            startActivity(intent);
            finish();
        }else if(v.getId() == R.id.register_button){
            intent.putExtra("str", "register");
            startActivity(intent);
            finish();
        }
    }
}
