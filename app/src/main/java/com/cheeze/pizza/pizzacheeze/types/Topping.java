package com.cheeze.pizza.pizzacheeze.types;

import android.net.Uri;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class Topping implements Serializable {
    private String name;
    private ArrayList<Boolean> onQuarters;
    private String picUri;

    public Topping(String name,String picUri){
        this.name = name;
        this.picUri=picUri;
        onQuarters = new ArrayList<>();
        for(int i=0;i<4;i++)
            onQuarters.add(false);
    }

    public int getQuarterCount(){
        int sum=0;
        for (int i=0;i<4;i++)
            if (onQuarters.get(i))
                sum++;
        return sum;
    }

    public void loadRotatedPic(ImageView imageView, int index) {
        Picasso.get()
                .load(picUri)
                .rotate(index == 1 ? 270 : index == 2 ? 90 : index == 3? 180: 0)
                .into(imageView);
    }

    public Uri getPicUri() {
        return Uri.parse(picUri);
    }

    public void setPicUri(Uri picUri) {
        this.picUri = picUri.toString();
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }

    public boolean getQuarterAt(int index){
        return onQuarters.get(index);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void changeQuater(int index){
        onQuarters.set(index, !onQuarters.get(index));
    }

    public void changeQuater(int index,boolean mode){
        onQuarters.set(index,mode);
    }

    public String toString() {
        String s="";
        switch (this.getQuarterCount()){
            case 1:
                s +="רבע "+name;
                break;
            case 2:
                s +="חצי "+name;
                break;
            case 3:
                s+="3 רבעים "+ name;
                break;
            case 4:
                s+="תוספת "+name;
        }
        return s;
    }

    public ArrayList<Boolean> getOnQuarters() {
        return onQuarters;
    }

    public void setOnQuarters(ArrayList<Boolean> onQuarters) {
        this.onQuarters = onQuarters;
    }
}
