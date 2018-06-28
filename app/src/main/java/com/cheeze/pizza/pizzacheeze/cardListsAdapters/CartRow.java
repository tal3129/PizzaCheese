package com.cheeze.pizza.pizzacheeze.cardListsAdapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cheeze.pizza.pizzacheeze.EditCartItem;
import com.cheeze.pizza.pizzacheeze.LoadingDialog;
import com.cheeze.pizza.pizzacheeze.R;
import com.cheeze.pizza.pizzacheeze.ShoppingCart;
import com.cheeze.pizza.pizzacheeze.SplashActivity;
import com.cheeze.pizza.pizzacheeze.types.Pasta;
import com.cheeze.pizza.pizzacheeze.types.Pizza;
import com.cheeze.pizza.pizzacheeze.types.Product;
import com.cheeze.pizza.pizzacheeze.types.Topping;
import com.cheeze.pizza.pizzacheeze.types.ToppingProduct;
import com.lb.auto_fit_textview.AutoResizeTextView;

import org.w3c.dom.Text;

public class CartRow extends RelativeLayout {


    ImageView ivRemoveItem;
    RelativeLayout.LayoutParams ivRemoveItemParams;

    TextView tvItemName;
    RelativeLayout.LayoutParams tvItemNameParams;

    RelativeLayout priceLayout;
    RelativeLayout.LayoutParams priceLayoutParams;

    TextView tvPrice;
    RelativeLayout.LayoutParams tvPriceParams;

    ImageView ivEditItem;
    RelativeLayout.LayoutParams ivEditItemParams;

    NonScrollListView listView;
    RelativeLayout.LayoutParams listViewParams;

    int iv_remove_item_size;
    int iv_remove_item_margin_right;
    int tv_name_size;
    int tv_name_margin_rigt;
    int tv_price_layout_margin_right;
    int tv_price_text_size;
    int tv_price_margins;
    int iv_edit_size;
    int iv_edit_margin_right;
    int listview_margin_top;

    int height;
    int width;
    Context _contex;

    Product product;
    public static ShoppingCart shoppingCart;

    public CartRow(final Context context , int width, int height, String name, String price,Product p) {
        super(context);

        this.width = width;
        this.height = height;
        this._contex = context;
        this.product = p;
        setSizes();

        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/VarelaRound-Regular.ttf");

        ivRemoveItem = new ImageView(context);
        ivRemoveItem.setOnClickListener(view -> {
            SplashActivity.order.getCart().removeProduct(product);
            if(shoppingCart!=null)
                shoppingCart.updatePrices();
        });
        ivRemoveItem.setId(generateViewId());
        ivRemoveItem.setImageResource(R.drawable.ic_cancel_black_48dp);
        ivRemoveItem.setAlpha(0.4f);
        ivRemoveItemParams = new RelativeLayout.LayoutParams(iv_remove_item_size,iv_remove_item_size);
        ivRemoveItemParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ivRemoveItemParams.setMargins(0,0,iv_remove_item_margin_right,0);

        tvItemName = new TextView(context);
        tvItemName.setId(generateViewId());
        tvItemName.setText(name);
        tvItemName.setTypeface(face);
        tvItemName.setTextSize(tv_name_size);
        tvItemNameParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvItemNameParams.addRule(RelativeLayout.LEFT_OF,ivRemoveItem.getId());
        tvItemNameParams.setMargins(0,0,tv_name_margin_rigt,0);


        ivEditItem = new ImageView(context);
        ivEditItem.setId(generateViewId());
        ivEditItem.setAlpha(0.4f);
        ivEditItem.setOnClickListener(view -> {
            Intent intent = new Intent(context, EditCartItem.class);
            EditCartItem.product = product;
            context.startActivity(intent);
        });

        ivEditItem.setImageResource(R.drawable.ic_edit_black_48dp);
        ivEditItemParams = new RelativeLayout.LayoutParams(iv_edit_size,iv_edit_size);
        ivEditItemParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        ivEditItemParams.setMargins(iv_edit_margin_right,0,0,0);

        priceLayout = new RelativeLayout(context);
        priceLayout.setId(generateViewId());
        priceLayout.setAlpha(0.35f);
        priceLayout.setBackgroundResource(R.drawable.rounded_corners);
        priceLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        priceLayoutParams.addRule(RelativeLayout.RIGHT_OF,ivEditItem.getId());
        priceLayoutParams.setMargins(tv_price_layout_margin_right,0,0,0);

        tvPrice = new TextView(context);
        tvPrice.setId(generateViewId());
        tvPrice.setText(price);
        tvPrice.setTextSize(tv_price_text_size);
        tvPrice.setTextColor(Color.WHITE);
        tvPriceParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvPriceParams.setMargins((int)(tv_price_margins*1.5),tv_price_margins,tv_price_margins,tv_price_margins);

        priceLayout.addView(tvPrice,tvPriceParams);


        setTextSize();

        this.addView(ivRemoveItem,ivRemoveItemParams);
        this.addView(tvItemName,tvItemNameParams);
        this.addView(priceLayout,priceLayoutParams);
        this.addView(ivEditItem,ivEditItemParams);

        if (product instanceof ToppingProduct)
            setToppingForToppingProduct();
        else if(product instanceof Pasta)
            if(((Pasta)p).isExtraCheese())
                setForPasta();
    }

