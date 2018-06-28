package com.cheeze.pizza.pizzacheeze.types;

import com.cheeze.pizza.pizzacheeze.EditPizza;
import com.cheeze.pizza.pizzacheeze.SplashActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Tal on 29/09/17.
 */

public class Pizza extends Product implements Serializable {
    private double originalPrice;
    private ArrayList<Topping> toppingArrayList;
    private boolean onOneFreeTop=false;

    public Pizza(String productId) {
        super();
        for (Product p : SplashActivity.productArrayList)
            if (p.getId().equals(productId))
                super.setProduct(p);
        this.toppingArrayList = new ArrayList<>();
        this.originalPrice = super.price;
    }

    public Pizza(Product p) {
        super(p);
        this.toppingArrayList = new ArrayList<>();
        this.originalPrice = super.price;
        if(p instanceof Pizza)
            onOneFreeTop=((Pizza)p).isOnOneFreeTop();
    }

    public Pizza(Pizza pizza) {
        super(pizza);
        this.toppingArrayList = pizza.getToppingArrayList();
        this.originalPrice = pizza.getOriginalPrice();
        onOneFreeTop=pizza.isOnOneFreeTop();
    }

    public double getOriginalPrice() {
        return this.originalPrice;
    }



    public void addTopping(Topping t) {
        toppingArrayList.add(t);
        updatePrice();
    }

    public ArrayList<Topping> getToppingArrayList() {
        return toppingArrayList;
    }

    public void setToppingArrayList(ArrayList<Topping> toppingArrayList) {
        this.toppingArrayList = toppingArrayList;
    }

    @Override
    public void updatePrice() {
        double tqs = 0;
        for (Topping t : toppingArrayList) {
            for (int i = 0; i < 4; i++)
                if (t.getQuarterAt(i))
                    tqs++;
        }
        tqs = Math.ceil(tqs / 4);
        if (SplashActivity.oneFreeTop) {
            if(tqs>0) {
                EditPizza.freeTopUsed=true;
                this.onOneFreeTop=true;
            }
            else {
                EditPizza.freeTopUsed = false;
                this.onOneFreeTop=false;
            }
        }
        if (SplashActivity.onePlusActive)
            tqs = Math.ceil(tqs / 2);
        price = originalPrice + (tqs * 5);
        super.updateTotalPrice();
    }

    public ArrayList<Topping> getToppingsOnQuarter(int quarter) {
        ArrayList<Topping> tempList = new ArrayList<>();
        for (Topping t : this.getToppingArrayList())
            if (t.getQuarterAt(quarter))
                tempList.add(t);
        return tempList;
    }

    //returns whether there is more then one topping on the same quarter
    private boolean hasMultiple() {
        boolean hasMultiple = false;
        boolean hasOneTop;
        for (int i = 0; i < 4; i++) {
            hasOneTop = false;
            for (Topping t : toppingArrayList)
                if (t.getQuarterAt(i)) {
                    if (hasOneTop)
                        hasMultiple = true;
                    else
                        hasOneTop = true;
                }
        }
        return hasMultiple;
    }

    @Override
    public String toString() {
        //first, get the super string and go a line down- e.g.: "פיצה משפחתית:₪19.90*3
        String s = super.toString() + "\n";
        if (onOneFreeTop)
            s += "הפיצה במבצע תוספת חינם" + "\n";
        if (toppingArrayList.size() > 0) {
            s += "תוספות: ";
            if (!hasMultiple()) {
                s += this.getToppingArrayList().get(0).toString();
                if (this.getToppingArrayList().size() > 1) {
                    for (int i = 1; i < toppingArrayList.size(); i++)
                        s += (" , " + toppingArrayList.get(i));
                }
            } else {
                Topping t;
                int temp=0;
                for (int i = 0; i < 4; i++) {
                    s += "\n";
                    s += "רבע " + Integer.toString(i + 1) + "- ";
                    //adjusting for the weird design my yuval did
                    if(i==1)
                        temp=2;
                    if(i==2)
                        temp=3;
                    if(i==3)
                        temp=1;
                    for (int j = 0; j < getToppingsOnQuarter(temp).size(); j++){
                        t = getToppingsOnQuarter(temp).get(j);
                        s += (j==0?"":", ")+t.getName();
                    }
                }
            }
        }
        return s;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public boolean isOnOneFreeTop() {
        return onOneFreeTop;
    }

    public void setOnOneFreeTop(boolean onOneFreeTop) {
        this.onOneFreeTop = onOneFreeTop;
    }
}