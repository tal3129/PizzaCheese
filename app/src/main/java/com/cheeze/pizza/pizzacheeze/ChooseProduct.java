package com.cheeze.pizza.pizzacheeze;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import com.cheeze.pizza.pizzacheeze.CustomViews.MyCardView;
import com.cheeze.pizza.pizzacheeze.types.*;
import com.lb.auto_fit_textview.AutoResizeTextView;

import java.util.ArrayList;

public class ChooseProduct extends AppCompatActivity implements View.OnClickListener {
    public static final int RSC_CLOSE = 2;
    public static final int RSC_SHOW_DIALOG = 64;
    private boolean productAdded;

    AddProductDialog addProductDialog;

    RelativeLayout mainLayout;
    RelativeLayout.LayoutParams mainLayoutParams;

    RelativeLayout actionBarLayout;
    RelativeLayout.LayoutParams actionBarLayoutParams;

    AutoResizeTextView tvActionbar;
    RelativeLayout.LayoutParams tvActionbarParams;

    ImageView ivBackButton;
    RelativeLayout.LayoutParams ivBackButtonParams;

    ScrollView scrollView;
    RelativeLayout.LayoutParams scrollViewParams;

    RelativeLayout cardListLayout;
    RelativeLayout.LayoutParams cardListLayoutParams;

    ImageView ivLogo;
    RelativeLayout.LayoutParams ivLogoParams;


    Typeface face;

    ArrayList<MyCardView> myCardViews;
    String textTitle;
    int currentImageDrawable;
    int cartImage;

    int width;
    int height;
    int actionbar_height;
    int text_size;
    int text_height;
    int text_width;
    int back_button_size;
    int back_button_margin_right;
    int text_margin_right;
    int actionbar_layout_margin_top;
    int logo_size;
    int logo_margin_left;

    int card_width;
    int card_height;
    int card_margin_top;
    Product oldP;


    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productAdded = false;
        //hide action bar
        this.getSupportActionBar().hide(); // hide the action bar this.getSupportActionBar().hide(); // hide the action ba
        //finish


        //get screen size
        final Display display = this.getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        //finish

