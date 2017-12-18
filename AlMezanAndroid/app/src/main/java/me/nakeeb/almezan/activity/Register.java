package me.nakeeb.almezan.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.nakeeb.almezan.R;

/**
 * Created by mamdouhelnakeeb on 12/15/17.
 */

public class Register extends AppCompatActivity {

    EditText nameET, emailET, passET, ageET, imgET;
    Button regBtn, regToLineBtn;

    private FirebaseAuth mAuth;

    SimpleDateFormat formatDate;
    Calendar dateCalender = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        mAuth = FirebaseAuth.getInstance();



        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        passET = findViewById(R.id.passET);
        ageET = findViewById(R.id.ageET);

        regBtn = findViewById(R.id.regBtn);
        regToLineBtn = findViewById(R.id.regToLoginBtn);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        regToLineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Register.this, Register.class);
//                startActivity(intent);
                finish();
            }
        });

        configAge();

        loadADs();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void configAge(){

        ageET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(Register.this, d, dateCalender.get(Calendar.YEAR), dateCalender.get(Calendar.MONTH), dateCalender.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateCalender.set(Calendar.YEAR, year);
            dateCalender.set(Calendar.MONTH, monthOfYear);
            dateCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            formatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

            ageET.setText(formatDate.format(dateCalender.getTime()));
        }
    };

    private void register(){

        String name = nameET.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        String password = passET.getText().toString().trim();
        String dob = ageET.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dob.isEmpty()) {
            Toast.makeText(this, "Please enter your age", Toast.LENGTH_SHORT).show();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Firebase Res", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            saveUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            updateUI(null);

                            String err = "";

                            switch (((FirebaseAuthException) task.getException()).getErrorCode()){

                                case "ERROR_INVALID_EMAIL":
                                    err = "Please enter a valid Email";
                                    break;
                                case "ERROR_INVALID_PASSWORD":
                                    err = "Please enter a valid password";
                                    break;
                                case "ERROR_ACCOUNT_EXISTED_WITH_DIFFERENT_CREDENTIALS":
                                    err = "This account is already existed, try to login!";
                                    break;
                                case "ERROR_WEAK_PASSWORD":
                                    err = "Please enter a valid password";
                                    break;
                            }

                            if (err.isEmpty()){
                                err = task.getException().getMessage();
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);

                            builder.setMessage(err)
                                    .setTitle(getResources().getString(R.string.app_name))
                                    .setNegativeButton("Dismiss", null);

                            AlertDialog dialog = builder.create();
                            dialog.show();

                            Log.w("Firebase res", "signInWithEmail:failure", task.getException());
                        }

                        // ...
                    }
                });

    }


    private void updateUI (FirebaseUser currentUser){

        if (currentUser == null){
            FirebaseAuth.getInstance().signOut();
            return;
        }

        Intent intent = new Intent(this, Landing.class);
        startActivity(intent);

    }

    private void saveUser (){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser fbUser = mAuth.getCurrentUser();

        final long startTime = System.currentTimeMillis();

        Map<String, Object> user = new HashMap<>();
        user.put("name", nameET.getText().toString().trim());
        user.put("email", emailET.getText().toString().trim());
        user.put("dob", ageET.getText().toString().trim());
        user.put("startTime", startTime);

        // Add a new document with a generated ID
        db.collection("users")
                .document(fbUser.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("save user: ", "DocumentSnapshot added ");

                        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();

                        editor.putString("name", nameET.getText().toString().trim());
                        editor.putString("email", emailET.getText().toString().trim());
                        editor.putLong("startTIme", startTime);

                        editor.apply();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("save user: ", "Error adding document", e);
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
