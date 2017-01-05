package com.intermediary.job.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.intermediary.job.R;
import com.intermediary.job.model.CheckLogin;
import com.intermediary.job.utlis.HttpCallbackListener;
import com.intermediary.job.utlis.HttpPost;

/**
 * Created by kalogchen on 2017/1/2.
 */

public class resumeActivity extends Activity {

    //登录状态
    private String result;
    //账号id
    private String employeeID;

    private EditText etName;
    private EditText etSex;
    private EditText etNation;
    private EditText etAddress;
    private EditText etMajor;
    private EditText etHeight;
    private EditText etIdCard;
    private EditText etPhone;
    private EditText etBirthday;
    private EditText etCollege;
    private EditText etAward;
    private EditText etTelHome;
    private EditText etDscribe;
    private Button btCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume);

        getEmployeeID();

        etName = (EditText) findViewById(R.id.et_name);
        etSex = (EditText) findViewById(R.id.et_sex);
        etNation = (EditText) findViewById(R.id.et_nation);
        etAddress = (EditText) findViewById(R.id.et_address);
        etMajor = (EditText) findViewById(R.id.et_major);
        etHeight = (EditText) findViewById(R.id.et_height);
        etIdCard = (EditText) findViewById(R.id.et_idCard);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etBirthday = (EditText) findViewById(R.id.et_birthday);
        etCollege = (EditText) findViewById(R.id.et_college);
        etAward = (EditText) findViewById(R.id.et_award);
        etTelHome = (EditText) findViewById(R.id.et_tel_home);
        etDscribe = (EditText) findViewById(R.id.et_descript);
        btCommit = (Button) findViewById(R.id.bt_commit);

        btCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String data = "birthday=" + "1995-10-10" + "&address=" + "empty" + "&award=" + "empty"
                        + "&college=" +  "empty" + "&descript=" + "empty" + "&height=" + "160"
                        + "&id_card=" + "123456789123456789" + "&major=" + "empty" + "&name=" + "empty"
                        + "&nation=" + "empty" + "&sex=" + "empty" + "&tel=" + "12345678901"
                        + "&tel_home=" + "1234567";

                String resumeUrl = "http://139.199.12.99:8080/agencysys/app/resumeservlet?method=fillResume";
                String checkResumeUrl = "http://139.199.12.99:8080/agencysys/app/resumeservlet?method=checkResume";
                HttpPost.sendHttpPost(resumeUrl, data, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        //无法提交简历
                        Log.d("msg", "--------employeeID:" + employeeID);
                        Log.d("msg", "----------简历返回信息：" + response);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });






               /* if (result.equals("true")) {
                    //获取用户所填写简历信息
                    String name = etName.getText().toString();
                    String sex = etSex.getText().toString();
                    String nation = etNation.getText().toString();
                    String address = etAddress.getText().toString();
                    String major = etMajor.getText().toString();
                    String height = etHeight.getText().toString();
                    String idCard = etIdCard.getText().toString();
                    String phone = etPhone.getText().toString();
                    String birthday = etBirthday.getText().toString();
                    String college = etCollege.getText().toString();
                    String award = etAward.getText().toString();
                    String telHome = etTelHome.getText().toString();
                    String describe = etDscribe.getText().toString();

                    //判断简历信息格式是否正确
                    //判断是否有没有填写的数据
                    if (name.isEmpty() || sex.isEmpty() || nation.isEmpty() || address.isEmpty() || major.isEmpty()
                            || height.isEmpty() || idCard.isEmpty() || phone.isEmpty() || birthday.isEmpty()
                            || college.isEmpty() || telHome.isEmpty() || describe.isEmpty() || award.isEmpty()) {
                        ToastUtils.showToast(resumeActivity.this, "请完整填写简历");
                    } else if (phone.length() != 11) {
                        ToastUtils.showToast(resumeActivity.this, "手机号码填写不正确，请重新输入");
                    } else if (idCard.length() != 18) {
                        ToastUtils.showToast(resumeActivity.this, "身份证号码填写不正确，请重新输入");
                    } else {
                        //填写简历接口
                        String resumeUrl = "http://139.199.12.99:8080/agencysys/app/employeeservlet?method=fillResume";
                        //拼接处要提交数据的字符串
                        String Data = null;
                        try {
                            Data = "address=" + address + "&award=" + award
                                    + "&birthday=" + birthday + "&college=" +  college
                                    + "&descript=" + describe + "&employeeID=" + employeeID
                                    + "&height=" + height + "&idCard=" + idCard + "&major=" + major
                                    + "&nation=" + nation + "&sex=" + sex+ "&tel=" + phone
                                    + "&tel_home=" + telHome + "&name=" + name;
                            String resumeData = URLEncoder.encode(Data, "utf-8");
                            Log.d("msg", "-----------未转码数据：" + Data);
                            Log.d("msg", "-----------已转码数据：" + resumeData);

                            HttpPost.sendHttpPost(resumeUrl, Data, new HttpCallbackListener() {
                                @Override
                                public void onFinish(String response) {
                                    Log.d("msg", "----------------填写简历返回信息：" + response);
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (result.equals("false")) {
                    ToastUtils.showToast(resumeActivity.this, "登录超时，请重新登陆！");
                    startActivity(new Intent(resumeActivity.this, loginActivity.class));
                    finish();
                    homeActivity.instance.finish();
                }*/
            }
        });

    }

    private void getEmployeeID() {
        //检测是否已经登录，获取employeeID，用来获取应聘信息
        //检测是否登录接口
        final String checkLogin = "http://139.199.12.99:8080/agencysys/app/employeeservlet?method=checkLogin";
        HttpPost.sendHttpPost(checkLogin, "1", new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Gson gson = new Gson();
                CheckLogin json = gson.fromJson(response, CheckLogin.class);
                result = json.getResult();
                employeeID = json.getEmployeeID();
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }
}