        mainLayout = new RelativeLayout(this);
        mainLayout.setId(View.generateViewId());
        mainLayout.setBackgroundColor(Color.parseColor("#e4e5e6"));
        mainLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);





        setSize();
        changeStatusBarColor();

        buildActionBar();
        buildCenterLayout();
        setMyCardList();
        //startAnim();


        setContentView(mainLayout);
    }

    public void setSize() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        textTitle = bundle.getString("titleName");
        currentImageDrawable = bundle.getInt("image");

        face = Typeface.createFromAsset(getAssets(), "fonts/VarelaRound-Regular.ttf");

        actionbar_height = height / 4;
        text_size = convertSpToPixels(100, this);
        text_margin_right = width / 30;

        back_button_size = (int) (actionbar_height / 6.5);
        back_button_margin_right = text_margin_right;

        actionbar_layout_margin_top = height / 40;

        logo_size = actionbar_height / 4;
        logo_margin_left = logo_size / 7;

        card_width = width;
        card_height = height / 8;
        card_margin_top = card_height / 15;



        text_height = actionbar_height / 3;
        text_width = width / 2;

        addProductDialog = new AddProductDialog(this, width, height);
    }

    public void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color);
        }
    }

    public void buildActionBar() {
        //set the action bar
        actionBarLayout = new RelativeLayout(this);
        actionBarLayout.setId(View.generateViewId());
        actionBarLayout.setBackgroundResource(R.drawable.choose_order_background);

        actionBarLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, actionbar_height);
        actionBarLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        //finish

        //set the back button
        ivBackButton = new ImageView(this);
        ivBackButton.setOnClickListener(view -> finish());
        ivBackButton.setId(View.generateViewId());
        ivBackButton.setImageResource(R.drawable.ic_arrow_back_white_48dp);


        ivBackButtonParams = new RelativeLayout.LayoutParams(back_button_size, back_button_size);
        ivBackButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ivBackButtonParams.setMargins(0, 0, back_button_margin_right, 0);
        //finsih


        //set text view
        tvActionbar = new AutoResizeTextView(this);
        tvActionbar.setId(View.generateViewId());
        tvActionbar.setText(textTitle);
        tvActionbar.setTextColor(Color.WHITE);
        tvActionbar.setTextSize(text_size);
        tvActionbar.setGravity(Gravity.CENTER);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/VarelaRound-Regular.ttf");
        tvActionbar.setTypeface(face);

        tvActionbarParams = new RelativeLayout.LayoutParams(text_width, text_height);
        tvActionbarParams.addRule(RelativeLayout.CENTER_VERTICAL);
        tvActionbarParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //finish


        //set the logo
        ivLogo = new ImageView(this);
        ivLogo.setImageResource(R.drawable.home_screen_logo_transporment);

        ivLogoParams = new RelativeLayout.LayoutParams(logo_size, logo_size);
        ivLogoParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        ivLogoParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        ivLogoParams.setMargins(logo_margin_left, 0, 0, logo_margin_left);
        //finish

        actionBarLayout.addView(tvActionbar, tvActionbarParams);
        actionBarLayout.addView(ivBackButton, ivBackButtonParams);
        actionBarLayout.addView(ivLogo, ivLogoParams);

        mainLayout.addView(actionBarLayout, actionBarLayoutParams);
    }

    public void buildCenterLayout() {
        //set the card list layout
        cardListLayout = new RelativeLayout(this);
        cardListLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //finish

        //set scrollview
        scrollView = new ScrollView(this);
        scrollView.setBackgroundColor(Color.parseColor("#f5f6fb"));
        scrollViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scrollViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        scrollViewParams.addRule(RelativeLayout.BELOW, actionBarLayout.getId());

        //finish

        scrollView.addView(cardListLayout, cardListLayoutParams);
        mainLayout.addView(scrollView, scrollViewParams);
    }



    public void setMyCardList() {
        myCardViews = new ArrayList<>();

        for (Product product : SplashActivity.productArrayList) {
            if (product.getType().equals(textTitle)) {
                myCardViews.add(new MyCardView(this, card_width, card_height, Color.parseColor("#ffffff"), product.getName(), product));
                myCardViews.get(myCardViews.size() - 1).setOnClickListener(this);
            }
        }


        RelativeLayout.LayoutParams[] myCardViewsParams = new RelativeLayout.LayoutParams[myCardViews.size()];

        for (int i = 0; i < myCardViews.size(); i++) {
            myCardViews.get(i).setId(View.generateViewId());
            myCardViewsParams[i] = new RelativeLayout.LayoutParams(card_width, card_height);
            myCardViewsParams[i].addRule(RelativeLayout.CENTER_HORIZONTAL);
            if (i > 0) {
                myCardViewsParams[i].addRule(RelativeLayout.BELOW, myCardViews.get(i - 1).getId());
                myCardViewsParams[i].setMargins(0, card_margin_top, 0, 0);
            }
            cardListLayout.addView(myCardViews.get(i), myCardViewsParams[i]);
        }

    }

    public static int convertSpToPixels(float sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

    public int converDPtoPX(int dp) {
        return dp * (int) this.getResources().getDisplayMetrics().density;
    }

    public static int getTextHeight(Context context, String text, int textSize, int deviceWidth, Typeface typeface, int padding) {
        TextView textView = new TextView(context);
        textView.setPadding(padding, 0, padding, padding);
        textView.setTypeface(typeface);
        textView.setText(text, TextView.BufferType.SPANNABLE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }


    public void onClick(View v) {
        Intent intent = new Intent(this, ShoppingCart.class);

        if (v instanceof MyCardView) {//If a card was clicked-----
            oldP = (((MyCardView) v).getProduct());

            if (!oldP.isAvailable()) {//if the product isn't available
                Toast.makeText(v.getContext(), "המוצר אינו זמין במלאי", Toast.LENGTH_LONG).show();
            } else {
                if (oldP instanceof Pizza) { //if a pizza card was clicked, start EditPizza (and set it's pizza)
                    intent = new Intent(this, EditPizza.class);
                    EditPizza.thisPizza = new Pizza(oldP);
                    EditPizza.thisPizza=(Pizza)discountIfAvailable(EditPizza.thisPizza);

                    startActivityForResult(intent, ChooseType.RC_PIZZA);
                } else if (oldP instanceof ToppingProduct) {//if a Topping Product card was clicked
                    ToppingProduct toppingProduct = new ToppingProduct(oldP);
                    AddProductDialog.product = toppingProduct;
                    intent = new Intent(this, SetProductToppingActivity.class);
                    SetProductToppingActivity.myProd = toppingProduct;
                    SetProductToppingActivity.myProd=(ToppingProduct) discountIfAvailable(toppingProduct);
                    startActivityForResult(intent, ChooseType.RC_PIZZA);

                } else if (oldP instanceof Pasta) {//if a Pasta card was clicked
                    AlertDialog.Builder ad = new AlertDialog.Builder(this)
                            .setPositiveButton("הוסף", (dialogInterface, i) -> {
                                Product product = new Pasta(oldP);
                                product = discountIfAvailable(product);
                                ((Pasta) product).setExtraCheese(true);
                                SplashActivity.order.getCart().addProduct(product);
                                AddProductDialog.product = product;
                                addProductDialog.show();
                                addProductDialog.getbContoniue().setOnClickListener(ChooseProduct.this);
                                addProductDialog.getbFinishOrder().setOnClickListener(ChooseProduct.this);
                            }).setNegativeButton("לא תודה", (dialogInterface, i) -> {
                                Product product = new Pasta(oldP);
                                product = discountIfAvailable(product);
                                SplashActivity.order.getCart().addProduct(product);
                                AddProductDialog.product = product;
                                addProductDialog.show();
                                addProductDialog.getbContoniue().setOnClickListener(ChooseProduct.this);
                                addProductDialog.getbFinishOrder().setOnClickListener(ChooseProduct.this);
                            })
                            .setMessage("תוספת הקרמה ב-3₪?");

                    ad.show();

                } else {//if a regular card was clicked
                    Product p=new Product(oldP);

                    SplashActivity.order.getCart().addProduct(discountIfAvailable(p));
                    AddProductDialog.product = p;
                    addProductDialog.show();
                    addProductDialog.getbContoniue().setOnClickListener(this);
                    addProductDialog.getbFinishOrder().setOnClickListener(this);
                }
            }
        } else if (v.getId() == AddProductDialog.button_finish_order_id) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            loadingDialog.setCancelable(false);
            loadingDialog.show();
            addProductDialog.dismiss();

            startActivityForResult(intent, ChooseType.RQC_SHOPPINGCART);

        } else if (v.getId() == AddProductDialog.button_contoniue_id) {
            addProductDialog.dismiss();
            backToMenu();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (loadingDialog != null)
            loadingDialog.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RSC_CLOSE) {
            finish();
            overridePendingTransition(0, 0);
        } else if (resultCode == RSC_SHOW_DIALOG) {
            addProductDialog.show();
            addProductDialog.getbContoniue().setOnClickListener(this);
            addProductDialog.getbFinishOrder().setOnClickListener(this);
            productAdded = true;
        }
    }

    private void backToMenu() {
        Intent i = new Intent(this, ChooseType.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    //go through the discountList and discounts the product
    private  Product discountIfAvailable(Product p) {
        int count;
        for (Discount discount:SplashActivity.discountArrayList){
            count=0;
            if(discount.isActive()&&!discount.getId().equals("toppingDiscount")&&(discount.getDiscounted().getName()).equals(p.getName())){
                for (Product prod:SplashActivity.order.getCart().getSelectedProducts()){
                    if(prod.getName().equals(discount.getDiscounter().getName()))
                        count+=prod.getCount();}
                if(count>=discount.getDiscounterAmount()) {
                    p.setPrice(discount.getNewPrice());
                    p.setOnDiscount(true);
                    discount.setActive(false);
                }
            }
        }
        return p;
    }


}