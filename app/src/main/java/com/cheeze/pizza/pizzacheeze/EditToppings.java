package com.cheeze.pizza.pizzacheeze;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheeze.pizza.pizzacheeze.types.Topping;

public class EditToppings extends AppCompatActivity {

    RelativeLayout mainLayout;
    RelativeLayout.LayoutParams mainLayoutParams;

    TextView tvTitle;
    RelativeLayout.LayoutParams tvTitleParams;

    RelativeLayout quatersLayout;
    RelativeLayout.LayoutParams quatersLayoutParams;

    ImageView[] ivQuaters;
    RelativeLayout.LayoutParams[] ivQuatersParams;

    TextView tvInstructions;
    RelativeLayout.LayoutParams tvInstructionsParams;

    RelativeLayout editLayout;
    RelativeLayout.LayoutParams editLayoutParams;

    ImageView ivAllPizza;
    RelativeLayout.LayoutParams ivAllPizzaParams;

    ImageView ivRightHalf;
    RelativeLayout.LayoutParams ivRightHalfParams;

    ImageView ivLefttHalf;
    RelativeLayout.LayoutParams ivLeftHalfParams;

    FloatingActionButton fabExit;
    RelativeLayout.LayoutParams fabExitParams;

    int width;
    int height;
    int mainlayout_width;
    int mainlayout_height;
    int quater_size;
    int tv_size;
    int quaterslayout_size;
    int quaters_margin_top;
    int tv_instruction_size;
    int edditlayout_height;
    int edditlayout_width;
    int edditlayout_button_size;
    int edditlayout_button_margin;
    int fab_margins;

    String title;
    Topping topping;

    int[] resourcesFullRed;
    int[] resourcesStrokeOnly;

    boolean allPizza;
    boolean rightHalf;
    boolean leftHalf;

    int index;

    public static EditPizza editPizza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().getAttributes().windowAnimations = R.style.slideRightAnimations;
        setSizes();

        mainLayout = new RelativeLayout(this);
        mainLayout.setBackgroundColor(Color.WHITE);
        mainLayoutParams = new RelativeLayout.LayoutParams(mainlayout_width, ViewGroup.LayoutParams.WRAP_CONTENT);


        buildtitle();
        buildQuaters();
        buildOptions();
        onChangeQuater();
        buildExitButton();


