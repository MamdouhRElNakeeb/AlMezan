package me.nakeeb.almezan.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import me.nakeeb.almezan.R;
import me.nakeeb.almezan.helper.Utils;

/**
 * Created by mamdouhelnakeeb on 12/15/17.
 */

public class ZekrStats extends AppCompatActivity {


    TextView doaa0TV, doaa1TV, doaa2TV, doaa3TV, doaa4TV, doaa5TV, doaa6TV, doaa7TV, doaa8TV;

    FirebaseAuth mAuth;

    int daysNo = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zekr_stats_activity);

        long currentMillis = System.currentTimeMillis();

        daysNo = Utils.getDaysMonthsYearsNo(currentMillis, getSharedPreferences("User", MODE_PRIVATE).getLong("startTime", currentMillis)).get(0);

        Log.d("currentMilllis", String.valueOf(currentMillis));
        Log.d("oldtMilllis", String.valueOf(getSharedPreferences("User", MODE_PRIVATE).getLong("startTime", currentMillis)));
        if (daysNo == 0)
            daysNo = 1;
        Log.d("daysNoTotal", String.valueOf(daysNo));

        mAuth = FirebaseAuth.getInstance();

        initViews();

        initNav();

        getZekrStats();

        loadADs();
    }

    private void getZekrStats(){

        final FirebaseFirestore db = FirebaseFirestore.getInstance();


        final CollectionReference docRef = db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("zekr");

        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot document : task.getResult()) {
                        Log.d("PrayerData: ", document.getId() + " => " + document.getData());

                        int countPerDay = (int) (Float.parseFloat(document.get("count").toString()) / (float) daysNo);

                        switch (Integer.parseInt(document.getId())){

                            case 0:
                                doaa0TV.setText(String.valueOf(countPerDay));
                                break;
                            case 1:
                                doaa1TV.setText(String.valueOf(countPerDay));
                                break;
                            case 2:
                                doaa2TV.setText(String.valueOf(countPerDay));
                                break;
                            case 3:
                                doaa3TV.setText(String.valueOf(countPerDay));
                                break;
                            case 4:
                                doaa4TV.setText(String.valueOf(countPerDay));
                                break;
                            case 5:
                                doaa5TV.setText(String.valueOf(countPerDay));
                                break;
                            case 6:
                                doaa6TV.setText(String.valueOf(countPerDay));
                                break;
                            case 7:
                                doaa7TV.setText(String.valueOf(countPerDay));
                                break;
                            case 8:
                                doaa8TV.setText(String.valueOf(countPerDay));
                                break;
                        }

                    }

                } else {
                    Log.d("Error: ", "Error getting documents: ", task.getException());
                }
            }
        });

    }

    private void initViews(){

        doaa0TV = findViewById(R.id.doaa0StatsTV);
        doaa1TV = findViewById(R.id.doaa1StatsTV);
        doaa2TV = findViewById(R.id.doaa2StatsTV);
        doaa3TV = findViewById(R.id.doaa3StatsTV);
        doaa4TV = findViewById(R.id.doaa4StatsTV);
        doaa5TV = findViewById(R.id.doaa5StatsTV);
        doaa6TV = findViewById(R.id.doaa6StatsTV);
        doaa7TV = findViewById(R.id.doaa7StatsTV);
        doaa8TV = findViewById(R.id.doaa8StatsTV);

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
                        finish();
                    }
                });

        ((LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.settingsBtnLL))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

        ((LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.logoutLL))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAuth.signOut();
                        startActivity(new Intent(ZekrStats.this, Login.class));
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
