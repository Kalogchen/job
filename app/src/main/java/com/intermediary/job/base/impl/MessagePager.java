package com.intermediary.job.base.impl;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.intermediary.job.R;
import com.intermediary.job.activity.acceptActivity;
import com.intermediary.job.base.BasePager;
import com.intermediary.job.utlis.SharedPreferencesUtils;

/**
 * Created by kalogchen on 2016/12/23.
 */

public class MessagePager extends BasePager {

    public MessagePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        int messagePager = SharedPreferencesUtils.getInt(mActivity, "messagePager", 0);
        if (messagePager == 0) {
            //刚进来初始化数据，从其他页面再切换过来就不重新初始化
            //修改标题栏
            tvTitle.setText("消 息");
            initMessage();
        }
    }

    private void initMessage() {
        View view = View.inflate(mActivity, R.layout.activity_message, null);
        LinearLayout llMessage = (LinearLayout) view.findViewById(R.id.ll_message);
        RelativeLayout rlSearchRecord = (RelativeLayout) view.findViewById(R.id.rl_search_record);
        RelativeLayout rlInterviewNotice = (RelativeLayout) view.findViewById(R.id.rl_interview_notice);
        flContent.addView(llMessage);
        SharedPreferencesUtils.setInt(mActivity, "messagePager", 1);

        //查看应聘记录
        rlSearchRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转动应聘记录界面
                mActivity.startActivity(new Intent(mActivity, acceptActivity.class));
            }
        });

    }
}
