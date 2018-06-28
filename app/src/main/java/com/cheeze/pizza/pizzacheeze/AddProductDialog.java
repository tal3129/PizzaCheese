package com.cheeze.pizza.pizzacheeze;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cheeze.pizza.pizzacheeze.types.Product;
import com.lb.auto_fit_textview.AutoResizeTextView;

/**
 * Created by yuval on 23/10/2017.
 */



public class AddProductDialog extends Dialog {

    public static int button_finish_order_id;
    public static int button_contoniue_id;

    public static Product product;


    RelativeLayout mainLayout;
    RelativeLayout.LayoutParams mainLayoutParams;

    Button bFinishOrder;
    RelativeLayout.LayoutParams bFinishOrderParams;

    Button bContoniue;
    RelativeLayout.LayoutParams bContoniueParams;

    RelativeLayout buttonLayout;
    RelativeLayout.LayoutParams buttonLayoutParams;

    AutoResizeTextView tvSuccess;
    RelativeLayout.LayoutParams tvSuccessParams;

    FloatingActionButton fabEdit;
    RelativeLayout.LayoutParams fabEditParams;

    AutoResizeTextView tvEdit;
    RelativeLayout.LayoutParams tvEditParams;

    RelativeLayout editLayout;
    RelativeLayout.LayoutParams editLayoutParams;

    Context context;

    int width;
    int height;
    int button_width;
    int button_height;
    int tv_height;
    int tv_width;
    int edit_margins;

    public AddProductDialog(@NonNull Context context,int width,int height) {
        super(context);
        this.context = context;
        this.width = width;
        this.height = height;

        setSizes();

        mainLayout = new RelativeLayout(context);
        mainLayoutParams = new RelativeLayout.LayoutParams(width,(int) (0.9*height));


        buttonLayout = new RelativeLayout(context);
        buttonLayout.setId(View.generateViewId());
        buttonLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        buttonLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

        tvSuccess = new AutoResizeTextView(context);
        tvSuccess.setId(View.generateViewId());
        tvSuccess.setText("המוצר התווסף בהצלחה");
        tvSuccess.setTextColor(Color.WHITE);
        tvSuccess.setTextSize(100);
        tvSuccess.setMaxLines(1);
        tvSuccess.setGravity(Gravity.CENTER_HORIZONTAL);
        tvSuccessParams = new RelativeLayout.LayoutParams(width,height/10);
        tvSuccessParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tvSuccessParams.addRule(RelativeLayout.ABOVE,buttonLayout.getId());


        bFinishOrder = new Button(context);
        bFinishOrder.setId(View.generateViewId());
        bFinishOrder.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
        bFinishOrder.setTextColor(Color.WHITE);
        bFinishOrder.setAlpha(0.80f);
        bFinishOrder.setText("גש לקופה");
        bFinishOrderParams = new RelativeLayout.LayoutParams(button_width,button_height);

        bContoniue = new Button(context);
        bContoniue.setId(View.generateViewId());
        bContoniue.setBackgroundColor(ContextCompat.getColor(context,R.color.colorAccent));
        bContoniue.setTextColor(Color.WHITE);
        bContoniue.setText("המשך");
        bContoniue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
        bContoniueParams = new RelativeLayout.LayoutParams(button_width,button_height);
        bContoniueParams.addRule(RelativeLayout.RIGHT_OF,bFinishOrder.getId());

        buttonLayout.addView(bFinishOrder,bFinishOrderParams);
        buttonLayout.addView(bContoniue,bContoniueParams);


        mainLayout.addView(tvSuccess,tvSuccessParams);
        mainLayout.addView(buttonLayout,buttonLayoutParams);

        buildEdit();

        this.addContentView(mainLayout,mainLayoutParams);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setCancelable(false);

        button_contoniue_id = bContoniue.getId();
        button_finish_order_id = bFinishOrder.getId();
    }

    public Button getbContoniue() {
        return bContoniue;
    }

    public Button getbFinishOrder() {
        return bFinishOrder;
    }

    public void buildEdit(){

        editLayout = new RelativeLayout(context);
        editLayout.setId(View.generateViewId());
        editLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        editLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        editLayoutParams.setMargins(0,0,edit_margins,0);


        fabEdit = new FloatingActionButton(context);
        fabEdit.setId(View.generateViewId());
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,EditCartItem.class);
                context.startActivity(intent);
                EditCartItem.product = product;
            }
        });
        fabEdit.setImageResource(R.drawable.ic_edit_white_48dp);
        fabEditParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fabEditParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        fabEditParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);


        tvEdit = new AutoResizeTextView (context);
        tvEdit.setId(View.generateViewId());
        tvEdit.setText("הוספת הערה / שינוי כמות");
        tvEdit.setTextColor(Color.WHITE);
        tvEdit.setTextSize(1000);
        tvEdit.setMaxLines(1);
        tvEdit.setGravity(Gravity.CENTER_VERTICAL);
        tvEditParams = new RelativeLayout.LayoutParams(tv_width, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvEditParams.addRule(RelativeLayout.START_OF,fabEdit.getId());
        tvEditParams.addRule(RelativeLayout.ALIGN_TOP,fabEdit.getId());
        tvEditParams.addRule(RelativeLayout.ALIGN_BOTTOM,fabEdit.getId());
        tvEditParams.setMargins(0,0,edit_margins,0);

        editLayout.addView(fabEdit,fabEditParams);
        editLayout.addView(tvEdit,tvEditParams);

        mainLayout.addView(editLayout,editLayoutParams);

    }


    public void setSizes(){
        button_height = height/11;
        button_width = width/4;

        tv_height = converDPtoPX(54);
        tv_width = width/2;
        edit_margins = tv_height/4;
    }
    public int converDPtoPX(int dp) {
        return dp * (int) context.getResources().getDisplayMetrics().density;
    }
}