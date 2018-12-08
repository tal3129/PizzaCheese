package com.cheeze.pizza.pizzacheeze;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cheeze.pizza.pizzacheeze.MailSender.GMailSender;
import com.cheeze.pizza.pizzacheeze.MailSender.OutlookSender;
import com.cheeze.pizza.pizzacheeze.types.Discount;
import com.cheeze.pizza.pizzacheeze.types.Order;
import com.cheeze.pizza.pizzacheeze.types.Pasta;
import com.cheeze.pizza.pizzacheeze.types.Pizza;
import com.cheeze.pizza.pizzacheeze.types.Product;
import com.cheeze.pizza.pizzacheeze.types.SpecialProductLists;
import com.cheeze.pizza.pizzacheeze.types.ToppingProduct;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class MainActivity extends AppCompatActivity {
    final int RC_LUCKY = 1758;
    final int RC_ON_CLOSE_HATAVOT = 321;
    final int RC_ON_CLOSE_MIVTZAIM = 320;

    public static final int SECRET_CLICK_AMOUNT = 7;
    public static int secretCounter = 0;

    public static boolean needToLoad = false;

    RelativeLayout mainLayout; // main layout
    RelativeLayout.LayoutParams mainLayoutParams;

    RelativeLayout imageLayout; // top layout with pizza image
    RelativeLayout.LayoutParams imageLayoutParams;

    ImageView bigPizzaImage; // pizza image on top layout
    RelativeLayout.LayoutParams bigPizzaImageParams;

    ImageView ivHomeScreenLogo; // center logo
    RelativeLayout.LayoutParams ivHomeScreenLogoParams;

    FloatingActionButton ivCartIcon;
    RelativeLayout.LayoutParams ivCartIconParams;

    RelativeLayout bottomLayout; // bottom layout
    RelativeLayout.LayoutParams bottomLayoutParams;

    RelativeLayout buttonsLayout; // layout with buttons
    RelativeLayout.LayoutParams buttonsLayoutParams;

    ImageView ivOrderButton;
    PulsatorLayout.LayoutParams ivOrderButtonParams;

    PulsatorLayout orderButtonLayout;
    RelativeLayout.LayoutParams orderButtonLayotParams;

    ImageView ivInfoButton;
    RelativeLayout.LayoutParams ivInfoButtonParams;

    LoadingDialog loadingDialog;


    int width;
    int height;
    int center_logo_size;
    int image_layout_height;
    int bottom_layout_height;
    int space_between_buttons;
    int cart_icon_size;
    int buttons_size;
    int order_button_size;
    int buttons_layout_width;
    private static boolean loadedLast;
    public static boolean finished;
    private SharedPreferences sp, lastOrderSp, specialProductsSp;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        lastOrderSp = getSharedPreferences("lastOrder", MODE_PRIVATE);
        sp = getSharedPreferences("details", MODE_PRIVATE);
        specialProductsSp = getSharedPreferences("specialProducts", MODE_PRIVATE);

        //hide action bar
        this.getSupportActionBar().hide(); // hide the action bar
        //finish
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); // set opacity of status bar

        //get screen size
        final Display display = this.getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        //finish

        mainLayout = new RelativeLayout(this);
        mainLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        setSizes();

        buildImageLayout();

        buildButtonsLayout();

        buildCenterLogo();

        setContentView(mainLayout);

        if (SplashActivity.order.isSpecialCostumer() & SplashActivity.firstTime)
            Toast.makeText(this, "זוהה לקוח פרימיום, ינתן טיפול מיוחד", Toast.LENGTH_LONG).show();

        ArrayList<Discount> discounts = new ArrayList<>();
        for (Discount discount : SplashActivity.discountArrayList)
            if (discount.isActive())
                discounts.add(discount);

        if (discounts.size() != 0 && SplashActivity.firstTimeMivtzaim) {
            SplashActivity.firstTimeMivtzaim = false;
            Intent intent = new Intent(MainActivity.this, MivtzaimBanner.class);
            startActivityForResult(intent, RC_ON_CLOSE_MIVTZAIM);
        } else {
            loadHatavotOrLastOrder();
        }
        //if there was a previous order, ask the user if he wants to set it


        loadingDialog = new LoadingDialog(this);
        loadingDialog.setCancelable(false);

        if (finished) {
            loadingDialog.show();
        }

        SplashActivity.firstTime = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // if we are in test mode, don't send mails

        if (finished) {
            if (SplashActivity.TEST_MODE)
                spinNsave();
            else {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        String order = SplashActivity.order.toString();
                        boolean successes = sendMail(order);
                        if (!successes) {
                            boolean successes1 = sendBackupMail(order);
                            if (!successes1) {
                                runOnUiThread(() -> {
                                    loadingDialog.cancel();
                                    AlertDialog.Builder errorDialog = new AlertDialog.Builder(MainActivity.this).setTitle("אירעה שגיאה.").setMessage("אירעה שגיאה בשליחת הזמנך, אנא נסה שנית מאוחר יותר.")
                                            .setPositiveButton("אישור", (dialogInterface, i) -> dialogInterface.cancel());
                                    errorDialog.show();
                                });
                            } else {
                                spinNsave();
                            }
                        } else {
                            spinNsave();
                        }
                    }
                };
                thread.start();
            }
        }
    }

    private void openHatavot() {
        SplashActivity.order.setFreeProducts(SplashActivity.lastOrder.getFreeProducts());
        Intent intent = new Intent(MainActivity.this, FreeProductsBanner.class);
        startActivityForResult(intent, RC_ON_CLOSE_HATAVOT);
    }

    private void openLoadLastOrder() {
        if (needToLoad) {
            try {
                setLoadLastOrderDialog();
            } catch (Exception e) {
                Toast.makeText(this, "ארעה שגיאה בטעינת ההזמנה האחרונה", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setLoadLastOrderDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.setlastorder_dialog);
        dialog.findViewById(R.id.btnNope).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.btnYep).setOnClickListener(v -> {
            SplashActivity.order.addToCartWithMainPrice(SplashActivity.lastOrder.getCart());
            Toast.makeText(MainActivity.this, "נטען סל הקניות הקודם...", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            Intent intent = new Intent(MainActivity.this, ShoppingCart.class);
            startActivity(intent);
        });
        dialog.setCancelable(true);
        dialog.show();
        loadedLast = true;
    }

    public void setSizes() {
        image_layout_height = (int) (0.66 * height);
        center_logo_size = (int) (width / 1.8);
        bottom_layout_height = height - (int) (0.9 * image_layout_height);
        space_between_buttons = (int) ((bottom_layout_height - 1.54 * center_logo_size) / 2.5);
        cart_icon_size = converDPtoPX(54);
        buttons_size = (int) (1.65 * (center_logo_size / 4));
        order_button_size = (int) (buttons_size * 1.23);
        buttons_layout_width = (int) (buttons_size * 2.5);
    }

    public void buildImageLayout() {
        imageLayout = new RelativeLayout(this);
        imageLayout.setId(View.generateViewId());
        imageLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, image_layout_height);


        bigPizzaImage = new ImageView(this);
        bigPizzaImage.setId(View.generateViewId());
        bigPizzaImage.setImageResource(R.drawable.main_big_pizza);
        bigPizzaImage.setScaleType(ImageView.ScaleType.FIT_XY);
        bigPizzaImageParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        imageLayout.addView(bigPizzaImage, bigPizzaImageParams);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bigPizzaImage.setTransitionName("actionbar");
        }


        mainLayout.addView(imageLayout, imageLayoutParams);
    }

    public void buildButtonsLayout() {
        //set the bottom layout
        bottomLayout = new RelativeLayout(this);
        bottomLayout.setId(View.generateViewId());
        bottomLayout.setBackgroundResource(R.drawable.buttons_layout_circle_background);

        bottomLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, bottom_layout_height);
        bottomLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        //finish


        //set the buttons layout
        buttonsLayout = new RelativeLayout(this);
        buttonsLayoutParams = new RelativeLayout.LayoutParams(buttons_layout_width, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonsLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        buttonsLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        //finish

        //set order button layout
        orderButtonLayout = new PulsatorLayout(this);
        orderButtonLayout.setId(View.generateViewId());
        orderButtonLayout.setCount(2);
        orderButtonLayout.setDuration(3830);
        orderButtonLayout.setColor(Color.WHITE);
        orderButtonLayout.start();

        orderButtonLayotParams = new RelativeLayout.LayoutParams(order_button_size, order_button_size);
        orderButtonLayotParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        //finish

        //set order button
        ivOrderButton = new ImageView(this);
        ivOrderButton.setScaleType(ImageView.ScaleType.FIT_XY);
        ivOrderButton.setImageResource(R.drawable.order_button);
        ivOrderButton.setId(View.generateViewId());

        ivOrderButtonParams = new RelativeLayout.LayoutParams(order_button_size, order_button_size);
        //finish

        //set info button
        ivInfoButton = new ImageView(this);
        ivInfoButton.setImageResource(R.drawable.new_info_button);
        ivInfoButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Info.class);
            startActivity(intent);
        });

        ivInfoButtonParams = new RelativeLayout.LayoutParams(buttons_size, buttons_size);
        ivInfoButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ivInfoButtonParams.addRule(RelativeLayout.ALIGN_TOP, orderButtonLayout.getId());
        ivInfoButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, orderButtonLayout.getId());
        ivInfoButtonParams.addRule(RelativeLayout.CENTER_VERTICAL);
        //finish

        orderButtonLayout.addView(ivOrderButton, ivOrderButtonParams);
        buttonsLayout.addView(orderButtonLayout, orderButtonLayotParams);
        buttonsLayout.addView(ivInfoButton, ivInfoButtonParams);

        ivOrderButton.setOnClickListener(v -> {
            // we can open the outdated app in test mode
            boolean developer = SplashActivity.TEST_MODE || secretCounter == -1;

            if (SplashActivity.needToUpdate && !developer) {
                Toast.makeText(MainActivity.this, R.string.plsUpdate, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.cheeze.pizza.pizzacheeze"));
                startActivity(intent);
            } else if (!SplashActivity.myAppSettings.isAppStatus() && !developer) {
                Toast.makeText(MainActivity.this, R.string.appInactive, Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, ChooseType.class);
                finish();
                startActivity(intent);
            }
        });


        bottomLayout.addView(buttonsLayout, buttonsLayoutParams);
        mainLayout.addView(bottomLayout, bottomLayoutParams);

    }

    public void buildCenterLogo() {
        ivHomeScreenLogo = new ImageView(this);
        ivHomeScreenLogo.setImageResource(R.drawable.home_screen_logo_transporment);
        ivHomeScreenLogo.setId(View.generateViewId());
        ivHomeScreenLogo.setOnClickListener(v -> secretCounter++);


        ivHomeScreenLogoParams = new RelativeLayout.LayoutParams(center_logo_size, center_logo_size);
        ivHomeScreenLogoParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        ivHomeScreenLogoParams.addRule(RelativeLayout.CENTER_VERTICAL);

        ivCartIcon = new FloatingActionButton(this);
        ivCartIcon.setId(View.generateViewId());
        ivCartIcon.setImageResource(R.drawable.cart_icon_circle);
        ivCartIcon.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        ivCartIcon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShoppingCart.class);
            startActivity(intent);
        });

        ivCartIconParams = new RelativeLayout.LayoutParams(cart_icon_size, cart_icon_size);
        ivCartIconParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        ivCartIconParams.setMargins(0, image_layout_height - cart_icon_size / 2, 0, 0);


        imageLayout.addView(ivHomeScreenLogo, ivHomeScreenLogoParams);
        mainLayout.addView(ivCartIcon, ivCartIconParams);
    }

    public int converDPtoPX(int dp) {
        return dp * (int) this.getResources().getDisplayMetrics().density;
    }

    public void saveThisOrder() {
        //update the entries
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("entries", (SplashActivity.order.getEntries() + 1));
        editor.apply();
        //save this order
        editor = lastOrderSp.edit();
        Gson gson = new Gson();
        String orderJson = gson.toJson(SplashActivity.order);
        editor.putString("orderJson", orderJson);
        editor.apply();
        //save the order's special products
        SpecialProductLists spl = new SpecialProductLists();
        for (Product p : SplashActivity.order.getCart().getSelectedProducts()) {
            if (p instanceof Pizza)
                spl.getPizzas().add((Pizza) p);
            if (p instanceof Pasta)
                spl.getPastas().add((Pasta) p);
            if (p instanceof ToppingProduct)
                spl.getToppingProducts().add((ToppingProduct) p);
        }
        editor = specialProductsSp.edit();
        String pizzaListJson = gson.toJson(spl);
        editor.putString("specialProductsJson", pizzaListJson);
        editor.apply();

        finished = false;
    }

    //sends the mail from PizzaCheeseServer@gmail to the email chosen in the manager app
    public boolean sendMail(String order) {
        final boolean[] successfulMail = {true};
        final String senderMail = getString(R.string.senderMail);
        final String senderPassword = getString(R.string.senderPassword);

        final String receiver = SplashActivity.myAppSettings.getReceiverMail();
        final String extraReceivers = TextUtils.join(",", SplashActivity.myAppSettings.extraMails);

        final String subject = "הזמנה חדשה";
        final String finalMessage = order;


        Thread thread = new Thread(() -> {
            GMailSender sender = new GMailSender(senderMail, senderPassword);
            try {
                sender.sendMail(subject, finalMessage, senderMail, receiver);
            } catch (Exception e) {
                e.printStackTrace();
                successfulMail[0] = false;
            }
        });
        Thread backUpThread = new Thread() {
            @Override
            public void run() {
                GMailSender sender = new GMailSender(senderMail, senderPassword);
                try {
                    sender.sendMail(subject, finalMessage, senderMail, extraReceivers);
                } catch (Exception e) {
                    e.printStackTrace();
                    successfulMail[0] = false;
                }
            }
        };

        thread.start();
        backUpThread.start();


        try {
            thread.join();
            backUpThread.join();
            return successfulMail[0];
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }


    }

    //sends the mail from pizzaCheeseBackupServer@outlook.co.il to the email chosen in the manager app
    public boolean sendBackupMail(String order) {
        final String senderMail = "pizzaCheeseBackupServer@outlook.co.il";
        final String senderPassword = "Leno3129";

        final String receiver = SplashActivity.myAppSettings.getReceiverMail();
        final String subject = "הזמנה חדשה";
        final String finalMessage = order;
        OutlookSender outlookSender = new OutlookSender(senderMail, senderPassword);

        final String extraReceivers = TextUtils.join(",", SplashActivity.myAppSettings.extraMails);

        outlookSender.sendMail(subject, finalMessage, extraReceivers);
        return outlookSender.sendMail(subject, finalMessage, receiver);
    }

    private void saveAndGoodbye() {
        saveThisOrder();
        AlertDialog.Builder ad = new AlertDialog.Builder(this)
                .setMessage("ההזמנה התקבלה בהצלחה ותהיה " + (SplashActivity.order.isShipment() ? "אצלך " : "מוכנה ") + "בזמן שצוין.")
                .setCancelable(false)
                .setPositiveButton(" להתראות!", (dialog, id) -> {
                    alert.dismiss();
                    finishAffinity();
                });
        alert = ad.create();
        alert.show();
        SplashActivity.order = new Order();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_LUCKY && resultCode == RESULT_OK) {
            // lucky was closed
            saveAndGoodbye();
        }
        if (requestCode == RC_ON_CLOSE_MIVTZAIM) {
            // if MIVTZAIM was closed
            loadHatavotOrLastOrder();
        }
        if (requestCode == RC_ON_CLOSE_HATAVOT) {
            // hatavot was closed
            if (SplashActivity.firstTimeLastOrder) {
                SplashActivity.firstTimeLastOrder = false;
                openLoadLastOrder();
            }
        }
    }

    private void loadHatavotOrLastOrder() {
        if (!SplashActivity.lastOrder.getPhoneNumber().equals("") && !loadedLast) {
            needToLoad = true;
            if (SplashActivity.lastOrder.getFreeProducts() != null && SplashActivity.lastOrder.getFreeProducts().size() > 0 && SplashActivity.firstTimeHatavot) {
                // if there are free products that were not shown, show them
                SplashActivity.firstTimeHatavot = false;
                openHatavot();
            } else if (SplashActivity.firstTimeLastOrder) {
                // if there are none, show the last order dialog
                SplashActivity.firstTimeLastOrder = false;
                openLoadLastOrder();
            }
        }
    }

    private void spinNsave() {
        if (!SplashActivity.TEST_MODE)
            sendOrderToFB(SplashActivity.order);
        Intent intent = new Intent(MainActivity.this, LuckyWheel.class);
        startActivityForResult(intent, RC_LUCKY);
        loadingDialog.cancel();
        finished = false;
    }

    private void sendOrderToFB(Order order) {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        order.setId(ordersRef.push().getKey());
        ordersRef.child(order.getId()).setValue(order.toString());
    }
}