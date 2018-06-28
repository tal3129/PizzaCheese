package com.cheeze.pizza.pizzacheeze.types;



import org.joda.time.LocalDate;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Tal on 15/11/17.
 */

public class DailyEarning implements Serializable{
    private Date date;
    private double ccSum;
    private double cashSum;
    private String key;



    public DailyEarning(Date date) {
        this.date = date;
        this.ccSum=0;
        this.cashSum=0;
    }

    public DailyEarning(){}

    public DailyEarning(Date date, double cashSum, double ccSum) {
        this.date = date;
        this.cashSum=cashSum;
        this.ccSum=ccSum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getCcSum() {
        return ccSum;
    }

    public void setCcSum(double ccSum) {
        this.ccSum = ccSum;
    }

    public double getCashSum() {
        return cashSum;
    }

    public void setCashSum(double cashSum) {
        this.cashSum = cashSum;
    }
}
