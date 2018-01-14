package me.nakeeb.almezan.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

import me.nakeeb.almezan.R;
import me.nakeeb.almezan.helper.LocaleManager;
import me.nakeeb.almezan.helper.Utils;

/**
 * Created by mamdouhelnakeeb on 12/30/17.
 */

public class Settings extends BaseActivity {

    AppCompatSpinner langSpinner;

    Button aboutBtn;

    int lang = 0;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.settings_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        langSpinner = findViewById(R.id.langSpinner);
        aboutBtn = findViewById(R.id.aboutBtn);

        initLanguages();

        langSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (lang != i){

                    lang = i;
                    AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
                    builder.setMessage(getString(R.string.change_lang))
                            .setTitle(getResources().getString(R.string.app_name))
                            .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    String langCode = "en";
                                    switch (lang){
                                        case 1:
                                            langCode = "en";
                                            break;
                                        case 2:
                                            langCode = "ar";
                                            break;
                                        case 3:
                                            langCode = "fr";
                                            break;
                                    }

                                    setNewLocale(langCode);

                                }
                            })
                            .setNegativeButton(getString(R.string.no), null);

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

    private void setNewLocale(String language) {
        LocaleManager.setNewLocale(this, language);

        Intent i = new Intent(this, Landing.class);
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

    }

    private void initLanguages(){

        ArrayAdapter<CharSequence> rstArrAdapter = ArrayAdapter.createFromResource(this, R.array.languages, R.layout.spinner_item);

        langSpinner.setAdapter(rstArrAdapter);

    }

}
