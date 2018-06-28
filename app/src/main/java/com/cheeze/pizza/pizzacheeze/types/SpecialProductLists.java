package com.cheeze.pizza.pizzacheeze.types;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Tal on 02/11/17.
 */

public class SpecialProductLists {
    private ArrayList<Pizza>pizzas;
    private ArrayList<Pasta>pastas;
    private ArrayList<ToppingProduct>toppingProducts;

    public ArrayList<Pizza> getPizzas() {
        return pizzas;
    }

    public void setPizzas(ArrayList<Pizza> pizzas) {
        this.pizzas = pizzas;
    }

    public ArrayList<Pasta> getPastas() {
        return pastas;
    }

    public void setPastas(ArrayList<Pasta> pastas) {
        this.pastas = pastas;
    }

    public ArrayList<ToppingProduct> getToppingProducts() {
        return toppingProducts;
    }

    public void setToppingProducts(ArrayList<ToppingProduct> toppingProducts) {
        this.toppingProducts = toppingProducts;
    }

    public SpecialProductLists() {
        this.pizzas=new ArrayList<>();
        this.pastas=new ArrayList<>();
        this.toppingProducts=new ArrayList<>();
    }
}
