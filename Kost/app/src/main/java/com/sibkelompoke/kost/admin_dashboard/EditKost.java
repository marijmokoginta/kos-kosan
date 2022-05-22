package com.sibkelompoke.kost.admin_dashboard;

import static com.sibkelompoke.kost.util.KostKonstan.ALAMAT_PGTRN;
import static com.sibkelompoke.kost.util.KostKonstan.CATATAN_KOST;
import static com.sibkelompoke.kost.util.KostKonstan.FASILTAS_KAMAR_PGTRN;
import static com.sibkelompoke.kost.util.KostKonstan.HARGA_PGTRN;
import static com.sibkelompoke.kost.util.KostKonstan.JAM_BUKA_KOST_PGTRN;
import static com.sibkelompoke.kost.util.KostKonstan.JUMLAH_KAMAR_PGTRN;
import static com.sibkelompoke.kost.util.KostKonstan.KABUPATEN_BOALEMO;
import static com.sibkelompoke.kost.util.KostKonstan.KABUPATEN_BONEBOLANGO;
import static com.sibkelompoke.kost.util.KostKonstan.KABUPATEN_GORONTALO;
import static com.sibkelompoke.kost.util.KostKonstan.KABUPATEN_GORONTALO_UTARA;
import static com.sibkelompoke.kost.util.KostKonstan.KABUPATEN_POHUWATO;
import static com.sibkelompoke.kost.util.KostKonstan.KOST_CAMPUR;
import static com.sibkelompoke.kost.util.KostKonstan.KOST_PRIA;
import static com.sibkelompoke.kost.util.KostKonstan.KOST_WANITA;
import static com.sibkelompoke.kost.util.KostKonstan.KOTA_GORONTALO;
import static com.sibkelompoke.kost.util.KostKonstan.NAMA_KOST_PGTRN;
import static com.sibkelompoke.kost.util.KostKonstan.PERATURAN_KOST;
import static com.sibkelompoke.kost.util.KostKonstan.TIPE_KOST_PGTRN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.model.Alamat;
import com.sibkelompoke.kost.model.Kost;
import com.sibkelompoke.kost.util.LoadingProgress;

