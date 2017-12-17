package me.nakeeb.almezan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mamdouhelnakeeb on 12/15/17.
 */

public class ZekrDetails extends AppCompatActivity {

    int doaa = 0;

    TextView zekrTitleTV, zekrCountTV;
    ImageButton zekrAddBtn;
    Button saveCountBtn;

    int count = 0;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zekr_details_activity);

        mAuth = FirebaseAuth.getInstance();

        doaa = getIntent().getExtras().getInt("doaa", 0);

        zekrTitleTV = findViewById(R.id.zekrTitleTV);
        zekrCountTV = findViewById(R.id.zekrCountTV);
        zekrAddBtn = findViewById(R.id.zekrAddBtn);
        saveCountBtn = findViewById(R.id.saveCountBtn);
        saveCountBtn.setAlpha((float) 0.3);
        saveCountBtn.setEnabled(false);

        zekrTitleTV.setText(Consts.Doaa[doaa]);

        getZekrTotal();

        zekrAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;

                zekrCountTV.setText(String.valueOf(count));

                saveCountBtn.setAlpha((float) 1.0);
                saveCountBtn.setEnabled(true);
            }
        });

        saveCountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveZekrCount();

            }
        });

    }

    private void saveZekrCount(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> zekr = new HashMap<>();
        zekr.put("count", count);

        // Add a new document with a generated ID
        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("zekr")
                .document(String.valueOf(doaa))
                .set(zekr)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("save user: ", "DocumentSnapshot added ");
                        getZekrTotal();
                        saveCountBtn.setAlpha((float) 0.3);
                        saveCountBtn.setEnabled(false);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("save user: ", "Error adding document", e);
                    }
                });

    }

    private void getZekrTotal(){

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .collection("zekr")
                .document(String.valueOf(doaa));

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (!documentSnapshot.exists())
                    return;
                Log.d("docCon", documentSnapshot.getData().toString());
                Zekr zekr = new Zekr();// = documentSnapshot.toObject(User.class);
                zekr.doaa = doaa;
                zekr.count = Integer.parseInt(documentSnapshot.get("count").toString());

                count = zekr.count;
                zekrCountTV.setText(String.valueOf(count));

                calcZekrStats(zekr);

            }
        });

    }

    private void calcZekrStats(Zekr zekr){

        TextView zekrDailyTV = findViewById(R.id.zekrDailyTV);
        TextView zekrMonthlyTV = findViewById(R.id.zekrMonthlyTV);
        TextView zekrYearlyTV = findViewById(R.id.zekrYearlyTV);

        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);

        if (!prefs.contains("dob")){
            Toast.makeText(this, "Your age isn't registered!", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Utils.calcAge(prefs.getString("dob", "0/0/0"));

        zekrYearlyTV.setText(String.valueOf(zekr.count / age));
        zekrMonthlyTV.setText(String.valueOf(zekr.count / age / 12));
        zekrDailyTV.setText(String.valueOf(zekr.count / age / 365));

    }

}
