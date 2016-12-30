package com.intermediary.job.model;

/**
 * Created by kalogchen on 2016/12/25.
 */

public class CompanyMessage {

    private String companyType;
    private String companyName;
    private String bussiness;
    private String descript;

    public CompanyMessage(String companyType, String companyName, String bussiness, String descript) {
        this.companyType = companyType;
        this.companyName = companyName;
        this.bussiness = bussiness;
        this.descript = descript;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBussiness() {
        return bussiness;
    }

    public void setBussiness(String bussiness) {
        this.bussiness = bussiness;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }
}