public class EditKost extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private LinearLayout edNamaKostlayout, edJamBukaKostLayout, edTipeKostLayout,
            edAlamatlayout, edHargaLayout, edJumlahLayout, edPeraturanKost, edCatatanKost, edFasilitasLayout;
    private ImageButton btnBack, btnSimpan;

    private EditText namaKost, waktuBukaKost, waktuTutupKost;
    private EditText etJalan, etNo, etKelurahan, etKecamatan;
    private EditText etHarga, etPeraturanKost, etCatatanKost, etJumlahKamar;
    private Spinner tipeKost, kabupaten;

    private Kost kost;

    private final String[] tpKost = {KOST_PRIA, KOST_WANITA, KOST_CAMPUR};
    private final String[] listKabupaten = {KOTA_GORONTALO,KABUPATEN_BONEBOLANGO,
            KABUPATEN_GORONTALO, KABUPATEN_GORONTALO_UTARA,
            KABUPATEN_BOALEMO, KABUPATEN_POHUWATO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kost);

        initView();

        Intent getData = getIntent();

        String nmPengaturan = getData.getStringExtra("nmPengaturan");
        kost = getData.getParcelableExtra("kost");

        switch (nmPengaturan){
            case NAMA_KOST_PGTRN :
                edNamaKostlayout.setVisibility(View.VISIBLE);
                namaKost.setText(kost.getNamaKost());

                btnSimpan.setOnClickListener(v -> {
                    if (namaKost.length() == 0){
                        namaKost.setError("Silahkan di isi");
                    } else if (!namaKost.getText().toString().equals(kost.getNamaKost())) {
                        kost.setNamaKost(namaKost.getText().toString());
                        save();
                    }
                });
                break;

            case JAM_BUKA_KOST_PGTRN:
                edJamBukaKostLayout.setVisibility(View.VISIBLE);
                String waktuBuka = kost.getWaktuBukaKost();
                try {
                    String[] waktuSplit = waktuBuka.split("-");
                    waktuBukaKost.setText(waktuSplit[0]);
                    waktuTutupKost.setText(waktuSplit[1]);
                } catch (NullPointerException e) {
                    Toast.makeText(getApplicationContext(), "Maaf, terjadi kesalahan", Toast.LENGTH_SHORT).show();
                    finish();
                }
                btnSimpan.setOnClickListener(v -> {
                    if (waktuBuka.length() == 0) {
                        waktuBukaKost.setError("Silahkan di isi");
                    } else if (waktuTutupKost.length() == 0) {
                        waktuTutupKost.setError("Silahkan di isi");
                    } else {
                        kost.setWaktuBukaKost(waktuBukaKost.getText().toString() + "-" + waktuTutupKost.getText().toString());
                        save();
                    }
                });
                break;

            case TIPE_KOST_PGTRN:
                edTipeKostLayout.setVisibility(View.VISIBLE);
                ArrayAdapter<String> tipeKostAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list_item, tpKost);
                tipeKost.setAdapter(tipeKostAdapter);
                btnSimpan.setOnClickListener(v -> {
                    kost.setTipeKost(tipeKost.getSelectedItem().toString());
                    save();
                });
                break;

            case ALAMAT_PGTRN:
                edAlamatlayout.setVisibility(View.VISIBLE);
                etJalan.setText(kost.getAlamat().getJalan());
                etNo.setText(kost.getAlamat().getNo());
                etKelurahan.setText(kost.getAlamat().getKelurahan());
                etKecamatan.setText(kost.getAlamat().getKecamatan());

                ArrayAdapter<String> kabupatenAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list_item, listKabupaten);
                kabupaten.setAdapter(kabupatenAdapter);

                btnSimpan.setOnClickListener(v -> {
                    if (etJalan.getText().length() == 0) {
                        etJalan.setError("silahkan di isi");
                    } else if (etNo.getText().length() == 0) {
                        etNo.setError("silahkan di isi");
                    } else if (etKelurahan.getText().length() == 0) {
                        etKelurahan.setError("silahkan di isi");
                    } else if (etKecamatan.getText().length() == 0) {
                        etKecamatan.setError("silahkan di isi");
                    } else {
                        Alamat alamat = new Alamat();
                        alamat.setAlamatId(kost.getUserId());
                        alamat.setJalan(etJalan.getText().toString());
                        alamat.setNo(etNo.getText().toString());
                        alamat.setKelurahan(etKelurahan.getText().toString());
                        alamat.setKecamatan(etKecamatan.getText().toString());
                        alamat.setKabupaten(kabupaten.getSelectedItem().toString());
                        kost.setAlamat(alamat);
                        save();
                    }
                });
                break;

            case HARGA_PGTRN:
                edHargaLayout.setVisibility(View.VISIBLE);
                etHarga.setText(kost.getHarga());
                btnSimpan.setOnClickListener(v -> {
                    if (etHarga.length() == 0) {
                        etHarga.setError("Silahkan di isi");
                    } else {
                        kost.setHarga(etHarga.getText().toString());
                        save();
                    }
                });
                break;

            case JUMLAH_KAMAR_PGTRN:
                edJumlahLayout.setVisibility(View.VISIBLE);
                etJumlahKamar.setText(kost.getJumlahKamar());
                btnSimpan.setOnClickListener(v -> {
                    if (etJumlahKamar.length() == 0) {
                        etJumlahKamar.setError("Silahkan di isi");
                    } else {
                        kost.setJumlahKamar(etJumlahKamar.getText().toString());
                        save();
                    }
                });
                break;

            case PERATURAN_KOST:
                edPeraturanKost.setVisibility(View.VISIBLE);
                etPeraturanKost.setText(kost.getPeraturanKost());
                btnSimpan.setOnClickListener(v -> {
                    if (etPeraturanKost.length() > 0) {
                        kost.setPeraturanKost(etPeraturanKost.getText().toString());
                        save();
                    }
                });
                break;

            case CATATAN_KOST:
                edCatatanKost.setVisibility(View.VISIBLE);
                etCatatanKost.setText(kost.getCatatanKost());
                btnSimpan.setOnClickListener(v -> {
                    if (etCatatanKost.length() > 0) {
                        kost.setCatatanKost(etCatatanKost.getText().toString());
                        save();
                    }
                });
                break;

            case FASILTAS_KAMAR_PGTRN:
                edFasilitasLayout.setVisibility(View.VISIBLE);
                break;
        }

        btnBack.setOnClickListener(v -> finish());
    }

    private void save() {
        LoadingProgress progress = new LoadingProgress(this);
        progress.showDialog();
        db.collection("kosts").document(kost.getId()).set(kost).addOnSuccessListener(unused -> {
            Toast.makeText(getApplicationContext(), "Berhasil di edit", Toast.LENGTH_SHORT).show();
            progress.dismissDialog();
            finish();
        }).addOnFailureListener(e -> {
            progress.dismissDialog();
            Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
        });
    }

    private void initView() {
        edNamaKostlayout = findViewById(R.id.edNamaKost);
        edJamBukaKostLayout = findViewById(R.id.edJamBukaKost);
        edTipeKostLayout = findViewById(R.id.edTipeKost);
        edAlamatlayout = findViewById(R.id.edAlamatKost);
        edHargaLayout = findViewById(R.id.edHargaKost);
        edJumlahLayout = findViewById(R.id.edJumlahKamarKost);
        edPeraturanKost = findViewById(R.id.edPeraturanKost);
        edCatatanKost = findViewById(R.id.edCatatanKost);
        edFasilitasLayout = findViewById(R.id.edFasilitasKost);

        btnBack = findViewById(R.id.btnBack);
        btnSimpan = findViewById(R.id.btnSimpan);

        namaKost = findViewById(R.id.etNamaKost);
        waktuBukaKost = findViewById(R.id.etWaktuBukaKost);
        waktuTutupKost = findViewById(R.id.etWaktuTutupKost);
        etJalan = findViewById(R.id.etJalan);
        etNo = findViewById(R.id.etNo);
        etKelurahan = findViewById(R.id.etKelurahan);
        etKecamatan = findViewById(R.id.etKecamatan);
        etHarga = findViewById(R.id.etHarga);
        etPeraturanKost = findViewById(R.id.etPeraturanKost);
        etCatatanKost = findViewById(R.id.etCatatanKost);
        etJumlahKamar = findViewById(R.id.etJumlahKamar);

        tipeKost =findViewById(R.id.spTipeKost);
        kabupaten = findViewById(R.id.spKabupaten);
    }
}