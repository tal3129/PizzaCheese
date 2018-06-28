package com.cheeze.pizza.pizzacheeze;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.Locale;

public class CreditCardForm extends AppCompatActivity {
    private WebView wvTranz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_form);
        wvTranz=findViewById(R.id.wvTranz);

        final String sum=String.format(Locale.ROOT, "%.2f",(SplashActivity.order.getCart().getSum()+(SplashActivity.order.isShipment()?ShipForm.shipment.getPrice():0)));
        wvTranz.loadUrl("https://direct.tranzila.com/cheese/iframe.php?sum="+sum+" &currency=1&cred_type=1&lang=il&trButtonColor=c0232c");
        wvTranz.getSettings().setJavaScriptEnabled(true);
        wvTranz.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


        wvTranz.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //if the transaction was successful
                if(url.contains("https://direct.tranzila.com/done")){
                    view.stopLoading();
                    //if the response from the credit card company is ok
                    if(url.contains("Response%3D000")) {
                        setResult(RESULT_OK);
                        Toast.makeText(getApplicationContext(), "התשלום עבר בהצלחה!", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "ארעה שגיאה בסליקת הכרטיס" , Toast.LENGTH_SHORT).show();
                    finish();
                    return true;
                }
                //if this is loading the normal Iframe
                else if(url.equals("https://direct.tranzila.com/cheese/iframe.php?sum="+sum+" &currency=1&cred_type=1&lang=il&nologo=1&trButtonColor=c0232c"))
                    return false;
                    //if the user tries to click on random stuff
                else {
                    return true;
                }
            }
        });
    }
}




