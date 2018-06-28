package com.cheeze.pizza.pizzacheeze.types;

import java.util.ArrayList;

/**
 * Created by Tal on 02/11/17.
 */

public class ToppingProduct extends Product {
    private double originalPrice;
    private ArrayList<Topping> toppings;

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public ArrayList<Topping> getToppings() {
        return toppings;
    }

    public void setToppings(ArrayList<Topping> toppings) {
        this.toppings = toppings;
    }

    public ToppingProduct(Product p) {
        super(p);
        this.originalPrice=super.price;
        this.toppings=new ArrayList<>();
    }
    public void addTopping(Topping topping){
        this.toppings.add(topping);
        updatePrice();
    }
    public void removeTopping(Topping topping){
        this.toppings.remove(topping);
        updatePrice();
    }

    @Override
    public void updatePrice(){
        double extraPrice=toppings.size()==0?0:(toppings.size()-1)*4;
        super.price=originalPrice+extraPrice;
    }

    public boolean hasTopping(Topping topping){
       for (Topping t : toppings)
           if (t.getName().equals(topping.getName()))
               return true;
       return false;
    }

    @Override
    public String toString(){
        String s= super.toString()+"\n";
        return s+this.toppingString();
    }

    public String toppingString(){
        String s = "תוספות: ";

        for (int i=0;i<toppings.size();i++){
            s += " " +toppings.get(i).getName();
            if (i!=toppings.size()-1)
                s+=", ";
        }
        if (toppings.size()==0)
            s="";

        return s;
    }

}
