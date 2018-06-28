package com.cheeze.pizza.pizzacheeze.types;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jivimberg.library.AutoResizeTextView;

/**
 * Created by yuval on 29/11/2017.
 */

public class MyLuckyWheelInfo extends RelativeLayout  {


    int screenHeight;
    int itemHeight;

    int image_width;

    ImageView imageView;
    RelativeLayout.LayoutParams imageViewParams;

    AutoResizeTextView textView;
    RelativeLayout.LayoutParams textViewParams;

    public MyLuckyWheelInfo(Context context,int itemHeight, String text, int color) {
        super(context);
        this.image_width = itemHeight;
        this.itemHeight = itemHeight;

        this.imageView = new ImageView(context);
        this.imageView.setId(generateViewId());
        this.imageView.setBackgroundColor(color);
        this.imageViewParams = new RelativeLayout.LayoutParams(itemHeight,itemHeight);
        this.imageViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);


        this.textView = new AutoResizeTextView(context);
        this.textView.setId(generateViewId());
        this.textView.setTextSize(convertSpToPixels(25,context));
        this.textView.setGravity(Gravity.CENTER_VERTICAL);
        this.textView.setText(text);
        this.textViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,itemHeight);
        this.textViewParams.addRule(RelativeLayout.LEFT_OF,this.imageView.getId());
        this.textViewParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        this.textViewParams.setMargins(0,0,itemHeight/10,0);

        this.addView(this.imageView,this.imageViewParams);
        this.addView(this.textView,this.textViewParams);

    }

    public static int convertSpToPixels(float sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }


}
