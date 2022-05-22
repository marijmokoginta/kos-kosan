package com.sibkelompoke.kost.user_dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.model.Catatan;
import com.sibkelompoke.kost.model.User;

public class CatatanUser extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText etCatatan;
    private TextView simpan, catatanResult;
    private ScrollView catatanResultWrap;
    private ImageButton btnBack;

    private User user = null;
    private Catatan cttn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catatan_user);

        Intent getData = getIntent();

        if (getData.getExtras() != null) {
            user = getData.getParcelableExtra("user");
        }

        initView();

        db.collection("catatan").whereEqualTo("userId", user.getId()).addSnapshotListener((value, error) -> {
           if (value != null){
               for (QueryDocumentSnapshot snapshot : value) {
                   cttn = snapshot.toObject(Catatan.class);
                   cttn.setId(snapshot.getId());
                   etCatatan.setText(cttn.getCatatan());
                   catatanResult.setText(cttn.getCatatan());
               }
           }
        });

        simpan.setOnClickListener(v -> {
            if (user != null) {
                if (cttn == null) {
                    Catatan catatan = new Catatan();
                    catatan.setUserId(user.getId());
                    catatan.setCatatan(etCatatan.getText().toString());
                    db.collection("catatan").add(catatan);
                } else {
                    cttn.setCatatan(etCatatan.getText().toString());
                    db.collection("catatan").document(cttn.getId()).set(cttn);
                }
                etCatatan.setVisibility(View.GONE);
                catatanResultWrap.setVisibility(View.VISIBLE);
            }
        });

        catatanResult.setOnClickListener(v -> {
            simpan.setVisibility(View.VISIBLE);
            catatanResultWrap.setVisibility(View.GONE);
            etCatatan.setVisibility(View.VISIBLE);
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        etCatatan = findViewById(R.id.catatan);
        simpan = findViewById(R.id.btnSimpan);
        catatanResult = findViewById(R.id.catatanResult);
        catatanResultWrap = findViewById(R.id.catatanResultWrap);
        btnBack = findViewById(R.id.btnBack);
    }
}