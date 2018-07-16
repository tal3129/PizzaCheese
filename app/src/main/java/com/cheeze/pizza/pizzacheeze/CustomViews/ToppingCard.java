package com.cheeze.pizza.pizzacheeze.CustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.cheeze.pizza.pizzacheeze.EditToppings;
import com.cheeze.pizza.pizzacheeze.R;
import com.cheeze.pizza.pizzacheeze.types.Topping;
import com.lb.auto_fit_textview.AutoResizeTextView;

public class ToppingCard extends RelativeLayout {

    private int height;
    private int width;
    private Topping topping;

    private RelativeLayout mainLayout;
    private RelativeLayout.LayoutParams mainLayoutParams;

    private AutoResizeTextView tvName;
    private RelativeLayout.LayoutParams tvNameParams;

    private RelativeLayout finalQuatersLayout;
    private RelativeLayout.LayoutParams finalQuatersLayoutParams;

    private RelativeLayout quatersLayout;
    private RelativeLayout.LayoutParams quatersLayoutParams;

    private ImageView[] quaters;
    private RelativeLayout.LayoutParams[] quatersParams;

    private int quaters_size;
    private int quaters_margin_left;
    private int card_unactive_color;
    private int card_active_color;
    private int one_quater_size;
    private int text_size;
    private int text_margin_right;
    private int[] Ids={R.drawable.red_quater1,R.drawable.red_quater2,R.drawable.red_quater3,R.drawable.red_quater4};

    int text_height;
    int text_width;


    Context context;

    @SuppressLint("NewApi")
    public ToppingCard(final Context context1, int height, int width, final Topping topping) {
        super(context1);

        this.context = context1;

        this.height = height;
        this.width = width;
        this.topping = topping;

        setSize();

        mainLayout = new RelativeLayout(context);
        mainLayout.setBackgroundColor(card_unactive_color);
        mainLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        tvName = new AutoResizeTextView(context);
        tvName.setText(topping.getName());
        tvName.setTextSize(text_size);
        tvName.setTextColor(Color.parseColor("#c0232c"));
        tvName.setGravity(Gravity.CENTER_VERTICAL);
        tvNameParams = new RelativeLayout.LayoutParams(text_width,text_height);
        tvNameParams.addRule(RelativeLayout.CENTER_VERTICAL);
        tvNameParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tvNameParams.setMargins(0,0,text_margin_right,0);



        quaters = new ImageView[4];
        quatersParams = new RelativeLayout.LayoutParams[4];
        for (int i = 0 ; i<4 ; i++){
            quaters[i] = new ImageView(context);
            quaters[i].setId(generateViewId());
            quatersParams[i] = new RelativeLayout.LayoutParams(one_quater_size,one_quater_size);
        }

        finalQuatersLayout = new RelativeLayout(context);
        finalQuatersLayout.setId(generateViewId());
        finalQuatersLayout.setBackgroundResource(R.drawable.red_stroke_circle);
        finalQuatersLayoutParams = new RelativeLayout.LayoutParams(quaters_size,quaters_size);
        finalQuatersLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        finalQuatersLayoutParams.setMargins(quaters_margin_left,0,0,0);
        finalQuatersLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);

        quatersLayout = new RelativeLayout(context);
        quatersLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        quatersLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        quatersLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        quatersParams[0].addRule(RelativeLayout.RIGHT_OF, quaters[1].getId());
        quatersParams[2].addRule(RelativeLayout.BELOW, quaters[1].getId());
        quatersParams[3].addRule(RelativeLayout.BELOW, quaters[1].getId());
        quatersParams[2].addRule(RelativeLayout.RIGHT_OF, quaters[3].getId());


        for (int i=0;i<4;i++){
            quaters[i].setVisibility(INVISIBLE);
            quatersLayout.addView(quaters[i],quatersParams[i]);
        }

        finalQuatersLayout.setVisibility(INVISIBLE);
        finalQuatersLayout.addView(quatersLayout,quatersLayoutParams);
        mainLayout.addView(finalQuatersLayout,finalQuatersLayoutParams);
        mainLayout.addView(tvName,tvNameParams);

        this.addView(mainLayout,mainLayoutParams);

        mainLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditToppings.class);
            intent.putExtra("title", topping.getName());
            context.startActivity(intent);
        });
    }


    public void setSize(){
        quaters_size =(int) (height/1.5);
        one_quater_size = (int)(quaters_size/2.35);
        quaters_margin_left =  quaters_size / 2 ;
        card_unactive_color = Color.parseColor("#dddde4");
        card_active_color = Color.parseColor("#caedcc");
        text_size = convertSpToPixels(100,getContext());
        text_margin_right = quaters_margin_left;

        text_height = (int) (height/2.0);
        text_width = (int)(width/4.0);
    }

    public void onChange(){
        int count = 0;
        for (int i=0; i < 4 ; i++){
            if (topping.getQuarterAt(i)){
                count++;
                if(quaters[i].getDrawable()==null)
                    quaters[i].setImageResource(Ids[i]);
                quaters[i].setVisibility(VISIBLE);
            }else {
                quaters[i].setVisibility(INVISIBLE);
            }
        }

        if (count==0){
            mainLayout.setBackgroundColor(card_unactive_color);
            finalQuatersLayout.setVisibility(INVISIBLE);
        }else {
            finalQuatersLayout.setVisibility(VISIBLE);
            mainLayout.setBackgroundColor(card_active_color);
        }
    }


    public void onCart(Context context){
        this.context = context;
        this.mainLayout.setClickable(false);
        this.onChange();
        this.mainLayout.setBackgroundColor(Color.parseColor("#f0f0f7"));

        this.quaters[0].setImageResource(R.drawable.red_cart_quater1);
        this.quaters[1].setImageResource(R.drawable.red_cart_quater2);
        this.quaters[2].setImageResource(R.drawable.red_cart_quater3);
        this.quaters[3].setImageResource(R.drawable.red_cart_quater4);
    }

    public Topping getTopping() {
        return topping;
    }

    public static int convertSpToPixels(float sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }
}
