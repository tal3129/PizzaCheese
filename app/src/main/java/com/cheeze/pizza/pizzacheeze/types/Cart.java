package com.cheeze.pizza.pizzacheeze.types;

import com.cheeze.pizza.pizzacheeze.SplashActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by student on 12/09/2017.
 */

public class Cart implements Serializable {
    private ArrayList<Product> selectedProducts;
    private double sum;


    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public ArrayList<Product> getSelectedProducts() {
        return selectedProducts;
    }

    public void setSelectedProducts(ArrayList<Product> selectedProducts) {
        this.selectedProducts = selectedProducts;
    }

    public Cart() {
        selectedProducts = new ArrayList<>();
        sum = 0;
    }


    public void addProduct(Product p) {
        p.updateTotalPrice();
        sum += p.getTotalPrice();
        selectedProducts.add(p);
    }

    public void updatePrice() {
        sum = 0;
        for (Product p : selectedProducts) {
            p.updateTotalPrice();
            sum += p.getTotalPrice();
        }
    }

    public void removeProduct(Product p) {
        sum -= p.getTotalPrice();
        selectedProducts.remove(p);
    }

    public String toString() {
        String s = "";
        for (Product product : this.selectedProducts)
            s += "\n" + product.toString();
        s += "\n\n" + "מחיר כולל לתשלום: " + String.format("%.2f", sum) + " ₪ ";
        return s;
    }


}
