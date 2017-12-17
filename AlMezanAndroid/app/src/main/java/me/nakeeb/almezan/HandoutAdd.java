package me.nakeeb.almezan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import java.util.Map;

/**
 * Created by mamdouhelnakeeb on 12/15/17.
 */

public class HandoutAdd extends AppCompatActivity {

    EditText amountET;
    TextView handoutsTotalTV;

    Button saveBtn, editBtn;

    float handoutsTotal = 0;
    float dayHandoutsTotal = 0;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handout_add_activity);

        initViews();

        getHandoutsTotal();

        updatePeriod();

    }

    private void saveHandout(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        float amount = Float.parseFloat(amountET.getText().toString().trim());

        Map<String, Object> handout = new HashMap<>();
        handout.put("amount", amount + dayHandoutsTotal);

        // Add a new document with a generated ID
        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("handouts")
                .document(String.valueOf(Utils.getDayMillis(System.currentTimeMillis())))
                .set(handout)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("save user: ", "DocumentSnapshot added ");
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

        CollectionReference docRef = db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("handouts");

        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    handoutsTotal = 0;
                    for (DocumentSnapshot document : task.getResult()) {
                        Log.d("PrayerData: ", document.getId() + " => " + document.getData());

                        Handout handout = new Handout();
                        handout.amount = Float.parseFloat(document.get("amount").toString());
                        handout.dayInMillis = Long.parseLong(document.getId());

                        if (Utils.getDayMillis(System.currentTimeMillis()) == handout.dayInMillis)
                            dayHandoutsTotal += handout.amount;

                        updateHandoutsTotal(handout);

                    }

                } else {
                    Log.d("Error: ", "Error getting documents: ", task.getException());
                }
            }
        });

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

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveHandout();
            }
        });

    }
}