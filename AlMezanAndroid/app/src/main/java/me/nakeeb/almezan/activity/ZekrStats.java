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
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.math.BigDecimal;

import me.nakeeb.almezan.R;
import me.nakeeb.almezan.helper.Utils;

/**
 * Created by mamdouhelnakeeb on 12/15/17.
 */

public class ZekrStats extends BaseActivity {


    TextView doaa0TV, doaa1TV, doaa2TV, doaa3TV, doaa4TV, doaa5TV, doaa6TV, doaa7TV, doaa8TV;

    AppCompatSpinner filterSpinner;
    FirebaseAuth mAuth;

    int daysNo = 0;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.zekr_stats_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long currentMillis = System.currentTimeMillis();

        daysNo = Utils.getDaysMonthsYearsNo(currentMillis, getSharedPreferences("User", MODE_PRIVATE).getLong("startTime", currentMillis)).get(0);

        Log.d("currentMilllis", String.valueOf(currentMillis));
        Log.d("oldtMilllis", String.valueOf(getSharedPreferences("User", MODE_PRIVATE).getLong("startTime", currentMillis)));
        if (daysNo == 0)
            daysNo = 1;
        Log.d("daysNoTotal", String.valueOf(daysNo));

        mAuth = FirebaseAuth.getInstance();

        initViews();

        getZekrStats(365);

    }

    private void getZekrStats(final int period){

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


                        float age = Utils.calcAge(getSharedPreferences("User", MODE_PRIVATE).getString("dob", "0/0/0"));

                        BigDecimal countPerDay = BigDecimal.valueOf(Float.parseFloat(document.get("count").toString()) / age / period);

                        String countStr = String.valueOf(countPerDay);

                        if (countStr.length() > 6){
                            countStr = countStr.substring(0, 6);
                        }

                        switch (Integer.parseInt(document.getId())){

                            case 0:
                                doaa0TV.setText(countStr);
                                break;
                            case 1:
                                doaa1TV.setText(countStr);
                                break;
                            case 2:
                                doaa2TV.setText(countStr);
                                break;
                            case 3:
                                doaa3TV.setText(countStr);
                                break;
                            case 4:
                                doaa4TV.setText(countStr);
                                break;
                            case 5:
                                doaa5TV.setText(countStr);
                                break;
                            case 6:
                                doaa6TV.setText(countStr);
                                break;
                            case 7:
                                doaa7TV.setText(countStr);
                                break;
                            case 8:
                                doaa8TV.setText(countStr);
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

        filterSpinner = findViewById(R.id.filterSpinner);

        initFilter();

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

    private void initFilter(){

        ArrayAdapter<CharSequence> rstArrAdapter = ArrayAdapter.createFromResource(this, R.array.time_filter, R.layout.spinner_item);

        filterSpinner.setAdapter(rstArrAdapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i){
                    case 0:
                        getZekrStats(365);
                        break;

                    case 1:
                        getZekrStats(12);
                        break;

                    case 2:
                        getZekrStats(1);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

}
