package com.cheeze.pizza.pizzacheeze.types;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tal on 22/03/18.
 */

public class PicList {
    private List<Pic> pics;
    private int version;

    public PicList(int ver) {
        pics = new ArrayList<>();
        version = ver;
    }

    public PicList(){

    }

    public List<Pic> getPics() {
        return pics;
    }

    public void setPics(List<Pic> pics) {
        this.pics = pics;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Uri getPicUri(String id) {
        for (Pic pic : pics)
            if (pic.getId().equals(id))
                return Uri.parse(pic.getUri());

        return null;
    }
}
