package com.sibkelompoke.kost.fragment;

import static com.sibkelompoke.kost.constant.KostKonstan.GUEST;
import static com.sibkelompoke.kost.constant.KostKonstan.KABUPATEN_GORONTALO;
import static com.sibkelompoke.kost.constant.KostKonstan.KAMPUS_1_UNG;
import static com.sibkelompoke.kost.constant.KostKonstan.KAMPUS_4_UNG;
import static com.sibkelompoke.kost.constant.KostKonstan.KOTA_GORONTALO;
import static com.sibkelompoke.kost.constant.KostKonstan.LIMBOTO;
import static com.sibkelompoke.kost.constant.KostKonstan.MISSING;
import static com.sibkelompoke.kost.constant.KostKonstan.SUWAWA;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sibkelompoke.kost.DetailKost;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.adapter.KostAdapter;
import com.sibkelompoke.kost.adapter.KostAreaAdapter;
import com.sibkelompoke.kost.database.KostData;
import com.sibkelompoke.kost.model.Kost;
import com.sibkelompoke.kost.model.KostArea;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private final String TAG = "HomeFragment";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private KostData kostData;

    String userId, role;

    // UI komponent
    private RecyclerView kostMainArea, kostArea1, kostArea2, viewArea;
    private TextView titleKostArea1, titleKostArea2, titleKostArea3;
    private TextView search;
    private ImageButton btnNotofication;
    private ImageView banner;

    private KostAreaAdapter kostAreaAdapter;

    private KostAdapter kostAdapter, kostsFiltered1Adapter, kostFiltered2Adapter, kostFiltered3Adapter;
    private ArrayList<Kost> kosts;
    private ArrayList<Kost> kostsFiltered1;
    private ArrayList<Kost> kostsFiltered2;
    private ArrayList<Kost> kostsFiltered3;

    private ArrayList<KostArea> areas;

    @SuppressLint({"ResourceAsColor", "NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getContext());
        userId = mSettings.getString("userId", MISSING);
        role = mSettings.getString("userRole", GUEST);

        kostData = new KostData();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        kosts = new ArrayList<>();
        kostsFiltered1 = new ArrayList<>();
        kostsFiltered2 = new ArrayList<>();
        kostsFiltered3 = new ArrayList<>();

        initUiView(view);

        getDataAreas();
        kostAreaAdapter = new KostAreaAdapter(getContext(), areas);
        viewArea.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        viewArea.setAdapter(kostAreaAdapter);

        kostAdapter = new KostAdapter(getContext(), kosts);
        kostsFiltered1Adapter = new KostAdapter(getContext(), kostsFiltered1);
        kostFiltered2Adapter = new KostAdapter(getContext(), kostsFiltered2);
        kostFiltered3Adapter = new KostAdapter(getContext(), kostsFiltered3);

        // main layout
        kostMainArea.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        kostMainArea.setAdapter(kostAdapter);
        adapterClickListener(kostAdapter);
        titleKostArea1.setText("Kost-kostan terbaru :");
        kostData.findAll(kostAdapter, kosts);

        // layout filter 1
        kostArea1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        kostArea1.setAdapter(kostsFiltered1Adapter);
        kostData.getKostFilterByKabupaten(KOTA_GORONTALO, kostsFiltered1, kostsFiltered1Adapter);
        adapterClickListener(kostsFiltered1Adapter);
        titleKostArea2.setText("Kost Area Kota Gorontalo :");

        // layout filter 2
        kostArea2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        kostArea2.setAdapter(kostFiltered2Adapter);
        kostData.getKostFilterByKabupaten(KABUPATEN_GORONTALO, kostsFiltered2, kostFiltered2Adapter);
        adapterClickListener(kostFiltered2Adapter);
        titleKostArea3.setText("Kost Area Limboto :");

        return view;
    }

    private void getDataAreas() {
        areas = new ArrayList<>();
        areas.add(new KostArea(R.drawable.kotagorontalo, KOTA_GORONTALO));
        areas.add(new KostArea(R.drawable.limboto, LIMBOTO));
        areas.add(new KostArea(R.drawable.suwawa, SUWAWA));
        areas.add(new KostArea(R.drawable.kampus1, KAMPUS_1_UNG));
        areas.add(new KostArea(R.drawable.kampus4, KAMPUS_4_UNG));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        super.onStart();
    }

    private void adapterClickListener(KostAdapter adapter) {
        adapter.setOnClickListener((view1, kost) -> {
            String alamat, jalan, no, kelurahan, kecamatan, kabupaten;
            jalan = kost.getAlamat().getJalan();
            no = "No." + kost.getAlamat().getNo();
            kelurahan = kost.getAlamat().getKelurahan();
            kecamatan = kost.getAlamat().getKecamatan();
            kabupaten = kost.getAlamat().getKabupaten();
            alamat = jalan + ", " + no + ", " + kelurahan + ", " + kecamatan + ", " + kabupaten;

            Intent intent = new Intent(getContext(), DetailKost.class);
            intent.putExtra("kost", kost);
            intent.putExtra("alamat", alamat);
            intent.putExtra("userId", userId);
            intent.putExtra("role", role);
            Log.i(TAG, "home : " + role);
            startActivity(intent);
        });
    }

    private void initUiView(@NonNull View v) {
        titleKostArea1 = v.findViewById(R.id.titleKostFilter1);
        titleKostArea2 = v.findViewById(R.id.titleKostFilter2);
        titleKostArea3 = v.findViewById(R.id.titleKostFilter3);

        kostMainArea = v.findViewById(R.id.kostArea1);
        kostArea1 = v.findViewById(R.id.kostArea2);
        kostArea2 = v.findViewById(R.id.kostArea3);

        banner = v.findViewById(R.id.banner);
        viewArea = v.findViewById(R.id.viewArea);

        search = v.findViewById(R.id.tvSearch);
        btnNotofication = v.findViewById(R.id.btnNotification);
    }
}