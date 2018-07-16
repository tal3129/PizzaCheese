package com.cheeze.pizza.pizzacheeze.CustomViews;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.*;
import com.cheeze.pizza.pizzacheeze.R;
import com.cheeze.pizza.pizzacheeze.types.Product;


public class MyNumberPicker extends LinearLayout {

    RelativeLayout rlMinus;
    LinearLayout.LayoutParams rlMinusParams;

    ImageView ivMinus;
    RelativeLayout.LayoutParams ivMinusParams;

    EditText etCount;
    LinearLayout.LayoutParams etCountParams;

    RelativeLayout rlPlus;
    LinearLayout.LayoutParams rlPlusParams;

    ImageView ivPlus;
    RelativeLayout.LayoutParams ivPlusParams;

    int minValue=1;
    int maxValue=200;

    int correctCount=1;

    Product product;

    public MyNumberPicker(Context context, int item_height, Product p,final TextView finalPrice) {
        super(context);

        this.product = p;

        correctCount = product.getCount();

        this.setOrientation(HORIZONTAL);

        int image_view_icon_size = item_height / 3;

        rlMinus = new RelativeLayout(context);
        rlMinus.setBackgroundResource(R.drawable.et_background);
        rlMinus.setOnClickListener(view -> {
            if (etCount.getText().toString().equals("")) {
                etCount.setText(minValue + "");
                correctCount = minValue;
            }

            if (correctCount > minValue) {
                correctCount--;
                etCount.setText(correctCount + "");
            }
        });
        rlMinusParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rlMinusParams.weight = 8;

        ivMinus = new ImageView(context);
        ivMinus.setImageResource(R.drawable.minus_red);
        ivMinusParams = new RelativeLayout.LayoutParams(image_view_icon_size,image_view_icon_size);
        ivMinusParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        ivMinusParams.addRule(RelativeLayout.CENTER_VERTICAL);

        etCount = new EditText(context);
        etCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0){
                    if (s.charAt(0)=='0'){
                        s = s.subSequence(1,s.length());
                        etCount.setText(s);
                    }
                }

                if (s.length()!=0){
                    correctCount =  Integer.valueOf(s.toString());
                    if (Integer.valueOf(s.toString())>maxValue){
                        etCount.setText(maxValue + "");
                        correctCount = maxValue;
                    }
                    else if (Integer.valueOf(s.toString())<minValue){
                        etCount.setText(minValue + "");
                        correctCount = minValue;
                    }
                }

                product.setCount(correctCount);
                product.updateTotalPrice();
                finalPrice.setText("₪"+String.format("%.2f",product.getTotalPrice()));
            }


        });
        etCount.setGravity(Gravity.CENTER_HORIZONTAL);
        etCount.setTextSize(convertSpToPixels(4,context));
        etCount.setText(String.valueOf(correctCount));
        etCount.setInputType(InputType.TYPE_CLASS_NUMBER);
        etCount.setBackgroundResource(R.drawable.et_background);
        etCountParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        etCountParams.gravity = Gravity.CENTER;
        etCountParams.weight=10;

        rlPlus = new RelativeLayout(context);
        rlPlus.setOnClickListener(view -> {

            if (etCount.getText().toString().equals("")) {
                etCount.setText(minValue + "");
                correctCount = minValue;
            }

            if (correctCount < maxValue) {
                correctCount++;
                etCount.setText(correctCount + "");
            }
        });
        rlPlus.setBackgroundResource(R.drawable.et_background);
        rlPlusParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rlPlusParams.weight = 8;

        ivPlus = new ImageView(context);
        ivPlus.setImageResource(R.drawable.plus_red);
        ivPlusParams = new RelativeLayout.LayoutParams(image_view_icon_size,image_view_icon_size);
        ivPlusParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        ivPlusParams.addRule(RelativeLayout.CENTER_VERTICAL);


        rlMinus.addView(ivMinus,ivMinusParams);
        rlPlus.addView(ivPlus,ivPlusParams);

        this.addView(rlMinus,rlMinusParams);
        this.addView(etCount,etCountParams);
        this.addView(rlPlus,rlPlusParams);
    }


    public void setMinValue(int minValue){
        this.minValue = minValue;
    }

    public void setMaxValue(int maxValue){
        this.maxValue = maxValue;
    }

    public void setCorrectCount(int correctCount){
        this.correctCount = correctCount;
        etCount.setText(correctCount + "");
    }

    public static int convertSpToPixels(float sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }
}