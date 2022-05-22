package com.sibkelompoke.kost.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.adapter.KostAdapter;
import com.sibkelompoke.kost.model.Kost;
import com.sibkelompoke.kost.service.KostService;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private EditText etSearch;
    private TextView btnBack;
    private RecyclerView resultView;

    private ArrayList<Kost> kosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        KostService kostService = new KostService();
        kosts = new ArrayList<>();

        initView();

        KostAdapter adapter = new KostAdapter(getApplicationContext(), kosts);
        resultView.setAdapter(adapter);
        resultView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        kostService.findAll(adapter, kosts);

        eventListener();
    }

    private void initView() {
        etSearch = findViewById(R.id.etSearch);
        btnBack = findViewById(R.id.btnBack);
        resultView = findViewById(R.id.resultView);
    }

    private void eventListener() {
        btnBack.setOnClickListener(v -> finish());
    }
}