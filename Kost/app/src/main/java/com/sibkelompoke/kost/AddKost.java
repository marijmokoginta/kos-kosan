package com.sibkelompoke.kost;

import static com.sibkelompoke.kost.constant.KostKonstan.KABUPATEN_BOALEMO;
import static com.sibkelompoke.kost.constant.KostKonstan.KABUPATEN_BONEBOLANGO;
import static com.sibkelompoke.kost.constant.KostKonstan.KABUPATEN_GORONTALO;
import static com.sibkelompoke.kost.constant.KostKonstan.KABUPATEN_GORONTALO_UTARA;
import static com.sibkelompoke.kost.constant.KostKonstan.KABUPATEN_POHUWATO;
import static com.sibkelompoke.kost.constant.KostKonstan.KOST_CAMPUR;
import static com.sibkelompoke.kost.constant.KostKonstan.KOST_PEREMPUAN;
import static com.sibkelompoke.kost.constant.KostKonstan.KOST_PRIA;
import static com.sibkelompoke.kost.constant.KostKonstan.KOTA_GORONTALO;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sibkelompoke.kost.service.KostData;
import com.sibkelompoke.kost.service.UserData;
import com.sibkelompoke.kost.model.Alamat;
import com.sibkelompoke.kost.model.Kost;
import com.sibkelompoke.kost.model.User;

import java.util.ArrayList;
import java.util.Date;

public class AddKost extends AppCompatActivity {
    private final String TAG = "AddKost";

    private KostData kostData;
    private UserData userData;

    ArrayList<Kost> kosts;
    ArrayList<User> users;

    private EditText namaKost, waktuBukaKost, waktuTutupKost;
    private EditText etJalan, etNo, etKelurahan, etKecamatan;
    private EditText etHarga, etPeraturanKost, etCatatanKost, etJumlahKamar;
    private Spinner tipeKost, kabupaten;
    private TextView tandaiPeta;
    private Button btnBuatKost, btnAddPhoto;

    private FloatingActionButton prevBtn, nextBtn;

    private ImageView defaultImage;
    private ImageSwitcher kostPhotos;

    private static final int PICK_IMAGE_CODE = 0;
    private int position = 0;

    private ArrayList<Uri> kostPhotosUri;

    private Kost kost;

    private final String[] tpKost = {KOST_PRIA, KOST_PEREMPUAN, KOST_CAMPUR};
    private final String[] listKabupaten = {KOTA_GORONTALO,KABUPATEN_BONEBOLANGO,
            KABUPATEN_GORONTALO, KABUPATEN_GORONTALO_UTARA,
            KABUPATEN_BOALEMO, KABUPATEN_POHUWATO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kost);

        kostData = new KostData();
        userData = new UserData();

        kostPhotosUri = new ArrayList<>();

        initView();
        defaultImage.setMinimumHeight(200);

