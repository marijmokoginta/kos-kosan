package com.sibkelompoke.kost.admin_dashboard;

import static com.sibkelompoke.kost.util.KostKonstan.KOSTS_COLLECTION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.adapter.TagihanAdapter;
import com.sibkelompoke.kost.model.Kost;
import com.sibkelompoke.kost.model.OrderKost;
import com.sibkelompoke.kost.model.Tagihan;
import com.sibkelompoke.kost.model.User;
import com.sibkelompoke.kost.util.DialogDecision;
import com.sibkelompoke.kost.util.LoadingProgress;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PelangganDetail extends AppCompatActivity {
    private final String TAG = "PelangganDetail";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ImageView userImage;
    private TextView namaLengkap, username, noTelepon, noKamar, tanggalKontrak, akhiriKontrak;
    private RecyclerView daftarTagihan;
    private ImageButton btnBack;

    private OrderKost orderKost;
    private ArrayList<Tagihan> listTagihan;
    private User user;

    private String orderId = "";
    private String kostId = "";

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelanggan_detail);

        listTagihan = new ArrayList<>();

        Intent getData = getIntent();
        if (getData != null) {
            orderId = getData.getStringExtra("orderId");
            kostId = getData.getStringExtra("kostId");
            user = getData.getParcelableExtra("user");
        }

        initView();

        db.collection("orderKost").document(orderId).get().addOnSuccessListener(documentSnapshot -> {
            orderKost = documentSnapshot.toObject(OrderKost.class);
            setData();
        });

        TagihanAdapter adapter = new TagihanAdapter(getApplicationContext(), listTagihan, user.getRole());
        daftarTagihan.setAdapter(adapter);
        daftarTagihan.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        LoadingProgress progress = new LoadingProgress(PelangganDetail.this);

        adapter.setOnBtnAksiClickListener(((view, tagihan) -> {
            if (!tagihan.isLunas()) {
                progress.showDialog();
                tagihan.setLunas(true);

                db.collection("tagihan").document(tagihan.getTagihanId()).set(tagihan).addOnSuccessListener(unused -> {
                    Toast.makeText(getApplicationContext(), "Pembayaran lunas", Toast.LENGTH_SHORT).show();
                    progress.dismissDialog();
                });
            }
        }));

        db.collection("tagihan").whereEqualTo("orderId", orderId).addSnapshotListener(((value, error) -> {
            if (value != null) {
                listTagihan.clear();
                for (QueryDocumentSnapshot snapshot : value) {
                    Tagihan tagihan = snapshot.toObject(Tagihan.class);
                    tagihan.setTagihanId(snapshot.getId());
                    listTagihan.add(tagihan);
                    adapter.notifyDataSetChanged();
                }
            }
        }));
        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        userImage = findViewById(R.id.userImage);
        namaLengkap = findViewById(R.id.namaLengkap);
        username = findViewById(R.id.username);
        noTelepon = findViewById(R.id.noTelepon);
        noKamar = findViewById(R.id.nomorKamar);
        tanggalKontrak = findViewById(R.id.taggalKontrak);
        daftarTagihan = findViewById(R.id.daftarTagihan);
        akhiriKontrak = findViewById(R.id.akhiriKontrak);
        btnBack = findViewById(R.id.btnBack);
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        Glide.with(getApplicationContext()).load(orderKost.getUser().getImageUrl()).into(userImage);
        namaLengkap.setText(orderKost.getUser().getNamaLengkap());
        username.setText(orderKost.getUser().getUsername());
        noTelepon.setText(orderKost.getUser().getNoTelepon());

        if (orderKost.isContract()) {
            noKamar.setVisibility(View.VISIBLE);
            tanggalKontrak.setVisibility(View.VISIBLE);
            noKamar.setText(orderKost.getNoKamar());

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            tanggalKontrak.setText("Memulai kontrak pada tanggal : " + sdf.format(orderKost.getOnContract()));
        } else {
            noKamar.setVisibility(View.GONE);
            tanggalKontrak.setVisibility(View.GONE);
        }

        akhiriKontrak.setOnClickListener(v -> {
            DialogDecision decision = new DialogDecision(this, "");
            decision.showDialog();
            decision.setOnBtnYesClickListener(() -> {
                LoadingProgress loadingProgress = new LoadingProgress(this);
                loadingProgress.showDialog();
                db.collection("tagihan").whereEqualTo("orderId", orderKost.getOrderId()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        db.collection("tagihan").document(snapshot.getId()).delete().addOnFailureListener(e -> {
                                loadingProgress.dismissDialog();
                                Toast.makeText(getApplicationContext(), "Gagal mengakhiri kontrak", Toast.LENGTH_SHORT).show();
                        });
                    }
                }).addOnFailureListener(e -> {
                    loadingProgress.dismissDialog();
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Gagal mengakhiri kontrak", Toast.LENGTH_SHORT).show();
                });

                db.collection(KOSTS_COLLECTION).whereEqualTo("kostId", kostId).get().addOnSuccessListener(documentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot : documentSnapshots) {
                        Kost kost = snapshot.toObject(Kost.class);
                        int jml = Integer.parseInt(kost.getJumlahKamar());
                        kost.setJumlahKamar(String.valueOf(jml + 1));
                        db.collection(KOSTS_COLLECTION).document(snapshot.getId()).set(kost);
                    }
                });

                db.collection("orderKost").document(orderKost.getOrderId()).delete().addOnSuccessListener(unused1 -> {
                    Toast.makeText(getApplicationContext(), "Berhasil mengakhiri kontrak", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });
    }
}