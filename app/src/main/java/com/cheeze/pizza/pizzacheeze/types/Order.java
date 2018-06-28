package com.cheeze.pizza.pizzacheeze.types;


import com.cheeze.pizza.pizzacheeze.EditPizza;
import com.cheeze.pizza.pizzacheeze.SplashActivity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Tal on 26/09/17.
 */

public class Order implements Serializable {
    private Cart cart;

    private String id;
    private int entries;
    private boolean specialCostumer;
    private String cName;
    private String phoneNumber;
    private String area;
    private String address;
    private boolean shipment;
    private boolean creditCardPayment;
    private String arrivalTime;
    private ArrayList<Product> freeProducts;

    public ArrayList<Product> getFreeProducts() {
        return freeProducts;
    }

    public void setFreeProducts(ArrayList<Product> freeProducts) {
        this.freeProducts = freeProducts;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public boolean isShipment() {
        return shipment;
    }

    public void setShipment(boolean shipment) {
        this.shipment = shipment;
    }

    public boolean isCreditCardPayment() {
        return creditCardPayment;
    }

    public void setCreditCardPayment(boolean creditCardPayment) {
        this.creditCardPayment = creditCardPayment;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getEntries() {
        return entries;
    }

    public void setEntries(int entries) {
        this.entries = entries;
    }


    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isSpecialCostumer() {
        return specialCostumer;
    }

    public void setSpecialCostumer(boolean specialCostumer) {
        this.specialCostumer = specialCostumer;
    }


    public Order() {
        freeProducts = new ArrayList<>();
        this.cart = new Cart();
        specialCostumer = false;
        phoneNumber = "";
        cName = "";
        address = "";
        area = "";
    }

    public String toString() {
        String s = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String strDate = sdf.format(c.getTime());
        s += "תאריך: " + strDate + "\n";
        s += "שם לקוח: " + this.cName + "\n";
        if (specialCostumer)
            s += "לקוח פרימיום \n";
        s += "טלפון: " + this.phoneNumber + "\n";
        s += (this.creditCardPayment ? "תשלום באשראי" : "תשלום במזומן") + "\n";
        s += (this.shipment ? "משלוח" : "איסוף עצמי") + "\n";
        if (this.shipment) {
            s += "    אזור: " + this.area + "\n";
            s += "    כתובת: " + this.address + "\n";
        }

        s += this.cart.toString();

        s += "\n" + (this.shipment ? "זמן הגעה: " : "זמן איסוף: ") + this.arrivalTime;

        return s;
    }

    public void addToCartWithMainPrice(Cart cart) {
        EditPizza.freeTopUsed = true;
        for (Product p : cart.getSelectedProducts()) {
            p.setPrice(p.getMainPrice());
            p.setTotalPrice(p.getCount() * p.getPrice());
            p.updatePrice();
            this.cart.addProduct(p);
        }
        EditPizza.freeTopUsed = false;
    }

    public void addToCartForFree(Cart cart) {
        for (Product p : cart.getSelectedProducts()) {
            p.setPrice(0);
            p.setTotalPrice(0);
            p.setOnDiscount(true);
            this.cart.addProduct(p);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}