package com.james.stockparser.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.james.stockparser.R;
import com.james.stockparser.util.IabHelper;
import com.james.stockparser.util.IabResult;
import com.james.stockparser.util.Inventory;
import com.james.stockparser.util.Purchase;
import com.james.stockparser.util.upNewData;

public class FragmentAbout extends AppCompatActivity {
    private static Toolbar mToolbar;
    private static ViewPager viewPager;
    private static TabLayout tabLayout;
    String TAG = FragmentAbout.class.getSimpleName();
    TextView tvContener;
    Button btn_evalution, btn_attention, btn_share, btn_feedback;
    IabHelper mHelper;
    ImageView iv_aboutLog;
    boolean mIsPremium = false;
    static final int RC_REQUEST = 10001;
    static final String SKU_PREMIUM = "master_power";
    static final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtnB5b/JRahdkri2hQI5U2poHfioefrhZA5cgd7Znuddj4DIPuVSgLm33oTTGVanIRsfi5lomgVvOltNDSnSu6gcfHcG4qRjK3CLNLlwSCUXeOBt/OotXkghufRgYY8JAq+8iz4e4/RV/rba3z778u/B973q/XUQPVBmNifGBVqHIgkHLPlcZE80kQpxXALjKFF4EiCDv1PDKrTU4fhJzEt5mGHVv6qUYppj9TVHH4a5XhANH0DSHPHCvJeXHEC8tHmzE1NNHDuxjKdfVsKhTBxAEXK2wWnLi/uTaNREGfojCEu8YW5flcA3Dn4sH4DNNRaQyLbXUzowsqmH0DW7yOwIDAQAB";

    //about_logo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_about);
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.enableDebugLogging(true);
        iv_aboutLog = (ImageView) findViewById(R.id.about_logo);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.e(TAG, "Setup finished.");
                if (!result.isSuccess()) {
                    Log.e(TAG, "Problem setting up in-app billing: " + result);
                    return;
                }
                if (mHelper == null) return;
                Log.e(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
        btn_evalution = (Button) findViewById(R.id.btn_eva);
        btn_attention = (Button) findViewById(R.id.btn_attent);
        btn_share = (Button) findViewById(R.id.btn_share);
        btn_feedback = (Button) findViewById(R.id.btn_feedback);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvContener = (TextView) findViewById(R.id.tvContent);
        tvContener.setMovementMethod(new ScrollingMovementMethod());
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_48dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvContener.setText(getString(R.string.stockFYI));
            }
        });
        btn_evalution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDL = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.james.stockparser"));
                startActivity(intentDL);
            }
        });
        btn_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payload = "";
                mHelper.launchPurchaseFlow(FragmentAbout.this, SKU_PREMIUM, RC_REQUEST, mPurchaseFinishedListener, payload);
            }
        });
        iv_aboutLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FragmentAbout.this,upNewData.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.e(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
            if (mHelper == null) return;
            if (purchase.getSku().equals(SKU_PREMIUM)) {
                Log.e(TAG, "Update Super User");
                //alert("Thank you for upgrading to premium!");
                //mIsPremium = true;
                //updateUi();
                //setWaitScreen(false);
            }
        }
    };
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
            if (mHelper == null) return;
            if (result.isFailure()) {
                Log.e(TAG, "Failed to query inventory: " + result);
                return;
            }
            Log.e(TAG, "Query inventory was successful.");
            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            Log.e(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
            Log.e(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        return true;
    }

}
