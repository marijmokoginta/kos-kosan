package com.sibkelompoke.kost.user_dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.model.Kost;

public class InfoUser extends AppCompatActivity {
    private ImageButton btnBack;
    private TextView namaKost, jamBuka, alamat, peraturanKost, catatanKost, harga, jumlahKamar;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);

        Kost kost = getIntent().getParcelableExtra("kost");

        initView();

        btnBack.setOnClickListener(v -> finish());

        String alamatKost, jalan, no, kelurahan, kecamatan, kabupaten;
        jalan = kost.getAlamat().getJalan();
        no = "No." + kost.getAlamat().getNo();
        kelurahan = kost.getAlamat().getKelurahan();
        kecamatan = kost.getAlamat().getKecamatan();
        kabupaten = kost.getAlamat().getKabupaten();
        alamatKost = jalan + ", " + no + ", " + kelurahan + ", " + kecamatan + ", " + kabupaten;

        namaKost.setText(": " + kost.getNamaKost());
        jamBuka.setText(": " + kost.getWaktuBukaKost());
        alamat.setText(": " + alamatKost);
        peraturanKost.setText(": " + kost.getPeraturanKost());
        catatanKost.setText(": " + kost.getCatatanKost());
        harga.setText(": " + kost.getHarga());
        jumlahKamar.setText(": " + kost.getJumlahKamar());
    }

    private void initView() {
        btnBack = findViewById(R.id.btnBack);

        namaKost = findViewById(R.id.namaKost);
        jamBuka = findViewById(R.id.jamBukaKost);
        alamat = findViewById(R.id.alamatKost);
        peraturanKost = findViewById(R.id.peraturanKost);
        catatanKost = findViewById(R.id.catatanKost);
        harga = findViewById(R.id.hargaKost);
        jumlahKamar = findViewById(R.id.jumlahKamar);
    }
}