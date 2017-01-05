package com.intermediary.job.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.intermediary.job.R;
import com.intermediary.job.model.returnData;
import com.intermediary.job.utlis.HttpCallbackListener;
import com.intermediary.job.utlis.HttpPost;
import com.intermediary.job.utlis.SharedPreferencesUtils;
import com.intermediary.job.utlis.ToastUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 修改密码
 * Created by kalogchen on 2017/1/1.
 */

public class changePwdActivity extends Activity {
    private EditText etOldPwd;
    private EditText etNewPwd;
    private EditText etConfirmPwd;
    private Button btChangePwd;
    private String account;
    private String confirmOldPwd;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        //获取密码文本框和注册按钮对象
        etOldPwd = (EditText) findViewById(R.id.et_old_password);
        etNewPwd = (EditText) findViewById(R.id.et_new_password);
        etConfirmPwd = (EditText) findViewById(R.id.et_confirmPwd);
        btChangePwd = (Button) findViewById(R.id.bt_change_pwd);

        //获取用户登录的账号密码
        account = SharedPreferencesUtils.getString(changePwdActivity.this, "account", "");
        confirmOldPwd = SharedPreferencesUtils.getString(changePwdActivity.this, "passWord", "");

        btChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInfo();
            }
        });
    }

    //检测修改的密码是否符合规范，符合则进行修改
    private void checkInfo() {
        String oldPwd = etOldPwd.getText().toString();
        String newPwd = etNewPwd.getText().toString();
        String confirmNewPwd = etConfirmPwd.getText().toString();

        //判断数据格式是否正确
        if (oldPwd.isEmpty() || newPwd.isEmpty()) {
            ToastUtils.showToast(changePwdActivity.this, "旧密码或新密码不能为空，请重新输入");
        }else if (!oldPwd.equals(confirmOldPwd)) {
            ToastUtils.showToast(changePwdActivity.this, "旧密码输入错误，请重新输入");
        }else if (newPwd.length()<6 || newPwd.length()>20) {
            ToastUtils.showToast(changePwdActivity.this, "新密码长度为6-20个字符，请重新输入");
        }else if (!newPwd.equals(confirmNewPwd)) {
            ToastUtils.showToast(changePwdActivity.this, "两次输入新密码不一致，请重新输入");
        }else {
            try {
                //拼接出要提交的字符串
                String changePwdData = "account=" + URLEncoder.encode(account, "utf-8") + "&password=" + oldPwd + "&newPassword=" + newPwd;
                //修改密码接口
                String changePwdUrl = "http://139.199.12.99:8080/agencysys/app/employeeservlet?method=changePassword";
                HttpPost.sendHttpPost(changePwdUrl, changePwdData, new HttpCallbackListener() {
                    @Override
                    public void onFinish(final String response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                returnData data = gson.fromJson(response, returnData.class);
                                String result = data.getResult();
                                if (result.equals("true")) {
                                    ToastUtils.showToast(changePwdActivity.this, "密码修改成功，请重新登录");
                                    startActivity(new Intent(changePwdActivity.this, loginActivity.class));
                                    finish();
                                    homeActivity.instance.finish();
                                }
                            }
                        });

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
