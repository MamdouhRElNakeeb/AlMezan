package me.nakeeb.almezan;

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
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by mamdouhelnakeeb on 12/12/17.
 */

public class Login extends AppCompatActivity {

    EditText emailET, passET;
    Button loginBtn, loginToRegBtn;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mAuth = FirebaseAuth.getInstance();


//        FirebaseAuth.getInstance().signOut();


//        ArrayList<String> temp =  Utils.getDate(1513306115068L, "dd-MMM-yyyy");
//
//        Log.d("miladi", temp.get(0));
//        Log.d("hegri", temp.get(1));

//        MiladiHegriDate miladiHegriDate = new MiladiHegriDate(1, 6, 1995, true);

        emailET = findViewById(R.id.emailET);
        passET = findViewById(R.id.passET);
        loginBtn = findViewById(R.id.loginBtn);
        loginToRegBtn = findViewById(R.id.loginToRegBtn);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        loginToRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void login(){

        final String email = emailET.getText().toString().trim();
        String password = passET.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Firebase res", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
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
                                    err = "Wrong password!";
                                    break;
                                case "ERROR_USER_NOT_FOUND":
                                    err = "User not found";
                                    break;
                            }

                            if (err.isEmpty()){
                                err = task.getException().getMessage();
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

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

        getUser();
        Intent intent = new Intent(this, Landing.class);
        startActivity(intent);
        finish();

    }

    private void getUser(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference users = db.collection("users").document(mAuth.getCurrentUser().getUid());


        users.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                    Log.d("UserID", documentSnapshot.getId());
                else
                    return;
                Log.d("docCon", documentSnapshot.toString());
                User user = new User();// = documentSnapshot.toObject(User.class);
                user.name = documentSnapshot.getString("name");
                user.email = documentSnapshot.getString("email");
                user.dob = documentSnapshot.getString("dob");
                user.startTime = documentSnapshot.getLong("startTime");

                Log.d("name", user.name);
                Log.d("startTime", String.valueOf(user.startTime));

                SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("name", user.name);
                editor.putString("email", user.email);
                editor.putString("dob", user.dob);
                editor.putLong("startTime", user.startTime);

                editor.apply();

            }
        });

    }
}
