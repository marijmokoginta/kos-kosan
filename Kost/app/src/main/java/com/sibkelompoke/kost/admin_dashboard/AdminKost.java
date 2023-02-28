package com.sibkelompoke.kost.admin_dashboard;

import static com.sibkelompoke.kost.util.KostKonstan.MISSING;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.model.Kost;
import com.sibkelompoke.kost.model.User;

public class AdminKost extends AppCompatActivity {
    private final String TAG = "AdminKost";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private TextView namaKost, tipeKost, alamatKost;
    private Button btnPelanggan, btnChat, btnPengaturan;
    private ImageButton btnBack;

    private Kost kost;
    private User user;
    private Fragment fragment;
    private Bundle bundle;
    private String userId = MISSING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_kost);

        initView();

        Intent getData = getIntent();

        if (getData.getExtras() != null) {
            userId = getData.getStringExtra("userId");
            user = getData.getParcelableExtra("user");
        }

        db.collection("kosts").whereEqualTo("userId", userId).addSnapshotListener((value, error) -> {
            if (value != null) {
                for (QueryDocumentSnapshot snapshot : value) {
                    kost = snapshot.toObject(Kost.class);
                    kost.setId(snapshot.getId());
                    Log.i(TAG, snapshot.getId());
                    setData(userId);
                    eventListener(userId);
                }
            }
        });
    }

    private void initView() {
        namaKost = findViewById(R.id.namaKost);
        tipeKost = findViewById(R.id.tipeKost);
        alamatKost = findViewById(R.id.alamatKost);

        btnPelanggan = findViewById(R.id.btnPelanggan);
        btnChat = findViewById(R.id.btnChat);
        btnPengaturan = findViewById(R.id.btnPengaturan);

        btnBack = findViewById(R.id.btnBack);
    }

    private void eventListener(String userId) {
        bundle = new Bundle();
        bundle.putString("userId", userId);
        bundle.putParcelable("user", user);
        bundle.putParcelable("kost", kost);

        btnPelanggan.setOnClickListener(v -> {
            fragment = new Pelanggan();
            bundle.putString("kostId", kost.getKostId());
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.menuKostKonten, fragment).commit();
        });

        btnChat.setOnClickListener(v -> {
            fragment = new AdminChat();
            bundle.putString("kostId", kost.getKostId());
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.menuKostKonten, fragment).commit();
        });

        btnPengaturan.setOnClickListener(v -> {
            fragment = new Pengaturan();
            bundle.putString("kostId", kost.getKostId());
            Log.i(TAG, kost.getKostId());
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.menuKostKonten, fragment).commit();
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void setData(String userId) {
        String alamat, jalan, no, kelurahan, kecamatan, kabupaten;
        jalan = kost.getAlamat().getJalan();
        no = "No." + kost.getAlamat().getNo();
        kelurahan = kost.getAlamat().getKelurahan();
        kecamatan = kost.getAlamat().getKecamatan();
        kabupaten = kost.getAlamat().getKabupaten();
        alamat = jalan + ", " + no + ", " + kelurahan + ", " + kecamatan + ", " + kabupaten;

        namaKost.setText(kost.getNamaKost());
        tipeKost.setText(kost.getTipeKost());
        alamatKost.setText(alamat);

        Pelanggan pelanggan = new Pelanggan();
        Bundle dfBundle = new Bundle();
        dfBundle.putString("kostId", kost.getKostId());
        dfBundle.putString("userId", userId);
        dfBundle.putParcelable("user", user);
        pelanggan.setArguments(dfBundle);
        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.menuKostKonten, pelanggan).commit();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}