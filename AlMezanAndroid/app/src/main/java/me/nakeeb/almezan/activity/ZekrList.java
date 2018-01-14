package me.nakeeb.almezan.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;

import me.nakeeb.almezan.R;
import me.nakeeb.almezan.helper.Utils;

/**
 * Created by mamdouhelnakeeb on 12/15/17.
 */

public class ZekrList extends BaseActivity implements View.OnClickListener {

    Button doaa0, doaa1, doaa2, doaa3, doaa4, doaa5, doaa6, doaa7, doaa8;

    private InterstitialAd mInterstitialAd;


    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.zekr_add_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        prefs = getSharedPreferences("ADs", MODE_PRIVATE);
        editor = prefs.edit();

        loadPopup();

        doaa0 = findViewById(R.id.doaa0);
        doaa1 = findViewById(R.id.doaa1);
        doaa2 = findViewById(R.id.doaa2);
        doaa3 = findViewById(R.id.doaa3);
        doaa4 = findViewById(R.id.doaa4);
        doaa5 = findViewById(R.id.doaa5);
        doaa6 = findViewById(R.id.doaa6);
        doaa7 = findViewById(R.id.doaa7);
        doaa8 = findViewById(R.id.doaa8);

        doaa0.setOnClickListener(this);
        doaa1.setOnClickListener(this);
        doaa2.setOnClickListener(this);
        doaa3.setOnClickListener(this);
        doaa4.setOnClickListener(this);
        doaa5.setOnClickListener(this);
        doaa6.setOnClickListener(this);
        doaa7.setOnClickListener(this);
        doaa8.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        Intent intent;

        switch (view.getId()){

            case R.id.doaa0:
                intent = new Intent(ZekrList.this, ZekrDetails.class);
                intent.putExtra("doaa", 0);
                startActivity(intent);
                break;

            case R.id.doaa1:
                intent = new Intent(ZekrList.this, ZekrDetails.class);
                intent.putExtra("doaa", 1);
                startActivity(intent);
                break;

            case R.id.doaa2:
                intent = new Intent(ZekrList.this, ZekrDetails.class);
                intent.putExtra("doaa", 2);
                startActivity(intent);
                break;

            case R.id.doaa3:
                intent = new Intent(ZekrList.this, ZekrDetails.class);
                intent.putExtra("doaa", 3);
                startActivity(intent);
                break;

            case R.id.doaa4:
                intent = new Intent(ZekrList.this, ZekrDetails.class);
                intent.putExtra("doaa", 4);
                startActivity(intent);
                break;

            case R.id.doaa5:
                intent = new Intent(ZekrList.this, ZekrDetails.class);
                intent.putExtra("doaa", 5);
                startActivity(intent);
                break;

            case R.id.doaa6:
                intent = new Intent(ZekrList.this, ZekrDetails.class);
                intent.putExtra("doaa", 6);
                startActivity(intent);
                break;

            case R.id.doaa7:
                intent = new Intent(ZekrList.this, ZekrDetails.class);
                intent.putExtra("doaa", 7);
                startActivity(intent);
                break;
            case R.id.doaa8:
                intent = new Intent(ZekrList.this, ZekrDetails.class);
                intent.putExtra("doaa", 8);
                startActivity(intent);
                break;

        }
    }

    private void loadPopup(){

//        test: ca-app-pub-3940256099942544/1033173712

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5330928698678581/7252992079");

        if (!prefs.getBoolean("popup", false)){

            mInterstitialAd.loadAd(new AdRequest.Builder().build());

        }

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.

                editor.putBoolean("popup", true);
                editor.apply();
                showPopup();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                editor.putBoolean("popup", false);
                editor.apply();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.

            }
        });

    }

    private void showPopup(){

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

    }

}
