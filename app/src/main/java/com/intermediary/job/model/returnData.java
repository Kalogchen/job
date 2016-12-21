package com.intermediary.job.model;

/**
 * Created by kalogchen on 2016/12/21.
 */

public class returnData {
    private String result;
    private String error;

    public returnData() {

    }

    public returnData(String result, String error) {
        this.result = result;
        this.error = error;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
