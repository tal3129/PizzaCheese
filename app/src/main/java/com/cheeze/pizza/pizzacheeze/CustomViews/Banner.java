package com.cheeze.pizza.pizzacheeze.CustomViews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cheeze.pizza.pizzacheeze.R;
import com.cheeze.pizza.pizzacheeze.SplashActivity;
import com.cheeze.pizza.pizzacheeze.types.Product;


public class Banner extends Dialog {
    private Context context;
    private Product discounted;
    private TextView tvTitle;
    private TextView tvDescription;
    private ImageView ivProd;


    public Banner(@NonNull Context context, final Product discounted) {
        super(context);
        this.setCancelable(false);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/VarelaRound-Regular.ttf");
        this.context = context;
        this.discounted = discounted;

        Point size = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;

        setContentView(R.layout.banner_layout);
        RelativeLayout mainLayout = this.findViewById(R.id.mainBannerLayout);
        mainLayout.getLayoutParams().width = (int) (width / 1.5);
        mainLayout.getLayoutParams().height = (int) (height / 2.3);

        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setTypeface(face);
        switch (discounted.getType()) {
            case "שתייה":
                tvTitle.setText("משהו לשתות?");
                break;
            case "סלטים":
                tvTitle.setText("תוספת בריאה");
                break;
            case "מאפים":
            case "פסטות":
                tvTitle.setText("משהו בצד?");
                break;
            case "מנות אחרונות":
                tvTitle.setText("קינוח מתוק");
                break;
        }
        tvDescription = findViewById(R.id.tvDescription);
        tvDescription.setText(discounted.getName() + " ב " + String.format("%.2f", discounted.getPrice()) + " ₪  בלבד");
        ivProd = findViewById(R.id.ivProd);
        ivProd.getLayoutParams().width = (int) (width / 3.5);
        ivProd.getLayoutParams().height = height / 10;

        discounted.loadPicToIv(ivProd);

        findViewById(R.id.btnAdd).setOnClickListener(v -> {
            SplashActivity.order.getCart().addProduct(discounted);
            dismiss();
        });

        findViewById(R.id.btnDismiss).setOnClickListener(v -> dismiss());
        this.getWindow().getAttributes().windowAnimations = R.style.slideRightAnimations;
    }

    public Banner(Context context) {
        super(context);
        this.setCancelable(false);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/VarelaRound-Regular.ttf");
        this.context = context;
        Point size = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;
        setContentView(R.layout.banner_layout);
        RelativeLayout mainLayout = this.findViewById(R.id.mainBannerLayout);
        mainLayout.getLayoutParams().width = (int) (width / 1.5);
        mainLayout.getLayoutParams().height = (int) (height / 2.3);

        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        ivProd = findViewById(R.id.ivProd);
        tvTitle.setTypeface(face);
        findViewById(R.id.btnAdd).setOnClickListener(v -> {
            SplashActivity.order.getCart().addProduct(new Product(discounted));
            dismiss();
        });

        findViewById(R.id.btnDismiss).setOnClickListener(v -> dismiss());
        this.getWindow().getAttributes().windowAnimations = R.style.slideRightAnimations;
    }


    public void setToppingDiscount() {

        tvTitle.setText("מבצע!");
        tvDescription.setText("1+1 על כל התוספות");
        ivProd.setImageResource(R.drawable.one_plus_one);

        findViewById(R.id.btnAdd).setVisibility(View.GONE);
        findViewById(R.id.btnDismiss).setVisibility(View.GONE);
        Button banToppingDismiss = findViewById(R.id.btnBannerFinish);
        banToppingDismiss.setVisibility(View.VISIBLE);
        banToppingDismiss.setOnClickListener(v -> dismiss());
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public void setTvTitle(TextView tvTitle) {
        this.tvTitle = tvTitle;
    }

    public TextView getTvDescription() {
        return tvDescription;
    }

    public void setTvDescription(TextView tvDescription) {
        this.tvDescription = tvDescription;
    }

    public ImageView getIvProd() {
        return ivProd;
    }

    public void setIvProd(ImageView ivProd) {
        this.ivProd = ivProd;
    }

    //set's the banner's image to the passed product's one
    public void setDiscounted(Product discounted) {
        this.discounted = discounted;
        discounted.loadPicToIv(ivProd);
    }
}