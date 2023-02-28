package com.sibkelompoke.kost.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.adapter.KostAdapter;
import com.sibkelompoke.kost.adapter.KostAdapter2;
import com.sibkelompoke.kost.model.Kost;
import com.sibkelompoke.kost.model.User;
import com.sibkelompoke.kost.service.KostService;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private EditText etSearch;
    private TextView btnBack;
    private RecyclerView resultView;

    private ArrayList<Kost> kosts;
    private KostAdapter2 adapter;

    private User user;
    private String area;

    private KostService kostService;

    private boolean bounded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        kostService = new KostService();

        Intent getData = getIntent();

        if (getData.getExtras() != null) {
            user = getData.getParcelableExtra("user");
            area = getData.getStringExtra("area");
        }

        kosts = new ArrayList<>();

        initView();

        adapter = new KostAdapter2(getApplicationContext(), kosts);
        resultView.setAdapter(adapter);
        resultView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        eventListener();

        adapter.setOnItemClickListener((v, kost) -> {
            String alamat, jalan, no, kelurahan, kecamatan, kabupaten;
            jalan = kost.getAlamat().getJalan();
            no = "No." + kost.getAlamat().getNo();
            kelurahan = kost.getAlamat().getKelurahan();
            kecamatan = kost.getAlamat().getKecamatan();
            kabupaten = kost.getAlamat().getKabupaten();
            alamat = jalan + ", " + no + ", " + kelurahan + ", " + kecamatan + ", " + kabupaten;

            Intent intent = new Intent(getApplicationContext(), DetailKost.class);
            intent.putExtra("kost", kost);
            intent.putExtra("alamat", alamat);
            intent.putExtra("user", user);
            intent.putExtra("fasilitas", kost.getFasilitasKamar());
            startActivity(intent);
        });

        if (area != null) {
            kostService.getKostFilterByKabupaten(area, kosts, adapter);
        } else {
            etSearch.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void initView() {
        etSearch = findViewById(R.id.etSearch);
        btnBack = findViewById(R.id.btnBack);
        resultView = findViewById(R.id.resultView);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void eventListener() {
        ArrayList<Kost> kostFilter = kostService.findAll();

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                kosts.clear();
                String keyword = etSearch.getText().toString().toLowerCase();
                boolean isExist = false;
                for (Kost kost : kostFilter) {
                    String namaKost = kost.getNamaKost().toLowerCase();
                    if (namaKost.contains(keyword)){
                        kosts.add(kost);
                        adapter.notifyDataSetChanged();
                        isExist = true;
                    }
                }
                if (!isExist) {
                    kosts.clear();
                }
                return true;
            }
            return false;
        });

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent intent = new Intent(getApplicationContext(), KostService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            bounded = true;
            KostService.KostBinder binder = (KostService.KostBinder) iBinder;
            kostService = binder.getInstance();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bounded = false;
            kostService = null;
            Toast.makeText(getApplicationContext(), "gagal memuat", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        if (bounded) {
            unbindService(connection);
            bounded = false;
        }
    }
}