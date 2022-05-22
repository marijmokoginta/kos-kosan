package com.sibkelompoke.kost.activity;

import static com.sibkelompoke.kost.util.KostKonstan.ADMIN;
import static com.sibkelompoke.kost.util.KostKonstan.DEFAULT_IMG_URL;
import static com.sibkelompoke.kost.util.KostKonstan.GUEST;
import static com.sibkelompoke.kost.util.KostKonstan.ORDER_KOST_COLLECTION;
import static com.sibkelompoke.kost.util.KostKonstan.USERS_COLLECTION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.adapter.FasilitasAdapter;
import com.sibkelompoke.kost.model.FasilitasKamar;
import com.sibkelompoke.kost.model.Kost;
import com.sibkelompoke.kost.model.OrderKost;
import com.sibkelompoke.kost.model.User;
import com.sibkelompoke.kost.util.LoadingProgress;

import java.util.ArrayList;
import java.util.Date;

public class DetailKost extends AppCompatActivity {
    private final String TAG = "DetailKost";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // UI komponent
    private TextView namaKost, waktuBukaKost, tipeKost, alamat, peraturan, catatan, harga, jumlahKamar;
    private ImageView kostImage;
    private Button btnPesanKamar;
    private ImageButton btnChat;
    private RecyclerView fasilitasKamar;

    // var
    private Kost kost;
    private String alamatKost;
    private User user, userDestination;
    private ArrayList<FasilitasKamar> fasilitas;

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kost);

        initView();

        fasilitas = new ArrayList<>();
        Intent getData = getIntent();

        if (getData.getExtras() != null) {
            kost = getData.getParcelableExtra("kost");
            alamatKost = getData.getStringExtra("alamat");
            user = getData.getParcelableExtra("user");
            kost.setFasilitasKamar(getData.getParcelableExtra("fasilitas"));
        } else {
            Toast.makeText(getApplicationContext(), "Maaf terjadi kesalahan", Toast.LENGTH_SHORT).show();
            finish();
        }

        FasilitasAdapter fasilitasAdapter = new FasilitasAdapter(getApplicationContext(), fasilitas);
        fasilitasKamar.setAdapter(fasilitasAdapter);
        fasilitasKamar.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3, RecyclerView.VERTICAL, false));

        setData();

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(view -> finish());

        db.collection(USERS_COLLECTION).document(kost.getUserId()).get().addOnSuccessListener(documentSnapshot -> {
            userDestination = documentSnapshot.toObject(User.class);
            eventListener();
        });
    }

    @SuppressLint("SetTextI18n")
    private void setData () {
        namaKost.setText(kost.getNamaKost());
        waktuBukaKost.setText(": " + kost.getWaktuBukaKost());
        tipeKost.setText(kost.getTipeKost());
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
    }

    private void eventListener() {
        LoadingProgress progress = new LoadingProgress(this);
        btnPesanKamar.setOnClickListener(v -> {
            Log.i(TAG, "order kost : user role -> " + user.getRole());
            OrderKost orderKost = new OrderKost(user.getId(), kost.getKostId(), kost.getUserId());
            orderKost.setUser(user);

            if (user.getRole().equals(GUEST)) {
                // jika user belum login maka akan diarahkan ke login activity
                Intent in = new Intent(getApplicationContext(), Login.class);
                in.putExtra("errorMessage", "Silahkan login terlebih dahulu");
                startActivity(in);
                finish();
            } else if (user.getRole().equals(ADMIN)) {
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

                if (jml <= 0) {
                    Snackbar.make(v, "Maaf saat ini tidak ada kamar yang tersedia", Snackbar.LENGTH_SHORT).show();
                } else {
                    progress.showDialog();
                    // mengambil semua data user yang mengorder kost
                    db.collection(ORDER_KOST_COLLECTION).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String kostId, userId;
                            boolean notOrdered = true;
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                kostId = documentSnapshot.getString("kostId");
                                userId = documentSnapshot.getString("userId");

                                assert kostId != null;
                                assert userId != null;

                                // cek jika user telah memesan kost
                                if (kostId.equals(orderKost.getKostId()) && userId.equals(orderKost.getUserId())) {
                                    Snackbar.make(v, "Anda telah memesan kost ini", Snackbar.LENGTH_SHORT).show();
                                    notOrdered = false;
                                    progress.dismissDialog();
                                }
                            }

                            // menyimpan order kost dari user
                            if (notOrdered) {
                                orderKost.setOnOrder(new Date(System.currentTimeMillis()));
                                db.collection("orderKost").add(orderKost)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Snackbar.make(v, "Berhasil memesan kamar", Snackbar.LENGTH_SHORT).show();
                                            } else {
                                                Snackbar.make(v, "Gagal! Silahkan coba lagi nanti", Snackbar.LENGTH_SHORT).show();
                                            }
                                            progress.dismissDialog();
                                        });
                            }
                        }
                    });
                }
            }
        });

        btnChat.setOnClickListener(v -> {
            Intent intent;
            if (user.getRole().equals(GUEST)) {
                intent = new Intent(getApplicationContext(), Login.class);
                intent.putExtra("errorMessage", "Silahkan login terlebih dahulu");
            } else {
                intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("userId", user.getId());
                intent.putExtra("userDestinationId", kost.getUserId());
                intent.putExtra("userDest", userDestination);
                intent.putExtra("kost", kost);
            }
            startActivity(intent);
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
        fasilitasKamar = findViewById(R.id.fasilitasKamar);

        btnPesanKamar = findViewById(R.id.btnPesanKost);
        btnChat = findViewById(R.id.btnChat);
    }
}