        ArrayAdapter<String> tipeKostAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list_item, tpKost);
        tipeKost.setAdapter(tipeKostAdapter);

        ArrayAdapter<String> kabupatenAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list_item, listKabupaten);
        kabupaten.setAdapter(kabupatenAdapter);

        kostPhotos.setFactory(() -> new ImageView(getApplicationContext()));

        kosts = kostData.findAll();
        users = userData.findAll();
        setListener();
    }

    private void setListener () {
        Intent getData = getIntent();
        String userId = getData.getStringExtra("userId");

        btnBuatKost.setOnClickListener(v -> {
            if (dataValidate(v)) {
                kost = new Kost(userId + new Date().getTime());

                StorageReference imageFolder = FirebaseStorage.getInstance().getReference().child("kost image");
                Uri imgUri = kostPhotosUri.get(0);
                final StorageReference imageName = imageFolder.child("IMG" + new Date().getTime() + ".jpg");

                imageName.putFile(imgUri).addOnSuccessListener(taskSnapshot ->
                        imageName.getDownloadUrl().addOnSuccessListener(uri -> {
                            kost.getImageUrl().add(uri.toString());
                            saveData(kost, userId, v, uri.toString());
                        }));
            }

        });

        btnAddPhoto.setOnClickListener(v -> pickImageIntent());

        prevBtn.setOnClickListener(v -> {
            if (position > 0){
                position--;
                kostPhotos.setImageURI(kostPhotosUri.get(position));
            } else {
                Toast.makeText(getApplicationContext(), "gambar pertama", Toast.LENGTH_SHORT).show();
            }
        });

        nextBtn.setOnClickListener(v -> {
            if (position < kostPhotosUri.size() - 1) {
                position++;
                kostPhotos.setImageURI(kostPhotosUri.get(position));
            } else {
                Toast.makeText(getApplicationContext(), "gambar terakhir", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                defaultImage.setImageResource(0);
                defaultImage.setMinimumHeight(0);
                defaultImage.setMaxHeight(0);
                if (data.getClipData() != null) {
                    // picked multiple photo
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i ++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        kostPhotosUri.add(imageUri);
                    }
                } else {
                    // picked single photo
                    Uri imageUri = data.getData();
                    kostPhotosUri.add(imageUri);
                }
                position = 0;
                // set first photo
                kostPhotos.setImageURI(kostPhotosUri.get(0));
            }
        }
    }

    private void pickImageIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select photo(s)"), PICK_IMAGE_CODE);
    }

    private void saveData (Kost kost, String userId, View v, String url) {
        kost.setUserId(userId);
        kost.setNamaKost(namaKost.getText().toString());
        kost.setWaktuBukaKost(waktuBukaKost.getText().toString() + "-" + waktuTutupKost.getText().toString());
        kost.setTipeKost(tipeKost.getSelectedItem().toString());
        kost.setHarga(etHarga.getText().toString());
        kost.setJumlahKamar(etJumlahKamar.getText().toString());
        kost.getImageUrl().add(url);

        Alamat alamat = new Alamat();
        alamat.setAlamatId(userId);
        alamat.setJalan(etJalan.getText().toString());
        alamat.setNo(etNo.getText().toString());
        alamat.setKelurahan(etKelurahan.getText().toString());
        alamat.setKecamatan(etKecamatan.getText().toString());
        alamat.setKabupaten(kabupaten.getSelectedItem().toString());

        kost.setAlamat(alamat);

        if (etPeraturanKost.getText().length() > 0) {
            kost.setPeraturanKost(etPeraturanKost.getText().toString());
        }
        if (etCatatanKost.getText().length() > 0) {
            kost.setCatatanKost(etCatatanKost.getText().toString());
        }

        if (!kostData.addOne(kost)) {
            for (User user : users) {
                if (user.getId().equals(userId)) {
                    user.setRole("admin");
                    userData.edit(userId, user);
                }
            }
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("user", new User());
            startActivity(intent);
            finish();
        } else {
            getSnackBar(v, "Terjadi Kesalahan");
        }
    }

    private void initView() {
        namaKost = findViewById(R.id.etNamaKost);
        waktuBukaKost = findViewById(R.id.etWaktuBukaKost);
        waktuTutupKost = findViewById(R.id.etWaktuTutupKost);
        tipeKost = findViewById(R.id.spTipeKost);
        etJalan = findViewById(R.id.etJalan);
        etNo = findViewById(R.id.etNo);
        etKelurahan = findViewById(R.id.etKelurahan);
        etKecamatan = findViewById(R.id.etKecamatan);
        kabupaten = findViewById(R.id.spKabupaten);
        tandaiPeta = findViewById(R.id.kostLocation);
        etHarga = findViewById(R.id.etHarga);
        etPeraturanKost = findViewById(R.id.etPeraturanKost);
        etCatatanKost = findViewById(R.id.etCatatanKost);
        etJumlahKamar = findViewById(R.id.etJumlahKamar);
        btnBuatKost = findViewById(R.id.btnBuatKost);

        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextBtn);

        btnAddPhoto = findViewById(R.id.btnAddPhotos);
        kostPhotos = findViewById(R.id.kostPhotos);
        defaultImage = findViewById(R.id.defaultPhoto);
    }

    private void getSnackBar(View v, String message) {
        Snackbar.make(v, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private boolean dataValidate(View v) {
        if (namaKost.getText().length() <= 0) {
            namaKost.setError("silahkan isi bagian ini");
            getSnackBar(v, "lengkapi data diatas");
        } else if (waktuBukaKost.getText().length() <= 0) {
            waktuBukaKost.setError("silahkan isi bagian ini");
            getSnackBar(v, "lengkapi data diatas");
        } else if (waktuTutupKost.getText().length() <= 0) {
            waktuTutupKost.setError("silahkan isi bagian ini");
            getSnackBar(v, "lengkapi data diatas");
        } else if (etJalan.getText().length() <= 0) {
            etJalan.setError("silahkan isi bagian ini");
            getSnackBar(v, "lengkapi data diatas");
        } else if (etNo.getText().length() <= 0) {
            etNo.setError("silahkan isi bagian ini");
            getSnackBar(v, "lengkapi data diatas");
        } else if (etKelurahan.getText().length() <= 0) {
            etKelurahan.setError("silahkan isi bagian ini");
            getSnackBar(v, "lengkapi data diatas");
        } else if (etKecamatan.getText().length() <= 0) {
            etKecamatan.setError("silahkan isi bagian ini");
            getSnackBar(v, "lengkapi data diatas");
        } else if (etHarga.getText().length() <= 0) {
            etHarga.setError("silahkan isi bagian ini");
            getSnackBar(v, "lengkapi data diatas");
        } else if (etHarga.getText().length() <= 0) {
            etHarga.setError("silahkan isi bagian ini");
            getSnackBar(v, "lengkapi data diatas");
        } else if (kostPhotosUri.size() <= 0) {
            getSnackBar(v, "silahkan tambahkan gambar");
        } else {
            return true;
        }
        return false;
    }
}