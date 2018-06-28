package com.cheeze.pizza.pizzacheeze.cardListsAdapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cheeze.pizza.pizzacheeze.LoadingDialog;
import com.cheeze.pizza.pizzacheeze.types.Pizza;
import com.cheeze.pizza.pizzacheeze.types.Product;
import com.cheeze.pizza.pizzacheeze.R;
import com.cheeze.pizza.pizzacheeze.types.Topping;

import java.util.ArrayList;

/**
 * Created by Tal on 13/09/17.
 */

public class CartAdapter  extends ArrayAdapter<Product> {
    private Activity context;
    private ArrayList<Product> productList;
    private int popupWidth;
    private int popupHeight;


    public CartAdapter(Activity context, ArrayList<Product> productList, int popupWidth, int popupHeight ) {
        super(context, R.layout.cart_item, productList);
        this.context = context;
        this.productList = productList;

        this.popupWidth = popupWidth;
        this.popupHeight = popupHeight;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product product = productList.get(position);

        CartRow cartRow = new CartRow(context,popupWidth,popupHeight/10,product.getName(),"₪"+String.format("%.2f",product.getTotalPrice()),product);

        if (product instanceof Pizza)
            cartRow.setToppingForPizza((Pizza) product,popupWidth,popupHeight);
        return cartRow;
    }



}
