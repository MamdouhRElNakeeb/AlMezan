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

public class MonthlyStats extends AppCompatActivity {

    TextView jan0, jan1, jan2, jan3;
    TextView feb0, feb1, feb2, feb3;
    TextView march0, march1, march2, march3;
    TextView april0, april1, april2, april3;
    TextView may0, may1, may2, may3;
    TextView jun0, jun1, jun2, jun3;
    TextView jul0, jul1, jul2, jul3;
    TextView aug0, aug1, aug2, aug3;
    TextView sep0, sep1, sep2, sep3;
    TextView oct0, oct1, oct2, oct3;
    TextView nov0, nov1, nov2, nov3;
    TextView dec0, dec1, dec2, dec3;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthly_stats_activity);

        mAuth = FirebaseAuth.getInstance();

        initViews();


        

    }

    private void getPrayersStats(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference docRef = db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("prayers");

        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Log.d("PrayerData: ", document.getId() + " => " + document.getData());

                        PrayersDay prayersDay = new PrayersDay();
                        prayersDay.fajr = Integer.parseInt(document.get("fajr").toString());
                        prayersDay.duhr = Integer.parseInt(document.get("duhr").toString());
                        prayersDay.asr = Integer.parseInt(document.get("asr").toString());
                        prayersDay.mghreb = Integer.parseInt(document.get("mghreb").toString());
                        prayersDay.isha = Integer.parseInt(document.get("isha").toString());

//                        updatePrayers(prayersDay);

                    }
//                    addDataSet();
                } else {
                    Log.d("Error: ", "Error getting documents: ", task.getException());
                }
            }
        });

    }
    
    private void initViews(){
        
        jan0 = findViewById(R.id.jan0);
        jan1 = findViewById(R.id.jan1);
        jan2 = findViewById(R.id.jan2);
        jan3 = findViewById(R.id.jan3);

        feb0 = findViewById(R.id.feb0);
        feb1 = findViewById(R.id.feb1);
        feb2 = findViewById(R.id.feb2);
        feb3 = findViewById(R.id.feb3);

        march0 = findViewById(R.id.march0);
        march1 = findViewById(R.id.march1);
        march2 = findViewById(R.id.march2);
        march3 = findViewById(R.id.march3);

        april0 = findViewById(R.id.april0);
        april1 = findViewById(R.id.april1);
        april2 = findViewById(R.id.april2);
        april3 = findViewById(R.id.april3);

        may0 = findViewById(R.id.may0);
        may1 = findViewById(R.id.may1);
        may2 = findViewById(R.id.may2);
        may3 = findViewById(R.id.may3);

        jun0 = findViewById(R.id.jun0);
        jun1 = findViewById(R.id.jun1);
        jun2 = findViewById(R.id.jun2);
        jun3 = findViewById(R.id.jun3);

        jul0 = findViewById(R.id.jul0);
        jul1 = findViewById(R.id.jul1);
        jul2 = findViewById(R.id.jul2);
        jul3 = findViewById(R.id.jul3);

        aug0 = findViewById(R.id.aug0);
        aug1 = findViewById(R.id.aug1);
        aug2 = findViewById(R.id.aug2);
        aug3 = findViewById(R.id.aug3);

        sep0 = findViewById(R.id.sep0);
        sep1 = findViewById(R.id.sep1);
        sep2 = findViewById(R.id.sep2);
        sep3 = findViewById(R.id.sep3);

        oct0 = findViewById(R.id.oct0);
        oct1 = findViewById(R.id.oct1);
        oct2 = findViewById(R.id.oct2);
        oct3 = findViewById(R.id.oct3);

        nov0 = findViewById(R.id.nov0);
        nov1 = findViewById(R.id.nov1);
        nov2 = findViewById(R.id.nov2);
        nov3 = findViewById(R.id.nov3);

        dec0 = findViewById(R.id.dec0);
        dec1 = findViewById(R.id.dec1);
        dec2 = findViewById(R.id.dec2);
        dec3 = findViewById(R.id.dec3);
    }
}
