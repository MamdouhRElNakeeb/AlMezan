package me.nakeeb.almezan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import me.nakeeb.almezan.R;

/**
 * Created by mamdouhelnakeeb on 12/15/17.
 */

public class ZekrList extends AppCompatActivity implements View.OnClickListener {

    Button doaa0, doaa1, doaa2, doaa3, doaa4, doaa5, doaa6, doaa7, doaa8;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zekr_add_activity);

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

        loadADs();
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

    private void loadADs(){

        MobileAds.initialize(this, "ca-app-pub-6430998960222915~3066549688");

        AdView mAdView;

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
