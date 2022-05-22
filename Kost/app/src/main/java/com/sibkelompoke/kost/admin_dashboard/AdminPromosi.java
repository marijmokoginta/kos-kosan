package com.sibkelompoke.kost.admin_dashboard;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.model.Promosi;
import com.sibkelompoke.kost.util.LoadingProgress;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class AdminPromosi extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ImageButton btnBack, btnPickImg;
    private Button btnUpload;
    private ImageView postImg;

    private final int PICK_IMAGE_CODE = 1;
    private Uri imgUri;

    private Promosi promosi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_promosi);

        Intent getData = getIntent();
        String userId = getData.getStringExtra("userId");

        promosi = new Promosi();

        initView();
        eventListener(userId);
    }

    private void initView() {
        btnBack = findViewById(R.id.btnBack);
        btnPickImg = findViewById(R.id.btnPickImg);
        btnUpload = findViewById(R.id.btnUpload);
        postImg = findViewById(R.id.postImg);
    }

    private void eventListener(String userId) {
        btnBack.setOnClickListener(view -> finish());

        btnPickImg.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "select img"), PICK_IMAGE_CODE);
        });

        LoadingProgress progress = new LoadingProgress(this);
        btnUpload.setOnClickListener(view -> {
            if (imgUri != null) {
                progress.showDialog();
                StorageReference imgFolder = FirebaseStorage.getInstance().getReference().child("promosi");
                StorageReference imgName = imgFolder.child("PROMOSI" + new Date().getTime() + ".jpg");

                imgName.putFile(imgUri).addOnSuccessListener(taskSnapshot -> {
                    imgName.getDownloadUrl().addOnSuccessListener(uri -> {
                        promosi.setUserId(userId);
                        promosi.setImgUrl(uri.toString());
                        save();
                        progress.dismissDialog();
                    });
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                    Snackbar.make(view, "Gagal mengupload gambar. silahkan coba lagi nanti!", Snackbar.LENGTH_SHORT).show();
                    progress.dismissDialog();
                });
            }
        });
    }

    private void save() {
        db.collection("promosi").add(promosi).addOnSuccessListener(documentReference -> {
            Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(Throwable::printStackTrace);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    imgUri = data.getData();
                    postImg.setImageURI(imgUri);
                }
            }
        }
    }
}