package com.cheeze.pizza.pizzacheeze.types;

import java.util.UUID;

/**
 * Created by Tal on 06/10/17.
 */

public class Discount {
    private String id;
    private Product discounter;
    private int discounterAmount;
    private Product discounted;
    private double newPrice;
    private boolean active;


    public Discount(Product discounter, int discounterAmount, Product discounted, double newPrice) {
        this.discounter = discounter;
        this.discounterAmount = discounterAmount;
        this.discounted = discounted;
        this.newPrice = newPrice;
        active=true;
        id= UUID.randomUUID().toString();
    }
    public Discount(){}

    public void setId(String id){
        this.id=id;
    }

    public String toString(){
        String s="";
        if(discounter!=null)
            s+= (" בקניית "+ (discounterAmount==1?"":discounterAmount)+" "+discounter.getName());
        s+=" קבל "+discounted.getName()+" ב"+(newPrice==0?"מתנה":(newPrice+"₪"));
        return id.equals("toppingsDiscount")?"1+1 על התוספות":s;
    }

    public String getId() {
        return id;
    }

    public Product getDiscounter() {
        return discounter;
    }

    public void setDiscounter(Product discounter) {
        this.discounter = discounter;
    }

    public int getDiscounterAmount() {
        return discounterAmount;
    }

    public void setDiscounterAmount(int discounterAmount) {
        this.discounterAmount = discounterAmount;
    }

    public Product getDiscounted() {
        return discounted;
    }

    public void setDiscounted(Product discounted) {
        this.discounted = discounted;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
