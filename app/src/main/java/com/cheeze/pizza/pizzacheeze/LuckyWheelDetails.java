package com.cheeze.pizza.pizzacheeze;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cheeze.pizza.pizzacheeze.types.MyLuckyWheelInfo;

import java.util.ArrayList;

public class LuckyWheelDetails extends AppCompatActivity {

    ArrayList<MyLuckyWheelInfo> myLuckyWheelInfos;
    LinearLayout linearLayout;
    LinearLayout mainLayout;

    int screenHeight;
    int screenWidth;

    int activityHeight;
    int activityWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_lucky_wheel_details);

        mainLayout = findViewById(R.id.mainLayout);

        //get screen size
        final Display display = this.getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        activityHeight = (int)(screenHeight*0.7);
        activityWidth = (int)(screenWidth*0.7);

        mainLayout.getLayoutParams().width = activityWidth;
        mainLayout.getLayoutParams().height = activityHeight;
        // finish

        this.getWindow().getAttributes().windowAnimations = R.style.fadeAnimations;

        linearLayout = findViewById(R.id.LinearLayout);

        for (int i=0;i<SplashActivity.myWheelItems.size();i++){
            boolean duplicate = false;
            for (int j=0;j<i;j++){
                if (SplashActivity.myWheelItems.get(i).getColor() == SplashActivity.myWheelItems.get(j).getColor()){
                    duplicate = true;
                }
            }


            if (!duplicate) {
                MyLuckyWheelInfo myLuckyWheelInfo = new MyLuckyWheelInfo(this, activityHeight / 10, SplashActivity.myWheelItems.get(i).getName(), SplashActivity.myWheelItems.get(i).getColor());
                LinearLayout.LayoutParams myLuckyWheelInfoParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                myLuckyWheelInfoParams.setMargins(0, activityHeight / 40, 0, 0);
                linearLayout.addView(myLuckyWheelInfo, myLuckyWheelInfoParams);
            }
        }
    }

    public void close(View view){
        finish();
    }
}
