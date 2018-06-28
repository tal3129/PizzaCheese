package com.cheeze.pizza.pizzacheeze;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import com.cheeze.pizza.pizzacheeze.cardListsAdapters.FreeProductsBannerAdapter;

public class FreeProductsBanner extends AppCompatActivity {

    ViewPager mViewPager;
    Context context;

    int width;
    int height;

    ImageView scrollRightArrow;
    ImageView scrollLeftArrow;

    int popupWidth;
    int popupHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_products_banner);
        serSizes();
        setPopupSizes();

        scrollRightArrow = findViewById(R.id.scrollRightArrow);
        scrollLeftArrow = findViewById(R.id.scrollLeftArrow);


        mViewPager = (ViewPager) findViewById(R.id.pagers);
        FreeProductsBannerAdapter mAdapter = new FreeProductsBannerAdapter(this, SplashActivity.order.getFreeProducts());
        mViewPager.setAdapter(mAdapter);

        if (SplashActivity.order.getFreeProducts().size() == 1){
            scrollLeftArrow.setVisibility(View.INVISIBLE);
            scrollRightArrow.setVisibility(View.INVISIBLE);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0 ) {
                    scrollLeftArrow.setAlpha(0.4f);
                    scrollLeftArrow.setClickable(false);
                    scrollRightArrow.setAlpha(1f);
                    scrollRightArrow.setClickable(true);
                }
                else if (position ==  SplashActivity.order.getFreeProducts().size()-1) {
                    scrollRightArrow.setAlpha(0.4f);
                    scrollRightArrow.setClickable(false);
                    scrollLeftArrow.setAlpha(1f);
                    scrollLeftArrow.setClickable(true);
                }
                else {
                    scrollLeftArrow.setAlpha(1f);
                    scrollRightArrow.setAlpha(1f);
                    scrollLeftArrow.setClickable(true);
                    scrollRightArrow.setClickable(true);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        scrollLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1,true);
            }
        });
        scrollRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1,true);
            }
        });


    }

    private void serSizes(){
        //get screen size
        final Display display = this.getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        //finish
    }

    private void setPopupSizes(){
        (findViewById(R.id.freeProductDialog)).getLayoutParams().height = height/2;

    }
}
