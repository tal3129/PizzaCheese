package com.cheeze.pizza.pizzacheeze;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.cheeze.pizza.pizzacheeze.CustomViews.Banner;
import com.cheeze.pizza.pizzacheeze.cardListsAdapters.CartAdapter;
import com.cheeze.pizza.pizzacheeze.cardListsAdapters.CartRow;
import com.cheeze.pizza.pizzacheeze.types.Product;

import java.util.ArrayList;

public class ShoppingCart extends Activity implements View.OnClickListener {
    ListView cartList;
    CartAdapter cartAdapter;
    TextView tvTotalPrice;
    ImageView btnContinue;
    Button btnFinishOrder;
    ImageView btnShip, btnTake;
    Dialog d;
    final int RC_EXECUTE_ORDER = 66;

    int width;
    int height;

    int popupWidth;
    int popupHeight;
    private double prevPrice;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //trying to set the window's animations
        this.getWindow().getAttributes().windowAnimations = R.style.growAnimations;
        loadingDialog = new LoadingDialog(this);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadingDialog.setCancelable(false);
        EditCartItem.shoppingCart = this;
        CartRow.shoppingCart = this;
        setContentView(R.layout.activity_shopping_cart);

        setSizes();
        setPopupSize();

        findViewById(R.id.cartTitle).setOnClickListener(this);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        cartList = findViewById(R.id.lwCartList);
        btnContinue = findViewById(R.id.btnContinue);
        btnFinishOrder = findViewById(R.id.btnFinishOrder);


        btnContinue.setOnClickListener(this);
        btnFinishOrder.setOnClickListener(this);

