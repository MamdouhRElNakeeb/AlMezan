package me.nakeeb.almezan.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.nakeeb.almezan.R;
import me.nakeeb.almezan.helper.Utils;

/**
 * Created by mamdouhelnakeeb on 12/30/17.
 */

public class Reset extends AppCompatActivity {

    AppCompatSpinner rstSpinner;
    Button yesBtn, noBtn;


    private FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_reset_activity);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        rstSpinner = findViewById(R.id.rstSpinner);
        yesBtn = findViewById(R.id.yesBtn);
        noBtn = findViewById(R.id.noBtn);

        initRst();
        loadADs();


        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset(rstSpinner.getSelectedItemPosition());
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }

    private void initRst(){

        ArrayAdapter<CharSequence> rstArrAdapter = ArrayAdapter.createFromResource(this, R.array.reset, R.layout.spinner_item);

        rstSpinner.setAdapter(rstArrAdapter);

    }


    private void reset(final int i){


        Map<String, Object> user = new HashMap<>();

        final long currentMillis = System.currentTimeMillis();

        switch (i){
            case 0:
                user.put("prayerTime", currentMillis);
                user.put("handoutTime", currentMillis);
                user.put("zekrTime", currentMillis);
                break;
            case 1:
                user.put("prayerTime", currentMillis);
                break;
            case 2:
                user.put("handoutTime", currentMillis);
                break;
            case 3:
                user.put("zekrTime", currentMillis);
                break;
        }


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser fbUser = mAuth.getCurrentUser();

        progressDialog.setMessage("Resetting ... Please wait");
        progressDialog.show();

        // Add a new document with a generated ID
        db.collection("users")
                .document(fbUser.getUid())
                .update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("save user: ", "DocumentSnapshot added ");

                        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();

                        switch (i){
                            case 0:
                                editor.putString("prayerTime", String.valueOf(currentMillis));
                                editor.putString("handoutTime", String.valueOf(currentMillis));
                                editor.putString("zekrTime", String.valueOf(currentMillis));
                                break;
                            case 1:
                                editor.putString("prayerTime", String.valueOf(currentMillis));
                                break;
                            case 2:
                                editor.putString("handoutTime", String.valueOf(currentMillis));
                                break;
                            case 3:
                                editor.putString("zekrTime", String.valueOf(currentMillis));
                                break;
                        }

                        editor.apply();
                        progressDialog.hide();

                        Toast.makeText(Reset.this, "Resetting Done", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("save user: ", "Error adding document", e);
                        progressDialog.hide();

                        Toast.makeText(Reset.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
