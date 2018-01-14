package me.nakeeb.almezan.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import me.nakeeb.almezan.R;
import me.nakeeb.almezan.helper.LocaleManager;

public class Splash extends AppCompatActivity {


    private static final long SPLASH_TIME_OUT = 2000 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        SharedPreferences preferences = getSharedPreferences("ADs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("popup", false);
        editor.apply();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(Splash.this, Login.class));
                finish();
            }
        },SPLASH_TIME_OUT);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        Log.d("Base", "attachBaseContext");
    }

}
