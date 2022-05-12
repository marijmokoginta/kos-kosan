package com.sibkelompoke.kost.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.activity.Login;
import com.sibkelompoke.kost.service.UserService;
import com.sibkelompoke.kost.model.User;

public class Register extends AppCompatActivity {

    UserService userService;

    EditText etUsername, etPassword, etTelepon;
    Button btnReegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userService = new UserService();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etTelepon = findViewById(R.id.etNoTelepon);
        btnReegister = findViewById(R.id.btnRegister);

        btnReegister.setOnClickListener(v -> {
            User user = new User(etUsername.getText().toString(), etPassword.getText().toString());
            user.setNoTelepon(etTelepon.getText().toString());
            user.setRole("user");

            if (userService.save(user)) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
            }

        });
    }
}