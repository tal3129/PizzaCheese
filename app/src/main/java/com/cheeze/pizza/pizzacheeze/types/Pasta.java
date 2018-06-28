package com.cheeze.pizza.pizzacheeze.types;



/**
 * Created by Tal on 02/11/17.
 */

public class Pasta extends Product {
    private double originalPrice;
    private boolean extraCheese;

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }
    public Pasta(Product p) {
        super(p);
        this.originalPrice=super.price;
        this.extraCheese=false;
    }

    public boolean isExtraCheese() {
        return extraCheese;
    }

    public void setExtraCheese(boolean extraCheese) {
        this.extraCheese = extraCheese;
        super.price+=extraCheese?3:-3;
    }

    @Override
    public void updatePrice(){
        super.price+=(extraCheese?3:0);
    }

    @Override
    public String toString(){
        return super.toString()+(extraCheese?" (תוספת הקרמה) ":"");
    }
}
