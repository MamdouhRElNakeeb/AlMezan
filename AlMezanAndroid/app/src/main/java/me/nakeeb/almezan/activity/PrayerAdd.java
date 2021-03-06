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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.nakeeb.almezan.helper.DateRVAdapter;
import me.nakeeb.almezan.R;
import me.nakeeb.almezan.helper.Utils;
import me.nakeeb.almezan.model.DateItem;
import me.nakeeb.almezan.model.PrayersDay;

/**
 * Created by mamdouhelnakeeb on 12/13/17.
 */

public class PrayerAdd extends BaseActivity {

    RadioGroup fajrRG, duhrRG, asrRG, mghrebRG, ishaRG;
    RadioButton f0, f1, f2, f3, d0, d1, d2, d3, a0, a1, a2, a3, m0, m1, m2, m3, i0, i1, i2, i3;

    int fajrStats = -1, duhrStats = -1, asrStats = -1, mghrebStats = -1, ishaStats = -1;

    RecyclerView dateRV;
    DateRVAdapter dateRVAdapter;
    LinearLayoutManager linearLayoutManager;
    
    int datePos = 0;

    Button saveBtn, editBtn;

    ArrayList<DateItem> datesArr;

    private FirebaseAuth mAuth;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.prayer_add_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();

        dateRV = findViewById(R.id.dateRV);

        saveBtn = findViewById(R.id.saveBtn);
        editBtn = findViewById(R.id.editBtn);

        initRG();

        getDates();

        findViewById(R.id.datePrevIB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datePos <= 0)
                    return;

                datePos--;
                dateRV.smoothScrollToPosition(datePos);

