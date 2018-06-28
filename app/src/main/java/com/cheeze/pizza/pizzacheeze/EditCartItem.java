package com.cheeze.pizza.pizzacheeze;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheeze.pizza.pizzacheeze.CustomViews.MyNumberPicker;
import com.cheeze.pizza.pizzacheeze.types.Pizza;
import com.cheeze.pizza.pizzacheeze.types.Product;
import com.cheeze.pizza.pizzacheeze.types.ToppingProduct;

public class EditCartItem extends AppCompatActivity {

    RelativeLayout mainLayout;
    RelativeLayout.LayoutParams mainLayoutParams;

    RelativeLayout quantityLayout;
    RelativeLayout.LayoutParams quantityLayoutParams;

    TextView tvQuantity;
    RelativeLayout.LayoutParams tvQuantityParams;

    MyNumberPicker numberPicker;
    RelativeLayout.LayoutParams numberPickerParams;

    RelativeLayout noteLayout;
    RelativeLayout.LayoutParams noteLayoutParams;

    TextView tvNote;
    RelativeLayout.LayoutParams tvNoteParams;

    EditText etNote;
    RelativeLayout.LayoutParams etNoteParams;

    RelativeLayout finalPriceLayout;
    RelativeLayout.LayoutParams finalPriceLayoutParams;

    TextView finalPriceText;
    RelativeLayout.LayoutParams finalPriceTextParams;

    TextView finalPriceValue;
    RelativeLayout.LayoutParams finalPriceValueParams;

    Button bOk;
    RelativeLayout.LayoutParams bOKParams;

    ImageView ivEditTopings;
    RelativeLayout.LayoutParams ivEditTopingsParams;


    Typeface face;

    int width;
    int height;
    int popupWidth;
    int popupHeight;
    int text_quantity_size;
    int number_picker_width;
    int number_picker_height;
    int quantity_layout_margin_top;
    int number_picker_margin_right;
    int note_layout_margin_right;
    int ok_button_height;
    int final_price_text_margin_right;
    int iv_topping_size;

    public static Product product;


    public static ShoppingCart shoppingCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().getAttributes().windowAnimations = R.style.slideRightAnimations;

        setSizes();

        mainLayout = new RelativeLayout(this);
        mainLayoutParams = new RelativeLayout.LayoutParams(popupWidth,popupHeight);

        buildButton();
        buildFinalPrice();
        buildQuantity();
        buildNote();

        if(product instanceof Pizza || product instanceof ToppingProduct)
            setEditToppingButton();



        setTextsSizes();
        setContentView(mainLayout,mainLayoutParams);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (shoppingCart!=null)
            shoppingCart.updatePrices();

