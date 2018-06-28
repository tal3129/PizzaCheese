package com.cheeze.pizza.pizzacheeze;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheeze.pizza.pizzacheeze.types.Discount;
import com.cheeze.pizza.pizzacheeze.types.Product;

import java.util.ArrayList;

/**
 * Created by yuval on 18/01/2018.
 */

public class MivtzaimBannersAdapter extends PagerAdapter {


    ArrayList<Discount> discounts = new ArrayList<Discount>();
    Discount d;


    TextView tvTitle;
    ImageView ivProd;
    TextView tvDescription;

    Button btnAdd;
    Button btnDismiss;
    Button btnBannerFinish;


    Context context;

    public MivtzaimBannersAdapter(Context context, ArrayList<Discount> newDiscounts) {
        super();


        this.context = context;
        this.discounts = newDiscounts;

    }

    @SuppressLint("NewApi")
    @Override
    public void finishUpdate(ViewGroup container) {

        super.finishUpdate(container);

    }

    @Override
    public int getCount() {

        return discounts.size();

    }

    @Override
    public boolean isViewFromObject(View collection, Object object) {

        return collection == ((View) object);
    }

    @Override
    public Object instantiateItem(View collection, int position) {
        // Inflating layout
        LayoutInflater inflater = (LayoutInflater) collection.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Setting view you want to display as a row element
        final View view = inflater.inflate(R.layout.banner_layout, null);


        tvTitle = view.findViewById(R.id.tvTitle);
        ivProd = view.findViewById(R.id.ivProd);
        tvDescription = view.findViewById(R.id.tvDescription);

        btnAdd = view.findViewById(R.id.btnAdd);
        btnDismiss = view.findViewById(R.id.btnDismiss);
        btnBannerFinish = view.findViewById(R.id.btnBannerFinish);


        // Getting reference for text view and inflate the view for Answers

        try {
            d = discounts.get(position);
            btnAdd.setVisibility(View.GONE);
            btnDismiss.setVisibility(View.GONE);
            btnBannerFinish.setVisibility(View.GONE);


            setToppingDiscount();
            tvTitle.setText("מבצע!");
            if(!d.getId().equals("toppingsDiscount")) {
                d.getDiscounted().loadPicToIv(ivProd);
                tvDescription.setText(d.toString());
            }



        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ((ViewPager) collection).addView(view, 0);
        return view;


    }





    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);

    }


    public void setToppingDiscount() {
        tvTitle.setText("מבצע!");
        tvDescription.setText("1+1 על כל התוספות");
        ivProd.setImageResource(R.drawable.one_plus_one);

    }

}