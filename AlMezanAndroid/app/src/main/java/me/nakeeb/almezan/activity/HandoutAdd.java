package me.nakeeb.almezan.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.nakeeb.almezan.R;
import me.nakeeb.almezan.helper.Utils;
import me.nakeeb.almezan.model.DateItem;
import me.nakeeb.almezan.model.Handout;

/**
 * Created by mamdouhelnakeeb on 12/15/17.
 */

public class HandoutAdd extends AppCompatActivity {

    EditText amountET;
    TextView handoutsTotalTV;

    Button saveBtn, editBtn;

    float handoutsTotal = 0;
    float monthHandoutsTotal = 0;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handout_add_activity);

        initViews();

        getHandoutsTotal();

        updatePeriod();

        loadADs();
    }

    private void saveHandout(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        float amount = Float.parseFloat(amountET.getText().toString().trim());

        Map<String, Object> handout = new HashMap<>();
        handout.put("amount", amount + monthHandoutsTotal);

        // Add a new document with a generated ID
        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("handouts")
                .document(String.valueOf(Utils.getMillis(System.currentTimeMillis(), "y")))
                .collection("months")
                .document(String.valueOf(Utils.getMillis(System.currentTimeMillis(), "MM/y")))
                .set(handout)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("save user: ", "DocumentSnapshot added ");

                        saveBtn.setAlpha((float) 0.3);
                        saveBtn.setEnabled(false);

                        amountET.setText("0");

                        getHandoutsTotal();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("save user: ", "Error adding document", e);
                    }
                });

    }

    private void getHandoutsTotal(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        long currentMillis = System.currentTimeMillis();
//        int yearsNo = Utils.yearsNo(currentMillis, getSharedPreferences("startTime", MODE_PRIVATE).getLong("", currentMillis));

        ArrayList<DateItem> dateItemArrayList = Utils.getYears(currentMillis, getSharedPreferences("startTime", MODE_PRIVATE).getLong("", currentMillis), new Locale("ar", "EG"));

        handoutsTotal = 0;

        Log.d("yearsNoDates", String.valueOf(dateItemArrayList.size()));

        for (int i = 0; i < dateItemArrayList.size(); i++){


            CollectionReference docRef = db.collection("users")
                    .document(mAuth.getCurrentUser().getUid())
                    .collection("handouts")
                    .document(String.valueOf(Utils.getMillis(dateItemArrayList.get(i).timeInMillis, "y")))
                    .collection("months");

            Log.d("timeMillisYear", String.valueOf(dateItemArrayList.get(i).timeInMillis));

            docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {

                        for (DocumentSnapshot document : task.getResult()) {
                            Log.d("PrayerData: ", document.getId() + " => " + document.getData());

                            Handout handout = new Handout();
                            handout.amount = Float.parseFloat(document.get("amount").toString());
                            handout.timeInMillis = Long.parseLong(document.getId());

                            if (Utils.getMillis(System.currentTimeMillis(), "MM/y") == handout.timeInMillis)
                                monthHandoutsTotal = handout.amount;

                            updateHandoutsTotal(handout);

                        }

                    } else {
                        Log.d("Error: ", "Error getting documents: ", task.getException());
                    }
                }
            });


        }

//        CollectionReference docRef = db.collection("users")
//                .document(mAuth.getCurrentUser().getUid())
//                .collection("handouts");
//
//        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//
//                    for (DocumentSnapshot document : task.getResult()) {
//                        Log.d("PrayerData: ", document.getId() + " => " + document.getData());
//
//                        Handout handout = new Handout();
//                        handout.amount = Float.parseFloat(document.get("amount").toString());
//                        handout.timeInMillis = Long.parseLong(document.getId());
//
//                        if (Utils.getMillis(System.currentTimeMillis(), "MM/y") == handout.timeInMillis)
//                            monthHandoutsTotal = handout.amount;
//
//                        updateHandoutsTotal(handout);
//
//                    }
//
//                } else {
//                    Log.d("Error: ", "Error getting documents: ", task.getException());
//                }
//            }
//        });

    }

    private void updateHandoutsTotal(Handout handout){

        handoutsTotal += handout.amount;

        handoutsTotalTV.setText(String.valueOf(handoutsTotal));
    }

    private void updatePeriod(){

        ArrayList<Integer> periodArr;

        long currentMillis = System.currentTimeMillis();

        periodArr = Utils.getDaysMonthsYearsNo(currentMillis,
                getSharedPreferences("User", MODE_PRIVATE).getLong("startTime", currentMillis));

        TextView daysCounterTV = findViewById(R.id.daysCounterTV);
        TextView monthsCounterTV = findViewById(R.id.monthsCounterTV);
        TextView yearsCounterTV = findViewById(R.id.yearsCounterTV);

        daysCounterTV.setText(String.valueOf(periodArr.get(0)));
        monthsCounterTV.setText(String.valueOf(periodArr.get(1)));
        yearsCounterTV.setText(String.valueOf(periodArr.get(2)));

    }

    private void initViews(){

        amountET = findViewById(R.id.amountET);
        handoutsTotalTV = findViewById(R.id.handoutsTotalTV);
        saveBtn = findViewById(R.id.saveBtn);
        editBtn = findViewById(R.id.editBtn);

        saveBtn.setAlpha((float) 0.3);
        saveBtn.setEnabled(false);



        amountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!editable.toString().isEmpty()){

                    saveBtn.setAlpha((float) 1);
                    saveBtn.setEnabled(true);
                }
                else {

                    saveBtn.setAlpha((float) 0.3);
                    saveBtn.setEnabled(false);
                }
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveHandout();

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