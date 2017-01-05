package com.intermediary.job.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.intermediary.job.R;
import com.intermediary.job.model.returnData;
import com.intermediary.job.utlis.HttpCallbackListener;
import com.intermediary.job.utlis.HttpPost;
import com.intermediary.job.utlis.ToastUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 注册界面
 * Created by kalogchen on 2016/12/19.
 */

public class registerActivity extends Activity {

    private EditText etAccount;
    private EditText etPassWord;
    private EditText etConfirmPw;
    private Button btRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //获取账号密码文本框和注册按钮对象
        etAccount = (EditText) findViewById(R.id.et_account);
        etPassWord = (EditText) findViewById(R.id.et_password);
        etConfirmPw = (EditText) findViewById(R.id.et_confirmPw);
        btRegister = (Button) findViewById(R.id.bt_register);

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInfo();
            }
        });

    }

    //检查账号密码是否符合规范,符合规范则进行注册
    private void checkInfo() {
        //用户注册接口
        final String registerUrl = "http://139.199.12.99:8080/agencysys/app/employeeservlet?method=employeeResiger";
        //检测账号是否存在接口
        final String accountExistUrl = "http://139.199.12.99:8080/agencysys/app/employeeservlet?method=checkAccount";
        //获取用户的注册信息（账号密码）
        final String account = etAccount.getText().toString();
        final String passWord = etPassWord.getText().toString();
        String confirmPw = etConfirmPw.getText().toString();

        //判断数据格式是否正确
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(passWord)) {
            ToastUtils.showToast(registerActivity.this, "账号或密码不能为空，请重新输入");
        }else if(account.length()<6 || account.length()>20 || passWord.length()<6 || passWord.length()>20) {
            ToastUtils.showToast(registerActivity.this, "账号或密码长度为6-20个字符，请重新输入");
        }else if (!passWord.equals(confirmPw)){
            ToastUtils.showToast(registerActivity.this, "两次输入密码不一致，请重新输入");
        }else {
            //检测账号是否存在
            try {
                //拼接出要提交的数据的字符串
                String accountData = "account=" + URLEncoder.encode(account, "utf-8");
                HttpPost.sendHttpPost(accountExistUrl, accountData, new HttpCallbackListener() {
                    @Override
                    public void onFinish(final String response) {
                        //通过runOnUiThread（）方法回到主线程处理逻辑
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //获取服务器返回的json数据并解析
                                Gson gson = new Gson();
                                returnData data = gson.fromJson(response, returnData.class);
                                String result = data.getResult();
                                if (result.equals("true")) {
                                    //如果账号已经注册了，则提示用户重新输入账号
                                    ToastUtils.showToast(registerActivity.this, "该帐号已经存在，请重新输入！");
                                }else if (result.equals("false")) {
                                    //如果该帐号没有注册，则注册该账号
                                    try {
                                        //拼接出要提交的数据的字符串
                                        final String registerData = "account=" + URLEncoder.encode(account, "utf-8") + "&password=" + passWord;
                                        HttpPost.sendHttpPost(registerUrl, registerData, new HttpCallbackListener() {
                                            @Override
                                            public void onFinish(final String response) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //获取服务器返回的json数据并解析
                                                        Gson gson = new Gson();
                                                        returnData data = gson.fromJson(response, returnData.class);
                                                        String result = data.getResult();
                                                        String error = data.getError();
                                                        //注册成功，返回登录界面
                                                        if (result.equals("true")){
                                                            ToastUtils.showToast(registerActivity.this, "注册成功，请重新登录");
                                                            startActivity(new Intent(registerActivity.this, loginActivity.class));
                                                            finish();
                                                        }else if (result.equals("false") && error.equals("9999")) {
                                                            ToastUtils.showToast(registerActivity.this, "服务器异常，请稍后重新注册");
                                                        }else {
                                                            ToastUtils.showToast(registerActivity.this, "网络异常，请稍后重新注册");
                                                        }
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ToastUtils.showToast(registerActivity.this, "网络错误，请稍后重试！");
                                                    }
                                                });
                                            }
                                        });
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        ToastUtils.showToast(registerActivity.this, "网络错误，请稍后重试！");
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }
}
