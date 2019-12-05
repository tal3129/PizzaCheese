package com.cheeze.pizza.pizzacheeze;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cheeze.pizza.pizzacheeze.CustomViews.Banner;
import com.cheeze.pizza.pizzacheeze.types.MyWheelItem;

import java.util.ArrayList;

public class LuckyWheel extends AppCompatActivity {

    RelativeLayout mainLayout;

    Button btnSpin;
    ImageView ivLuckyText;
    ImageView btnInfo;


    float x1;
    float y1;

    float x2;
    float y2;
    boolean isRound = false;


    int width;
    int height;

    int popupWidth;
    int popupHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_OK);
        setContentView(R.layout.activity_lucky_wheel);
        //get screen size
        final Display display = this.getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();

        this.getWindow().getAttributes().windowAnimations = R.style.growAnimations;
        popupWidth = (int) (width / 1.1);
        popupHeight = (int) (height / 1.1);

        ivLuckyText = findViewById(R.id.luckyImageText);

        int ivLuckyTextWidth = (int) (popupWidth / 1.5);
        ivLuckyText.getLayoutParams().width = ivLuckyTextWidth;
        ivLuckyText.getLayoutParams().height = (int) (ivLuckyTextWidth / (2.6));


        mainLayout = findViewById(R.id.mainLayout);
        mainLayout.getLayoutParams().width = popupWidth;
        mainLayout.getLayoutParams().height = popupHeight;

        btnSpin = findViewById(R.id.btnSpin);
        btnSpin.getLayoutParams().width = width / 2;

        btnInfo = findViewById(R.id.btnInfo);


        final Banner b = new Banner(LuckyWheel.this);
        b.setToppingDiscount();
        b.setToppingDiscount();
        b.getIvProd().setVisibility(View.GONE);
        b.getTvTitle().setText("לא נורא...");
        b.getTvDescription().setText("לא זכית הפעם, אולי בפעם הבאה");
        b.show();

        b.setOnDismissListener(dialog -> finish());


    }



}
