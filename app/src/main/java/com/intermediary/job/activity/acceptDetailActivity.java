package com.intermediary.job.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.intermediary.job.R;
import com.intermediary.job.model.AcceptDetailMessage;
import com.intermediary.job.utlis.HttpCallbackListener;
import com.intermediary.job.utlis.HttpPost;

/**
 * 查看单条招聘信息
 * Created by kalogchen on 2017/1/3.
 */

public class acceptDetailActivity extends Activity {

    private String inviteInfoID;
    private TextView tvPosition;
    private TextView tvPrice;
    private TextView tvCompanyName;
    private TextView tvAddress;
    private TextView tvJobDescribe;
    private TextView tvApplyRequire;
    private TextView tvBuildDate;
    private TextView tvCompanyType;
    private TextView tvBusiness;
    private TextView tvWorkCharacter;
    private TextView tvApplyTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_detail);


        //获取应聘信息的id，通过id去查看完整的应聘信息
        Bundle bundle = this.getIntent().getExtras();
        inviteInfoID = bundle.getString("inviteInfoID");

        tvPosition = (TextView) findViewById(R.id.tv_position);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvCompanyName = (TextView) findViewById(R.id.tv_companyName);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvJobDescribe = (TextView) findViewById(R.id.tv_jobDescribe);
        tvApplyRequire = (TextView) findViewById(R.id.tv_applyRequire);
        tvBuildDate = (TextView) findViewById(R.id.tv_buildDate);
        tvCompanyType = (TextView) findViewById(R.id.tv_companyType);
        tvBusiness = (TextView) findViewById(R.id.tv_business);
        tvWorkCharacter = (TextView) findViewById(R.id.tv_workCharacter);
        tvApplyTime = (TextView) findViewById(R.id.tv_applyTime);

        getAcceptDetail();
    }

    private void getAcceptDetail() {
        //查看单条应聘信息接口
        String acceptDetailUrl = "http://139.199.12.99:8080/agencysys/app/employservlet?method=showEmployInfoByID";
        //拼接出要提交数据的字符串
        String acceptDetailData = "inviteInfoID=" + inviteInfoID;
        Log.d("msg", "------------------inviteInfoID：" + acceptDetailData);
        HttpPost.sendHttpPost(acceptDetailUrl, acceptDetailData, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                Log.d("msg", "------------------单条应聘信息：" + response);
                if (response!=null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //解析服务器返回的单条应聘信息
                            Gson gson = new Gson();
                            AcceptDetailMessage json = gson.fromJson(response, AcceptDetailMessage.class);
                            tvPosition.setText("职位：" + json.getPosition());
                            tvPrice.setText("工资：" + json.getPrice());
                            tvCompanyName.setText("企业名称："  + json.getCompanyName());
                            tvAddress.setText("地址：" + json.getAddress());
                            tvJobDescribe.setText(json.getWorkDescript());
                            tvApplyRequire.setText(json.getApplyRequire());
                            tvBuildDate.setText("发布时间：" + json.getBuildDate());
                            tvCompanyType.setText("企业类型：" + json.getCompanyType());
                            tvBusiness.setText("企业业务：" + json.getBussiness());
                            tvWorkCharacter.setText("工作性质：" + json.getWorkCharacter());
                            tvApplyTime.setText("申请时间：" + json.getApplyTime());
                        }
                    });
                }


            }

            @Override
            public void onError(Exception e) {

            }
        });

    }


}
