package com.cheeze.pizza.pizzacheeze;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.cheeze.pizza.pizzacheeze.CustomViews.Banner;
import com.cheeze.pizza.pizzacheeze.CustomViews.MyNumberPicker;
import com.cheeze.pizza.pizzacheeze.CustomViews.ToppingCard;
import com.cheeze.pizza.pizzacheeze.types.Pizza;
import com.cheeze.pizza.pizzacheeze.types.Product;
import com.cheeze.pizza.pizzacheeze.types.Topping;
import com.github.jivimberg.library.AutoResizeTextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.cheeze.pizza.pizzacheeze.ChooseProduct.RSC_SHOW_DIALOG;

public class EditPizza extends AppCompatActivity {

    RelativeLayout mainLayout;
    RelativeLayout.LayoutParams mainLayoutParams;

    RelativeLayout pizzaLayout;
    RelativeLayout.LayoutParams pizzaLayoutParams;

    RelativeLayout[] pizzaQuaterLayout;
    RelativeLayout.LayoutParams[] pizzaQuaterLayoutParams;
    ImageView[] ivPizzaQuater;
    RelativeLayout.LayoutParams[] ivPizzaQuaterParams;

    AutoResizeTextView tvName;
    RelativeLayout.LayoutParams tvNameParams;

    RelativeLayout priceLayout;
    RelativeLayout.LayoutParams priceLayoutParams;

    TextView tvPrice;
    RelativeLayout.LayoutParams tvPriceParams;

    ImageView ivAddToCart;
    RelativeLayout.LayoutParams ivAddToCartParams;

    AutoResizeTextView tvChooseToppings;
    RelativeLayout.LayoutParams tvChooseToppingsParams;

    ScrollView scrollLayout;
    RelativeLayout.LayoutParams scrollLayoutParams;

    RelativeLayout toppingsListLayout;

    static ArrayList<ToppingCard> toppingCardList;
    ArrayList<RelativeLayout.LayoutParams> toppingsListParams;

    MyNumberPicker myNumberPicker;
    RelativeLayout.LayoutParams myNumberPickerParams;

    int width;
    int height;
    int quaterSize;
    int pizza_margin_top;
    int price_width;
    int price_height;
    int addToCart_size;
    int addToCart_margin_right;
    int pizza_text_size;
    int pizza_text_margin_top;
    int price_text_size;
    int tvchoosetopping_margin_top;
    int tvchoosetopping_margin_right;
    int tv_choostopping_text_size;
    int card_height;
    int card_margin_top;
    int number_picker_height;
    int number_picker_width;
    private ImageView[][] toppingIv;
    public static boolean freeTopUsed=!SplashActivity.oneFreeTop;

    int tvName_width;
    int tvName_height;
    int tvChoose_width;
    int tvChoose_height;

    String pizzaName;
    String priceString;
    double price;
    public static Pizza thisPizza = null;
    boolean edit, added;
    private RelativeLayout.LayoutParams matchParentParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide action bar
        this.getSupportActionBar().hide(); // hide the action bar this.getSupportActionBar().hide(); // hide the action ba
        //finish

        pizzaQuaterLayout = new RelativeLayout[4];
        pizzaQuaterLayoutParams = new RelativeLayout.LayoutParams[4];
        ivPizzaQuater = new ImageView[4];
        ivPizzaQuaterParams = new RelativeLayout.LayoutParams[4];

        added = false;
        matchParentParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mainLayout = new RelativeLayout(this);
        mainLayout.setBackgroundColor(Color.parseColor("#f0f0f7"));
        mainLayout.setFocusableInTouchMode(true);
        mainLayout.setFocusable(true);
        mainLayoutParams = matchParentParams;

        edit = getIntent().getBooleanExtra("Edit", false);

        thisPizza.setPrice(thisPizza.getOriginalPrice());

        if (SplashActivity.onePlusActive) {
            Banner banner = new Banner(this);
            banner.setToppingDiscount();
            banner.show();
        }

        setSizes();
        buildPizza();
        buildNameAndPrice();
        buildAddTopping();
        setToppingList();
        toppingIv = new ImageView[toppingCardList.size()][4];

