package com.intermediary.job.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.intermediary.job.base.BasePager;

/**
 * Created by kalogchen on 2016/12/23.
 */

public class MyPager extends BasePager {
    public MyPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        //修改标题栏
        tvTitle.setText("个人中心");

        //在这里写职位信息列表，然后添加到fragment中
        TextView text = new TextView(mActivity);
        text.setText("个人中心");
        text.setTextColor(Color.WHITE);
        text.setTextSize(25);
        text.setGravity(Gravity.CENTER);

        //向frameLayout中动态的添加布局
        flContent.addView(text);
    }
}
