package com.intermediary.job.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.intermediary.job.R;
import com.intermediary.job.model.RecruitMessage;
import com.intermediary.job.model.returnData;
import com.intermediary.job.utlis.HttpCallbackListener;
import com.intermediary.job.utlis.HttpPost;
import com.intermediary.job.utlis.ToastUtils;

/**
 * Created by kalogchen on 2016/12/27.
 */

public class recruitActivity extends Activity {

    private String inviteInfoID;
    private TextView tvPosition;
    private TextView tvPrice;
    private TextView tvCompanyName;
    private TextView tvAddress;
    private TextView tvJobDescribe;
    private TextView tvApplyRequire;
    private TextView tvWorkerNum;
    private TextView tvEmployNum;
    private TextView tvApplyNum;
    private TextView tvBusiness;
    private TextView tvPeopleNum;
    private TextView tvWorkCharacter;
    private TextView tvBuildDate;
    private TextView tv_companyDescribe;
    private TextView tvDialogText;
    private Button btOk;
    private Button btCancel;
    private Button btApply;
    //记录是否已应聘该招聘
    private String inviteStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit);

        //获取招聘信息的id，通过id去查看完整的招聘信息
        Bundle bundle = this.getIntent().getExtras();
        inviteInfoID = bundle.getString("inviteInfoID");

        tvPosition = (TextView) findViewById(R.id.tv_position);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvCompanyName = (TextView) findViewById(R.id.tv_companyName);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvJobDescribe = (TextView) findViewById(R.id.tv_jobDescribe);
        tvApplyRequire = (TextView) findViewById(R.id.tv_applyRequire);
        tvWorkerNum = (TextView) findViewById(R.id.tv_workerNum);
        tvEmployNum = (TextView) findViewById(R.id.tv_employNum);
        tvApplyNum = (TextView) findViewById(R.id.tv_applyNum);
        tvBusiness = (TextView) findViewById(R.id.tv_business);
        tvPeopleNum = (TextView) findViewById(R.id.tv_peopleNum);
        tvWorkCharacter = (TextView) findViewById(R.id.tv_workCharacter);
        tvBuildDate = (TextView) findViewById(R.id.tv_buildDate);
        tv_companyDescribe = (TextView) findViewById(R.id.tv_companyDescribe);
        btApply = (Button) findViewById(R.id.bt_apply);

        //申请应聘
        btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyJob();
            }
        });

        checkApply();

        getRecruitData();
    }

    //检测是否已经申请过该招聘，如果是，则显示已经申请应聘，按钮不可点击
    private void checkApply() {
        String checkApplyUrl = "http://139.199.12.99:8080/agencysys/app/employservlet?method=checkEmployInfo";
        //拼接要提交的数据的字符串
        String checkApplyData = "inviteInfoID=" + inviteInfoID;
        HttpPost.sendHttpPost(checkApplyUrl, checkApplyData, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                //在主线程修改按钮状态
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        returnData data = gson.fromJson(response, returnData.class);
                        String result = data.getResult();
                        if (result.equals("true")) {
                            btApply.setText("已申请应聘");
                            btApply.setBackgroundColor(Color.GRAY);
                            btApply.setClickable(false);
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    //申请应聘
    private void applyJob() {
        AlertDialog.Builder builder = new AlertDialog.Builder(recruitActivity.this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(recruitActivity.this, R.layout.dialog_apply_job, null);
        tvDialogText = (TextView) view.findViewById(R.id.tv_dialog_text);
        btOk = (Button) view.findViewById(R.id.bt_ok);
        btCancel = (Button) view.findViewById(R.id.bt_cancel);
        tvDialogText.setText("确定申请该职位吗？");
        //将自定义的布局文件设置给dialog
        dialog.setView(view, 0, 0, 0, 0);

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //申请应聘接口
                String applyJobUrl = "http://139.199.12.99:8080/agencysys/app/employservlet?method=applyEmploy";
                //拼接要提交的数据的字符串
                String applyJobData = "inviteInfoID=" + inviteInfoID;
                HttpPost.sendHttpPost(applyJobUrl, applyJobData, new HttpCallbackListener() {
                    @Override
                    public void onFinish(final String response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                returnData data = gson.fromJson(response, returnData.class);
                                String result = data.getResult();
                                if (result.equals("true")) {
                                    ToastUtils.showToast(recruitActivity.this, "恭喜，申请成功！");
                                    //改变申请职位按钮状态
                                    btApply.setText("已申请应聘");
                                    btApply.setBackgroundColor(Color.GRAY);
                                    btApply.setClickable(false);
                                    dialog.dismiss();
                                }
                            }
                        });

                    }
                    @Override
                    public void onError(Exception e) {
                    }
                });
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

    private void getRecruitData() {
        //查看单挑招聘信息接口
        String recruitUrl = "http://139.199.12.99:8080/agencysys/app/inviteservlet?method=showInviteInfoByInviteInfoID";
        //拼接要提交的数据的字符串
        String recruitData = "inviteInfoID=" + inviteInfoID;
        HttpPost.sendHttpPost(recruitUrl, recruitData, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                Log.d("msg", "--------------单条招聘信息：" + response);
                Gson gson = new Gson();
                final RecruitMessage json = gson.fromJson(response, RecruitMessage.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvPosition.setText("职位：" + json.getPosition());
                        tvPrice.setText("工资：" + json.getPrice() + "/小时");
                        tvCompanyName.setText("企业名称：" + json.getCompanyName());
                        tvAddress.setText("地址：" + json.getAddress());
                        tvJobDescribe.setText(json.getDescript());
                        tvApplyRequire.setText(json.getApplyRequire());
                        tvWorkerNum.setText("工作时间：" + json.getWorkerNum());
                        tvEmployNum.setText("招聘人数：" + json.getEmployNum());
                        tvApplyNum.setText("已申请人数：" + json.getApplyNum());
                        tvBusiness.setText("公司业务：" + json.getBussiness());
                        tvPeopleNum.setText("员工人数：" + json.getPeopleNum());
                        tvWorkCharacter.setText("工作性质：" + json.getWorkCharacter());
                        tvBuildDate.setText("发布时间：" + json.getBuildDate());
                        tv_companyDescribe.setText(json.getCompanyDescript());
                    }
                });

            }

            @Override
            public void onError(Exception e) {

            }
        });

    }
}