        setContentView(mainLayout,mainLayoutParams);
    }



    public void setSizes(){
        //get screen size
        final Display display = this.getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        //finish

        Intent intent = getIntent();
        title = intent.getExtras().getString("title");

        for (int i=0;i<EditPizza.toppingCardList.size();i++){
            if (EditPizza.toppingCardList.get(i).getTopping().getName().equals(title)){
                topping = EditPizza.toppingCardList.get(i).getTopping();
                index = i;
            }
        }

        resourcesFullRed = new int[4];
        resourcesStrokeOnly = new int[4];

        resourcesFullRed[0] = R.drawable.red_quater1;
        resourcesFullRed[1] = R.drawable.red_quater2;
        resourcesFullRed[2] = R.drawable.red_quater3;
        resourcesFullRed[3] = R.drawable.red_quater4;

        resourcesStrokeOnly[0] = R.drawable.red_stroke_quater1;
        resourcesStrokeOnly[1] = R.drawable.red_stroke_quater2;
        resourcesStrokeOnly[2] = R.drawable.red_stroke_quater3;
        resourcesStrokeOnly[3] = R.drawable.red_stroke_quater4;




        mainlayout_height = (int)(height/1.5);
        mainlayout_width = (int) (width/1.5);

        quaterslayout_size = (int) (mainlayout_width/1.2);
        quater_size =(int)(quaterslayout_size/2);
        quaters_margin_top = quater_size/15;

        tv_size = convertSpToPixels(10,this);

        tv_instruction_size = convertSpToPixels(7,this);
        edditlayout_height =(int) (quater_size/2);
        edditlayout_width = (int)(mainlayout_width/1.2);
        edditlayout_button_size = (int)(edditlayout_height/1.2);
        edditlayout_button_margin = edditlayout_button_size/5;

        fab_margins = quaters_margin_top;

    }

    @SuppressLint("NewApi")
    public void buildtitle(){
        tvTitle = new TextView(this);
        tvTitle.setId(View.generateViewId());
        tvTitle.setTextSize(tv_size);
        tvTitle.setText(title);
        tvTitle.setTextColor(Color.parseColor("#c0232c"));
        tvTitleParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvTitleParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        mainLayout.addView(tvTitle,tvTitleParams);
    }

    @SuppressLint("NewApi")
    public void buildQuaters(){
        final int size=100;
        quatersLayout = new RelativeLayout(this);
        quatersLayout.setId(View.generateViewId());
        quatersLayoutParams = new RelativeLayout.LayoutParams(quaterslayout_size,quater_size*2);
        quatersLayoutParams.addRule(RelativeLayout.BELOW,tvTitle.getId());
        quatersLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        quatersLayoutParams.setMargins(0,quaters_margin_top,0,0);

        ivQuaters = new ImageView[4];
        ivQuatersParams = new RelativeLayout.LayoutParams[4];

        for (int i=0;i<4;i++){
            ivQuaters[i] = new ImageView(this);
            ivQuaters[i].setTag(i+"");
            ivQuaters[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = Integer.valueOf(v.getTag().toString());
                    if (topping.getQuarterAt(id)){
                        //ivQuaters[id].setImageResource(resourcesStrokeOnly[id]);
                        ivQuaters[id].setImageBitmap(SplashActivity.decodeSampledBitmapFromResource(getResources(),resourcesStrokeOnly[id],size,size));
                    }
                    else {
                        ivQuaters[id].setImageBitmap(SplashActivity.decodeSampledBitmapFromResource(getResources(),resourcesFullRed[id],size,size));
                    }


                    topping.changeQuater(id);
                    onChangeQuater();
                }
            });
            ivQuaters[i].setId(View.generateViewId());
            ivQuatersParams[i] = new RelativeLayout.LayoutParams(quater_size,quater_size);
        }

        for (int i=0;i<4;i++){
            if (topping.getQuarterAt(i)){
                ivQuaters[i].setImageBitmap(SplashActivity.decodeSampledBitmapFromResource(getResources(),resourcesFullRed[i],size,size));
            }
            else {
                ivQuaters[i].setImageBitmap(SplashActivity.decodeSampledBitmapFromResource(getResources(),resourcesStrokeOnly[i],size,size));
            }
        }

        ivQuatersParams[0].addRule(RelativeLayout.RIGHT_OF, ivQuaters[1].getId());
        ivQuatersParams[2].addRule(RelativeLayout.BELOW, ivQuaters[1].getId());
        ivQuatersParams[3].addRule(RelativeLayout.BELOW, ivQuaters[1].getId());
        ivQuatersParams[2].addRule(RelativeLayout.RIGHT_OF, ivQuaters[3].getId());

        for (int i=0;i<4;i++){
            quatersLayout.addView(ivQuaters[i],ivQuatersParams[i]);
        }



        mainLayout.addView(quatersLayout,quatersLayoutParams);
    }

    @SuppressLint("NewApi")
    public void buildOptions(){
        tvInstructions = new TextView(this);
        tvInstructions.setId(View.generateViewId());
        tvInstructions.setText("הקש על רבע להוספה/הורדה");
        tvInstructions.setTextSize(tv_instruction_size);
        tvInstructions.setTextColor(Color.parseColor("#656568"));
        tvInstructionsParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvInstructionsParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tvInstructionsParams.addRule(RelativeLayout.BELOW,quatersLayout.getId());
        tvInstructionsParams.setMargins(0,quaters_margin_top,0,0);

        correctWidth(tvInstructions,(int)(mainlayout_width/1.1));

        editLayout = new RelativeLayout(this);
        editLayout.setId(View.generateViewId());
        editLayout.setBackgroundColor(Color.parseColor("#efeff6"));
        editLayoutParams = new RelativeLayout.LayoutParams(edditlayout_width,edditlayout_height);
        editLayoutParams.addRule(RelativeLayout.BELOW,tvInstructions.getId());
        editLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        editLayoutParams.setMargins(0,quaters_margin_top,0,0);


        ivAllPizza = new ImageView(this);
        ivAllPizza.setImageResource(R.drawable.all_pizza_uncheck);
        ivAllPizzaParams = new RelativeLayout.LayoutParams(edditlayout_button_size,edditlayout_button_size);
        ivAllPizzaParams.addRule(RelativeLayout.CENTER_VERTICAL);
        ivAllPizzaParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ivAllPizzaParams.setMargins(0,0,edditlayout_button_margin,0);

        ivRightHalf = new ImageView(this);
        ivRightHalf.setImageResource(R.drawable.right_half_uncheck);
        ivRightHalfParams = new RelativeLayout.LayoutParams(edditlayout_button_size,edditlayout_button_size);
        ivRightHalfParams.addRule(RelativeLayout.CENTER_VERTICAL);
        ivRightHalfParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        ivLefttHalf = new ImageView(this);
        ivLefttHalf.setImageResource(R.drawable.left_half_uncheck);
        ivLeftHalfParams = new RelativeLayout.LayoutParams(edditlayout_button_size,edditlayout_button_size);
        ivLeftHalfParams.addRule(RelativeLayout.CENTER_VERTICAL);
        ivLeftHalfParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        ivLeftHalfParams.setMargins(edditlayout_button_margin,0,0,0);

        editLayout.addView(ivAllPizza,ivAllPizzaParams);
        editLayout.addView(ivRightHalf,ivRightHalfParams);
        editLayout.addView(ivLefttHalf,ivLeftHalfParams);

        mainLayout.addView(tvInstructions,tvInstructionsParams);
        mainLayout.addView(editLayout,editLayoutParams);


        ivAllPizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allPizza){
                    allPizza = false;
                    ivAllPizza.setImageResource(R.drawable.all_pizza_uncheck);
                    for (int i=0;i<4;i++) {
                        topping.changeQuater(i, false);
                        ivQuaters[i].setImageResource(resourcesStrokeOnly[i]);
                        onChangeQuater();
                    }
                }

                else {
                    allPizza =true;
                    ivAllPizza.setImageResource(R.drawable.all_pizza_check);
                    for (int i=0;i<4;i++){
                        topping.changeQuater(i,true);
                        ivQuaters[i].setImageResource(resourcesFullRed[i]);
                        onChangeQuater();
                    }
                }

            }
        });

        ivRightHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rightHalf){
                    rightHalf = false;
                    ivRightHalf.setImageResource(R.drawable.right_half_uncheck);
                    topping.changeQuater(0,false);
                    topping.changeQuater(2,false);

                    ivQuaters[0].setImageResource(resourcesStrokeOnly[0]);
                    ivQuaters[2].setImageResource(resourcesStrokeOnly[2]);
                    onChangeQuater();
                }
                else {
                    rightHalf = true;
                    ivRightHalf.setImageResource(R.drawable.right_half_check);

                    topping.changeQuater(0,true);
                    topping.changeQuater(2,true);

                    topping.changeQuater(1,false);
                    topping.changeQuater(3,false);

                    ivQuaters[0].setImageResource(resourcesFullRed[0]);
                    ivQuaters[2].setImageResource(resourcesFullRed[2]);

                    ivQuaters[1].setImageResource(resourcesStrokeOnly[1]);
                    ivQuaters[3].setImageResource(resourcesStrokeOnly[3]);
                    onChangeQuater();
                }

            }
        });

        ivLefttHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (leftHalf){
                    leftHalf = false;
                    ivLefttHalf.setImageResource(R.drawable.left_half_uncheck);
                    topping.changeQuater(1,false);
                    topping.changeQuater(3,false);

                    ivQuaters[1].setImageResource(resourcesStrokeOnly[1]);
                    ivQuaters[3].setImageResource(resourcesStrokeOnly[3]);
                    onChangeQuater();
                }
                else {
                    leftHalf = true;
                    ivLefttHalf.setImageResource(R.drawable.left_half_check);

                    topping.changeQuater(1,true);
                    topping.changeQuater(3,true);

                    topping.changeQuater(0,false);
                    topping.changeQuater(2,false);

                    ivQuaters[1].setImageResource(resourcesFullRed[1]);
                    ivQuaters[3].setImageResource(resourcesFullRed[3]);

                    ivQuaters[0].setImageResource(resourcesStrokeOnly[0]);
                    ivQuaters[2].setImageResource(resourcesStrokeOnly[2]);
                    onChangeQuater();
                }

            }
        });
    }

    public void buildExitButton(){
        fabExit = new FloatingActionButton(this);
        fabExit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#c0232c")));
        fabExit.setImageResource(R.drawable.ic_done_white_48dp);
        fabExitParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fabExitParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        fabExitParams.addRule(RelativeLayout.BELOW,editLayout.getId());
        fabExitParams.setMargins(fab_margins,fab_margins,0,fab_margins);

        fabExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        mainLayout.addView(fabExit,fabExitParams);
    }

    public void onChangeQuater(){
        int count=0;
        for (int i=0;i<4;i++){
            if (topping.getQuarterAt(i))
                count++;
        }

        if (count==4){
            allPizza = true;
            rightHalf = false;
            leftHalf = false;
            ivLefttHalf.setImageResource(R.drawable.left_half_uncheck);
            ivRightHalf.setImageResource(R.drawable.right_half_uncheck);
            ivAllPizza.setImageResource(R.drawable.all_pizza_check);
        }
        else if (count == 2){
            if (topping.getQuarterAt(0) && topping.getQuarterAt(2) ){
                allPizza = false;
                rightHalf = true;
                leftHalf = false;

                ivLefttHalf.setImageResource(R.drawable.left_half_uncheck);
                ivRightHalf.setImageResource(R.drawable.right_half_check);
                ivAllPizza.setImageResource(R.drawable.all_pizza_uncheck);
            }

            else if (topping.getQuarterAt(1) && topping.getQuarterAt(3)){
                allPizza = false;
                rightHalf = false;
                leftHalf = true;
                ivLefttHalf.setImageResource(R.drawable.left_half_check);
                ivRightHalf.setImageResource(R.drawable.right_half_uncheck);
                ivAllPizza.setImageResource(R.drawable.all_pizza_uncheck);
            }

        }
        else {
            allPizza = false;
            rightHalf = false;
            leftHalf = false;
            ivLefttHalf.setImageResource(R.drawable.left_half_uncheck);
            ivRightHalf.setImageResource(R.drawable.right_half_uncheck);
            ivAllPizza.setImageResource(R.drawable.all_pizza_uncheck);
        }


    }

    @Override
    protected void onStop() {
        EditPizza.changeTopping();
        editPizza.onFinishEditTopping(index);
        super.onStop();
    }

    public static int convertSpToPixels(float sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
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

}
