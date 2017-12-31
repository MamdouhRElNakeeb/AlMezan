package me.nakeeb.almezan.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import me.nakeeb.almezan.helper.Consts;
import me.nakeeb.almezan.R;
import me.nakeeb.almezan.helper.Utils;
import me.nakeeb.almezan.model.Zekr;

/**
 * Created by mamdouhelnakeeb on 12/15/17.
 */

public class ZekrDetails extends AppCompatActivity {

    int doaa = 0;

    TextView zekrTitleTV, zekrCountTV;
    ImageButton zekrAddBtn;
    Button saveCountBtn;

    int count = 0, totalCount = 0;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zekr_details_activity);

        mAuth = FirebaseAuth.getInstance();

        initNav();

        doaa = getIntent().getExtras().getInt("doaa", 0);

        zekrTitleTV = findViewById(R.id.zekrTitleTV);
        zekrCountTV = findViewById(R.id.zekrCountTV);
        zekrAddBtn = findViewById(R.id.zekrAddBtn);
        saveCountBtn = findViewById(R.id.saveCountBtn);
        saveCountBtn.setAlpha((float) 0.3);
        saveCountBtn.setEnabled(false);

        zekrTitleTV.setText(Consts.Doaa[doaa]);

        getZekrTotal();

        zekrAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;

                zekrCountTV.setText(String.valueOf(count));

                saveCountBtn.setAlpha((float) 1.0);
                saveCountBtn.setEnabled(true);
            }
        });

        saveCountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveZekrCount();

            }
        });

        loadADs();

    }

    private void saveZekrCount(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        totalCount += count;

        Map<String, Object> zekr = new HashMap<>();
        zekr.put("count", totalCount);

        // Add a new document with a generated ID
        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("zekr")
                .document(String.valueOf(doaa))
                .set(zekr)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("save user: ", "DocumentSnapshot added ");
                        getZekrTotal();
                        saveCountBtn.setAlpha((float) 0.3);
                        saveCountBtn.setEnabled(false);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("save user: ", "Error adding document", e);
                    }
                });

    }

    private void getZekrTotal(){

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("zekr")
                .document(String.valueOf(doaa));

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (!documentSnapshot.exists())
                    return;
                Log.d("docCon", documentSnapshot.getData().toString());
                Zekr zekr = new Zekr();// = documentSnapshot.toObject(User.class);
                zekr.doaa = doaa;
                zekr.count = Integer.parseInt(documentSnapshot.get("count").toString());

                totalCount = zekr.count;
                count = 0;
                zekrCountTV.setText(String.valueOf(count));

                calcZekrStats(zekr);

            }
        });

    }

    private void calcZekrStats(Zekr zekr){

        TextView zekrDailyTV = findViewById(R.id.zekrDailyTV);
        TextView zekrMonthlyTV = findViewById(R.id.zekrMonthlyTV);
        TextView zekrYearlyTV = findViewById(R.id.zekrYearlyTV);

        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);

        if (!prefs.contains("dob")){
            Toast.makeText(this, "Your age isn't registered!", Toast.LENGTH_SHORT).show();
            return;
        }

        float age = Utils.calcAge(prefs.getString("dob", "0/0/0"));

        BigDecimal daysStats = BigDecimal.valueOf(zekr.count / age / 365);
        BigDecimal monthsStats = BigDecimal.valueOf(zekr.count / age / 12);
        BigDecimal yearsStats = BigDecimal.valueOf(zekr.count / age);

        zekrYearlyTV.setText(String.valueOf(yearsStats).substring(0, 6));
        zekrMonthlyTV.setText(String.valueOf(monthsStats).substring(0, 6));
        zekrDailyTV.setText(String.valueOf(daysStats).substring(0, 6));

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
