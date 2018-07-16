package com.cheeze.pizza.pizzacheeze.cardListsAdapters;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cheeze.pizza.pizzacheeze.ChooseProduct;
import com.cheeze.pizza.pizzacheeze.R;
import com.cheeze.pizza.pizzacheeze.SplashActivity;
import com.lb.auto_fit_textview.AutoResizeTextView;


public class ChooseOrderAdapter extends RecyclerView.Adapter<ChooseOrderAdapter.ViewHolder>  {


    public static String[] titleName = {"פיצות" , "פסטות"  , "מאפים" , "שתייה" , "סלטים" , "מנות אחרונות"};
    public static int[] imageId = {R.drawable.pizza_icon_red,R.drawable.spaguetti_icon_red,R.drawable.mafim_icon_red,R.drawable.drink_icon_red,R.drawable.salad_icon_red,R.drawable.desert_icon_red};

    int width;
    int height;

    RecyclerView recyclerView;

    private ChooseOrderAdapter.ViewHolder[] viewHolders;

    @Override
    public void onBindViewHolder(final ChooseOrderAdapter.ViewHolder viewHolder, final int i) {


        viewHolder.imageView.setImageBitmap(SplashActivity.decodeSampledBitmapFromResource(viewHolder.itemView.getResources(), imageId[i], 105, 105));
        viewHolder.position = i;


        viewHolder.autoResizeTextViewParams = (RelativeLayout.LayoutParams) viewHolder.titleText.getLayoutParams();
        viewHolder.autoResizeTextViewParams.height = (width / 10);
        Typeface face = Typeface.createFromAsset(viewHolder.autoResizeTextView.getContext().getAssets(), "fonts/VarelaRound-Regular.ttf");
        viewHolder.autoResizeTextView.setTypeface(face);
        viewHolder.autoResizeTextView.setText(titleName[i]);
        viewHolder.mainLayout.addView(viewHolder.autoResizeTextView, viewHolder.autoResizeTextViewParams);

        viewHolder.mainLayout.removeView(viewHolder.titleText);

        viewHolders[i] = viewHolder;


        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ChooseProduct.class);
            intent.putExtra("titleName", titleName[i]);
            intent.putExtra("image", imageId[i]);

            v.getContext().startActivity(intent);
        });


    }

    public ChooseOrderAdapter(int width, int height, RecyclerView recyclerView) {
        super();
        this.width = width;
        this.height = height;
        this.recyclerView = recyclerView;
        this.viewHolders = new ChooseOrderAdapter.ViewHolder[6];


    }

    @Override
    public ChooseOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_choose_order, viewGroup, false);
        ChooseOrderAdapter.ViewHolder viewHolder = new ChooseOrderAdapter.ViewHolder(v);
        return viewHolder;
    }

    class ViewHolder extends RecyclerView.ViewHolder{


        public int position;
        public ImageView imageView;
        public TextView titleText;
        public RelativeLayout mainLayout;

        public RelativeLayout.LayoutParams autoResizeTextViewParams;
        public AutoResizeTextView autoResizeTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            imageView = itemView.findViewById(R.id.imageView_listview_chooseOrder);
            titleText = itemView.findViewById(R.id.textView_listview_chooseOrder);

            imageView.getLayoutParams().width = width / 10;
            imageView.getLayoutParams().height = width / 10;


            autoResizeTextView = new AutoResizeTextView(itemView.getContext());
            autoResizeTextView.setTextSize(1000);
            autoResizeTextView.setTextColor(Color.parseColor("#d17a80"));



            itemView.getLayoutParams().height = height/8;
        }

    }


    @Override
    public int getItemCount() {
        return titleName.length;
    }
}
