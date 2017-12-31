package me.nakeeb.almezan.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;

import me.nakeeb.almezan.R;
import me.nakeeb.almezan.helper.Utils;

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

        initNav();

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

    private void initNav(){

        NavigationView navigationView = findViewById(R.id.navigation_view);


        final DrawerLayout mDrawerLayout = findViewById(R.id.drawer);
        ImageButton sideMenuIB = findViewById(R.id.sideMenuIB);

        sideMenuIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.nameTV))
                .setText(getSharedPreferences("User", MODE_PRIVATE).getString("name", ""));

        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.ageTV))
                .setText(String.valueOf(Utils.calcAge(getSharedPreferences("User", MODE_PRIVATE).getString("dob", "0/0/0"))));

        ((LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.homeBtnLL))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getBaseContext(), Landing.class));
                        finish();
                    }
                });

        ((LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.settingsBtnLL))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getBaseContext(), Settings.class));
                    }
                });

        ((LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.logoutLL))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getBaseContext(), Login.class));
                        finish();
                    }
                });


        ((ImageButton) navigationView.getHeaderView(0).findViewById(R.id.fbIB))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String FACEBOOK_URL = "https://www.facebook.com/MamdouhRElNakeeb";
                        String FACEBOOK_PAGE_ID = "MamdouhRElNakeeb";
                        String facebookUrl = "";
                        PackageManager packageManager = getPackageManager();
                        try {
                            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                            if (versionCode >= 3002850) { //newer versions of fb app
                                facebookUrl = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                            } else { //older versions of fb app
                                facebookUrl = "fb://page/" + FACEBOOK_PAGE_ID;
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            facebookUrl =  FACEBOOK_URL; //normal web url
                        }

                        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                        facebookIntent.setData(Uri.parse(facebookUrl));
                        startActivity(facebookIntent);
                    }
                });

        ((ImageButton) navigationView.getHeaderView(0).findViewById(R.id.twtIB))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = null;
                        try {
                            // get the Twitter app if possible
                            getPackageManager().getPackageInfo("com.twitter.android", 0);
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=mamdouhelnakeeb"));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        } catch (Exception e) {
                            // no Twitter app, revert to browser
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/mamdouhelnakeeb"));
                        }
                        startActivity(intent);
                    }
                });

        ((ImageButton) navigationView.getHeaderView(0).findViewById(R.id.instaIB))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Uri uri = Uri.parse("http://instagram.com/_u/mamdouhrelnakeeb");
                        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                        likeIng.setPackage("com.instagram.android");

                        try {
                            startActivity(likeIng);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://instagram.com/mamdouhrelnakeeb")));
                        }
                    }
                });

    }

    private void loadADs(){

        MobileAds.initialize(this, "ca-app-pub-6430998960222915~3066549688");

        AdView mAdView;

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
