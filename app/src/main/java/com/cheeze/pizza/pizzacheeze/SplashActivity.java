package com.cheeze.pizza.pizzacheeze;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.cheeze.pizza.pizzacheeze.types.AppSettings;
import com.cheeze.pizza.pizzacheeze.types.Discount;
import com.cheeze.pizza.pizzacheeze.types.MyWheelItem;
import com.cheeze.pizza.pizzacheeze.types.Order;
import com.cheeze.pizza.pizzacheeze.types.Pasta;
import com.cheeze.pizza.pizzacheeze.types.Pic;
import com.cheeze.pizza.pizzacheeze.types.PicList;
import com.cheeze.pizza.pizzacheeze.types.Pizza;
import com.cheeze.pizza.pizzacheeze.types.Product;
import com.cheeze.pizza.pizzacheeze.types.SpecialProductLists;
import com.cheeze.pizza.pizzacheeze.types.ToppingProduct;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.itangqi.waveloadingview.WaveLoadingView;


public class SplashActivity extends AppCompatActivity {
    // the order
    public static Order order = new Order();

    // data from shared prefs
    public static Order lastOrder = new Order();
    public static boolean needToUpdate = false;
    private PicList picList;

    // data from firebase
    public static AppSettings myAppSettings;
    public static ArrayList<Product> productArrayList = new ArrayList<>();
    public static ArrayList<Discount> discountArrayList = new ArrayList<>();
    public static ArrayList<MyWheelItem> myWheelItems = new ArrayList<>();
    private int dbStorageVersion;

    // the discounts
    public static boolean onePlusActive;
    public static boolean oneFreeTop;

    // not good but necessary
    public static Context thisContext;

    // state-checkers
    public static boolean firstTime = true;
    public static boolean firstTimeHatavot = true;
    public static boolean firstTimeMivtzaim = true;
    public static boolean firstTimeLastOrder = true;
    private boolean openActivity = true;


    SharedPreferences sp, lastOrderSp, specialProductsSp;

    DatabaseReference databaseProducts, appSettings, databaseDiscounts, databaseWheelItems;
    StorageReference picStorage;

    // things for the wave load
    int maxValue;
    int correctValue;
    WaveLoadingView waveLoadingView;


    boolean needToDownloadPics = true;

    boolean startedDownload = false;


    //todo************** false before release*************************
    public static final boolean TEST_MODE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adjustFontScale(getResources().getConfiguration());
        FirebaseApp.initializeApp(this);
        databaseWheelItems = FirebaseDatabase.getInstance().getReference("WheelItems");
        databaseDiscounts = FirebaseDatabase.getInstance().getReference("Discounts");
        databaseProducts = FirebaseDatabase.getInstance().getReference("Products");
        picStorage = FirebaseStorage.getInstance().getReference("pictures");
        thisContext = getBaseContext();

        downloadAppSettings();

        getLastOrder();

        setEntries();

        buildLayout();

        downloadWheelItems();

        downloadDiscounts();

