package me.nakeeb.almezan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Created by mamdouhelnakeeb on 12/15/17.
 */

public class ZekrStats extends AppCompatActivity {


    TextView doaa0TV, doaa1TV, doaa2TV, doaa3TV, doaa4TV, doaa5TV, doaa6TV, doaa7TV, doaa8TV;

    FirebaseAuth mAuth;

    int daysNo = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zekr_stats_activity);

        long currentMillis = System.currentTimeMillis();

        daysNo = Utils.getDaysMonthsYearsNo(currentMillis, getSharedPreferences("User", MODE_PRIVATE).getLong("startTime", currentMillis)).get(0);

        Log.d("currentMilllis", String.valueOf(currentMillis));
        Log.d("oldtMilllis", String.valueOf(getSharedPreferences("User", MODE_PRIVATE).getLong("startTime", currentMillis)));
        if (daysNo == 0)
            daysNo = 1;
        Log.d("daysNoTotal", String.valueOf(daysNo));

        mAuth = FirebaseAuth.getInstance();

        initViews();

        getZekrStats();
    }

    private void getZekrStats(){

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

                        int countPerDay = (int) (Float.parseFloat(document.get("count").toString()) / (float) daysNo);

                        switch (Integer.parseInt(document.getId())){

                            case 0:
                                doaa0TV.setText(String.valueOf(countPerDay));
                                break;
                            case 1:
                                doaa1TV.setText(String.valueOf(countPerDay));
                                break;
                            case 2:
                                doaa2TV.setText(String.valueOf(countPerDay));
                                break;
                            case 3:
                                doaa3TV.setText(String.valueOf(countPerDay));
                                break;
                            case 4:
                                doaa4TV.setText(String.valueOf(countPerDay));
                                break;
                            case 5:
                                doaa5TV.setText(String.valueOf(countPerDay));
                                break;
                            case 6:
                                doaa6TV.setText(String.valueOf(countPerDay));
                                break;
                            case 7:
                                doaa7TV.setText(String.valueOf(countPerDay));
                                break;
                            case 8:
                                doaa8TV.setText(String.valueOf(countPerDay));
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
}
