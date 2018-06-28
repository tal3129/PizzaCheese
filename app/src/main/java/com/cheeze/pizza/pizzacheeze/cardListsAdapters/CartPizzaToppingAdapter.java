package com.cheeze.pizza.pizzacheeze.cardListsAdapters;

/**
 * Created by yuval on 08/10/2017.
 */

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.cheeze.pizza.pizzacheeze.CustomViews.ToppingCard;
import com.cheeze.pizza.pizzacheeze.R;
import com.cheeze.pizza.pizzacheeze.types.Topping;

import java.util.ArrayList;

public class CartPizzaToppingAdapter  extends ArrayAdapter<Topping> {
    private Activity context;
    private ArrayList<Topping> toppingList;
    private int popupWidth;
    private int popupHeight;
    private int itemHeight;




    public CartPizzaToppingAdapter(Activity context, ArrayList<Topping> toppingList,int popupHeight , int popupWidth,int itemHeight) {
        super(context, R.layout.pizza_cart_topping_item,toppingList);
        this.context = context;
        this.toppingList = toppingList;
        this.popupWidth = popupWidth;
        this.popupHeight = popupHeight;
        this.itemHeight = itemHeight;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ToppingCard toppingCard = new ToppingCard(context,itemHeight,popupWidth , toppingList.get(position));
        toppingCard.onCart(context);

/*
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.cart_item, null, true);

        final TextView productName = (TextView) listViewItem.findViewById(R.id.tvProductName);
        final TextView productPrice = (TextView) listViewItem.findViewById(R.id.tvProductPrice);
        //final TextView productNote = (TextView) listViewItem.findViewById(R.id.tvNote);

        String s="";
        if(product instanceof Pizza)
            for(Topping topping:((Pizza)product).getToppingArrayList()) {
                switch (topping.getQuarterCount()){
                    case 1:
                        s +="רבע "+topping.getName()+"\n";
                        break;
                    case 2:
                        s +="חצי "+topping.getName()+"\n";
                        break;
                    case 3:
                        s+="3 רבעים "+ topping.getName()+"\n";
                        break;
                    case 4:
                        s+="תוספת "+topping.getName()+"\n";
            }
        }

        //productNote.setText(s);
        productName.setText(product.getName());
        productPrice.setText(String.format("%.2f",product.getPrice())+" ₪ ");
 */
        return toppingCard;
    }


}
