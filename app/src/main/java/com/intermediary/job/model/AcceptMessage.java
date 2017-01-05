package com.intermediary.job.model;

/**
 * Created by kalogchen on 2017/1/3.
 */

public class AcceptMessage {

    private String position;
    private String price;
    private String companyName;
    private String address;
    private String inviteInfoID;
    private String applyTime;

    public AcceptMessage(String position, String price, String companyName, String address, String inviteInfoID, String applyTime) {
        this.position = position;
        this.price = price;
        this.companyName = companyName;
        this.address = address;
        this.inviteInfoID = inviteInfoID;
        this.applyTime = applyTime;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInviteInfoID() {
        return inviteInfoID;
    }

    public void setInviteInfoID(String inviteInfoID) {
        this.inviteInfoID = inviteInfoID;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }
}
