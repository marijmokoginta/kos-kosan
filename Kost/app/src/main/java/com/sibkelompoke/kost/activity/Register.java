package com.sibkelompoke.kost.activity;

import static com.sibkelompoke.kost.util.KostKonstan.AVATAR_A;
import static com.sibkelompoke.kost.util.KostKonstan.AVATAR_B;
import static com.sibkelompoke.kost.util.KostKonstan.AVATAR_C;
import static com.sibkelompoke.kost.util.KostKonstan.AVATAR_D;
import static com.sibkelompoke.kost.util.KostKonstan.AVATAR_E;
import static com.sibkelompoke.kost.util.KostKonstan.AVATAR_F;
import static com.sibkelompoke.kost.util.KostKonstan.AVATAR_G;
import static com.sibkelompoke.kost.util.KostKonstan.AVATAR_H;
import static com.sibkelompoke.kost.util.KostKonstan.AVATAR_I;
import static com.sibkelompoke.kost.util.KostKonstan.AVATAR_J;
import static com.sibkelompoke.kost.util.KostKonstan.AVATAR_K;
import static com.sibkelompoke.kost.util.KostKonstan.USERS_COLLECTION;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.activity.Login;
import com.sibkelompoke.kost.adapter.AvatarAdapter;
import com.sibkelompoke.kost.service.UserService;
import com.sibkelompoke.kost.model.User;
import com.sibkelompoke.kost.util.LoadingProgress;

import java.util.ArrayList;
import java.util.Date;

public class Register extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayList<User> users;

    private EditText etNamaLengkap, etPekerjaan, etUsername, etPassword, etTelepon;
    private ImageView userImg;
    private ImageButton pickImage;
    private Button btnReegister;

    private Uri imgUri;

    private final int PICK_CODE = 100;

    private RecyclerView listAvatar;
    private ArrayList<Uri> avatarUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        UserService userService = new UserService();
        users = userService.findAll();

        getAvatar();

        initView();

        AvatarAdapter adapter = new AvatarAdapter(getApplicationContext(), avatarUrl);
        listAvatar.setAdapter(adapter);
        listAvatar.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        LoadingProgress progress = new LoadingProgress(Register.this);

        adapter.setOnAvatarClickListener(uri -> {
            userImg.setImageURI(uri);
            imgUri = uri;
        });

        btnReegister.setOnClickListener(v -> {
            if (dataValidate()) {
                progress.showDialog();

                User user = new User(etUsername.getText().toString(), etPassword.getText().toString());

                user.setNoTelepon(etTelepon.getText().toString());
                user.setNamaLengkap(etNamaLengkap.getText().toString());
                user.setPekerjaan(etPekerjaan.getText().toString());
                user.setRole("user");

                if (imgUri != null) {
                    StorageReference imgFolder = FirebaseStorage.getInstance().getReference().child("user image");
                    StorageReference imgName = imgFolder.child("IMG_USER" + new Date().getTime() + ".jpg");

                    imgName.putFile(imgUri).addOnSuccessListener(taskSnapshot -> {
                        imgName.getDownloadUrl().addOnSuccessListener(uri -> {
                           user.setImageUrl(uri.toString());
                           save(progress, user, users);
                        });
                    });
                }
            }
        });

        pickImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    imgUri = data.getData();
                    userImg.setImageURI(imgUri);
                }
            }
        }
    }

    private void save(LoadingProgress progress, User user, ArrayList<User> users) {
        boolean newUser = true;
        for (User oldUser : users) {
            if (user.getUsername().equals(oldUser.getUsername())) {
                progress.dismissDialog();
                etUsername.setError("username tidak tersedia");
                newUser = false;
            }
        }
        if (newUser) {
            db.collection(USERS_COLLECTION).add(user).addOnSuccessListener(documentReference -> {
                progress.dismissDialog();
                Toast.makeText(getApplicationContext(), "Berhasil mendaftar", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }).addOnFailureListener(e -> {
                Toast.makeText(getApplicationContext(), "Gagal mendaftar!", Toast.LENGTH_SHORT).show();
                progress.dismissDialog();
            });
        }
    }

    private void initView() {
        etNamaLengkap = findViewById(R.id.etNamaLengkap);
        etPekerjaan = findViewById(R.id.etPekerjaan);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etTelepon = findViewById(R.id.etNoTelepon);
        userImg = findViewById(R.id.userImage);
        pickImage = findViewById(R.id.pickImg);
        btnReegister = findViewById(R.id.btnRegister);
        listAvatar = findViewById(R.id.listAvatar);
    }

    private boolean dataValidate() {
        if (etNamaLengkap.length() == 0) {
            etNamaLengkap.setError("Silahkan isi bagian ini");
        } else if (etPekerjaan.length() == 0) {
            etPekerjaan.setError("Silahkan isi bagian ini");
        } else if (etUsername.length() == 0) {
            etUsername.setError("Silahkan isi bagian ini");
        } else if (etTelepon.length() == 0) {
            etTelepon.setError("Silahkan isi bagian ini");
        } else if (etPassword.length() == 0) {
            etPassword.setError("Silahkan isi bagian ini");
        } else {
            return true;
        }
        return false;
    }

    private void getAvatar() {
        avatarUrl = new ArrayList<>();
        avatarUrl.add(getUri(AVATAR_A));
        avatarUrl.add(getUri(AVATAR_B));
        avatarUrl.add(getUri(AVATAR_C));
        avatarUrl.add(getUri(AVATAR_D));
        avatarUrl.add(getUri(AVATAR_E));
        avatarUrl.add(getUri(AVATAR_F));
        avatarUrl.add(getUri(AVATAR_G));
        avatarUrl.add(getUri(AVATAR_H));
        avatarUrl.add(getUri(AVATAR_I));
        avatarUrl.add(getUri(AVATAR_J));
        avatarUrl.add(getUri(AVATAR_K));
    }

    private Uri getUri(int resId) {
        Resources resources = getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(resId) + "/" + resources.getResourceTypeName(resId) + "/" +
                resources.getResourceEntryName(resId));
    }
}