        if (etNote.getText().toString().trim().isEmpty()){
            product.setNote("");
        }
        else
            product.setNote(etNote.getText().toString());
        finish();
    }

    public void setSizes(){
        //get screen size
        final Display display = this.getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        //finish

        face = Typeface.createFromAsset(this.getAssets(), "fonts/VarelaRound-Regular.ttf");

        popupHeight = (int)(height/2.4);
        popupWidth = (int)(width/1.1);

        text_quantity_size = convertSpToPixels(14,this);

        number_picker_height = (int)(popupHeight/7.7);
        number_picker_width = (int)(popupWidth/3.5);
        quantity_layout_margin_top = (int)(number_picker_height/2);
        number_picker_margin_right = quantity_layout_margin_top/3;

        note_layout_margin_right = quantity_layout_margin_top;

        ok_button_height = (int)(popupHeight/6);

        final_price_text_margin_right = note_layout_margin_right/4;

        iv_topping_size = (int)( number_picker_height*1.5);
    }

    public void buildQuantity(){

        quantityLayout = new RelativeLayout(this);
        quantityLayout.setId(View.generateViewId());
        quantityLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        quantityLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        quantityLayoutParams.setMargins(0,quantity_layout_margin_top,0,0);

        numberPicker = new MyNumberPicker(this,number_picker_height,product,finalPriceValue);
        numberPicker.setId(View.generateViewId());
        numberPicker.setMaxValue(200);
        numberPicker.setMinValue(1);
        numberPicker.setCorrectCount(product.getCount());
        numberPickerParams = new RelativeLayout.LayoutParams(number_picker_width, number_picker_height);

        tvQuantity = new TextView(this);
        tvQuantity.setId(View.generateViewId());
        tvQuantity.setTextSize(text_quantity_size);
        tvQuantity.setTextColor(Color.parseColor("#c0232c"));
        tvQuantity.setTypeface(face);
        tvQuantity.setText("כמות:");
        tvQuantityParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvQuantityParams.addRule(RelativeLayout.RIGHT_OF,numberPicker.getId());
        tvQuantityParams.setMargins(number_picker_margin_right,0,0,0);


        quantityLayout.addView(numberPicker,numberPickerParams);
        quantityLayout.addView(tvQuantity,tvQuantityParams);

        mainLayout.addView(quantityLayout,quantityLayoutParams);
    }

    public void buildNote(){
        noteLayout = new RelativeLayout(this);
        noteLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        noteLayoutParams.addRule(RelativeLayout.BELOW,quantityLayout.getId());
        noteLayoutParams.setMargins(0,quantity_layout_margin_top,note_layout_margin_right,0);

        tvNote = new TextView(this);
        tvNote.setId(View.generateViewId());
        tvNote.setText("הערה:");
        tvNote.setTypeface(face);
        tvNote.setTextSize(text_quantity_size);
        tvNote.setTextColor(Color.parseColor("#c0232c"));
        tvNoteParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvNoteParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        etNote = new EditText(this);
        etNote.setSingleLine(true);
        etNote.setTextSize(convertSpToPixels(6,this));
        etNote.setText(product.getNote());
        etNoteParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        etNoteParams.addRule(RelativeLayout.LEFT_OF,tvNote.getId());
        //etNoteParams.addRule(RelativeLayout.ALIGN_BOTTOM,tvNote.getId());
        etNoteParams.setMargins(note_layout_margin_right,0,note_layout_margin_right,0);

        noteLayout.addView(tvNote,tvNoteParams);
        noteLayout.addView(etNote,etNoteParams);

        mainLayout.addView(noteLayout,noteLayoutParams);
    }

    public void buildFinalPrice(){

        finalPriceLayout = new RelativeLayout(this);
        finalPriceLayout.setId(View.generateViewId());
        finalPriceLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        finalPriceLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        finalPriceLayoutParams.addRule(RelativeLayout.ABOVE,bOk.getId());
        finalPriceLayoutParams.setMargins(0,0,note_layout_margin_right,note_layout_margin_right);

        finalPriceText = new TextView(this);
        finalPriceText.setId(View.generateViewId());
        finalPriceText.setText("מחיר כולל:");
        finalPriceText.setTextColor(Color.parseColor("#c0232c"));
        finalPriceText.setTextSize(convertSpToPixels(8,this));
        finalPriceTextParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        finalPriceTextParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        finalPriceValue = new TextView(this);
        finalPriceValue.setId(View.generateViewId());
        finalPriceValue.setTextColor(Color.parseColor("#c0232c"));
        finalPriceValue.setTextSize(convertSpToPixels(8,this));
        finalPriceValueParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        finalPriceValueParams.addRule(RelativeLayout.LEFT_OF,finalPriceText.getId());
        finalPriceValueParams.setMargins(0,0,final_price_text_margin_right,0);


        finalPriceLayout.addView(finalPriceText,finalPriceTextParams);
        finalPriceLayout.addView(finalPriceValue,finalPriceValueParams);

        mainLayout.addView(finalPriceLayout,finalPriceLayoutParams);
    }

    public void buildButton(){
        bOk = new Button(this);
        bOk.setId(View.generateViewId());
        bOk.setBackgroundColor(Color.parseColor("#c0232c"));
        bOk.setText("אישור");
        bOk.setTextColor(Color.WHITE);
        bOk.setTextSize(convertSpToPixels(7,this));
        bOKParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ok_button_height);
        bOKParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        bOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoppingCart!=null)
                    shoppingCart.updatePrices();

                if (etNote.getText().toString().length()!=0){
                    product.setNote(etNote.getText().toString());
                }

                finish();
            }
        });

        mainLayout.addView(bOk,bOKParams);
    }

    public void setTextsSizes(){

        correctWidth(tvQuantity,(int) (number_picker_width/1.8));
        correctWidth(tvNote,(int) (number_picker_width/1.8));
    }

    public void setEditToppingButton(){
        ivEditTopings = new ImageView(this);
        ivEditTopings.setImageResource(R.drawable.edit_topings);

        ivEditTopings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product instanceof Pizza) {
                    EditPizza.thisPizza = (Pizza) product;
                    SplashActivity.order.getCart().removeProduct(product);
                    Intent intent = new Intent(EditCartItem.this, EditPizza.class);
                    intent.putExtra("Edit", true);
                    startActivity(intent);
                    finish();
                }
                if (product instanceof ToppingProduct){
                    SetProductToppingActivity.myProd = (ToppingProduct) product;
                    SplashActivity.order.getCart().removeProduct(product);
                    Intent intent = new Intent(EditCartItem.this,SetProductToppingActivity.class);
                    intent.putExtra("Edit", true);
                    startActivity(intent);
                    SetProductToppingActivity.shoppingCart = shoppingCart;
                    finish();
                }

            }
        });

        ivEditTopingsParams  = new RelativeLayout.LayoutParams(iv_topping_size,iv_topping_size);
        ivEditTopingsParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        ivEditTopingsParams.addRule(RelativeLayout.ABOVE,bOk.getId());
        ivEditTopingsParams.setMargins(note_layout_margin_right,0,0,note_layout_margin_right);

        mainLayout.addView(ivEditTopings,ivEditTopingsParams);
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
