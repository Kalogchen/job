package com.intermediary.job.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.intermediary.job.R;
import com.intermediary.job.adapter.AcceptAdapter;
import com.intermediary.job.model.AcceptMessage;
import com.intermediary.job.model.CheckLogin;
import com.intermediary.job.utlis.HttpCallbackListener;
import com.intermediary.job.utlis.HttpPost;
import com.intermediary.job.utlis.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看应聘信息
 * Created by kalogchen on 2017/1/1.
 */

public class acceptActivity extends Activity {

    private int page;
    private String hasNextPage;
    private List<AcceptMessage> acceptMessages;
    private AcceptAdapter acceptAdapter;
    //登录状态
    private String result;
    //账号id
    private String employeeID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_listview);

        acceptMessages = new ArrayList<AcceptMessage>();

        //检测是否已经登录，获取employeeID，用来获取应聘信息
        //检测是否登录接口
        final String checkLogin = "http://139.199.12.99:8080/agencysys/app/employeeservlet?method=checkLogin";
        HttpPost.sendHttpPost(checkLogin, "1", new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        CheckLogin json = gson.fromJson(response, CheckLogin.class);
                        result = json.getResult();
                        employeeID = json.getEmployeeID();

                        showAcceptInfo();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
            }
        });

    }

    //展示应聘信息
    public void showAcceptInfo() {
        if (result.equals("true")) {
            //查看应聘记录接口
            final String acceptUrl = "http://139.199.12.99:8080/agencysys/app/employservlet?method=showEmployInfo";
            //应聘信息页码
            final int currentPage = 1;
            //应聘信息条数
            final int everyPage = 50;
            //下一次加载的信息页码数
            page = 0;
            //拼接出要提交的数据的字符串
            String acceptData = "employeeID=" + employeeID + "&currentPage=" + String.valueOf(currentPage) + "&everyPage=" + String.valueOf(everyPage);
            HttpPost.sendHttpPost(acceptUrl, acceptData, new HttpCallbackListener() {
                @Override
                public void onFinish(final String response) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Log.d("msg", "-------------服务器返回应聘信息：" + response);
                                //解析服务器返回招聘信息
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("list");
                                //判断是否还有下一页数据
                                hasNextPage = jsonObject.getString("hasNextPage");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    AcceptMessage message = new AcceptMessage("position", "price", "companyName", "address", "inviteInfoID", "applyTime");
                                    message.setPosition(object.getString("position"));
                                    message.setPrice(object.getString("price"));
                                    message.setCompanyName(object.getString("companyName"));
                                    message.setAddress(object.getString("address"));
                                    message.setApplyTime(object.getString("applyTime"));
                                    //记录应聘信息id，方便用id通过post请求查看单条完整信息
                                    message.setInviteInfoID(object.getString("inviteInfoID"));
                                    acceptMessages.add(message);
                                }
                                acceptAdapter = new AcceptAdapter(acceptActivity.this, R.layout.accept_listview_item, acceptMessages);
                                final ListView lvAccept = (ListView) findViewById(R.id.lv_accept);
                                lvAccept.setAdapter(acceptAdapter);

                                //监听listView条目
                                lvAccept.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        AcceptMessage acceptMessage = acceptMessages.get(position);
                                        //用Bunble携带inviteInfoID数据，跳转到具体的应聘信息页面
                                        Intent intent = new Intent(acceptActivity.this, acceptDetailActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("inviteInfoID", acceptMessage.getInviteInfoID());
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });

                                //listview下拉加载更多
                                lvAccept.setOnScrollListener(new AbsListView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                                        switch (scrollState) {
                                            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                                                int lastVisiblePosition = lvAccept.getLastVisiblePosition();
                                                if (lastVisiblePosition == acceptMessages.size() - 1 && hasNextPage.equals("true")) {
                                                    ToastUtils.showToast(acceptActivity.this, "数据加载中，请稍后...");
                                                    page++;
                                                    int loadPage = page + currentPage;
                                                    //拼接出要提交数据的字符串
                                                    String acceptMoreData = "employeeID=" + employeeID + "&currentPage=" + String.valueOf(loadPage) + "&everyPage=" + String.valueOf(everyPage);
                                                    HttpPost.sendHttpPost(acceptUrl, acceptMoreData, new HttpCallbackListener() {
                                                        @Override
                                                        public void onFinish(String response) {
                                                            try {
                                                                JSONObject jsonObject1 = new JSONObject(response);
                                                                JSONArray jsonArray1 = jsonObject1.getJSONArray("list");
                                                                //判断是否还有下一页数据
                                                                hasNextPage = jsonObject1.getString("hasNextPage");
                                                                for (int i = 0; i < jsonArray1.length(); i++) {
                                                                    JSONObject object = jsonArray1.getJSONObject(i);
                                                                    AcceptMessage message = new AcceptMessage("position", "price", "companyName", "address", "inviteInfoID", "applyTime");
                                                                    message.setPosition(object.getString("position"));
                                                                    message.setPrice(object.getString("price"));
                                                                    message.setCompanyName(object.getString("companyName"));
                                                                    message.setAddress(object.getString("address"));
                                                                    message.setApplyTime(object.getString("applyTime"));
                                                                    //记录应聘信息id，方便用id通过post请求查看单条完整信息
                                                                    message.setInviteInfoID(object.getString("inviteInfoID"));
                                                                    acceptMessages.add(message);

                                                                    runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            //数据加载完毕主线程刷新界面
                                                                            acceptAdapter.notifyDataSetChanged();
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

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(acceptActivity.this, "网络错误，无法获取数据，请稍后重试");
                        }
                    });
                }
            });
        }
    }
}
