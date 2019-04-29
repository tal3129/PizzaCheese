package com.cheeze.pizza.pizzacheeze;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * Created by yuval on 22/10/2017.
 */

public class LoadingDialog extends Dialog {

    ProgressBar progressBar;
    RelativeLayout.LayoutParams params;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        progressBar = new ProgressBar(context);//progress bar
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        this.addContentView(progressBar, params);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }


}
