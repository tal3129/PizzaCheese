package com.cheeze.pizza.pizzacheeze.CustomViews;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheeze.pizza.pizzacheeze.R;
import com.cheeze.pizza.pizzacheeze.types.Product;
import com.lb.auto_fit_textview.AutoResizeTextView;

import java.text.DecimalFormat;

public class MyCardView extends RelativeLayout{

    RelativeLayout mainLayout;
    RelativeLayout.LayoutParams mainLayoutParams;

    ImageView imageView;
    RelativeLayout.LayoutParams imageViewParams;

    AutoResizeTextView textView;
    RelativeLayout.LayoutParams textViewParams;

    RelativeLayout rlPrice;
    RelativeLayout.LayoutParams rlPriceParams;

    AutoResizeTextView tvPrice;
    RelativeLayout.LayoutParams tvPriceParams;

    Typeface face;

    ImageView imageButton;
    RelativeLayout.LayoutParams imageButtonParams;


    private Product product;
    int image_view_size;
    int image_view_margin_right;
    int text_size;
    int text_margin_right;
    int image_button_size;
    int image_button_margin_left;

    int text_height;
    int text_width;

    public MyCardView(Context context , int width , int height , int color , String text, Product product) {
        super(context);
        this.product=product;
        setSizes(height,width,context);
        mainLayout = new RelativeLayout(context);
        mainLayout.setBackgroundColor(color);
        mainLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.ripple));
        mainLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        imageView = new ImageView(context);
        imageView.setId(generateViewId());
        product.loadPicToIv(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageViewParams = new LayoutParams(image_view_size,image_view_size);
        imageViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        imageViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        imageViewParams.setMargins(0,0,0,image_view_margin_right);

        face = Typeface.createFromAsset(context.getAssets(), "fonts/VarelaRound-Regular.ttf");
        textView = new AutoResizeTextView(context);
        textView.setId(generateViewId());
        textView.setTextSize(text_size);
        textView.setText(text);
        textView.setTextColor(Color.parseColor("#d17a80"));
        textView.setTypeface(face);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textViewParams = new LayoutParams(text_width, text_height);
        textViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        textViewParams.addRule(RelativeLayout.LEFT_OF,imageView.getId());
        textViewParams.setMargins(0,0,text_margin_right,0);

        imageButton = new ImageView(context);
        imageButton.setId(generateViewId());
        imageButton.setImageResource(R.drawable.add_proudoct_icon);
        imageButtonParams = new LayoutParams(image_button_size,image_button_size);
        imageButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        imageButtonParams.addRule(RelativeLayout.CENTER_VERTICAL);
        imageButtonParams.setMargins(image_button_margin_left,0,0,0);

        rlPrice = new RelativeLayout(context);
        rlPriceParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,(int)(image_button_size*0.75));
        rlPriceParams.addRule(RelativeLayout.START_OF,textView.getId());
        rlPriceParams.addRule(RelativeLayout.END_OF,imageButton.getId());
        rlPriceParams.addRule(RelativeLayout.CENTER_VERTICAL);

        tvPrice = new AutoResizeTextView(context);
        tvPrice.setId(generateViewId());
        DecimalFormat df = new DecimalFormat("#.00");
        tvPrice.setText("₪" + df.format(product.getPrice()));
        tvPrice.setTextColor(Color.parseColor("#d17a80"));
        tvPrice.setTextSize(1000);
        tvPrice.setGravity(Gravity.CENTER_HORIZONTAL);
        tvPriceParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tvPriceParams.addRule(RelativeLayout.START_OF,textView.getId());
        tvPriceParams.addRule(RelativeLayout.RIGHT_OF,imageButton.getId());
        tvPriceParams.addRule(RelativeLayout.CENTER_VERTICAL);

        rlPrice.addView(tvPrice,tvPriceParams);

        mainLayout.addView(imageView,imageViewParams);
        mainLayout.addView(textView,textViewParams);
        mainLayout.addView(imageButton,imageButtonParams);
        mainLayout.addView(rlPrice,rlPriceParams);

        this.addView(mainLayout,mainLayoutParams);
    }


    public void setSizes(int height,int width,Context context){
        image_view_size =(int)(height/1.3);
        image_view_margin_right = (int)(image_view_size/8);
        text_size=convertSpToPixels(100,context);
        text_margin_right = image_view_margin_right;
        image_button_size = (int)( height/2.5);
        image_button_margin_left = width/30;

         text_height = (int)( height/2);
         text_width = (int)(width/2.1);
    }

    public static int convertSpToPixels(float sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

    public Product getProduct() {
        return product;
    }
}
