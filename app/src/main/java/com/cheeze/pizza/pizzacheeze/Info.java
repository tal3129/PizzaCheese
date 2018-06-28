package com.cheeze.pizza.pizzacheeze;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jivimberg.library.AutoResizeTextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Info extends AppCompatActivity implements OnMapReadyCallback {

    int height;
    int width;
    int back_button_size;
    int actionbar_height;
    int back_button_margin_right;

    AutoResizeTextView tvPhone;


    int text_height;
    int text_width;

    public static int easterCount = 0;
    private GoogleMap mMap;
    private RelativeLayout actionBarLayout;
    private com.github.jivimberg.library.AutoResizeTextView tvCp;
    ImageView ivBackButton;
    RelativeLayout.LayoutParams ivBackButtonParams;

    TextView tvActionbar;
    RelativeLayout.LayoutParams tvActionbarParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        this.getSupportActionBar().hide();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tvCp = findViewById(R.id.tvCpright);
        actionBarLayout = findViewById(R.id.titleLayout);
        ivBackButton = findViewById(R.id.backBtn);
        buildActionBar();


        tvPhone = findViewById(R.id.tvPhone);
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:08-971-8176"));
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng sydney = new LatLng(31.900005, 35.013709);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("פיצה צ'יז"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));

    }

    public void buildActionBar() {
        actionBarLayout.setBackgroundResource(R.drawable.choose_order_background);
        //finish

        //set the back button
        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ivBackButton.setId(View.generateViewId());
        ivBackButton.setImageResource(R.drawable.ic_arrow_back_white_48dp);

        final Display display = this.getWindowManager().getDefaultDisplay();
        height = display.getHeight();
        width = display.getWidth();
        actionbar_height = height / 4;
        back_button_size = (int) (actionbar_height / 6);
        back_button_margin_right = width / 30;

        ivBackButtonParams = new RelativeLayout.LayoutParams(back_button_size, back_button_size);
        ivBackButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ivBackButtonParams.setMargins(0, 0, back_button_margin_right, 0);

        ivBackButton.setLayoutParams(ivBackButtonParams);
        //finsih


        int text_height = (height / 4) / 3;
        int text_width = width / 3;

        //set text view
        tvActionbar = new AutoResizeTextView(this);
        tvActionbar.setId(View.generateViewId());
        tvActionbar.setText("מידע");
        tvActionbar.setTextColor(Color.WHITE);
        tvActionbar.setTextSize(1000);
        tvActionbar.setGravity(Gravity.CENTER);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/VarelaRound-Regular.ttf");
        tvActionbar.setTypeface(face);

        tvActionbarParams = new RelativeLayout.LayoutParams(text_width, text_height);
        tvActionbarParams.addRule(RelativeLayout.CENTER_VERTICAL);
        tvActionbarParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        //finish

        actionBarLayout.addView(tvActionbar, tvActionbarParams);


    }

}