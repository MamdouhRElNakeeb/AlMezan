package me.nakeeb.almezan.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
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

import java.util.ArrayList;
import java.util.Locale;

import me.nakeeb.almezan.R;
import me.nakeeb.almezan.helper.DateRVAdapter;
import me.nakeeb.almezan.helper.Utils;
import me.nakeeb.almezan.model.DateItem;
import me.nakeeb.almezan.model.Handout;
import me.nakeeb.almezan.model.PrayersDay;

/**
 * Created by mamdouhelnakeeb on 12/15/17.
 */

public class MonthlyStats extends AppCompatActivity {

    TextView jan0, jan1, jan2, jan3;
    TextView feb0, feb1, feb2, feb3;
    TextView march0, march1, march2, march3;
    TextView april0, april1, april2, april3;
    TextView may0, may1, may2, may3;
    TextView jun0, jun1, jun2, jun3;
    TextView jul0, jul1, jul2, jul3;
    TextView aug0, aug1, aug2, aug3;
    TextView sep0, sep1, sep2, sep3;
    TextView oct0, oct1, oct2, oct3;
    TextView nov0, nov1, nov2, nov3;
    TextView dec0, dec1, dec2, dec3;

    FirebaseAuth mAuth;

    RecyclerView dateRV;
    DateRVAdapter dateRVAdapter;
    LinearLayoutManager linearLayoutManager;

    ArrayList<DateItem> yearsArr;
    int datePos = 0;