        SplashActivity.order.getCart().updatePrice();
        tvTotalPrice.setText("₪" + String.format("%.2f", SplashActivity.order.getCart().getSum()));
        cartAdapter = new CartAdapter(this, SplashActivity.order.getCart().getSelectedProducts(), popupWidth, popupHeight);
        cartList.setAdapter(cartAdapter);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (loadingDialog != null)
            loadingDialog.cancel();
    }

    public void updatePrices() {
        SplashActivity.order.getCart().updatePrice();
        tvTotalPrice.setText("₪" + String.format("%.2f", SplashActivity.order.getCart().getSum()));
        cartAdapter.notifyDataSetChanged();
    }

    public void setSizes() {
        //get screen size
        final Display display = this.getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        //finish

        popupWidth = (int) (width / 1.1);
        popupHeight = (int) (height / 1.1);
    }


    public void setPopupSize() {
        findViewById(R.id.shoppingCartMainLayoot).getLayoutParams().width = popupWidth;
        findViewById(R.id.shoppingCartMainLayoot).getLayoutParams().height = popupHeight;

    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.cartTitle:
                setResult(ChooseProduct.RSC_CLOSE);
                finish();
                break;
            case R.id.btnContinue:
                setResult(ChooseProduct.RSC_CLOSE);
                finish();
                break;
            case R.id.btnFinishOrder:
                //if the app is inactive, show a proper toast
                if (SplashActivity.needToUpdate && !SplashActivity.TEST_MODE) {
                    Toast.makeText(ShoppingCart.this, R.string.plsUpdate, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse("market://details?id=com.cheeze.pizza.pizzacheeze"));
                    startActivity(intent);
                } else if (!SplashActivity.myAppSettings.isAppStatus())
                    Toast.makeText(this, R.string.appInactive, Toast.LENGTH_SHORT).show();
                else {
                    //if there are products to show, show a banner of one of them at random
                    if (randomAvailableProduct() != null) {
                        Banner banner = new Banner(this, randomAvailableProduct());
                        banner.show();
                        prevPrice = SplashActivity.order.getCart().getSum();
                        banner.setOnDismissListener(dialog -> {
                            if (SplashActivity.order.getCart().getSum() > prevPrice)
                                updateBannerPrices();
                            createLoginDialog();
                        });
                    } else
                        createLoginDialog();
                }
                break;
            case R.id.btnShip:
                d.dismiss();
                i = new Intent(this, ShipForm.class);
                setResult(ChooseProduct.RSC_CLOSE);
                startActivityForResult(i, RC_EXECUTE_ORDER);
                break;

            case R.id.btnTake:
                d.dismiss();
                i = new Intent(this, TakeForm.class);
                setResult(ChooseProduct.RSC_CLOSE);
                startActivityForResult(i, RC_EXECUTE_ORDER);
                break;
            default:
                updatePrices();
                break;
        }
    }

    private void updateBannerPrices() {
        tvTotalPrice.setText("₪" + String.format("%.2f", SplashActivity.order.getCart().getSum()));
        cartAdapter.notifyDataSetChanged();
    }

    public void createLoginDialog() {
        //there needs to be a product in order to finish the order
        if (SplashActivity.order.getCart().getSelectedProducts().isEmpty())
            Toast.makeText(this, "יש לבחור במוצר לפני סיום ההזמנה", Toast.LENGTH_SHORT).show();
        else {
            d = new Dialog(this);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            d.setContentView(R.layout.shiportake_dialog);


            if (d.getActionBar() != null)
                d.getActionBar().hide();
            d.setCancelable(true);

            btnShip = d.findViewById(R.id.btnShip);
            btnShip.setOnClickListener(this);
            btnTake = d.findViewById(R.id.btnTake);
            btnTake.setOnClickListener(this);

            btnShip.getLayoutParams().width = width / 4;
            btnShip.getLayoutParams().height = width / 4;
            btnTake.getLayoutParams().width = width / 4;
            btnTake.getLayoutParams().height = width / 4;

            loadingDialog.cancel();
            d.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }

    public Product randomAvailableProduct() {
        //first, check for a possible preferred product
        ArrayList<Product> poss = new ArrayList<>();
        for (Product product : SplashActivity.productArrayList) {
            //if some products are banner-preferred, if they aren't already in the cart, return one of them (at random)
            if (product.isPreferred()) {
                boolean add = true;
                for (Product p : SplashActivity.order.getCart().getSelectedProducts())
                    if (product.getName().equals(p.getName()))
                        add = false;
                if (add)
                    poss.add(product);
            }
        }
        if (poss.size() > 0)
            return poss.get((int) Math.floor(Math.random() * poss.size()));

        //if there are no available preferred products, show a regular one
        boolean pastasPossible = true, pastriesPossible = true, drinksPossible = true, saladsPossible = true, desertsPossible = true;
        //we don't want to show banners about product types that are already in the cart
        for (Product product : SplashActivity.order.getCart().getSelectedProducts()) {
            switch (product.getType()) {
                case "פסטות":
                    pastasPossible = false;
                    break;
                case "מאפים":
                    pastriesPossible = false;
                    break;
                case "שתייה":
                    drinksPossible = false;
                    break;
                case "מנות אחרונות":
                    desertsPossible = false;
                    break;
                case "סלטים":
                    saladsPossible = false;
                    break;
            }
        }

        ArrayList<Product> possibleBannerProds = new ArrayList<>();
        for (Product product : SplashActivity.productArrayList) {
            if (product.isAvailable()) {
                boolean temp = true;
                switch (product.getType()) {
                    case "פיצות":
                    case "תוספות":
                        temp = false;
                        break;
                    case "פסטות":
                        temp = pastasPossible;
                        break;
                    case "מאפים":
                        temp = pastriesPossible;
                        break;
                    case "שתייה":
                        temp = drinksPossible;
                        break;
                    case "מנות אחרונות":
                        temp = desertsPossible;
                        break;
                    case "סלטים":
                        temp = saladsPossible;
                }
                if (temp)
                    possibleBannerProds.add(product);
            }
        }
        if (possibleBannerProds.size() > 0)
            return possibleBannerProds.get((int) Math.floor(Math.random() * possibleBannerProds.size()));
        else
            return null;
    }

}
