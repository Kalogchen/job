package com.intermediary.job.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
 * 登录界面
 * Created by kalogchen on 2016/12/19.
 */

public class loginActivity extends Activity{


    private EditText etAccount;
    private EditText etPassWord;
    private CheckBox cbRememberPassword;
    private Button btRegister;
    private Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //获取账号密码文本框和单选框对象
        etAccount = (EditText) findViewById(R.id.et_account);
        etPassWord = (EditText) findViewById(R.id.et_password);
        cbRememberPassword = (CheckBox) findViewById(R.id.cb_rememberPassword);

        //如果用户上次登录的时候选择了记住密码，则帮助用户自动输入账号密码，否则只输入账号
        boolean savePassWord = SharedPreferencesUtils.getBoolean(loginActivity.this, "savePassWord", false);
        cbRememberPassword.setChecked(savePassWord);
        String account = SharedPreferencesUtils.getString(loginActivity.this, "account", "");
        if (savePassWord) {
            String passWord = SharedPreferencesUtils.getString(loginActivity.this, "passWord", "");
            etAccount.setText(account);
            etPassWord.setText(passWord);
        }else {
            etAccount.setText(account);
        }

        btRegister = (Button) findViewById(R.id.bt_register);
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到注册界面
                startActivity(new Intent(loginActivity.this, registerActivity.class));
                finish();
            }
        });

        btLogin = (Button) findViewById(R.id.bt_login);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    //检测用户账号密码，没有错误则登陆
    public void login() {

        //用户登录接口
        String loginUrl = "http://139.199.12.99:8080/agencysys/app/employeeservlet?method=login";
        //获取用户登录信息
        final String account = etAccount.getText().toString();
        final String passWord = etPassWord.getText().toString();
        String checkNum = "123456";
        final boolean checked = cbRememberPassword.isChecked();

        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(passWord)) {
            ToastUtils.showToast(loginActivity.this, "账号或密码不能为空，请重新输入");
        }else if(account.length()<6 || account.length()>20 || passWord.length()<6 || passWord.length()>20) {
            ToastUtils.showToast(loginActivity.this, "账号或密码长度为6-20个字符，请重新输入");
        }else {
            try {
            //拼接出要提交的数据的字符串
            final String loginData = "account=" + URLEncoder.encode(account, "utf-8") + "&password=" + passWord + "&checkNum=" + checkNum;
            HttpPost.sendHttpPost(loginUrl, loginData, new HttpCallbackListener() {
                @Override
                public void onFinish(final String response) {
                    //通过runOnUiThread（）方法回到主线程处理逻辑
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            returnData data = gson.fromJson(response, returnData.class);
                            String result = data.getResult();
                            if (result.equals("true")) {
                                //记录用户账号密码
                                SharedPreferencesUtils.setString(loginActivity.this, "account", account);
                                SharedPreferencesUtils.setString(loginActivity.this, "passWord", passWord);
                                //记录用户是否点击了“记住密码”
                                SharedPreferencesUtils.setBoolean(loginActivity.this, "savePassWord", checked);
                                startActivity(new Intent(loginActivity.this, homeActivity.class));
                                finish();
                            }else if (result.equals("false")) {
                                ToastUtils.showToast(loginActivity.this, "账号密码错误，请重新输入");
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
