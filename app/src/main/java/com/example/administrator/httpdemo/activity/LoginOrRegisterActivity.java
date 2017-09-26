package com.example.administrator.httpdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.httpdemo.Data.entity.MyUser;
import com.example.administrator.httpdemo.R;
import com.example.administrator.httpdemo.Utils.MyException;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class LoginOrRegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mEditText1, mEditText2, mEditText3;
    private TextView mTextView;
    private Button mButton1, mButton2;
    private LinearLayout mLayout;

    private String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);
        initView();
        Intent intent = getIntent();
        str = intent.getStringExtra("str");
        if (str.equals("login") ) {
            initToolbar("登录");
            mButton2.setText("登录");
            mLayout.setVisibility(View.GONE);
        }else{
            initToolbar("注册");
            mTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void initView(){
        mEditText1 = (EditText) findViewById(R.id.number_editText);
        mEditText2 = (EditText) findViewById(R.id.password_editText);
        mEditText3 = (EditText) findViewById(R.id.SMS_editText);
        mButton1 = (Button) findViewById(R.id.SMS_button);
        mButton2 = (Button) findViewById(R.id.bt_res);
        mTextView = (TextView) findViewById(R.id.notremeber_tv);
        mLayout = (LinearLayout) findViewById(R.id.register_linear);
        mButton2.setOnClickListener(this);
        mButton1.setOnClickListener(this);
        mTextView.setOnClickListener(this);
    }

    private void initToolbar(String title){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginOrRegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.SMS_button:{
                String number = mEditText1.getText().toString();
                requestSMS(number);
                break;
            }
            case R.id.bt_res:
                if(str.equals("register")){
                    String number = mEditText1.getText().toString();
                    String password = mEditText2.getText().toString();
                    String code = mEditText3.getText().toString();
                    MyUser myUser = new MyUser();
                    myUser.setMobilePhoneNumber(number);
                    myUser.setPassword(password);
                    registerAndLogin(myUser, code);
                }else if (str.equals("login")){
                    String number = mEditText1.getText().toString();
                    String password = mEditText2.getText().toString();
                    login(number, password);
                }else if (str.equals("重置密码")){
                    String number = mEditText1.getText().toString();
                    String password = mEditText2.getText().toString();
                    String code = mEditText3.getText().toString();
                    requestSMS(number);
                    resetPassword(number, code, password, getApplicationContext());
                }
                break;
            case R.id.notremeber_tv:
                mLayout.setVisibility(View.VISIBLE);
                mTextView.setVisibility(View.GONE);
                mEditText2.setHint("请重置密码");
                str = "重置密码";
                initToolbar("忘记密码");
                break;
            default:
                break;
        }
    }
    public void requestSMS(String numberPhone){
        BmobSMS.requestSMSCode(numberPhone, "mySMS", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                MyException.solve("requestSMS", e, LoginOrRegisterActivity.this);
            }
        });
    }

    public void registerAndLogin(MyUser myUser, String SMSCode){
        myUser.signOrLogin(SMSCode, new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if(e == null){
                    Intent intent = new Intent(LoginOrRegisterActivity.this, MainActivity.class);
                    LoginOrRegisterActivity.this.startActivity(intent);
                    LoginOrRegisterActivity.this.finish();
                }
            }
        });
    }

    public void resetPassword(final String numberPhone, String SMSCode, final String password, final Context context){
        MyUser.resetPasswordBySMSCode(SMSCode, password, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                MyException.solve("resetPassword", e, LoginOrRegisterActivity.this);
                if (e == null){
                    login(numberPhone, password);
                }
            }
        });
    }

    public void login(String numberPhone, String password){
        MyUser.loginByAccount(numberPhone, password, new LogInListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                MyException.solve("login", e, LoginOrRegisterActivity.this);
                if (e == null){
                    Intent intent = new Intent(LoginOrRegisterActivity.this, MainActivity.class);
                    LoginOrRegisterActivity.this.startActivity(intent);
                    LoginOrRegisterActivity.this.finish();
                }
            }
        });
    }

    public boolean isLogin(){
        if(MyUser.getCurrentUser() != null){
            return true;
        }else {
            return false;
        }
    }

    public void updateMyUser(MyUser newUser){
        if(isLogin()){
            newUser.update(MyUser.getCurrentUser().getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    MyException.solve("login", e, LoginOrRegisterActivity.this);
                }
            });
        }
    }

    public void logout(){
        MyUser.logOut();
    }
}
