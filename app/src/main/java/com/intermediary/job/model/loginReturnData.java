package com.intermediary.job.model;

/**
 * Created by kalogchen on 2016/12/21.
 */

public class loginReturnData {
    private String result;
    private String error;
    private String JSESSIONID;

    public loginReturnData() {

    }

    public loginReturnData(String result, String error, String JSESSIONID) {
        this.result = result;
        this.error = error;
        this.JSESSIONID = JSESSIONID;
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

    public String getJSESSIONID() {
        return JSESSIONID;
    }

    public void setJSESSIONID(String JSESSIONID) {
        this.JSESSIONID = JSESSIONID;
    }
}