                loadPrayers();
            }
        });

        findViewById(R.id.dateNextIB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datePos >= datesArr.size() - 1)
                    return;

                datePos++;
                dateRV.smoothScrollToPosition(datePos);

                loadPrayers();
            }
        });


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

                Log.d("posss", String.valueOf(datePos) + " -- " + String.valueOf(datesArr.size() - 1));

                loadPrayers();


                return targetPosition;
            }
        };


        snapHelper.attachToRecyclerView(dateRV);
        dateRV.setOnFlingListener(snapHelper);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dateRV.setNestedScrollingEnabled(true);

                savePrayer();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dateRV.setNestedScrollingEnabled(false);

                editBtn.setAlpha((float) 0.3);
                editBtn.setEnabled(false);

                saveBtn.setAlpha(1);
                saveBtn.setEnabled(true);

                editMode(true);
                updateUI(true);

            }
        });

    }

    private void getDates(){

        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);

        long currentMillis = System.currentTimeMillis();
        initRV(Utils.getDayMillis(currentMillis), Utils.getDayMillis(prefs.getLong("prayerTime", currentMillis)));

    }

    private void updateUI(boolean edit){

        if (datePos != datesArr.size() - 1){
            editBtn.setAlpha((float) 0.3);
            editBtn.setEnabled(false);

            saveBtn.setAlpha((float) 0.3);
            saveBtn.setEnabled(false);
        }
        else {
            if (edit) {
                editBtn.setAlpha((float) 0.3);
                editBtn.setEnabled(false);

                saveBtn.setAlpha(1);
                saveBtn.setEnabled(true);
            }
            else {
                editBtn.setAlpha(1);
                editBtn.setEnabled(true);

                saveBtn.setAlpha((float) 0.3);
                saveBtn.setEnabled(false);
            }
        }

    }

    private void editMode(boolean edit){

        f0.setEnabled(edit);
        f1.setEnabled(edit);
        f2.setEnabled(edit);
        f3.setEnabled(edit);
        d0.setEnabled(edit);
        d1.setEnabled(edit);
        d2.setEnabled(edit);
        d3.setEnabled(edit);
        a0.setEnabled(edit);
        a1.setEnabled(edit);
        a2.setEnabled(edit);
        a3.setEnabled(edit);
        m0.setEnabled(edit);
        m1.setEnabled(edit);
        m2.setEnabled(edit);
        m3.setEnabled(edit);
        i0.setEnabled(edit);
        i1.setEnabled(edit);
        i2.setEnabled(edit);
        i3.setEnabled(edit);

    }

    private void initRV(long currentMillis, long oldMillis){

        Log.d("currentTime", String.valueOf(currentMillis));
        Log.d("oldTime", String.valueOf(oldMillis));

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        dateRV.setLayoutManager(linearLayoutManager);

        dateRV.setItemAnimator(new DefaultItemAnimator());

        datesArr = Utils.getDates(currentMillis, oldMillis, Locale.getDefault());

        dateRVAdapter = new DateRVAdapter(this, datesArr);
        dateRV.setAdapter(dateRVAdapter);

        datePos = datesArr.size() - 1;

        dateRV.scrollToPosition(datePos);

        loadPrayers();

    }

    private void updateRG(PrayersDay prayersDay){


        switch (prayersDay.fajr){
            case 0:
                fajrRG.check(R.id.f0);
                break;
            case 1:
                fajrRG.check(R.id.f1);
                break;
            case 2:
                fajrRG.check(R.id.f2);
                break;
            case 3:
                fajrRG.check(R.id.f3);
                break;
        }

        switch (prayersDay.duhr){
            case 0:
                duhrRG.check(R.id.d0);
                break;
            case 1:
                duhrRG.check(R.id.d1);
                break;
            case 2:
                duhrRG.check(R.id.d2);
                break;
            case 3:
                duhrRG.check(R.id.d3);
                break;
        }

        switch (prayersDay.asr){
            case 0:
                asrRG.check(R.id.a0);
                break;
            case 1:
                asrRG.check(R.id.a1);
                break;
            case 2:
                asrRG.check(R.id.a2);
                break;
            case 3:
                asrRG.check(R.id.a3);
                break;
        }

        switch (prayersDay.mghreb){
            case 0:
                mghrebRG.check(R.id.m0);
                break;
            case 1:
                mghrebRG.check(R.id.m1);
                break;
            case 2:
                mghrebRG.check(R.id.m2);
                break;
            case 3:
                mghrebRG.check(R.id.m3);
                break;
        }

        switch (prayersDay.isha){
            case 0:
                ishaRG.check(R.id.i0);
                break;
            case 1:
                ishaRG.check(R.id.i1);
                break;
            case 2:
                ishaRG.check(R.id.i2);
                break;
            case 3:
                ishaRG.check(R.id.i3);
                break;
        }

    }

    private void clearRG(){

        fajrRG.clearCheck();
        duhrRG.clearCheck();
        asrRG.clearCheck();
        mghrebRG.clearCheck();
        ishaRG.clearCheck();
    }

    private void initRG(){

        fajrRG = findViewById(R.id.fajrRG);
        duhrRG = findViewById(R.id.duhrRG);
        asrRG = findViewById(R.id.asrRG);
        mghrebRG = findViewById(R.id.mghrebRG);
        ishaRG = findViewById(R.id.ishaRG);

        f0 = findViewById(R.id.f0);
        f1 = findViewById(R.id.f1);
        f2 = findViewById(R.id.f2);
        f3 = findViewById(R.id.f3);

        d0 = findViewById(R.id.d0);
        d1 = findViewById(R.id.d1);
        d2 = findViewById(R.id.d2);
        d3 = findViewById(R.id.d3);

        a0 = findViewById(R.id.a0);
        a1 = findViewById(R.id.a1);
        a2 = findViewById(R.id.a2);
        a3 = findViewById(R.id.a3);

        m0 = findViewById(R.id.m0);
        m1 = findViewById(R.id.m1);
        m2 = findViewById(R.id.m2);
        m3 = findViewById(R.id.m3);

        i0 = findViewById(R.id.i0);
        i1 = findViewById(R.id.i1);
        i2 = findViewById(R.id.i2);
        i3 = findViewById(R.id.i3);


        fajrRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (radioGroup.getCheckedRadioButtonId()){

                    case R.id.f0:
                        fajrStats = 0;
                        break;

                    case R.id.f1:
                        fajrStats = 1;
                        break;

                    case R.id.f2:
                        fajrStats = 2;
                        break;

                    case R.id.f3:
                        fajrStats = 3;
                        break;

                }
            }
        });

        duhrRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (radioGroup.getCheckedRadioButtonId()){

                    case R.id.d0:
                        duhrStats = 0;
                        break;

                    case R.id.d1:
                        duhrStats = 1;
                        break;

                    case R.id.d2:
                        duhrStats = 2;
                        break;

                    case R.id.d3:
                        duhrStats = 3;
                        break;

                }
            }
        });

        asrRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (radioGroup.getCheckedRadioButtonId()){

                    case R.id.a0:
                        asrStats = 0;
                        break;

                    case R.id.a1:
                        asrStats = 1;
                        break;

                    case R.id.a2:
                        asrStats = 2;
                        break;

                    case R.id.a3:
                        asrStats = 3;
                        break;

                }
            }
        });

        mghrebRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (radioGroup.getCheckedRadioButtonId()){

                    case R.id.m0:
                        mghrebStats = 0;
                        break;

                    case R.id.m1:
                        mghrebStats = 1;
                        break;

                    case R.id.m2:
                        mghrebStats = 2;
                        break;

                    case R.id.m3:
                        mghrebStats = 3;
                        break;

                }
            }
        });

        ishaRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch (radioGroup.getCheckedRadioButtonId()){

                    case R.id.i0:
                        ishaStats = 0;
                        break;

                    case R.id.i1:
                        ishaStats = 1;
                        break;

                    case R.id.i2:
                        ishaStats = 2;
                        break;

                    case R.id.i3:
                        ishaStats = 3;
                        break;

                }
            }
        });

    }

    private void savePrayer (){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("fajr",fajrStats);
        user.put("duhr", duhrStats);
        user.put("asr", asrStats);
        user.put("mghreb", mghrebStats);
        user.put("isha", ishaStats);

        long currentMillis = System.currentTimeMillis();

        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("prayers")
                .document(String.valueOf(Utils.getMillis(currentMillis, "y")))
                .collection("months")
                .document(String.valueOf(Utils.getMillis(currentMillis, "MM/y")))
                .collection("days")
                .document(String.valueOf(Utils.getMillis(currentMillis, "dd/MM/y")))
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("save user: ", "DocumentSnapshot added ");
                        updateUI(false);
                        editMode(false);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("save user: ", "Error adding document", e);
                    }
                });
    }

    private void loadPrayers(){

        clearRG();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null)
            return;

        DocumentReference docRef = db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("prayers")
                .document(String.valueOf(Utils.getMillis(datesArr.get(datePos).timeInMillis, "y")))
                .collection("months")
                .document(String.valueOf(Utils.getMillis(datesArr.get(datePos).timeInMillis, "MM/y")))
                .collection("days")
                .document(String.valueOf(Utils.getMillis(datesArr.get(datePos).timeInMillis, "dd/MM/y")));

        Log.d("timeInMillisOfUser", String.valueOf(datesArr.get(datePos).timeInMillis));

        if (datePos == datesArr.size() - 1){
            editMode(true);
            updateUI(true);
        }
        else {
            editMode(false);
            updateUI(false);
        }

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists())
                    return;

                Log.d("PrayerDatadsgd: ", documentSnapshot.getId() + " => " + documentSnapshot.getData());

                if (datePos == datesArr.size() - 1){
                    editMode(false);
                    updateUI(false);
                }

                PrayersDay prayersDay = new PrayersDay();

                prayersDay.timeInMillis = Long.parseLong(documentSnapshot.getId());
                prayersDay.fajr = Integer.parseInt(documentSnapshot.get("fajr").toString());
                prayersDay.duhr = Integer.parseInt(documentSnapshot.get("duhr").toString());
                prayersDay.asr = Integer.parseInt(documentSnapshot.get("asr").toString());
                prayersDay.mghreb = Integer.parseInt(documentSnapshot.get("mghreb").toString());
                prayersDay.isha = Integer.parseInt(documentSnapshot.get("isha").toString());

                updateRG(prayersDay);

            }
        });


    }

}