package com.sibkelompoke.kost;

import static com.sibkelompoke.kost.util.KostKonstan.GUEST;
import static com.sibkelompoke.kost.util.KostKonstan.MISSING;
import static com.sibkelompoke.kost.util.KostKonstan.USER;

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

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sibkelompoke.kost.activity.MainActivity;
import com.sibkelompoke.kost.model.OrderKost;
import com.sibkelompoke.kost.model.Tagihan;
import com.sibkelompoke.kost.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
                    user = document.toObject(User.class);
                    if (user != null) {
                        user.setId(document.getId());
                        getOrder();
                    } else {
                        Log.i(TAG, "user id tidak ditemukan : " + userId);
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
                    e.printStackTrace();
                    gagalMemuat.setVisibility(View.VISIBLE);
                });
    }

    private void getOrder() {
        if (user.getRole().equals(USER)) {
            db.collection("orderKost").whereEqualTo("userId", user.getId()).addSnapshotListener(((value, error) -> {
                if (value != null) {
                    for (QueryDocumentSnapshot snapshot : value) {
                        OrderKost orderKost = snapshot.toObject(OrderKost.class);
                        getTagihan(orderKost.getOrderId());
                    }
                }
            }));
        }
    }

    private void getTagihan(String orderId) {
        db.collection("tagihan").whereEqualTo("orderId", orderId).get().addOnSuccessListener(documentSnapshots -> {
            ArrayList<Integer> jumlahTagihan = new ArrayList<>();
            int jmlTagihan = 0;
            Tagihan tagihan = new Tagihan();
            for (QueryDocumentSnapshot snapshot : documentSnapshots) {
                tagihan = snapshot.toObject(Tagihan.class);
                jumlahTagihan.add(tagihan.getJumlahTagihan());
            }

            if (jumlahTagihan.size() > 1) {
                for (int i = 0; i < jumlahTagihan.size() - 1; i++) {
                    for (int j = i + 1; j < jumlahTagihan.size(); j++) {
                        if (jumlahTagihan.get(i) > jumlahTagihan.get(j)) {
                            jmlTagihan = jumlahTagihan.get(i);
                        } else {
                            jmlTagihan = jumlahTagihan.get(j);
                        }
                    }
                }
            } else {
                jmlTagihan = tagihan.getJumlahTagihan();
            }

            if (tagihan.getJumlahTagihan() == jmlTagihan) {
                Date date = tagihan.getTanggalTagihan();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                Date currentDate = new Date(System.currentTimeMillis());
                Calendar currentCalendar = Calendar.getInstance();
                currentCalendar.setTime(currentDate);

                if (currentCalendar.get(Calendar.MONTH) > calendar.get(Calendar.MONTH)) {
                    int pastMonth = currentCalendar.get(Calendar.MONTH) - calendar.get(Calendar.MONTH);
                    if (pastMonth == 1) {
                        Tagihan tagihanBaru = new Tagihan();
                        tagihanBaru.setJumlahTagihan(jmlTagihan + 1);

                        calendar.add(Calendar.MONTH, 1);
                        tagihanBaru.setTanggalTagihan(calendar.getTime());
                        tagihanBaru.setOrderId(tagihan.getOrderId());
                        tagihanBaru.setLunas(false);
                        db.collection("tagihan").add(tagihanBaru).addOnSuccessListener(documentReference -> {
                            Log.i(TAG, "sukses menambah tagihan");
                        });
                    } else {
                        for (int i = 0; i < pastMonth; i++) {
                            Tagihan tagihanBaru = new Tagihan();
                            tagihanBaru.setJumlahTagihan(jmlTagihan + (i+1));

                            calendar.add(Calendar.MONTH, 1);
                            tagihanBaru.setTanggalTagihan(calendar.getTime());
                            tagihanBaru.setOrderId(tagihan.getOrderId());
                            tagihanBaru.setLunas(false);
                            db.collection("tagihan").add(tagihanBaru).addOnSuccessListener(documentReference -> {
                                Log.i(TAG, "sukses menambah tagihan");
                            });
                        }
                    }
                }
            }
        });
    }
}