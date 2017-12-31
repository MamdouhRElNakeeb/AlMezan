package me.nakeeb.almezan.activity;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

import me.nakeeb.almezan.R;
import me.nakeeb.almezan.helper.Utils;

/**
 * Created by mamdouhelnakeeb on 12/30/17.
 */

public class Settings extends AppCompatActivity {

    AppCompatSpinner langSpinner;

    Button aboutBtn;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    int lang = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        langSpinner = findViewById(R.id.langSpinner);
        aboutBtn = findViewById(R.id.aboutBtn);

        prefs = getSharedPreferences("User", MODE_PRIVATE);
        editor = prefs.edit();

        initLanguages();
        initNav();
        loadADs();

        langSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (lang != i){

                    AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                    builder.setMessage("Are you sure you want change app language ?")
                            .setTitle(getResources().getString(R.string.app_name))
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Locale locale = new Locale("en");
                                    switch (i){
                                        case 1:
                                            Utils.changeLocale(getBaseContext(), "en");
                                            editor.putString("language", "en");
                                            locale = new Locale("en");
                                            break;
                                        case 2:
                                            Utils.changeLocale(getBaseContext(), "ar");
                                            editor.putString("language", "ar");
                                            locale = new Locale("ar");
                                            break;
                                        case 3:
                                            Utils.changeLocale(getBaseContext(), "tr");
                                            editor.putString("language", "tr");
                                            locale = new Locale("tr");
                                            break;
                                        case 4:
                                            Utils.changeLocale(getBaseContext(), "id");
                                            editor.putString("language", "id");
                                            locale = new Locale("id");
                                            break;
                                    }

                                    editor.apply();
//                                    Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage( getBaseContext().getPackageName() );
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    startActivity(intent);


//                                    Locale.setDefault(locale);
//                                    Configuration config = new Configuration();
//                                    config.locale = locale;
//                                    getResources().updateConfiguration(config, getResources().getDisplayMetrics());

//                                    recreate();

                                }
                            })
                            .setNegativeButton("Cancel", null);

                    final AlertDialog dialog = builder.create();

                    dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface arg0) {
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_grey));
                        }
                    });
                    dialog.show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), About.class));
            }
        });

    }

    private void initLanguages(){

        ArrayAdapter<CharSequence> rstArrAdapter = ArrayAdapter.createFromResource(this, R.array.languages, R.layout.spinner_item);

        langSpinner.setAdapter(rstArrAdapter);

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
                        mDrawerLayout.closeDrawer(Gravity.RIGHT);
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
