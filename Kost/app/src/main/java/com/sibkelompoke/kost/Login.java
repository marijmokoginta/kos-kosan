package com.sibkelompoke.kost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.sibkelompoke.kost.service.UserData;
import com.sibkelompoke.kost.model.User;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    private final String TAG = "Login";

    private UserData userData;

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView errorText;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userData = new UserData();

        ArrayList<User> users = userData.findAll();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        errorText = findViewById(R.id.error_text);
        btnBack = findViewById(R.id.btnBack);

        try {
            Intent getData = getIntent();
            String errorMessage = getData.getStringExtra("errorMessage");
            if (errorMessage.length() > 0) {
                errorText.setText(errorMessage);
                errorText.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            errorText.setVisibility(View.GONE);
            Log.e(TAG, e.getLocalizedMessage());
        }

        btnLogin.setOnClickListener(view -> {
            boolean isExist = false;
            if (dataValidate()) {
                for (User user : users) {
                    if (etUsername.getText().toString().equals(user.getUsername()) && etPassword.getText().toString().equals(user.getPassword())) {
                        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putString("userId", user.getId());
                        editor.putString("userRole", user.getRole());
                        editor.apply();

                        isExist = true;

                        Toast.makeText(getApplicationContext(), "logged in", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("user", user);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
                if (!isExist) {
                    Snackbar.make(view, "username/password salah", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private boolean dataValidate () {
        if (etUsername.length() <= 0) {
            etUsername.setError("masukkan username");
            return false;
        } else if (etPassword.length() <= 0) {
            etPassword.setError("masukkan password");
            return false;
        }
        return true;
    }
}