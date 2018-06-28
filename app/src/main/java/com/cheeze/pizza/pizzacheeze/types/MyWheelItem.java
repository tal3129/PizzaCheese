package com.cheeze.pizza.pizzacheeze.types;

import com.cheeze.pizza.pizzacheeze.SplashActivity;

/**
 * Created by Tal on 27/11/17.
 */

public class MyWheelItem {
    //the chance to get the item
    private double chance;
    //the name of the item
    private String name;
    //the item color
    private int color;
    //the product the item is giving
    private Product freeProduct;

    private String productName;

    public MyWheelItem() {
    }

    public MyWheelItem(double chance, String name, int color, Product freeProduct) {
        this.chance = chance;
        this.name = name;
        this.color = color;
        this.freeProduct = freeProduct;

    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Product getFreeProduct() {
        return freeProduct;
    }

    public void setFreeProduct(Product freeProduct) {
        this.freeProduct = freeProduct;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.freeProduct= SplashActivity.getProductByName(productName);
        this.productName = productName;
    }
}
