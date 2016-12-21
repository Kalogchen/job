package com.intermediary.job.utlis;

/**
 * Created by kalogchen on 2016/12/20.
 */

public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
