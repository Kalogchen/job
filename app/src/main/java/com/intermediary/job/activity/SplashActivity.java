package com.intermediary.job.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.intermediary.job.R;

/**
 * 闪屏页
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        checkVersion();
    }

    //检测版本是否需要跟新，这里不需要更新版本
    public void checkVersion() {

        //进入登录界面
        startActivity(new Intent(this, loginActivity.class));
        finish();

    }

}
