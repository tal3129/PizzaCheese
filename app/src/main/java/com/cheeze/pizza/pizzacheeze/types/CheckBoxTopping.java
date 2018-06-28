package com.cheeze.pizza.pizzacheeze.types;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import com.cheeze.pizza.pizzacheeze.R;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by student on 07/11/2017.
 */

public class CheckBoxTopping {

    private Topping topping;
    private CheckBox checkBox;

    public CheckBoxTopping(Topping t,Context context){
        topping = new Topping(t.getName(),t.getPicUri().toString());
        checkBox = (CheckBox)((LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.check_right_checkbox,null);
        checkBox.setText(topping.getName());
    }


    public CheckBox getCheckBox() {
        return checkBox;
    }

    public Topping getTopping() {
        return topping;
    }
}