    PrayersDay prayersDay;
            
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthly_stats_activity);

        mAuth = FirebaseAuth.getInstance();

        initViews();
        initRV();
        initNav();

        loadADs();

    }


    private void updateHandouts(Handout handout){

        switch (handout.month){

            case 1:
                jan3.setText(String.valueOf(handout.amount));
                break;
            case 2:
                feb3.setText(String.valueOf(handout.amount));
                break;
            case 3:
                march3.setText(String.valueOf(handout.amount));
                break;
            case 4:
                april3.setText(String.valueOf(handout.amount));
                break;
            case 5:
                may3.setText(String.valueOf(handout.amount));
                break;
            case 6:
                jun3.setText(String.valueOf(handout.amount));
                break;
            case 7:
                jul3.setText(String.valueOf(handout.amount));
                break;
            case 8:
                aug3.setText(String.valueOf(handout.amount));
                break;
            case 9:
                sep3.setText(String.valueOf(handout.amount));
                break;
            case 10:
                oct3.setText(String.valueOf(handout.amount));
                break;
            case 11:
                nov3.setText(String.valueOf(handout.amount));
                break;
            case 12:
                dec3.setText(String.valueOf(handout.amount));
                break;

        }


    }

    private void updatePrayers(){

        switch (prayersDay.fajr){
            case 0:
                prayersDay.fajr0++;
                break;
            case 1:
                prayersDay.fajr1++;
                break;
            case 2:
                prayersDay.fajr2++;
                break;
            case 3:
                prayersDay.fajr0++;
                break;
        }

        switch (prayersDay.duhr){
            case 0:
                prayersDay.duhr0++;
                break;
            case 1:
                prayersDay.duhr1++;
                break;
            case 2:
                prayersDay.duhr2++;
                break;
            case 3:
                prayersDay.duhr0++;
                break;
        }

        switch (prayersDay.asr){
            case 0:
                prayersDay.asr0++;
                break;
            case 1:
                prayersDay.asr1++;
                break;
            case 2:
                prayersDay.asr2++;
                break;
            case 3:
                prayersDay.asr0++;
                break;
        }

        switch (prayersDay.mghreb){
            case 0:
                prayersDay.mghreb0++;
                break;
            case 1:
                prayersDay.mghreb1++;
                break;
            case 2:
                prayersDay.mghreb2++;
                break;
            case 3:
                prayersDay.mghreb0++;
                break;
        }

        switch (prayersDay.isha){
            case 0:
                prayersDay.isha0++;
                break;
            case 1:
                prayersDay.isha1++;
                break;
            case 2:
                prayersDay.isha2++;
                break;
            case 3:
                prayersDay.isha0++;
                break;
        }

        switch (prayersDay.month){

            case 1:
                jan0.setText(String.valueOf(prayersDay.fajr0 + prayersDay.duhr0 + prayersDay.asr0 + prayersDay.mghreb0 + prayersDay.isha0));
                jan1.setText(String.valueOf(prayersDay.fajr1 + prayersDay.duhr1 + prayersDay.asr1 + prayersDay.mghreb1 + prayersDay.isha1));
                jan2.setText(String.valueOf(prayersDay.fajr2 + prayersDay.duhr2 + prayersDay.asr2 + prayersDay.mghreb2 + prayersDay.isha2));
                break;
            case 2:
                feb0.setText(String.valueOf(prayersDay.fajr0 + prayersDay.duhr0 + prayersDay.asr0 + prayersDay.mghreb0 + prayersDay.isha0));
                feb1.setText(String.valueOf(prayersDay.fajr1 + prayersDay.duhr1 + prayersDay.asr1 + prayersDay.mghreb1 + prayersDay.isha1));
                feb2.setText(String.valueOf(prayersDay.fajr2 + prayersDay.duhr2 + prayersDay.asr2 + prayersDay.mghreb2 + prayersDay.isha2));
                break;
            case 3:
                march0.setText(String.valueOf(prayersDay.fajr0 + prayersDay.duhr0 + prayersDay.asr0 + prayersDay.mghreb0 + prayersDay.isha0));
                march1.setText(String.valueOf(prayersDay.fajr1 + prayersDay.duhr1 + prayersDay.asr1 + prayersDay.mghreb1 + prayersDay.isha1));
                march2.setText(String.valueOf(prayersDay.fajr2 + prayersDay.duhr2 + prayersDay.asr2 + prayersDay.mghreb2 + prayersDay.isha2));
                break;
            case 4:
                april0.setText(String.valueOf(prayersDay.fajr0 + prayersDay.duhr0 + prayersDay.asr0 + prayersDay.mghreb0 + prayersDay.isha0));
                april1.setText(String.valueOf(prayersDay.fajr1 + prayersDay.duhr1 + prayersDay.asr1 + prayersDay.mghreb1 + prayersDay.isha1));
                april2.setText(String.valueOf(prayersDay.fajr2 + prayersDay.duhr2 + prayersDay.asr2 + prayersDay.mghreb2 + prayersDay.isha2));
                break;
            case 5:
                may0.setText(String.valueOf(prayersDay.fajr0 + prayersDay.duhr0 + prayersDay.asr0 + prayersDay.mghreb0 + prayersDay.isha0));
                may1.setText(String.valueOf(prayersDay.fajr1 + prayersDay.duhr1 + prayersDay.asr1 + prayersDay.mghreb1 + prayersDay.isha1));
                may2.setText(String.valueOf(prayersDay.fajr2 + prayersDay.duhr2 + prayersDay.asr2 + prayersDay.mghreb2 + prayersDay.isha2));
                break;
            case 6:
                jun0.setText(String.valueOf(prayersDay.fajr0 + prayersDay.duhr0 + prayersDay.asr0 + prayersDay.mghreb0 + prayersDay.isha0));
                jun1.setText(String.valueOf(prayersDay.fajr1 + prayersDay.duhr1 + prayersDay.asr1 + prayersDay.mghreb1 + prayersDay.isha1));
                jun2.setText(String.valueOf(prayersDay.fajr2 + prayersDay.duhr2 + prayersDay.asr2 + prayersDay.mghreb2 + prayersDay.isha2));
                break;
            case 7:
                jul0.setText(String.valueOf(prayersDay.fajr0 + prayersDay.duhr0 + prayersDay.asr0 + prayersDay.mghreb0 + prayersDay.isha0));
                jul1.setText(String.valueOf(prayersDay.fajr1 + prayersDay.duhr1 + prayersDay.asr1 + prayersDay.mghreb1 + prayersDay.isha1));
                jul2.setText(String.valueOf(prayersDay.fajr2 + prayersDay.duhr2 + prayersDay.asr2 + prayersDay.mghreb2 + prayersDay.isha2));
                break;
            case 8:
                aug0.setText(String.valueOf(prayersDay.fajr0 + prayersDay.duhr0 + prayersDay.asr0 + prayersDay.mghreb0 + prayersDay.isha0));
                aug1.setText(String.valueOf(prayersDay.fajr1 + prayersDay.duhr1 + prayersDay.asr1 + prayersDay.mghreb1 + prayersDay.isha1));
                aug2.setText(String.valueOf(prayersDay.fajr2 + prayersDay.duhr2 + prayersDay.asr2 + prayersDay.mghreb2 + prayersDay.isha2));
                break;
            case 9:
                sep0.setText(String.valueOf(prayersDay.fajr0 + prayersDay.duhr0 + prayersDay.asr0 + prayersDay.mghreb0 + prayersDay.isha0));
                sep1.setText(String.valueOf(prayersDay.fajr1 + prayersDay.duhr1 + prayersDay.asr1 + prayersDay.mghreb1 + prayersDay.isha1));
                sep2.setText(String.valueOf(prayersDay.fajr2 + prayersDay.duhr2 + prayersDay.asr2 + prayersDay.mghreb2 + prayersDay.isha2));
                break;
            case 10:
                oct0.setText(String.valueOf(prayersDay.fajr0 + prayersDay.duhr0 + prayersDay.asr0 + prayersDay.mghreb0 + prayersDay.isha0));
                oct1.setText(String.valueOf(prayersDay.fajr1 + prayersDay.duhr1 + prayersDay.asr1 + prayersDay.mghreb1 + prayersDay.isha1));
                oct2.setText(String.valueOf(prayersDay.fajr2 + prayersDay.duhr2 + prayersDay.asr2 + prayersDay.mghreb2 + prayersDay.isha2));
                break;
            case 11:
                nov0.setText(String.valueOf(prayersDay.fajr0 + prayersDay.duhr0 + prayersDay.asr0 + prayersDay.mghreb0 + prayersDay.isha0));
                nov1.setText(String.valueOf(prayersDay.fajr1 + prayersDay.duhr1 + prayersDay.asr1 + prayersDay.mghreb1 + prayersDay.isha1));
                nov2.setText(String.valueOf(prayersDay.fajr2 + prayersDay.duhr2 + prayersDay.asr2 + prayersDay.mghreb2 + prayersDay.isha2));
                break;
            case 12:
                dec0.setText(String.valueOf(prayersDay.fajr0 + prayersDay.duhr0 + prayersDay.asr0 + prayersDay.mghreb0 + prayersDay.isha0));
                dec1.setText(String.valueOf(prayersDay.fajr1 + prayersDay.duhr1 + prayersDay.asr1 + prayersDay.mghreb1 + prayersDay.isha1));
                dec2.setText(String.valueOf(prayersDay.fajr2 + prayersDay.duhr2 + prayersDay.asr2 + prayersDay.mghreb2 + prayersDay.isha2));
                break;

        }

    }

    private void loadStats(){

        getPrayersStats();
        getHandoutsStats();
    }

    private void getPrayersStats(){


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (int i = 0; i < 12; i++){

            prayersDay = new PrayersDay();
            prayersDay.month = i + 1;

            CollectionReference docRef = db.collection("users")
                    .document(mAuth.getCurrentUser().getUid())
                    .collection("prayers")
                    .document(String.valueOf(Utils.getMillis(yearsArr.get(datePos).timeInMillis, "y")))
                    .collection("months")
                    .document(String.valueOf(Utils.getMillis((Utils.getMillis(yearsArr.get(datePos).timeInMillis, "y") + 2678400000L * i),"MM/y"))) //2678400000
                    .collection("days");

            Log.d("prayersStatsTest", String.valueOf(Utils.getMillis((Utils.getMillis(yearsArr.get(datePos).timeInMillis, "y") + 2678400000L * i),"MM/y")));

            docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Log.d("PrayerData: ", document.getId() + " => " + document.getData());

                            prayersDay.fajr = Integer.parseInt(document.get("fajr").toString());
                            prayersDay.duhr = Integer.parseInt(document.get("duhr").toString());
                            prayersDay.asr = Integer.parseInt(document.get("asr").toString());
                            prayersDay.mghreb = Integer.parseInt(document.get("mghreb").toString());
                            prayersDay.isha = Integer.parseInt(document.get("isha").toString());

                            updatePrayers();

                        }

                    } else {
                        Log.d("Error: ", "Error getting documents: ", task.getException());
                    }
                }
            });

        }


    }

    private void getHandoutsStats(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        Log.d("yearsNoDates", String.valueOf(yearsArr.size()));

        CollectionReference docRef = db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("handouts")
                .document(String.valueOf(Utils.getMillis(yearsArr.get(datePos).timeInMillis, "y")))
                .collection("months");

        Log.d("timeMillisYear", String.valueOf(yearsArr.get(datePos).timeInMillis));

        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (DocumentSnapshot document : task.getResult()) {
                        Log.d("PrayerData: ", document.getId() + " => " + document.getData());

                        Handout handout = new Handout();
                        handout.amount = Float.parseFloat(document.get("amount").toString());
                        handout.month = Integer.parseInt(document.get("month").toString());
                        handout.timeInMillis = Long.parseLong(document.getId());

                        if (Utils.getMillis(System.currentTimeMillis(), "MM/y") == handout.timeInMillis) {
//                                monthHandoutsTotal = handout.amount;
                        }
                            updateHandouts(handout);

                    }

                } else {
                    Log.d("Error: ", "Error getting documents: ", task.getException());
                }
            }
        });

    }

    private void initRV(){

        long currentMillis = System.currentTimeMillis();
        long oldMillis = getSharedPreferences("startTime", MODE_PRIVATE).getLong("", currentMillis);
        yearsArr = Utils.getYears(currentMillis, oldMillis, new Locale("ar", "EG"));

        Log.d("currentTime", String.valueOf(currentMillis));
        Log.d("oldTime", String.valueOf(oldMillis));

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        dateRV = findViewById(R.id.yearNoRV);
        dateRV.setLayoutManager(linearLayoutManager);

        dateRV.setItemAnimator(new DefaultItemAnimator());


        dateRVAdapter = new DateRVAdapter(this, yearsArr);
        dateRV.setAdapter(dateRVAdapter);


        SnapHelper snapHelper = new LinearSnapHelper(){

            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                View centerView = findSnapView(layoutManager);
                if (centerView == null)
                    return RecyclerView.NO_POSITION;

                int position = layoutManager.getPosition(centerView);
                int targetPosition = -1;
                if (layoutManager.canScrollHorizontally()) {
                    if (velocityX < 0) { // opposite in RTL
                        targetPosition = position + 1;
                    } else {
                        targetPosition = position - 1;
                    }
                }

                if (layoutManager.canScrollVertically()) {
                    if (velocityY < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                final int firstItem = 0;
                final int lastItem = layoutManager.getItemCount() - 1;
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));

                datePos = targetPosition;

                loadStats();


                return targetPosition;
            }
        };


        snapHelper.attachToRecyclerView(dateRV);
        dateRV.setOnFlingListener(snapHelper);


        dateRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                Log.d("onScrolledStateFunc", "onScrolled");
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        datePos = yearsArr.size() - 1;

        dateRV.scrollToPosition(datePos);

        loadStats();


    }

    private void initViews(){
        
        jan0 = findViewById(R.id.jan0);
        jan1 = findViewById(R.id.jan1);
        jan2 = findViewById(R.id.jan2);
        jan3 = findViewById(R.id.jan3);

        feb0 = findViewById(R.id.feb0);
        feb1 = findViewById(R.id.feb1);
        feb2 = findViewById(R.id.feb2);
        feb3 = findViewById(R.id.feb3);

        march0 = findViewById(R.id.march0);
        march1 = findViewById(R.id.march1);
        march2 = findViewById(R.id.march2);
        march3 = findViewById(R.id.march3);

        april0 = findViewById(R.id.april0);
        april1 = findViewById(R.id.april1);
        april2 = findViewById(R.id.april2);
        april3 = findViewById(R.id.april3);

        may0 = findViewById(R.id.may0);
        may1 = findViewById(R.id.may1);
        may2 = findViewById(R.id.may2);
        may3 = findViewById(R.id.may3);

        jun0 = findViewById(R.id.jun0);
        jun1 = findViewById(R.id.jun1);
        jun2 = findViewById(R.id.jun2);
        jun3 = findViewById(R.id.jun3);

        jul0 = findViewById(R.id.jul0);
        jul1 = findViewById(R.id.jul1);
        jul2 = findViewById(R.id.jul2);
        jul3 = findViewById(R.id.jul3);

        aug0 = findViewById(R.id.aug0);
        aug1 = findViewById(R.id.aug1);
        aug2 = findViewById(R.id.aug2);
        aug3 = findViewById(R.id.aug3);

        sep0 = findViewById(R.id.sep0);
        sep1 = findViewById(R.id.sep1);
        sep2 = findViewById(R.id.sep2);
        sep3 = findViewById(R.id.sep3);

        oct0 = findViewById(R.id.oct0);
        oct1 = findViewById(R.id.oct1);
        oct2 = findViewById(R.id.oct2);
        oct3 = findViewById(R.id.oct3);

        nov0 = findViewById(R.id.nov0);
        nov1 = findViewById(R.id.nov1);
        nov2 = findViewById(R.id.nov2);
        nov3 = findViewById(R.id.nov3);

        dec0 = findViewById(R.id.dec0);
        dec1 = findViewById(R.id.dec1);
        dec2 = findViewById(R.id.dec2);
        dec3 = findViewById(R.id.dec3);
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
                        startActivity(new Intent(MonthlyStats.this, Login.class));
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
