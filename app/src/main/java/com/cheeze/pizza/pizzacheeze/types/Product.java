package com.cheeze.pizza.pizzacheeze.types;

/**
 * Created by yuval on 10/09/2017.
 */


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import com.cheeze.pizza.pizzacheeze.SplashActivity;
import com.cheeze.pizza.pizzacheeze.cardListsAdapters.ChooseOrderAdapter;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

/**
 * Created by Tal on 10/08/2017.
 */


public class Product implements Serializable {
    protected boolean preferred;
    private String objectType;
    protected String id;
    protected String name;
    protected String type;
    protected String pictureId;
    protected double price;
    protected boolean available;
    protected int count = 1;
    protected double totalPrice;
    protected String note = "";
    private boolean onDiscount;
    @Deprecated
    private String uri;
    private String picUri;

    public Product(String id, String name, double price, String type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public Product(Product p) {
        this.id = p.getId();
        this.name = p.getName();
        this.type = p.getType();
        this.pictureId = p.getPictureId();
        this.price = p.getPrice();
        this.available = p.isAvailable();
        this.count = p.getCount();
        this.totalPrice = p.getPrice();
        this.objectType = p.getObjectType();
        this.preferred = p.isPreferred();
        this.note = p.getNote();
    }

    public void setProduct(Product p) {
        this.id = p.getId();
        this.name = p.getName();
        this.type = p.getType();
        this.pictureId = p.getPictureId();
        this.price = p.getPrice();
        this.available = p.isAvailable();
        this.count = p.getCount();
        this.totalPrice = p.getPrice();
        this.note = p.getNote();
    }

    public Product() {
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Uri getPicUri() {
        return Uri.parse(picUri);
    }

    public void loadPicToIv(ImageView iv) {
        if (picUri != null) {
            Picasso.get().load(picUri).into(iv);
        } else {
            iv.setImageBitmap(getDefaultBitmap());
        }
    }

    public void setStringPicUri(Uri picUri) {
        if (picUri != null)
            this.picUri = picUri.toString();
    }


    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }


    //a virtual method that will be overridden by children
    public String toString() {
        String s = name + " : " + "₪" + Double.toString(totalPrice);
        if (count > 1)
            s += "  * " + count;
        if (onDiscount)
            s+= "\n" + "המוצר במבצע";
        if (!note.equals(""))
            s += "\n הערה: " + note;
        return s;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getPictureId() {
        return pictureId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void updateTotalPrice() {
        if (onDiscount)
            totalPrice = ((count - 1) * getMainPrice()) + price;
        else
            totalPrice = count * price;
        //if: 1. this is a pizza we are talking about
        //    2. the pizza is on the one free top discount
        if(this instanceof Pizza)
            if(((Pizza)this).isOnOneFreeTop())
                totalPrice = (count * price) - SplashActivity.myAppSettings.getToppingPrice();

    }

    public String getNote() {
        return note;
    }


    public void setNote(String note) {
        this.note = note;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPreferred() {
        return preferred;
    }

    public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public double getMainPrice() {
        for (Product p : SplashActivity.productArrayList)
            if (p.getName().equals(name))
                return p.getPrice();
        return price;
    }

    public Bitmap getDefaultBitmap(){
        switch (this.type) {
            case "פיצות":
                return BitmapFactory.decodeResource(SplashActivity.thisContext.getResources(), ChooseOrderAdapter.imageId[0]);
            case "פסטות":
                return BitmapFactory.decodeResource(SplashActivity.thisContext.getResources(), ChooseOrderAdapter.imageId[1]);
            case "מאפים":
                return BitmapFactory.decodeResource(SplashActivity.thisContext.getResources(), ChooseOrderAdapter.imageId[2]);
            case "שתייה":
                return BitmapFactory.decodeResource(SplashActivity.thisContext.getResources(), ChooseOrderAdapter.imageId[3]);
            case "סלטים":
                return BitmapFactory.decodeResource(SplashActivity.thisContext.getResources(), ChooseOrderAdapter.imageId[4]);
            case "מנות אחרונות":
                return BitmapFactory.decodeResource(SplashActivity.thisContext.getResources(), ChooseOrderAdapter.imageId[5]);
            default:
                return null;
        }
    }

    public boolean isOnDiscount() {
        return onDiscount;
    }

    public void setOnDiscount(boolean onDiscount) {
        this.onDiscount = onDiscount;
    }

    public void updatePrice() {
    }
}