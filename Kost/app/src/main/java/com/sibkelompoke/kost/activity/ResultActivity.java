package com.sibkelompoke.kost.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.sibkelompoke.kost.R;

public class ResultActivity extends AppCompatActivity {

    private EditText etSearch;
    private TextView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initView();
        eventListener();
    }

    private void initView() {
        etSearch = findViewById(R.id.etSearch);
        btnBack = findViewById(R.id.btnBack);
    }

    private void eventListener() {
        btnBack.setOnClickListener(v -> finish());
    }
}