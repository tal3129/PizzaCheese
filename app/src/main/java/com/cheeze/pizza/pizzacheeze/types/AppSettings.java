package com.cheeze.pizza.pizzacheeze.types;

/**
 * Created by Tal on 03/10/17.
 */


public class AppSettings {
    private double toppingPrice;
    private boolean luckyWheelEnabled;
    private String receiverMail;
    private boolean appStatus;
    private String appVersion;
    private Boolean ccStatus;
    private double minWheelSum;
    private int appVersionCode;


    public AppSettings(String receiverMail, boolean appStatus, boolean ccStatus, String appVersion,double minWheelSum, int appVersionCode){
        this.appStatus=appStatus;
        this.receiverMail=receiverMail;
        this.ccStatus= ccStatus;
        this.appVersion=appVersion;
        this.minWheelSum=minWheelSum;
        this.appVersionCode = appVersionCode;
    }

    public AppSettings(){
        toppingPrice = 5;
    }

    public boolean isLuckyWheelEnabled() {
        return luckyWheelEnabled;
    }

    public void setLuckyWheelEnabled(boolean luckyWheelEnabled) {
        this.luckyWheelEnabled = luckyWheelEnabled;
    }

    public int getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(int appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public void setReceiverMail(String receiverMail) {
        this.receiverMail = receiverMail;
    }

    public double getMinWheelSum() {
        return minWheelSum;
    }

    public void setMinWheelSum(double minWheelSum) {
        this.minWheelSum = minWheelSum;
    }

    public void setAppStatus(boolean appStatus) {
        this.appStatus = appStatus;
    }

    public String getReceiverMail(){
        return receiverMail;
    }

    public boolean isAppStatus() {
        return this.appStatus;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Boolean getCcStatus() {
        return ccStatus;
    }

    public void setCcStatus(Boolean ccStatus) {
        this.ccStatus = ccStatus;
    }

    public double getToppingPrice() {
        return this.toppingPrice;
    }

    public void setToppingPrice(double toppingPrice) {
        this.toppingPrice = toppingPrice;
    }
}


