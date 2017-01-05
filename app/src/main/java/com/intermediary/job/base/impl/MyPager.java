package com.intermediary.job.base.impl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intermediary.job.R;
import com.intermediary.job.activity.changePwdActivity;
import com.intermediary.job.activity.loginActivity;
import com.intermediary.job.activity.resumeActivity;
import com.intermediary.job.base.BasePager;
import com.intermediary.job.utlis.SharedPreferencesUtils;

/**
 * Created by kalogchen on 2016/12/23.
 */

public class MyPager extends BasePager {

    private TextView tvDialogText;
    private Button btOk;
    private Button btCancel;

    public MyPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        int myPager = SharedPreferencesUtils.getInt(mActivity, "myPager", 0);
        if (myPager == 0) {
            //刚进来初始化数据，从其他页面再切换过来就不重新初始化
            //修改标题栏
            tvTitle.setText("个人中心");
            initMyPager();
        }
    }

    private void initMyPager() {
        View view = View.inflate(mActivity, R.layout.activity_my_pager, null);
        LinearLayout llMyPager = (LinearLayout) view.findViewById(R.id.ll_my_pager);
        TextView tvResume = (TextView) view.findViewById(R.id.tv_resume);
        TextView tvChangePwd = (TextView) view.findViewById(R.id.tv_change_password);
        TextView tvLogout = (TextView) view.findViewById(R.id.tv_logout);
        flContent.addView(llMyPager);
        SharedPreferencesUtils.setInt(mActivity, "myPager", 1);

        //简历管理
        tvResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, resumeActivity.class));
            }
        });

        //修改密码
        tvChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mActivity, changePwdActivity.class));
            }
        });

        //退出登录
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                final AlertDialog dialog = builder.create();
                View inflate = View.inflate(mActivity, R.layout.dialog_apply_job, null);
                tvDialogText = (TextView) inflate.findViewById(R.id.tv_dialog_text);
                btOk = (Button) inflate.findViewById(R.id.bt_ok);
                btCancel = (Button) inflate.findViewById(R.id.bt_cancel);
                tvDialogText.setText("确定退出登录吗？ ");
                //将自定义的布局文件设置给dialog
                dialog.setView(inflate, 0, 0, 0, 0);

                btOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.startActivity(new Intent(mActivity, loginActivity.class));
                        dialog.dismiss();
                        mActivity.finish();
                    }
                });

                btCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


    }
}
