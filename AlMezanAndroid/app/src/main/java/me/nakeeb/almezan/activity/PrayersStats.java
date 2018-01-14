package me.nakeeb.almezan.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import me.nakeeb.almezan.R;
import me.nakeeb.almezan.helper.Utils;
import me.nakeeb.almezan.model.DateItem;
import me.nakeeb.almezan.model.PrayersDay;
import me.nakeeb.almezan.model.User;

/**
 * Created by mamdouhelnakeeb on 12/15/17.
 */

public class PrayersStats extends BaseActivity {

    TextView fajr0TV, duhr0TV, asr0TV, mghreb0TV, isha0TV;
    TextView fajr1TV, duhr1TV, asr1TV, mghreb1TV, isha1TV;
    TextView fajr2TV, duhr2TV, asr2TV, mghreb2TV, isha2TV;
    TextView fajr3TV, duhr3TV, asr3TV, mghreb3TV, isha3TV;
    
    private FirebaseAuth mAuth;

    PieChart mChart;

    private int [] prayersData = {0, 0, 0, 0};
    private int[] xData = {
            R.string.on_time,
            R.string.late,
            R.string.left_out,
            R.string.not_reg
    };

    PrayersDay prayersDay;

    int regDays = 0, totalDays = 0;

    ArrayList<DateItem> dateItemArrayList;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.prayer_stats_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prayersDay = new PrayersDay();

        initPrayers();
        initChart();

        mAuth = FirebaseAuth.getInstance();

        long currentMillis = System.currentTimeMillis();

        dateItemArrayList = Utils.getDates(Utils.getDayMillis(currentMillis),
                Utils.getDayMillis(getSharedPreferences("User", MODE_PRIVATE).getLong("prayerTime", currentMillis)),
                Locale.getDefault());

        getPrayersStats();

