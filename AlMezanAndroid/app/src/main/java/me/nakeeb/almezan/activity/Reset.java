package me.nakeeb.almezan.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import me.nakeeb.almezan.R;
import me.nakeeb.almezan.helper.LocaleManager;
import me.nakeeb.almezan.helper.Utils;
import me.nakeeb.almezan.model.DateItem;

/**
 * Created by mamdouhelnakeeb on 12/30/17.
 */

public class Reset extends AppCompatActivity {

    AppCompatSpinner rstSpinner;
    Button yesBtn, noBtn;


    private FirebaseAuth mAuth;

    FirebaseFirestore db;
    FirebaseUser fbUser;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_reset_activity);

        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        Log.d("Base", "attachBaseContext");
    }

    private void initRst(){

        ArrayAdapter<CharSequence> rstArrAdapter = ArrayAdapter.createFromResource(this, R.array.reset, R.layout.spinner_item);

        rstSpinner.setAdapter(rstArrAdapter);

    }


    private void reset(final int i){

        progressDialog.setCancelable(false);


        switch (i){
            case 0:
                resetPrayers();
                resetHandouts();
                resetZekr();
                break;
            case 1:
                resetPrayers();
                break;
            case 2:
                resetHandouts();
                break;
            case 3:
                resetZekr();
                break;
        }



    }

    private void resetPrayers(){


        progressDialog.setMessage("Resetting Prayers... Please wait");
        progressDialog.show();

        long currentMillis = System.currentTimeMillis();

        ArrayList<DateItem> datesArr = Utils.getDates(currentMillis,
                getSharedPreferences("User", MODE_PRIVATE).getLong("prayerTime", currentMillis), new Locale("ar", "EG"));

        for (int i = 0; i < datesArr.size(); i++) {

            // Add a new document with a generated ID
            db.collection("users")
                    .document(fbUser.getUid())
                    .collection("prayers")
                    .document(String.valueOf(Utils.getMillis(datesArr.get(i).timeInMillis, "y")))
                    .collection("months")
                    .document(String.valueOf(Utils.getMillis(datesArr.get(i).timeInMillis, "MM/y")))
                    .collection("days")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (DocumentSnapshot document : task.getResult()) {
                                    Log.d("PrayerData: ", document.getId() + " => " + document.getData());

                                    deleteDoc(document.getReference());
                                }


                            } else {
                                Log.d("Error: ", "Error getting documents: ", task.getException());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("save user: ", "Error adding document", e);

                            Toast.makeText(Reset.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        updateUser("prayerTime", System.currentTimeMillis());

    }

    private void resetHandouts(){


        progressDialog.setMessage("Resetting Handouts... Please wait");
        progressDialog.show();

        long currentMillis = System.currentTimeMillis();

        ArrayList<DateItem> dateItemArrayList = Utils.getYears(currentMillis,
                getSharedPreferences("User", MODE_PRIVATE).getLong("handoutTime", currentMillis), new Locale("ar", "EG"));

        for (int i = 0; i < dateItemArrayList.size(); i++) {


            // Add a new document with a generated ID
            db.collection("users")
                    .document(fbUser.getUid())
                    .collection("handouts")
                    .document(String.valueOf(Utils.getMillis(dateItemArrayList.get(i).timeInMillis, "y")))
                    .collection("months")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {

                                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                    Log.d("HandoutData: ", document.getId() + " => " + document.getData());

                                    deleteDoc(document.getReference());
                                }


                            } else {
                                Log.d("Error: ", "Error getting documents: ", task.getException());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("save user: ", "Error adding document", e);
                            Toast.makeText(Reset.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        updateUser("handoutTime", System.currentTimeMillis());

    }

    private void resetZekr(){


        progressDialog.setMessage("Resetting Zekr... Please wait");
        progressDialog.show();

        // Add a new document with a generated ID
        db.collection("users")
                .document(fbUser.getUid())
                .collection("zekr")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()){

                            for (DocumentSnapshot document: task.getResult()){
                                Log.d("ZekrData: ", document.getId() + " => " + document.getData());


                                deleteDoc(document.getReference());
                            }

                            updateUser("zekrTime", System.currentTimeMillis());
                        }
                        else {
                            Log.d("Error: ", "Error getting documents: ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("save user: ", "Error adding document", e);

                        Toast.makeText(Reset.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void deleteDoc(DocumentReference documentReference){

        documentReference
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Delete Doc", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Delete Doc", "Error deleting document", e);
                    }
                });
    }

    private void updateUser(final String key, final long value){

        Map<String, Object> user = new HashMap<>();

        user.put(key, value);


        // Add a new document with a generated ID
        db.collection("users")
                .document(fbUser.getUid())
                .update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("save user: ", "DocumentSnapshot added ");

                        progressDialog.hide();

                        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putLong(key, value);

                        editor.apply();

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

        MobileAds.initialize(this, "ca-app-pub-5330928698678581~9643351697");

        AdView mAdView;

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
