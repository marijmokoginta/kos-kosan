package com.sibkelompoke.kost;

import static android.content.ContentValues.TAG;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import static com.sibkelompoke.kost.constant.KostKonstan.ADMIN;
import static com.sibkelompoke.kost.constant.KostKonstan.DEFAULT_IMG_URL;
import static com.sibkelompoke.kost.constant.KostKonstan.GUEST;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.sibkelompoke.kost.model.Kost;
import com.sibkelompoke.kost.model.OrderedKost;

import java.util.zip.Inflater;

public class DetailKost extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // UI komponent
    private TextView namaKost, waktuBukaKost, tipeKost, alamat, peraturan, catatan, harga, jumlahKamar;
    private ImageView kostImage;
    private Button btnPesanKamar;

    // var
    private Kost kost;
    private String alamatKost, userId, role;

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kost);

        initView();

        Intent getData = getIntent();

        if (getData.getExtras() != null) {
            kost = getData.getParcelableExtra("kost");
            alamatKost = getData.getStringExtra("alamat");
            userId = getData.getStringExtra("userId");
            role = getData.getStringExtra("role");
        } else {
            Snackbar.make(getCurrentFocus(), "Maaf terjadi kesalahan", Snackbar.LENGTH_SHORT).show();
            finish();
        }

        setData();

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> finish());
    }

    @SuppressLint("SetTextI18n")
    private void setData () {
        namaKost.setText(kost.getNamaKost());
        waktuBukaKost.setText(": " + kost.getWaktuBukaKost());
        tipeKost.setText(": " + kost.getTipeKost());
        alamat.setText(": " + alamatKost);
        peraturan.setText(": " + kost.getPeraturanKost());
        catatan.setText(": " + kost.getCatatanKost());
        harga.setText(": " + kost.getHarga());
        jumlahKamar.setText(kost.getJumlahKamar());

        if (kost.getImageUrl().size() > 0) {
            Glide.with(getApplicationContext()).load(kost.getImageUrl().get(0)).into(kostImage);
        } else {
            Glide.with(getApplicationContext()).load(DEFAULT_IMG_URL).into(kostImage);
        }

        btnPesanKamar.setOnClickListener(v -> {
            Log.i(TAG, "order kost : user role -> " + role);
            if (role.equals(GUEST)) {
                // jika user belum login maka akan diarahkan ke login activity
                Intent in = new Intent(getApplicationContext(), Login.class);
                in.putExtra("errorMessage", "Silahkan login terlebih dahulu");
                startActivity(in);
                finish();
            } else if (role.equals(ADMIN)) {
                // jika role adalah admin, tidak dibolehkan untuk mengorder kost
                Snackbar.make(v, "Tidak dapat memesan kamar", Snackbar.LENGTH_SHORT).show();
            } else {
                // parsing jumlah kamar ke tipe data integer
                int jml = 0;
                try {
                    jml = Integer.parseInt(jumlahKamar.getText().toString());
                } catch (Exception e) {
                    Snackbar.make(v, "Maaf terjadi kesalahan", Snackbar.LENGTH_SHORT).show();
                    Log.i(TAG, "gagal parsing jumlah kamar kost ke tipe data integer");
                }

                // kondisi apabila jumlah kamar = 0
                if (jml <= 0) {
                    Snackbar.make(v, "Maaf saat ini tidak ada kamar yang tersedia", Snackbar.LENGTH_SHORT).show();
                } else {
                    OrderedKost orderedKost = new OrderedKost(userId, kost.getKostId());

                    // mengambil semua data user yang mengorder kost
                    db.collection("orderedKost").get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    String kostId, userId;
                                    boolean notAlreadyOrder = true;
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        kostId = documentSnapshot.getString("kostId");
                                        userId = documentSnapshot.getString("userId");

                                        assert kostId != null;
                                        assert userId != null;

                                        // cek jika user telah memesan kost
                                        if (kostId.equals(orderedKost.getKostId()) && userId.equals(orderedKost.getUserId())) {
                                            Log.i(TAG, "kostid from db : " + kostId);
                                            Log.i(TAG, "kostId from user input : " + kost.getKostId());
                                            Log.i(TAG, "userID from db : " + userId);
                                            Log.i(TAG, "userId from user Input : " + kost.getUserId());
                                            Snackbar.make(v, "Anda telah memesan kost ini", Snackbar.LENGTH_SHORT).show();
                                            notAlreadyOrder = false;
                                        }
                                    }

                                    // menyimpan order kost dari user
                                    if (notAlreadyOrder) {
                                        db.collection("orderedKost").add(orderedKost)
                                                .addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()) {
                                                        Snackbar.make(v, "Berhasil memesan kamar", Snackbar.LENGTH_SHORT).show();
                                                    } else {
                                                        Snackbar.make(v, "Gagal! Silahkan coba lagi nanti", Snackbar.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            });
                }
            }
        });
    }

    private void initView() {
        namaKost = findViewById(R.id.namaKostDetail);
        waktuBukaKost = findViewById(R.id.waktuBukaKostDetail);
        tipeKost = findViewById(R.id.tipeKostDetail);
        kostImage = findViewById(R.id.kostImageDetail);
        alamat = findViewById(R.id.alamatKostDetail);
        peraturan = findViewById(R.id.peraturanKostDetail);
        catatan = findViewById(R.id.catatanKostDetail);
        harga = findViewById(R.id.hargaKamarDetail);
        jumlahKamar = findViewById(R.id.jumlahKamarDetail);

        btnPesanKamar = findViewById(R.id.btnPesanKost);
    }
}