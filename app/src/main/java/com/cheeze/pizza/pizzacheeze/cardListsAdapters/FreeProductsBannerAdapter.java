package com.cheeze.pizza.pizzacheeze.cardListsAdapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.cheeze.pizza.pizzacheeze.MainActivity;
import com.cheeze.pizza.pizzacheeze.R;
import com.cheeze.pizza.pizzacheeze.ShoppingCart;
import com.cheeze.pizza.pizzacheeze.SplashActivity;
import com.cheeze.pizza.pizzacheeze.types.Product;

import java.util.ArrayList;

/**
 * Created by yuval on 18/01/2018.
 */

public class FreeProductsBannerAdapter extends PagerAdapter {


    ArrayList<Product> freeProduts = new ArrayList<>();
    Product p;

    ArrayList<String> titles = new ArrayList<>();
    ArrayList<Integer> images = new ArrayList<>();
    ArrayList<String> descriptions = new ArrayList<>();


    TextView tvTitle;
    ImageView ivProd;
    TextView tvDescription;

    Button btnAdd;
    Button btnDismiss;
    Button btnBannerFinish;


    Context context;

    public FreeProductsBannerAdapter(Context context, ArrayList<Product> newfreeProduts) {
        super();


        this.context = context;
        this.freeProduts = newfreeProduts;

    }

    @SuppressLint("NewApi")
    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);

    }

    @Override
    public int getCount() {

        return freeProduts.size();

    }

    @Override
    public boolean isViewFromObject(View collection, Object object) {

        return collection == object;
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
            /*
             tvTitle.setText(titles.get(position));
            ivProd.setImageResource(images.get(position));
            tvDescription.setText(descriptions.get(position));
             */

            p = freeProduts.get(position);

            switch (p.getId()) {
                //for a 1+1 discount
                case "freeToppings":
                    setToppingDiscount();
                    tvTitle.setText("נמצאה הטבה!");
                    btnBannerFinish.setOnClickListener(v -> {
                        SplashActivity.onePlusActive = true;
                        SplashActivity.order.getFreeProducts().remove(0);
                        ((Activity) view.getContext()).finish();
                    });
                    break;
                //for a one free topping
                case "oneFreeTop":
                    setToppingDiscount();
                    tvTitle.setText("נמצאה הטבה!");
                    tvDescription.setText("תוספת אחת במתנה!");
                    ivProd.setImageResource(R.drawable.gift);
                    btnBannerFinish.setOnClickListener(v -> {
                        SplashActivity.order.getFreeProducts().remove(0);
                        SplashActivity.oneFreeTop = true;
                        ((Activity) view.getContext()).finish();
                    });
                    break;
                //for a free order discount
                case "freeOrder":
                    //setting the banner as a topping discount banner to have only one acceptation button
                    setToppingDiscount();
                    tvTitle.setText("נמצאה הטבה!");
                    tvDescription.setText("כל ההזמנה הקודמת במתנה!!!");
                    ivProd.setImageResource(R.drawable.gift);
                    //add the previous cart to this one for free
                    btnBannerFinish.setOnClickListener(v -> {
                        SplashActivity.order.addToCartForFree(SplashActivity.lastOrder.getCart());
                        SplashActivity.order.getFreeProducts().remove(0);
                        MainActivity.needToLoad = false;
                        ((Activity) view.getContext()).finish();
                        Intent intent = new Intent(v.getContext(), ShoppingCart.class);
                        view.getContext().startActivity(intent);
                    });
                    break;
                //if this is a regular free product, add it for free
                default:
                    p.setPrice(0);
                    p.loadPicToIv(ivProd);
                    tvTitle.setText("נמצאה הטבה!");
                    tvDescription.setText(p.getName() + " במתנה!");
                    btnAdd.setOnClickListener(v -> {
                        Product tempP = new Product(p);
                        tempP.setOnDiscount(true);
                        SplashActivity.order.getCart().addProduct(tempP);
                        SplashActivity.order.getFreeProducts().remove(0);
                        ((Activity)view.getContext()).finish();
                    });
                    btnDismiss.setOnClickListener(view1 -> ((Activity) view1.getContext()).finish());
                    break;
            }

        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        ((ViewPager) collection).addView(view, 0);
        return view;


    }


    public void setToppingDiscount() {
        tvTitle.setText("מבצע!");
        tvDescription.setText("1+1 על כל התוספות");
        ivProd.setImageResource(R.drawable.one_plus_one);

        btnAdd.setVisibility(View.GONE);
        btnDismiss.setVisibility(View.GONE);
        btnBannerFinish.setVisibility(View.VISIBLE);
    }




    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);

    }

}