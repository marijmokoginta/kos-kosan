package com.sibkelompoke.kost;

import static android.content.ContentValues.TAG;
import static com.sibkelompoke.kost.constant.KostKonstan.GUEST;
import static com.sibkelompoke.kost.constant.KostKonstan.MISSING;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sibkelompoke.kost.model.User;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    private final String TAG = "SplashScreen";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        TextView gagalMemuat = findViewById(R.id.gagalMemuat);

        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userId = mSettings.getString("userId", MISSING);
        Log.i(TAG, "user id : " + userId);

        db.collection("users").document(userId).get()
                .addOnSuccessListener(document -> {
                    if (document.getString("username") != null) {
                        user = document.toObject(User.class);
                        user.setId(document.getId());
                    } else {
                        Log.i(TAG, "user id tidak ditemukan");
                        user = new User();
                        user.setRole(GUEST);
                        user.setId(userId);
                        user.setUsername(GUEST);
                    }
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();
                    }, 2000);
                }).addOnFailureListener(e -> {
                    gagalMemuat.setVisibility(View.VISIBLE);
                });
    }
}