        getPicListFromSP();
    }

    private void downloadWheelItems() {
        databaseWheelItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myWheelItems.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    myWheelItems.add(snapshot.getValue(MyWheelItem.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void downloadDiscounts() {
        databaseDiscounts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    discountArrayList.add(snapshot.getValue(Discount.class));
                    if (discountArrayList.get(discountArrayList.size() - 1).getId().equals("toppingsDiscount") && discountArrayList.get(discountArrayList.size() - 1).isActive())
                        onePlusActive = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getPicListFromSP() {
        sp = getSharedPreferences("PicList", MODE_PRIVATE);
        String jsonPicList = sp.getString("pics", "");
        Gson gson = new Gson();
        picList = gson.fromJson(jsonPicList, PicList.class);
        if (picList == null)
            picList = new PicList(0);

        DatabaseReference dbStorageVersionRef = FirebaseDatabase.getInstance().getReference("versions").child("storageVersion");
        dbStorageVersionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dbStorageVersion = dataSnapshot.getValue(Integer.class);
                if (picList.getVersion() == dbStorageVersion)
                    needToDownloadPics = false;

                Log.d("MyCusTag", "need to load: "+ needToDownloadPics + " open activity: "+ openActivity);
                if(!startedDownload) {
                    downloadProducts();
                    startedDownload = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void downloadProducts(){
        databaseProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productArrayList.clear();
                maxValue = (int) dataSnapshot.getChildrenCount();

                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product p;
                    final Product dbProduct = productSnapshot.getValue(Product.class);
                    if (dbProduct != null && dbProduct.getType().equals("פיצות")) {
                        p = new Pizza(dbProduct);
                    } else if (dbProduct != null && (dbProduct.getName().equals("זיוה") || dbProduct.getName().equals("סמבוסק") || dbProduct.getName().equals("טוסט"))) {
                        p = new ToppingProduct(dbProduct);
                    } else if (dbProduct != null && dbProduct.getType().equals("פסטות")) {
                        p = new Pasta(dbProduct);
                    } else
                        p = new Product(dbProduct);
                    final Product product = p;

                    if (needToDownloadPics) {
                        File localFile = null;
                        try {
                            localFile = File.createTempFile(product.getPictureId(), "png");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        final File finalLocalFile = localFile;
                        picStorage.child(product.getPictureId()).getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                            product.setStringPicUri(Uri.fromFile(finalLocalFile));

                            correctValue++;

                            waveLoadingView.setProgressValue((correctValue * 100 / (maxValue)));

                            if (correctValue == maxValue)
                                startNextAct();

                        }).addOnFailureListener(exception -> {
                        });
                    } else {
                        product.setStringPicUri(picList.getPicUri(product.getPictureId()));
                        correctValue++;

                        waveLoadingView.setProgressValue((correctValue * 100 / (maxValue)));
                        Log.d("MyCusTag", "progress: "+ correctValue + " max: "+ maxValue);
                        if (correctValue == maxValue)
                            startNextAct();
                    }

                    productArrayList.add(product);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void startNextAct() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        if (openActivity) {
            // saves the pic data in the shared pref
            if(needToDownloadPics)
                savePicListInSP();

            startActivity(intent);
            finish();
            openActivity = false;
        }
    }

    public void savePicListInSP(){
        picList.setPics(getPicsFromProducts(productArrayList));
        picList.setVersion(dbStorageVersion);
        sp = getSharedPreferences("PicList", MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonPicList = gson.toJson(picList);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("pics", jsonPicList);
        editor.apply();
    }

    private List<Pic> getPicsFromProducts(ArrayList<Product> productArrayList) {
        List<Pic>pics = new ArrayList<>();
        for (Product p: productArrayList){
            Pic pic = new Pic(p.getPicUri().toString(),p.getPictureId());
            pics.add(pic);
        }
        return pics;
    }

    public void downloadAppSettings() {
        appSettings = FirebaseDatabase.getInstance().getReference("AppSettings");
        appSettings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myAppSettings = dataSnapshot.getValue(AppSettings.class);
                setUpdateRequirement();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                myAppSettings = new AppSettings(getString(R.string.defaultReciverMail), false, false, "1", 5, 0);
            }
        });

    }

    //sets up the previous order
    public void getLastOrder() {
        String lastOrderJson, specialProductsJson;
        SpecialProductLists spl;
        Gson gson = new Gson();
        specialProductsSp = getSharedPreferences("specialProducts", MODE_PRIVATE);
        lastOrderSp = getSharedPreferences("lastOrder", MODE_PRIVATE);

        specialProductsJson = specialProductsSp.getString("specialProductsJson", "no");
        lastOrderJson = (lastOrderSp.getString("orderJson", "no"));
        if (!lastOrderJson.equals("no")) {//If there was a previous order, set it
            lastOrder = gson.fromJson(lastOrderJson, Order.class);

            //if the previous order was a shipment, remove the last product, which is the "shipment" product
            if (lastOrder.isShipment())
                lastOrder.getCart().removeProduct(lastOrder.getCart().getSelectedProducts().get(lastOrder.getCart().getSelectedProducts().size() - 1));

            //if there was specialProducts, remove the <Product> specialProduct and add the <T extends Product> specialProduct
            if (!specialProductsJson.equals("no")) {
                spl = gson.fromJson(specialProductsJson, SpecialProductLists.class);
                ArrayList<Product> removeProducts = new ArrayList<>();
                for (Product product : lastOrder.getCart().getSelectedProducts())
                    if (product.getType().equals("פיצות") || product.getType().equals("פסטות") || product.getName().equals("זיוה") || product.getName().equals("טוסט") || product.getName().equals("סמבוסק"))
                        removeProducts.add(product);

                lastOrder.getCart().getSelectedProducts().removeAll(removeProducts);

                for (Pizza pizza : spl.getPizzas())
                    lastOrder.getCart().getSelectedProducts().add(pizza);
                for (Pasta pasta : spl.getPastas())
                    lastOrder.getCart().getSelectedProducts().add(pasta);
                for (ToppingProduct toppingProduct : spl.getToppingProducts())
                    lastOrder.getCart().getSelectedProducts().add(toppingProduct);

                lastOrder.getCart().updatePrice();
            }
        }
    }

    private void setUpdateRequirement() {
        PackageInfo pinfo = null;
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int thisVersionCode = pinfo.versionCode;

        needToUpdate = thisVersionCode < myAppSettings.getAppVersionCode();


    }

    private void setEntries() {
        sp = getSharedPreferences("details", MODE_PRIVATE);
        int entries = sp.getInt("entries", 0);
        if (entries > 2)
            order.setSpecialCostumer(true);
        order.setEntries(entries);
    }

    private void buildLayout() {
        setContentView(R.layout.activity_splash);
        final Display display = this.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();

        waveLoadingView = findViewById(R.id.waveLoadingView);
        waveLoadingView.getLayoutParams().height = width;
        waveLoadingView.setProgressValue(0);
        correctValue = 0;

        waveLoadingView.setWaveColor(ContextCompat.getColor(this, R.color.colorAccent));
        waveLoadingView.setAnimDuration(4000);

        (findViewById(R.id.splashScreenImagePizza)).getLayoutParams().height = width;
        (findViewById(R.id.splashScreenImagePizza)).getLayoutParams().width = width;
    }

    public static Product getProductByName(String prodName) {
        for (Product p : productArrayList)
            if (p.getName().equals(prodName))
                return p;
        return null;
    }

    //bitmap things
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    //fixes teacher text size bug
    public void adjustFontScale(Configuration configuration) {
        if (configuration.fontScale > 1) {
            configuration.fontScale = (float) 1;
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            metrics.scaledDensity = configuration.fontScale * metrics.density;
            getBaseContext().getResources().updateConfiguration(configuration, metrics);
        }
    }
}





