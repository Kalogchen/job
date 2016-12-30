package com.intermediary.job.base.impl;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.intermediary.job.R;
import com.intermediary.job.adapter.CompanyAdapter;
import com.intermediary.job.base.BasePager;
import com.intermediary.job.model.CompanyMessage;
import com.intermediary.job.utlis.HttpCallbackListener;
import com.intermediary.job.utlis.HttpPost;
import com.intermediary.job.utlis.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业信息展示
 * Created by kalogchen on 2016/12/23.
 */

public class CompanyPager extends BasePager {

    public List<CompanyMessage> companyMessages;

    private int i = 0;
    private ListView lv;
    private String hasNextPage;
    private int page;

    public CompanyPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        if (i == 0) {
            //刚进来初始化数据，从其他页面再切换过来就不重新初始化
            //修改标题栏
            tvTitle.setText("公 司");

            getCompanyData();

        }
    }

    private void getCompanyData() {
        //企业信息接口
        final String companyUrl = "http://139.199.12.99:8080/agencysys/app/companyservlet?method=showCompanyPage";
        //企业信息页码
        final int currentPage = 1;
        //企业信息条数
        final int everyPage = 10;
        //下一次加载的信息页码数
        page = 0;
        //拼接出要提交的数据的字符串
        String companyData = "currentPage=" + String.valueOf(currentPage) + "&everyPage=" + String.valueOf(everyPage);

        HttpPost.sendHttpPost(companyUrl, companyData, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //解析服务器返回招聘信息
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("list");
                            //判断是否还有下一页数据
                            hasNextPage = jsonObject.getString("hasNextPage");
                            //用集合保存招聘信息
                            companyMessages = new ArrayList<CompanyMessage>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                CompanyMessage message = new CompanyMessage("companyName", "companyType", "bussiness", "descript");
                                message.setCompanyName(object.getString("companyName"));
                                message.setCompanyType(object.getString("companyType"));
                                message.setBussiness(object.getString("bussiness"));
                                message.setDescript(object.getString("descript"));
                                /*//记录信息id，方便用id通过post请求查看单条完整信息
                                message.setInviteInfoID(object.getString("inviteInfoID"));*/
                                companyMessages.add(message);
                            }

                            final CompanyAdapter comAdapter = new CompanyAdapter(mActivity, R.layout.company_listview_item, companyMessages);
                            //通过获取布局文件对象，从而获取布局文件对象里的控件对象
                            View view = View.inflate(mActivity, R.layout.fragment_listview, null);
                            lv = (ListView) view.findViewById(R.id.lv_listView);
                            lv.setAdapter(comAdapter);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    CompanyMessage comMessage = companyMessages.get(position);
                                    //跳转到具体的信息
                                    //ToastUtils.showToast(mActivity, "-----------invitedId:" + jobMessage.getInviteInfoID());
                                }
                            });

                            //listview下拉加载更多
                            lv.setOnScrollListener(new AbsListView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(AbsListView view, int scrollState) {
                                    switch (scrollState) {
                                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                                            int lastVisiblePosition = lv.getLastVisiblePosition();
                                            if (lastVisiblePosition == companyMessages.size() - 1 && hasNextPage.equals("true")) {
                                                ToastUtils.showToast(mActivity, "数据加载中，请稍后...");
                                                page++;
                                                int loadPage = page + currentPage;
                                                //拼接出要提交的数据的字符串
                                                String companyData = "currentPage=" + String.valueOf(loadPage) + "&everyPage=" + String.valueOf(everyPage);
                                                HttpPost.sendHttpPost(companyUrl, companyData, new HttpCallbackListener() {
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
                                                                CompanyMessage message = new CompanyMessage("companyName", "companyType", "bussiness", "descript");
                                                                message.setCompanyName(object.getString("companyName"));
                                                                message.setCompanyType(object.getString("companyType"));
                                                                message.setBussiness(object.getString("bussiness"));
                                                                message.setDescript(object.getString("descript"));
                                                                /*//记录信息id，方便用id通过post请求查看单条完整信息
                                                                message.setInviteInfoID(object.getString("inviteInfoID"));*/
                                                                companyMessages.add(message);

                                                                mActivity.runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        //数据加载完毕主线程刷新界面
                                                                        comAdapter.notifyDataSetChanged();
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
                            i=1;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
