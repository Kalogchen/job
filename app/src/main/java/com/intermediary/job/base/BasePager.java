package com.intermediary.job.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.intermediary.job.R;

/**
 * 主页面下4个子页面的基类
 * Created by kalogchen on 2016/12/23.
 */

public class BasePager {

    public Activity mActivity;

    //布局文件
    public View mRootView;
    //标题栏
    public TextView tvTitle;
    //内容
    public FrameLayout flContent;

    public BasePager(Activity activity) {
        mActivity = activity;

        initViews();
    }

    //初始化布局
    private void initViews() {
        mRootView = View.inflate(mActivity, R.layout.base_pager, null);
        tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        flContent = (FrameLayout) mRootView.findViewById(R.id.fl_content);
    }

    /**
     * 初始化数据
     */
    public void initData() {

    }

}
