package com.cheeze.pizza.pizzacheeze.types;

/**
 * Created by Tal on 03/10/17.
 */


public class AppSettings {
    private String receiverMail;
    private boolean appStatus;
    private boolean creditCardStatus;
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

    public boolean getAppStatus(){
        return this.appStatus;
    }

    public boolean isCreditCardStatus() {
        return creditCardStatus;
    }

    public void setCreditCardStatus(boolean creditCardStatus) {
        this.creditCardStatus = creditCardStatus;
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
}