        updatePeriod();

    }

    private void getPrayersStats(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Log.d("arrSizeee", String.valueOf(dateItemArrayList.size()));

        for (int i = 0; i < dateItemArrayList.size(); i++) {

            mAuth = FirebaseAuth.getInstance();
            if (mAuth.getCurrentUser() == null){
                return;
            }

            DocumentReference docRef = db.collection("users")
                    .document(mAuth.getCurrentUser().getUid())
                    .collection("prayers")
                    .document(String.valueOf(Utils.getMillis(dateItemArrayList.get(i).timeInMillis, "y")))
                    .collection("months")
                    .document(String.valueOf(Utils.getMillis(dateItemArrayList.get(i).timeInMillis, "MM/y")))
                    .collection("days")
                    .document(String.valueOf(Utils.getMillis(dateItemArrayList.get(i).timeInMillis, "dd/MM/y")));

            Log.d("yearP", String.valueOf(Utils.getMillis(dateItemArrayList.get(i).timeInMillis, "y")));
            Log.d("monthP", String.valueOf(Utils.getMillis(dateItemArrayList.get(i).timeInMillis, "MM/y")));
            Log.d("dayP", String.valueOf(Utils.getMillis(dateItemArrayList.get(i).timeInMillis, "dd/MM/y")));

            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (!documentSnapshot.exists())
                        return;

                    Log.d("PrayerDatadsgd" + String.valueOf(regDays) + ": ", documentSnapshot.getId() + " => " + documentSnapshot.getData());

                    prayersDay.fajr = Integer.parseInt(documentSnapshot.get("fajr").toString());
                    prayersDay.duhr = Integer.parseInt(documentSnapshot.get("duhr").toString());
                    prayersDay.asr = Integer.parseInt(documentSnapshot.get("asr").toString());
                    prayersDay.mghreb = Integer.parseInt(documentSnapshot.get("mghreb").toString());
                    prayersDay.isha = Integer.parseInt(documentSnapshot.get("isha").toString());

                    updatePrayers();

                    regDays++;

                    addDataSet();

                }
            });

        }

    }

    private void updatePeriod(){

        ArrayList<Integer> periodArr;

        long currentMillis = System.currentTimeMillis();

        periodArr = Utils.getDaysMonthsYearsNo(currentMillis,
                getSharedPreferences("User", MODE_PRIVATE).getLong("prayerTime", currentMillis));

        TextView daysCounterTV = findViewById(R.id.daysCounterTV);
        TextView monthsCounterTV = findViewById(R.id.monthsCounterTV);
        TextView yearsCounterTV = findViewById(R.id.yearsCounterTV);

        daysCounterTV.setText(String.valueOf(periodArr.get(0)));
        monthsCounterTV.setText(String.valueOf(periodArr.get(1)));
        yearsCounterTV.setText(String.valueOf(periodArr.get(2)));

        totalDays = periodArr.get(0) + periodArr.get(1) * 30 + periodArr.get(2) * 365;

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



        Log.d("fajrFB0", String.valueOf(prayersDay.fajr0));
        Log.d("fajrFB1", String.valueOf(prayersDay.fajr1));
        Log.d("fajrFB2", String.valueOf(prayersDay.fajr2));
        Log.d("fajrFB3", String.valueOf(prayersDay.fajr3));

        Log.d("duhrFB0", String.valueOf(prayersDay.duhr0));
        Log.d("duhrFB1", String.valueOf(prayersDay.duhr1));
        Log.d("duhrFB2", String.valueOf(prayersDay.duhr2));
        Log.d("duhrFB3", String.valueOf(prayersDay.duhr3));

        Log.d("asrFB0", String.valueOf(prayersDay.asr0));
        Log.d("asrFB1", String.valueOf(prayersDay.asr1));
        Log.d("asrFB2", String.valueOf(prayersDay.asr2));
        Log.d("asrFB3", String.valueOf(prayersDay.asr3));

        Log.d("mghrebFB0", String.valueOf(prayersDay.mghreb0));
        Log.d("mghrebFB1", String.valueOf(prayersDay.mghreb1));
        Log.d("mghrebFB2", String.valueOf(prayersDay.mghreb2));
        Log.d("mghrebFB3", String.valueOf(prayersDay.mghreb3));

        Log.d("ishaFB0", String.valueOf(prayersDay.isha0));
        Log.d("ishaFB1", String.valueOf(prayersDay.isha1));
        Log.d("ishaFB2", String.valueOf(prayersDay.isha2));
        Log.d("ishaFB3", String.valueOf(prayersDay.isha3));
        
    }

    private void updateUI(){

        prayersDay.fajr3 = prayersDay.duhr3 = prayersDay.asr3 = prayersDay.mghreb3 = prayersDay.isha3 = totalDays - regDays;

        int fajr = prayersDay.fajr0 + prayersDay.fajr1 + prayersDay.fajr2 + prayersDay.fajr3;

        Log.d("fajrCount", String.valueOf(fajr));
        fajr0TV.setText(String.valueOf((int) ((float) prayersDay.fajr0 / (float) fajr * 100) + " %"));
        fajr1TV.setText(String.valueOf((int) ((float) prayersDay.fajr1 / (float) fajr * 100) + " %"));
        fajr2TV.setText(String.valueOf((int) ((float) prayersDay.fajr2 / (float) fajr * 100) + " %"));
        fajr3TV.setText(String.valueOf((int) ((float) prayersDay.fajr3 / (float) fajr * 100) + " %"));

        int duhr = prayersDay.duhr0 + prayersDay.duhr1 + prayersDay.duhr2 + prayersDay.duhr3;

        Log.d("duhrCount", String.valueOf(duhr));

        duhr0TV.setText(String.valueOf((int) ((float) prayersDay.duhr0 / (float) duhr * 100) + " %"));
        duhr1TV.setText(String.valueOf((int) ((float) prayersDay.duhr1 / (float) duhr * 100) + " %"));
        duhr2TV.setText(String.valueOf((int) ((float) prayersDay.duhr2 / (float) duhr * 100) + " %"));
        duhr3TV.setText(String.valueOf((int) ((float) prayersDay.duhr3 / (float) duhr * 100) + " %"));

        int asr = prayersDay.asr0 + prayersDay.asr1 + prayersDay.asr2 + prayersDay.asr3;

        Log.d("asrCount", String.valueOf(asr));

        asr0TV.setText(String.valueOf((int) ((float) prayersDay.asr0 / (float) asr * 100) + " %"));
        asr1TV.setText(String.valueOf((int) ((float) prayersDay.asr1 / (float) asr * 100) + " %"));
        asr2TV.setText(String.valueOf((int) ((float) prayersDay.asr2 / (float) asr * 100) + " %"));
        asr3TV.setText(String.valueOf((int) ((float) prayersDay.asr3 / (float) asr * 100) + " %"));

        int mghreb = prayersDay.mghreb0 + prayersDay.mghreb1 + prayersDay.mghreb2 + prayersDay.mghreb3;

        Log.d("mghrebCount", String.valueOf(mghreb));

        mghreb0TV.setText(String.valueOf((int) ((float) prayersDay.mghreb0 / (float) mghreb * 100) + " %"));
        mghreb1TV.setText(String.valueOf((int) ((float) prayersDay.mghreb1 / (float) mghreb * 100) + " %"));
        mghreb2TV.setText(String.valueOf((int) ((float) prayersDay.mghreb2 / (float) mghreb * 100) + " %"));
        mghreb3TV.setText(String.valueOf((int) ((float) prayersDay.mghreb3 / (float) mghreb * 100) + " %"));


        int isha = prayersDay.isha0 + prayersDay.isha1 + prayersDay.isha2 + prayersDay.isha3;

        Log.d("ishaCount", String.valueOf(isha));

        isha0TV.setText(String.valueOf((int) ((float) prayersDay.isha0 / (float) isha * 100) + " %"));
        isha1TV.setText(String.valueOf((int) ((float) prayersDay.isha1 / (float) isha * 100) + " %"));
        isha2TV.setText(String.valueOf((int) ((float) prayersDay.isha2 / (float) isha * 100) + " %"));
        isha3TV.setText(String.valueOf((int) ((float) prayersDay.isha3 / (float) isha * 100) + " %"));
        
    }

    private void addDataSet() {

        updateUI();

        Log.d("Chart Data", "addDataSet started");
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        prayersData[0] = prayersDay.fajr0
                + prayersDay.duhr0
                + prayersDay.asr0
                + prayersDay.mghreb0
                + prayersDay.isha0;

        prayersData[1] = prayersDay.fajr1
                + prayersDay.duhr1
                + prayersDay.asr1
                + prayersDay.mghreb1
                + prayersDay.isha1;

        prayersData[2] = prayersDay.fajr2
                + prayersDay.duhr2
                + prayersDay.asr2
                + prayersDay.mghreb2
                + prayersDay.isha2;

        prayersData[3] = prayersDay.fajr3
                + prayersDay.duhr3
                + prayersDay.asr3
                + prayersDay.mghreb3
                + prayersDay.isha3;

        for(int i = 0; i < prayersData.length; i++){
            yEntrys.add(new PieEntry(prayersData[i] , i));
        }

        for(int i = 1; i < xData.length; i++){
            xEntrys.add(getString(xData[i]));
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "Prayers Data");
        pieDataSet.setSliceSpace(0);
        pieDataSet.setValueTextSize(14);
        pieDataSet.setValueTextColor(Color.BLACK);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.green));
        colors.add(getResources().getColor(R.color.yellow));
        colors.add(getResources().getColor(R.color.red));
        colors.add(getResources().getColor(R.color.dark_grey));

        pieDataSet.setColors(colors);

        //add legend to chart
        LegendEntry inTime = new LegendEntry();
        inTime.label = getString(xData[0]);
        inTime.form = Legend.LegendForm.SQUARE;
        inTime.formColor = getResources().getColor(R.color.green);
        inTime.formSize = 10;

        LegendEntry late = new LegendEntry();
        late.label = getString(xData[1]);
        late.form = Legend.LegendForm.SQUARE;
        late.formColor = getResources().getColor(R.color.yellow);
        late.formSize = 10;

        LegendEntry left = new LegendEntry();
        left.label = getString(xData[2]);
        left.form = Legend.LegendForm.SQUARE;
        left.formColor = getResources().getColor(R.color.red);
        left.formSize = 10;

        LegendEntry notReg = new LegendEntry();
        notReg.label = getString(xData[3]);
        notReg.form = Legend.LegendForm.SQUARE;
        notReg.formColor = getResources().getColor(R.color.dark_grey);
        notReg.formSize = 10;


        List<LegendEntry> legendEntrySet = new ArrayList<>();
        legendEntrySet.add(inTime);
        legendEntrySet.add(late);
        legendEntrySet.add(left);
        legendEntrySet.add(notReg);

        Legend l = mChart.getLegend();
        l.setCustom(legendEntrySet);
        l.setTextColor(getResources().getColor(R.color.white));
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(10f);
        l.setTextSize(14f);
        l.setXEntrySpace(4f);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        mChart.setData(pieData);
        mChart.invalidate();
    }

    private void initChart(){

        mChart = findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setHoleRadius(0f);
        mChart.setRotationAngle(0);

        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

    }

    private void initPrayers(){


        fajr0TV = findViewById(R.id.f0TV);
        fajr1TV = findViewById(R.id.f1TV);
        fajr2TV = findViewById(R.id.f2TV);
        fajr3TV = findViewById(R.id.f3TV);

        duhr0TV = findViewById(R.id.d0TV);
        duhr1TV = findViewById(R.id.d1TV);
        duhr2TV = findViewById(R.id.d2TV);
        duhr3TV = findViewById(R.id.d3TV);


        asr0TV = findViewById(R.id.a0TV);
        asr1TV = findViewById(R.id.a1TV);
        asr2TV = findViewById(R.id.a2TV);
        asr3TV = findViewById(R.id.a3TV);

        mghreb0TV = findViewById(R.id.m0TV);
        mghreb1TV = findViewById(R.id.m1TV);
        mghreb2TV = findViewById(R.id.m2TV);
        mghreb3TV = findViewById(R.id.m3TV);


        isha0TV = findViewById(R.id.i0TV);
        isha1TV = findViewById(R.id.i1TV);
        isha2TV = findViewById(R.id.i2TV);
        isha3TV = findViewById(R.id.i3TV);

    }

}
