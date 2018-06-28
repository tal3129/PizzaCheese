package com.cheeze.pizza.pizzacheeze;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cheeze.pizza.pizzacheeze.cardListsAdapters.ChooseOrderAdapter;
import com.lb.auto_fit_textview.AutoResizeTextView;

public class ChooseType extends AppCompatActivity implements View.OnClickListener {
    public static final int RQC_SHOPPINGCART = 1;
    public static final int RC_PIZZA = 2437;


    RecyclerView recyclerView;
    RelativeLayout.LayoutParams recyclerViewParams;

    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    RelativeLayout mainLayout;
    RelativeLayout.LayoutParams mainLayoutParams;

    RelativeLayout actionBarLayout;
    RelativeLayout.LayoutParams actionBarLayoutParams;

    AutoResizeTextView tvActionBar;
    RelativeLayout.LayoutParams tvActionBarParams;

    ImageView ivBackActionBarBtton;
    RelativeLayout.LayoutParams ivBackActionBarBttonParams;

    ImageView ivLogo;
    RelativeLayout.LayoutParams ivLogoParams;

    FloatingActionButton fabCart;
    RelativeLayout.LayoutParams fabCartParams;


    int width;
    int height;
    int action_bar_height;
    int actionbar_back_arrow;
    int back_arrow_margin_right;
    int text_size;
    int text_height;
    int text_width;
    int logo_size;
    int logo_margin_left;
    int fab_cart_size;
    int fab_cart_margin;
    public static final int RS_CODE_FINISH=4;

    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide(); // hide the action bar this.getSupportActionBar().hide(); // hide the action ba
        //get screen size
        final Display display = this.getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        //finish

        //set main layout
        mainLayout = new RelativeLayout(this);
        mainLayout.setBackgroundColor(Color.parseColor("#e4e5e6"));
        mainLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //finish

        setSize();
        buildActionBar();
        buildCardView();
        buildCart();

        setContentView(mainLayout, mainLayoutParams);

    }


    public void setSize() {
        action_bar_height = height / 4;
        actionbar_back_arrow = (int) (action_bar_height / 6.5);
        back_arrow_margin_right = width / 30;
        text_size = convertSpToPixels(100, this);
        logo_size = action_bar_height / 4;
        logo_margin_left = logo_size / 7;
        fab_cart_size = converDPtoPX(54);
        fab_cart_margin = fab_cart_size / 2;

        text_height = action_bar_height/3;
        text_width = width/2;
    }

    public void buildActionBar() {
        //set the action bar
        actionBarLayout = new RelativeLayout(this);
        actionBarLayout.setId(View.generateViewId());
        actionBarLayout.setBackgroundResource(R.drawable.choose_order_background);

        actionBarLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, action_bar_height);
        actionBarLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        //finish

        //set the back button
        ivBackActionBarBtton = new ImageView(this);
        ivBackActionBarBtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMain();
            }
        });
        ivBackActionBarBtton.setId(View.generateViewId());
        ivBackActionBarBtton.setImageResource(R.drawable.ic_arrow_back_white_48dp);


        ivBackActionBarBttonParams = new RelativeLayout.LayoutParams(actionbar_back_arrow, actionbar_back_arrow);
        ivBackActionBarBttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ivBackActionBarBttonParams.setMargins(0, 0, back_arrow_margin_right, 0);
        //finsih


        //set text view
        tvActionBar = new AutoResizeTextView(this);
        tvActionBar.setId(View.generateViewId());
        tvActionBar.setText("תפריט");
        tvActionBar.setTextColor(Color.WHITE);
        tvActionBar.setTextSize(text_size);
        tvActionBar.setGravity(Gravity.CENTER);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/VarelaRound-Regular.ttf");
        tvActionBar.setTypeface(face);

        tvActionBarParams = new RelativeLayout.LayoutParams(text_width, text_height);
        tvActionBarParams.addRule(RelativeLayout.CENTER_VERTICAL);
        tvActionBarParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //finish


        //set the logo
        ivLogo = new ImageView(this);
        ivLogo.setImageResource(R.drawable.home_screen_logo_transporment);

        ivLogoParams = new RelativeLayout.LayoutParams(logo_size, logo_size);
        ivLogoParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ivLogoParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        ivLogoParams.setMargins(logo_margin_left, 0, 0, logo_margin_left);
        //finish

        actionBarLayout.addView(ivLogo, ivLogoParams);
        actionBarLayout.addView(tvActionBar, tvActionBarParams);
        actionBarLayout.addView(ivBackActionBarBtton, ivBackActionBarBttonParams);
        mainLayout.addView(actionBarLayout, actionBarLayoutParams);
    }

    public void buildCardView() {
        //set card view
        layoutManager = new LinearLayoutManager(this);

        recyclerView = new RecyclerView(this);
        recyclerView.setId(View.generateViewId());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ChooseOrderAdapter(width, height, recyclerView);

        recyclerView.setAdapter(adapter);
        recyclerViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        recyclerViewParams.addRule(RelativeLayout.BELOW, actionBarLayout.getId());
        //finish

        mainLayout.addView(recyclerView, recyclerViewParams);
    }

    public void buildCart() {
        fabCart = new FloatingActionButton(this);
        fabCart.setImageResource(R.drawable.cart_icon_with_stroke_red);
        fabCart.setSize(FloatingActionButton.SIZE_AUTO);
        fabCart.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f2f3f8")));
        fabCart.setOnClickListener(this);


        fabCartParams = new RelativeLayout.LayoutParams(fab_cart_size, fab_cart_size);
        fabCartParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        fabCartParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        fabCartParams.setMargins(fab_cart_margin, 0, 0, fab_cart_margin);

        mainLayout.addView(fabCart, fabCartParams);
    }

    public static int convertSpToPixels(float sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

    public int converDPtoPX(int dp) {
        return dp * (int) this.getResources().getDisplayMetrics().density;
    }


    @Override
    public void onClick(View view) {

        loadingDialog = new LoadingDialog(view.getContext());
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadingDialog.setCancelable(false);
        loadingDialog.show();


        Intent intent = new Intent(this, ShoppingCart.class);
        startActivityForResult(intent, RQC_SHOPPINGCART);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (loadingDialog!=null)
            loadingDialog.cancel();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RQC_SHOPPINGCART&&resultCode==RS_CODE_FINISH)
            finish();
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(this,MainActivity.class);
        startActivity(i);
        super.onBackPressed();
    }

    private void toMain(){
        Intent i=new Intent(this,MainActivity.class);
        finish();
        startActivity(i);
    }


}