        if (edit) {
            for (int i = 0; i < thisPizza.getToppingArrayList().size(); i++) { //goes through the thisPizza's toppingList
                Topping t = thisPizza.getToppingArrayList().get(i);
                for (ToppingCard toppingCard : toppingCardList) { //goes through the toppingCardList
                    if (toppingCard.getTopping().getName().equals(t.getName())) {  //if this is the chosen topping,
                        toppingCard.getTopping().setOnQuarters((t.getOnQuarters()));  //set the toppingCard's topping (without boolean array data) to the chosen one with it
                        onFinishEditTopping(toppingCardList.indexOf(toppingCard)); //and update the pizza image with the topping
                        toppingCard.onChange();
                    }
                }
            }
        }


        setContentView(mainLayout, mainLayoutParams);
    }


    public void setSizes() {
        EditToppings.editPizza = EditPizza.this;
        //get screen size
        final Display display = this.getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        //finish

        quaterSize = width / 3;
        pizza_margin_top = quaterSize / 10;
        price_width = (int) (1.2 * quaterSize);
        price_height = price_width / 3;
        pizza_text_size = convertSpToPixels(12, this);
        pizza_text_margin_top = pizza_margin_top;
        price_text_size = convertSpToPixels(10, this);

        DecimalFormat df = new DecimalFormat("#.00");
        pizzaName = thisPizza.getName();
        price = thisPizza.getTotalPrice();
        priceString = df.format(price);

        addToCart_size = (int) (price_height*1.7);
        addToCart_margin_right = addToCart_size / 2;

        tvchoosetopping_margin_top = pizza_margin_top;
        tvchoosetopping_margin_right = tvchoosetopping_margin_top;
        tv_choostopping_text_size = convertSpToPixels(10, this);

        card_height = height / 8;
        card_margin_top = card_height / 15;

        number_picker_height = (int) (price_height * 0.7);
        number_picker_width = price_width;

        tvName_width = (int) (quaterSize * 1.5);
        tvName_height = (int) (0.5 * price_height);
        tvChoose_width = price_width;
        tvChoose_height = (int) (0.5 * price_height);
    }

    @SuppressLint("NewApi")
    public void buildPizza() {
        pizzaLayout = new RelativeLayout(this);
        pizzaLayout.setId(View.generateViewId());
        pizzaLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pizzaLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        pizzaLayoutParams.setMargins(0, pizza_margin_top, 0, 0);

        for (int i = 0; i < 4; i++) {
            pizzaQuaterLayout[i] = new RelativeLayout(this);
            pizzaQuaterLayout[i].setId(View.generateViewId());
            pizzaQuaterLayoutParams[i] = new RelativeLayout.LayoutParams(quaterSize, quaterSize);
            ivPizzaQuater[i] = new ImageView(this);
            ivPizzaQuaterParams[i] = new RelativeLayout.LayoutParams(matchParentParams);
        }

        pizzaQuaterLayoutParams[0].addRule(RelativeLayout.RIGHT_OF, pizzaQuaterLayout[1].getId());
        pizzaQuaterLayoutParams[2].addRule(RelativeLayout.BELOW, pizzaQuaterLayout[1].getId());
        pizzaQuaterLayoutParams[3].addRule(RelativeLayout.BELOW, pizzaQuaterLayout[1].getId());
        pizzaQuaterLayoutParams[2].addRule(RelativeLayout.RIGHT_OF, pizzaQuaterLayout[3].getId());

        final int size = 150;
        ivPizzaQuater[0].setImageBitmap(SplashActivity.decodeSampledBitmapFromResource(getResources(), R.drawable.quater1, size, size));
        ivPizzaQuater[1].setImageBitmap(SplashActivity.decodeSampledBitmapFromResource(getResources(), R.drawable.quater2, size, size));
        ivPizzaQuater[2].setImageBitmap(SplashActivity.decodeSampledBitmapFromResource(getResources(), R.drawable.quater3, size, size));
        ivPizzaQuater[3].setImageBitmap(SplashActivity.decodeSampledBitmapFromResource(getResources(), R.drawable.quater4, size, size));
        for (int i = 0; i < 4; i++) {
            pizzaQuaterLayout[i].addView(ivPizzaQuater[i]);
            pizzaLayout.addView(pizzaQuaterLayout[i], pizzaQuaterLayoutParams[i]);
        }

        mainLayout.addView(pizzaLayout, pizzaLayoutParams);
    }

    public void buildNameAndPrice() {
        tvName = new AutoResizeTextView(this);
        tvName.setId(View.generateViewId());
        tvName.setText(pizzaName);
        tvName.setTextColor(Color.parseColor("#c0232c"));
        tvName.setTextSize(10000);
        tvName.setGravity(Gravity.CENTER);
        tvNameParams = new RelativeLayout.LayoutParams(tvName_width, tvName_height);
        tvNameParams.addRule(RelativeLayout.BELOW, pizzaLayout.getId());
        tvNameParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tvNameParams.setMargins(0, pizza_text_margin_top, 0, 0);

        priceLayout = new RelativeLayout(this);
        priceLayout.setAlpha(0.85f);
        priceLayout.setId(View.generateViewId());
        priceLayout.setBackgroundResource(R.drawable.rounded_corners);
        priceLayoutParams = new RelativeLayout.LayoutParams(price_width, price_height);
        priceLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        priceLayoutParams.addRule(RelativeLayout.BELOW, tvName.getId());
        priceLayoutParams.setMargins(0, pizza_text_margin_top, 0, 0);

        tvPrice = new TextView(this);
        tvPrice.setText(priceString);
        tvPrice.setTextColor(Color.WHITE);
        tvPrice.setTextSize(price_text_size);
        tvPrice.setId(View.generateViewId());
        tvPriceParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvPriceParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tvPriceParams.addRule(RelativeLayout.CENTER_VERTICAL);

        correctWidth(tvPrice, price_width);


        ivAddToCart = new ImageView(this);
        ivAddToCart.setImageBitmap(SplashActivity.decodeSampledBitmapFromResource(getResources(), R.drawable.add_pizza_icon, 100, 100));
        ivAddToCartParams = new RelativeLayout.LayoutParams(addToCart_size, addToCart_size);
        ivAddToCartParams.addRule(RelativeLayout.ALIGN_BOTTOM, priceLayout.getId());
        ivAddToCartParams.addRule(RelativeLayout.LEFT_OF, priceLayout.getId());

        ivAddToCartParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        ivAddToCartParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        //ivAddToCartParams.setMargins(0, 0, addToCart_margin_right, 0);
        ivAddToCart.setOnClickListener(view -> {
            LoadingDialog loadingDialog = new LoadingDialog(view.getContext());
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loadingDialog.setCancelable(false);
            loadingDialog.show();

            Product newPizza = new Pizza(thisPizza);
            AddProductDialog.product = newPizza;
            SplashActivity.order.getCart().addProduct(newPizza);
            added = true;
            if (freeTopUsed)
                SplashActivity.oneFreeTop = false;
            thisPizza = null;
            setResult(RSC_SHOW_DIALOG);
            finish();
        });

        myNumberPicker = new MyNumberPicker(this, number_picker_height, thisPizza, tvPrice);
        myNumberPickerParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, number_picker_height);
        myNumberPickerParams.addRule(RelativeLayout.RIGHT_OF, priceLayout.getId());
        myNumberPickerParams.addRule(RelativeLayout.ALIGN_BOTTOM, priceLayout.getId());
        myNumberPickerParams.setMargins(number_picker_width / 10, 0, number_picker_width / 10, 0);

        priceLayout.addView(tvPrice, tvPriceParams);
        mainLayout.addView(tvName, tvNameParams);
        mainLayout.addView(priceLayout, priceLayoutParams);
        mainLayout.addView(ivAddToCart, ivAddToCartParams);
        mainLayout.addView(myNumberPicker, myNumberPickerParams);
    }

    public void buildAddTopping() {
        tvChooseToppings = new AutoResizeTextView(this);
        tvChooseToppings.setId(View.generateViewId());
        tvChooseToppings.setText("בחר תוספות:");
        tvChooseToppings.setTextSize(1000);
        tvChooseToppings.setTextColor(Color.parseColor("#c0232c"));
        tvChooseToppingsParams = new RelativeLayout.LayoutParams(tvChoose_width, tvChoose_height);
        tvChooseToppingsParams.addRule(RelativeLayout.BELOW, priceLayout.getId());
        tvChooseToppingsParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvChooseToppingsParams.setMargins(0, tvchoosetopping_margin_top, tvchoosetopping_margin_right, 0);

        scrollLayout = new ScrollView(this);
        scrollLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        scrollLayoutParams.addRule(RelativeLayout.BELOW, tvChooseToppings.getId());

        toppingsListLayout = new RelativeLayout(this);

        scrollLayout.addView(toppingsListLayout, matchParentParams);

        mainLayout.addView(tvChooseToppings, tvChooseToppingsParams);
        mainLayout.addView(scrollLayout, scrollLayoutParams);
    }

    public void setToppingList() {
        toppingCardList = new ArrayList<>();
        for (Product product : SplashActivity.productArrayList)
            if (product.getType().equals("תוספות"))
                toppingCardList.add(new ToppingCard(this, height / 10, width, new Topping(product.getName(), product.getPicUri().toString())));


        toppingsListParams = new ArrayList<>();


        for (int i = 0; i < toppingCardList.size(); i++) {
            toppingCardList.get(i).setId(View.generateViewId());
            toppingsListParams.add(new RelativeLayout.LayoutParams(width, card_height));
            toppingsListParams.get(i).addRule(RelativeLayout.CENTER_HORIZONTAL);
            if (i > 0) {
                toppingsListParams.get(i).addRule(RelativeLayout.BELOW, toppingCardList.get(i - 1).getId());
                toppingsListParams.get(i).setMargins(0, card_margin_top, 0, 0);
            }
            toppingsListLayout.addView(toppingCardList.get(i), toppingsListParams.get(i));
        }
    }


    private ImageView newToppingImage(Topping topping, int index) {
        ImageView imageView = new ImageView(this);
        topping.loadRotatedPic(imageView, index);
        return imageView;
    }

    public void onFinishEditTopping(int index) {
        Topping thisTopping = toppingCardList.get(index).getTopping();
        for (int i = 0; i < 4; i++) {
            if (thisTopping.getQuarterAt(i)) {
                //if the topping was already crated- don't crate a new one
                if (toppingIv[index][i] == null)
                    toppingIv[index][i] = newToppingImage(thisTopping, i);
                //if the topping is already shown- don't re-add it
                if (toppingIv[index][i].getParent() == null)
                    pizzaQuaterLayout[i].addView(toppingIv[index][i], matchParentParams);

            } else if (toppingIv[index][i] != null)
                pizzaQuaterLayout[i].removeView(toppingIv[index][i]);
        }

        Topping top = null;
        for (Topping t : thisPizza.getToppingArrayList())//checks a topping with the same name has been added
            if (t.getName().equals(thisTopping.getName()))
                top = t; //a topping in the pizza's list with the same name as the added one
        if (top != null && thisTopping.getQuarterCount() == 0)  //if he is removing an existing topping, remove it
            thisPizza.getToppingArrayList().remove(top);
        else if (thisTopping.getQuarterCount() > 0 && top == null)
            thisPizza.addTopping(thisTopping);
        thisPizza.updatePrice();
        DecimalFormat df = new DecimalFormat("#.00");
        tvPrice.setText("₪" + df.format(thisPizza.getTotalPrice()));
    }

    public static void changeTopping() {
        for (int i = 0; i < toppingCardList.size(); i++) {
            toppingCardList.get(i).onChange();
        }
    }

    public static int convertSpToPixels(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public void correctWidth(TextView textView, int desiredWidth) {
        Paint paint = new Paint();
        Rect bounds = new Rect();

        paint.setTypeface(textView.getTypeface());
        float textSize = textView.getTextSize();
        paint.setTextSize(textSize);
        String text = textView.getText().toString();
        paint.getTextBounds(text, 0, text.length(), bounds);

        while (bounds.width() > desiredWidth) {
            textSize--;
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), bounds);
        }

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (edit && !added) {//if he presses the back button
            SplashActivity.order.getCart().addProduct(thisPizza);
            thisPizza = null;

        }
    }
}