package com.cheeze.pizza.pizzacheeze;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheeze.pizza.pizzacheeze.types.CheckBoxTopping;
import com.cheeze.pizza.pizzacheeze.types.Product;
import com.cheeze.pizza.pizzacheeze.types.Topping;
import com.cheeze.pizza.pizzacheeze.types.ToppingProduct;

import java.util.ArrayList;

public class SetProductToppingActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<CheckBoxTopping> toppingList;
    public static ToppingProduct myProd;
    public static ShoppingCart shoppingCart;
    boolean isEdit=false;
    LinearLayout llTopping;
    private int topAmount;
    TextView totalPrice;
    Button btnFinish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_product_topping);
        totalPrice=findViewById(R.id.tvTotalPrice);
        btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(this);
        this.getWindow().getAttributes().windowAnimations = R.style.slideRightAnimations;
        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra("Edit",false);

        totalPrice.setText(String.format("%.2f", myProd.getPrice())+" ₪ ");
        //get all toppings
        toppingList = new ArrayList<>();
        llTopping = findViewById(R.id.llTopping);
        topAmount=0;
        for (Product product : SplashActivity.productArrayList)
            if (product.getType().equals("תוספות")) {
                CheckBoxTopping cbt=new CheckBoxTopping(new Topping(product.getName(), product.getPicUri().toString()), this);
                cbt.getCheckBox().setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if(isChecked)
                        topAmount++;
                    else
                        topAmount--;
                    updatePrice();

                });
                toppingList.add(cbt);
                llTopping.addView(cbt.getCheckBox());
            }

            if (isEdit){
               // topAmount = myProd.getToppings().size();
                for (CheckBoxTopping checkBoxTopping : toppingList){
                    for (Topping topping : myProd.getToppings()){
                        if (topping.getName().equals(checkBoxTopping.getTopping().getName())){
                            checkBoxTopping.getCheckBox().setChecked(true);
                        }
                    }
                }

            }

    }

    private void updatePrice(){
        double tqs=topAmount;
        tqs=Math.ceil(tqs/4);
        double finalPrice =myProd.getOriginalPrice()+(tqs*5);
        double extraPrice=topAmount==0?0:(topAmount-1)*4;
        //totalPrice.setText(String.format("%.2f", finalPrice)+" ₪ ");
        totalPrice.setText(String.format("%.2f", extraPrice + myProd.getOriginalPrice())+" ₪ ");
    }


    @Override
    public void onClick(View view) {
        myProd.setToppings(new ArrayList<Topping>());
        for (CheckBoxTopping topping : toppingList)
            if (topping.getCheckBox().isChecked())
                myProd.addTopping(topping.getTopping());


        SplashActivity.order.getCart().addProduct(myProd);
        setResult(ChooseProduct.RSC_SHOW_DIALOG);

        if (isEdit && shoppingCart!=null)
            shoppingCart.updatePrices();

        finish();


    }

}
