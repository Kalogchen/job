package com.intermediary.job.utlis;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kalogchen on 2016/12/20.
 */

public class HttpPost {

    /**
     * @param address  要访问的接口
     * @param text     要提交的数据
     * @param listener 回调接口
     */
    public static void sendHttpPost(final String address, final String text, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL(address);
                    //创建http请求连接
                    conn = (HttpURLConnection) url.openConnection();
                    //设置请求方式
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    /*//拼接出要提交的数据的字符串
                    String accountData = "account=" + URLEncoder.encode(account, "utf-8");*/
                    //添加post请求的两行属性
                    conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    //设置cookie
                    String jsessionid = SharedPreferencesUtils.getString(BaseApplication.getContext(), "JSESSIONID", "");
                    Log.d("msg", "-------------------cookie:" + jsessionid);
                    conn.addRequestProperty("Cookie", jsessionid);
                    //设置打开输出流
                    conn.setDoOutput(true);
                    //拿到输出流
                    OutputStream os = conn.getOutputStream();
                    //使用输出流往服务器提交数据
                    os.write(text.getBytes());
                    //判断数据是否提交成功，返回值为200表示提交成功
                    if (conn.getResponseCode() == 200) {

                        InputStream is = conn.getInputStream();
                        String data = StreamUtlis.getTextFromStream(is);

                        if (listener != null) {
                            //回调onFinish()方法
                            listener.onFinish(data);
                        }

                    }

                } catch (MalformedURLException e) {
                    Log.d("msg", "----------无法将String转换为Url");
                } catch (IOException e) {
                    Log.d("msg", "----------无法打开链接");
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }
}
