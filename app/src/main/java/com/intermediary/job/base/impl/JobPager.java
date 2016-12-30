package com.intermediary.job.base.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.intermediary.job.R;
import com.intermediary.job.activity.recruitActivity;
import com.intermediary.job.adapter.JobAdapter;
import com.intermediary.job.base.BasePager;
import com.intermediary.job.model.JobMessage;
import com.intermediary.job.utlis.HttpCallbackListener;
import com.intermediary.job.utlis.HttpPost;
import com.intermediary.job.utlis.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 职位信息展示
 * Created by kalogchen on 2016/12/23.
 */

public class JobPager extends BasePager {

    private List<JobMessage> jobMessages;
    private String hasNextPage;
    private int page;
    private JobAdapter jobAdapter;

    public JobPager(Activity activity) {
        super(activity);
    }

    private int i = 0;

    @Override
    public void initData() {
        if (i == 0) {
            //刚进来初始化数据，从其他页面再切换过来就不重新初始化
            //修改标题栏
            tvTitle.setText("职 位");
            //用集合保存招聘信息
            jobMessages = new ArrayList<JobMessage>();
            getRecruitData();
        }
    }

    //获取招聘信息
    public void getRecruitData() {
        //招聘信息接口
        final String recruitUrl = "http://139.199.12.99:8080/agencysys/app/inviteservlet?method=showInviteInfo";
        //招聘信息页码
        final int currentPage = 1;
        //下一次加载的信息页码数
        page = 0;
        //招聘信息条数
        final int everyPage = 20;
        //拼接出要提交的数据的字符串
        String recruitData = "currentPage=" + String.valueOf(currentPage) + "&everyPage=" + String.valueOf(everyPage);
        HttpPost.sendHttpPost(recruitUrl, recruitData, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("msg", "-------------服务器返回招聘信息：" + response);
                            //解析服务器返回招聘信息
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            //判断是否还有下一页数据
                            hasNextPage = jsonObject.getString("hasNextPage");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                JobMessage message = new JobMessage("position", "price", "companyName", "address", "inviteInfoId");
                                message.setPosition(object.getString("position"));
                                message.setPrice(object.getString("price"));
                                message.setCompanyName(object.getString("companyName"));
                                message.setAddress(object.getString("address"));
                                //记录招聘信息id，方便用id通过post请求查看单条完整信息
                                message.setInviteInfoID(object.getString("inviteInfoID"));
                                jobMessages.add(message);
                            }

                            jobAdapter = new JobAdapter(mActivity, R.layout.job_listview_item, jobMessages);
                            //通过获取布局文件对象，从而获取布局文件对象里的控件对象
                            View view = View.inflate(mActivity, R.layout.fragment_listview, null);
                            final ListView lv = (ListView) view.findViewById(R.id.lv_listView);

                            lv.setAdapter(jobAdapter);
                            //监听listview条目
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    JobMessage jobMessage = jobMessages.get(position);
                                    //跳转到具体的招聘信息页面
                                    Intent intent = new Intent(mActivity, recruitActivity.class);
                                    //用Bundle携带数据
                                    Bundle bundle = new Bundle();
                                    bundle.putString("inviteInfoID", jobMessage.getInviteInfoID());
                                    intent.putExtras(bundle);
                                    mActivity.startActivity(intent);
                                }
                            });
                            //listview下拉加载更多
                            lv.setOnScrollListener(new AbsListView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(AbsListView view, int scrollState) {
                                    switch (scrollState) {
                                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                                            int lastVisiblePosition = lv.getLastVisiblePosition();
                                            if (lastVisiblePosition == jobMessages.size() - 1 && hasNextPage.equals("true")) {
                                                ToastUtils.showToast(mActivity, "数据加载中，请稍后...");
                                                page++;
                                                int loadPage = page + currentPage;
                                                //拼接出要提交的数据的字符串
                                                String recruitData = "currentPage=" + String.valueOf(loadPage) + "&everyPage=" + String.valueOf(everyPage);
                                                HttpPost.sendHttpPost(recruitUrl, recruitData, new HttpCallbackListener() {
                                                    @Override
                                                    public void onFinish(final String response) {
                                                        Log.d("msg", "-------------服务器返回招聘信息：" + response);
                                                        //解析服务器返回招聘信息
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(response);
                                                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                                                            //判断是否还有下一页数据
                                                            hasNextPage = jsonObject.getString("hasNextPage");
                                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                                JSONObject object = jsonArray.getJSONObject(i);
                                                                JobMessage message = new JobMessage("position", "price", "companyName", "address", "inviteInfoId");
                                                                message.setPosition(object.getString("position"));
                                                                message.setPrice(object.getString("price"));
                                                                message.setCompanyName(object.getString("companyName"));
                                                                message.setAddress(object.getString("address"));
                                                                //记录招聘信息id，方便用id通过post请求查看单条完整信息
                                                                message.setInviteInfoID(object.getString("inviteInfoID"));
                                                                jobMessages.add(message);

                                                                mActivity.runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        //数据加载完毕主线程刷新界面
                                                                        jobAdapter.notifyDataSetChanged();
                                                                    }
                                                                });
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(Exception e) {

                                                    }
                                                });
                                            }
                                    }
                                }

                                @Override
                                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                }
                            });

                            LinearLayout ll_job = (LinearLayout) view.findViewById(R.id.ll_listView);
                            flContent.addView(ll_job);
                            i = 1;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(mActivity, "网络错误，无法获取数据，请稍后重试");
                    }
                });
            }
        });

    }

}
