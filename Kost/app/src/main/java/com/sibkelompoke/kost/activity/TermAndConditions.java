package com.sibkelompoke.kost.activity;

import static com.sibkelompoke.kost.util.KostKonstan.ORDER_KOST_COLLECTION;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.activity.AddKost;
import com.sibkelompoke.kost.model.OrderKost;
import com.sibkelompoke.kost.model.User;
import com.sibkelompoke.kost.util.DialogResult;
import com.sibkelompoke.kost.util.LoadingProgress;

public class TermAndConditions extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_conditions);

        Intent getData = getIntent();
        String userId = getData.getStringExtra("userId");

        Button btn = findViewById(R.id.btnTerm);
        btn.setOnClickListener(v -> {
            LoadingProgress progress = new LoadingProgress(this);
            progress.showDialog();
            db.collection(ORDER_KOST_COLLECTION).whereEqualTo("userId", userId).get().addOnSuccessListener(documentSnapshots -> {
                OrderKost orderKost = null;
                for (QueryDocumentSnapshot snapshot : documentSnapshots) {
                    orderKost = snapshot.toObject(OrderKost.class);
                }
                if (orderKost != null) {
                    progress.dismissDialog();
                    DialogResult result = new DialogResult(this, R.drawable.ic_warning,
                            "anda sedang dalam kontrak dengan kost lain. silahkan akhiri kontrak untuk melanjutkan!");
                    result.showDialog();
                } else {
                    Intent intent = new Intent(getApplicationContext(), AddKost.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Terjadi kesalahan jaringan", Toast.LENGTH_SHORT).show();
            });
        });
    }
}