    private void setForPasta(){
        AutoResizeTextView tvCheese = new AutoResizeTextView(_contex);
        tvCheese.setText("תוספת הקרמה");
        RelativeLayout.LayoutParams tvCheeseParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,iv_remove_item_size);
        tvCheeseParams.addRule(RelativeLayout.BELOW,priceLayout.getId());
        tvCheeseParams.setMargins(0,10,50,0);
        this.addView(tvCheese,tvCheeseParams);
    }

    public void setToppingForToppingProduct(){
        AutoResizeTextView tvToppings = new AutoResizeTextView(_contex);
        tvToppings.setText(((ToppingProduct)product).toppingString());
        RelativeLayout.LayoutParams tvToppingsParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,iv_remove_item_size);
        tvToppingsParams.addRule(RelativeLayout.BELOW,priceLayout.getId());
        tvToppingsParams.setMargins(0,30,0,0);
        this.addView(tvToppings,tvToppingsParams);
    }

    public void setSizes(){

        int view_margin_right = (int) (width/20);

        iv_remove_item_size = (int) (3*width/40);
        iv_remove_item_margin_right = (int) (view_margin_right/2.5);
        tv_name_size = convertSpToPixels(10,_contex);
        tv_name_margin_rigt = view_margin_right;
        tv_price_layout_margin_right = view_margin_right;
        tv_price_text_size =  convertSpToPixels(10,_contex);;
        tv_price_margins = view_margin_right/6;
        iv_edit_size = iv_remove_item_size;
        iv_edit_margin_right = view_margin_right;
        listview_margin_top = view_margin_right;


    }

    public void setToppingForPizza(Pizza pizza,int popupWidth,int popupHeight){
        int itemHeight = popupHeight/13;
        int sumHeight = itemHeight*(pizza.getToppingArrayList().size());
        CartPizzaToppingAdapter cartPizzaToppingAdapter = new CartPizzaToppingAdapter((Activity) _contex,pizza.getToppingArrayList(),popupHeight,popupWidth,itemHeight);
        listView = new NonScrollListView(_contex);
        listView.setId(generateViewId());
        listView.setDividerHeight(itemHeight/30);
        listView.setAdapter(cartPizzaToppingAdapter);
        listViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        listViewParams.addRule(RelativeLayout.BELOW,priceLayout.getId());
        listViewParams.setMargins(0,listview_margin_top,listview_margin_top*2,0);
        this.addView(listView,listViewParams);
    }


    public void setTextSize(){
        correctWidth(tvItemName,5*3*width/40);
        correctWidth(tvPrice,3*3*width/50);
    }

    public void correctWidth(TextView textView, int desiredWidth) {
        Paint paint = new Paint();
        Rect bounds = new Rect();

        paint.setTypeface(textView.getTypeface());
        float textSize = textView.getTextSize();
        paint.setTextSize(textSize);
        String text = textView.getText().toString();
        paint.getTextBounds(text, 0, text.length(), bounds);

        while (bounds.width() > desiredWidth)
        {
            textSize--;
            paint.setTextSize(textSize);
            paint.getTextBounds(text, 0, text.length(), bounds);
        }

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }


    public static int convertSpToPixels(float sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

}
