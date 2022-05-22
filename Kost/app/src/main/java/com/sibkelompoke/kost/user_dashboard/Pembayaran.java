package com.sibkelompoke.kost.user_dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.adapter.TagihanAdapter;
import com.sibkelompoke.kost.model.OrderKost;
import com.sibkelompoke.kost.model.Tagihan;
import com.sibkelompoke.kost.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Pembayaran extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView daftarPembayaran;
    private TextView tanggalPembayaran;
    private ImageButton btnBack;

    private ArrayList<Tagihan> listPembayaran;

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        Intent getData = getIntent();
        User user = getData.getParcelableExtra("user");
        OrderKost orderKost = getData.getParcelableExtra("orderKost");

        listPembayaran = new ArrayList<>();

        initView();

        TagihanAdapter adapter = new TagihanAdapter(getApplicationContext(), listPembayaran, user.getRole());
        daftarPembayaran.setAdapter(adapter);
        daftarPembayaran.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        db.collection("tagihan").whereEqualTo("orderId", orderKost.getOrderId()).orderBy("jumlahTagihan", Query.Direction.DESCENDING).addSnapshotListener(((value, error) -> {
            if (value != null) {
                listPembayaran.clear();
                for (QueryDocumentSnapshot snapshot : value) {
                    Tagihan tagihan = snapshot.toObject(Tagihan.class);
                    listPembayaran.add(tagihan);
                    adapter.notifyDataSetChanged();
                }
            }
        }));

//        @SuppressLint("SimpleDateFormat")
//        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
//
//        Date date = orderKost.getOnContract();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//
//        tanggalPembayaran.setText("Memulai kontrak pada " + sdf.format(calendar.getTime()));

        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        daftarPembayaran = findViewById(R.id.daftarPembayaran);
        tanggalPembayaran = findViewById(R.id.tanggalPembayaran);
        btnBack = findViewById(R.id.btnBack);
    }
}