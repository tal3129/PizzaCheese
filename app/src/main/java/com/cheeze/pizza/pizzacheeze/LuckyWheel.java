package com.cheeze.pizza.pizzacheeze;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.cheeze.pizza.pizzacheeze.CustomViews.Banner;
import com.cheeze.pizza.pizzacheeze.types.MyWheelItem;
import com.cheeze.pizza.pizzacheeze.types.Product;
import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

import java.util.ArrayList;
import java.util.List;

public class LuckyWheel extends AppCompatActivity {

    RelativeLayout mainLayout;

    Button btnSpin;
    ImageView ivLuckyText;
    ImageView btnInfo;

    LuckyWheelView luckyWheel;
    List<LuckyItem> luckyItems;

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

        luckyWheel = findViewById(R.id.luckyWheel);
        luckyWheel.getLayoutParams().height = popupWidth;
        luckyWheel.getLayoutParams().width = popupWidth;
        luckyWheel.setRound(6);

        setLuckyItems();

        btnSpin.setOnClickListener(view -> spin());

        luckyWheel.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x1 = event.getX();
                    y1 = event.getY();
                    return true;
                case MotionEvent.ACTION_UP:
                    x2 = event.getX();
                    y2 = event.getY();

                    if (x1 > x2) { // left swipe
                        spin();
                    } else if (x2 > x1) { // right swipe
                        spin();
                    }
                    return true;
            }
            return false;
        });

        luckyWheel.setLuckyRoundItemSelectedListener(i -> {
            final Banner b = new Banner(LuckyWheel.this);
            b.setToppingDiscount();
            Product p = SplashActivity.myWheelItems.get(i - 1).getFreeProduct();
            if (p == null) {
                b.setToppingDiscount();
                b.getIvProd().setVisibility(View.GONE);
                b.getTvTitle().setText("לא נורא...");
                b.getTvDescription().setText("לא זכית הפעם, אולי בפעם הבאה");
                b.show();
            } else {
                SplashActivity.order.getFreeProducts().add(p);
                switch (p.getId()) {
                    //for a 1+1 discount
                    case "freeToppings":
                        b.setToppingDiscount();
                        b.getTvTitle().setText("זכית!");
                        b.getTvDescription().setText("1+1 על התוספות בהזמנה הבאה!");
                        break;
                    case "oneFreeTop":
                        b.setToppingDiscount();
                        b.getTvTitle().setText("זכית!");
                        b.getTvDescription().setText("תוספת אחת מתנה בהזמנה הבאה!");
                        b.getIvProd().setImageResource(R.drawable.gift);
                        break;
                    //for a free order discount
                    case "freeOrder":
                        //setting the banner as a topping discount banner to have only one acceptation button
                        b.setToppingDiscount();
                        b.getTvTitle().setText("זכית!");
                        b.getTvDescription().setText("כל ההזמנה במתנה בהזמנה הבאה!!!");
                        b.getIvProd().setImageResource(R.drawable.gift);
                        break;
                    //if this is a regular free product, add it for free
                    default:
                        p.setPrice(0);
                        b.setDiscounted(p);
                        b.getTvTitle().setText("זכית!");
                        b.getTvDescription().setText(p.getName() + " במתנה בהזמנה הבאה!");
                        break;
                }
                b.show();
            }
            b.setOnDismissListener(dialog -> finish());

        });


        btnInfo.setOnClickListener(view -> {
            Intent intent = new Intent(LuckyWheel.this, LuckyWheelDetails.class);
            startActivity(intent);
        });


    }

    public void spin(){
        if (SplashActivity.order.getCart().getSum() < SplashActivity.myAppSettings.getMinWheelSum()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("אופס!").setMessage( "לא ניתן לסובב את הגלגל בביצוע הזמנה מתחת ל" + "₪" +  SplashActivity.myAppSettings.getMinWheelSum())
                    .setPositiveButton("אישור", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        finish();
                    });
            builder.show();
        } else if (!SplashActivity.myAppSettings.isLuckyWheelEnabled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("אופס!").setMessage("גלגל המזל אינו זמין כרגע..")
                    .setPositiveButton("אישור", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        finish();
                    });
            builder.show();
        } else if (!isRound) {
            luckyWheel.startLuckyWheelWithTargetIndex(getRandomIndex());
            btnSpin.setClickable(false);
            isRound = true;
        }
    }

    public void setLuckyItems() {
        luckyItems = new ArrayList<>();
        for (MyWheelItem myWheelItem : SplashActivity.myWheelItems) {
            LuckyItem luckyItem = new LuckyItem();
            luckyItem.color = myWheelItem.getColor();
            luckyItem.text = "";
            luckyItem.icon = R.drawable.tra;
            luckyItems.add(luckyItem);
        }
        luckyWheel.setData(luckyItems);
    }

    public int getRandomIndex() {
        double result = Math.random() * 100;
        double sum = 0;
        for (int i = 0; i < SplashActivity.myWheelItems.size(); i++) {
            double itemChance = SplashActivity.myWheelItems.get(i).getChance();
            if (result <= itemChance + sum) {
                return i + 1;

            }
            sum += itemChance;
        }

        return 0;
    }

}
