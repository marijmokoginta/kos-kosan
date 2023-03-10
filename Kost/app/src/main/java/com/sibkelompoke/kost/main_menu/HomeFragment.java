package com.sibkelompoke.kost.main_menu;

import static com.sibkelompoke.kost.util.KostKonstan.KABUPATEN_GORONTALO;
import static com.sibkelompoke.kost.util.KostKonstan.KAMPUS_1_UNG;
import static com.sibkelompoke.kost.util.KostKonstan.KAMPUS_4_UNG;
import static com.sibkelompoke.kost.util.KostKonstan.KOTA_GORONTALO;
import static com.sibkelompoke.kost.util.KostKonstan.LIMBOTO;
import static com.sibkelompoke.kost.util.KostKonstan.PROMOSI_COLLECTION;
import static com.sibkelompoke.kost.util.KostKonstan.SUWAWA;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sibkelompoke.kost.activity.DetailKost;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.activity.NotificationActivity;
import com.sibkelompoke.kost.activity.ResultActivity;
import com.sibkelompoke.kost.adapter.KostAdapter;
import com.sibkelompoke.kost.adapter.KostAreaAdapter;
import com.sibkelompoke.kost.adapter.PromosiAdapter;
import com.sibkelompoke.kost.model.NotificationModel;
import com.sibkelompoke.kost.model.Promosi;
import com.sibkelompoke.kost.model.User;
import com.sibkelompoke.kost.service.KostService;
import com.sibkelompoke.kost.model.Kost;
import com.sibkelompoke.kost.model.KostArea;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private final String TAG = "HomeFragment";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private KostService kostService;
    private boolean bounded;

    private User user;

    // UI komponent
    private RecyclerView kostMainArea, kostArea1, kostArea2, viewArea;
    private TextView titleKostArea1, titleKostArea2, titleKostArea3;
    private TextView search;
    private ImageButton btnNotofication;
    private ImageView banner;
    private ViewPager promosi;
    private CardView countNotification;
    private TextView count;

    private ArrayList<KostArea> areas;
    private ArrayList<Promosi> promosis;

    private NotificationModel model;

    @SuppressLint({"ResourceAsColor", "NotifyDataSetChanged", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user = getArguments().getParcelable("user");

        promosis = new ArrayList<>();

        kostService = new KostService();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ArrayList<Kost> kosts = new ArrayList<>();
        ArrayList<Kost> kostsFiltered1 = new ArrayList<>();
        ArrayList<Kost> kostsFiltered2 = new ArrayList<>();
        ArrayList<Kost> kostsFiltered3 = new ArrayList<>();

        initUiView(view);
        eventListener();

        getDataAreas();
        KostAreaAdapter kostAreaAdapter = new KostAreaAdapter(getContext(), areas);
        viewArea.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        viewArea.setAdapter(kostAreaAdapter);

        KostAdapter kostAdapter = new KostAdapter(getContext(), kosts);
        KostAdapter kostsFiltered1Adapter = new KostAdapter(getContext(), kostsFiltered1);
        KostAdapter kostFiltered2Adapter = new KostAdapter(getContext(), kostsFiltered2);
        KostAdapter kostFiltered3Adapter = new KostAdapter(getContext(), kostsFiltered3);

        // main layout
        kostMainArea.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        kostMainArea.setAdapter(kostAdapter);
        adapterClickListener(kostAdapter);
        kostService.findAll(kostAdapter, kosts);
        titleKostArea1.setText("Kost-kostan terbaru :");

        // layout filter 1
        kostArea1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        kostArea1.setAdapter(kostsFiltered1Adapter);
        kostService.getKostFilterByKabupaten(KOTA_GORONTALO, kostsFiltered1, kostsFiltered1Adapter);
        adapterClickListener(kostsFiltered1Adapter);
        titleKostArea2.setText("Kost Area Kota Gorontalo :");

        // layout filter 2
        kostArea2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        kostArea2.setAdapter(kostFiltered2Adapter);
        kostService.getKostFilterByKabupaten(KABUPATEN_GORONTALO, kostsFiltered2, kostFiltered2Adapter);
        adapterClickListener(kostFiltered2Adapter);
        titleKostArea3.setText("Kost Area Limboto :");

        PromosiAdapter promosiAdapter = new PromosiAdapter(getContext(), promosis);
        promosi.setAdapter(promosiAdapter);
        getImgUrls(promosiAdapter);

        kostAreaAdapter.setOnItemClickListener((view1, kostArea) -> {
            Intent intent = new Intent(getContext(),ResultActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("area", kostArea.getLocation());
            startActivity(intent);
        });

        db.collection("notification").whereEqualTo("userId", user.getId()).addSnapshotListener((value, error) -> {
            if (value != null) {
                int cnt = 0;
                for (QueryDocumentSnapshot snapshot : value) {
                    model = snapshot.toObject(NotificationModel.class);
                    if (!model.isRead()) {
                        cnt++;
                        countNotification.setVisibility(View.VISIBLE);
                        count.setText(String.valueOf(cnt));
                    }
                }
            }
        });

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

        Intent intent = new Intent(getContext(), KostService.class);
        requireActivity().bindService(intent, connection, Context.BIND_AUTO_CREATE);
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
            Toast.makeText(getContext(), "gagal memuat", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        if (bounded) {
            requireActivity().unbindService(connection);
            bounded = false;
        }
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
            intent.putExtra("user", user);
            intent.putExtra("fasilitas", kost.getFasilitasKamar());
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
        promosi = v.findViewById(R.id.promosi);

        search = v.findViewById(R.id.tvSearch);
        btnNotofication = v.findViewById(R.id.btnNotification);

        countNotification = v.findViewById(R.id.countNotification);
        count = v.findViewById(R.id.count);
    }

    private void eventListener() {
        search.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(),ResultActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });

        btnNotofication.setOnClickListener(v -> {
            db.collection("notification").whereEqualTo("userId", user.getId()).get().addOnSuccessListener(documentSnapshots -> {
               for (QueryDocumentSnapshot snapshot : documentSnapshots){
                   model.setRead(true);
                   db.collection("notification").document(snapshot.getId()).set(model);
               }
            });
            Intent intent = new Intent(getContext(), NotificationActivity.class);
            intent.putExtra("userId", user.getId());
            startActivity(intent);
        });
    }

    private void getImgUrls(PromosiAdapter promosiAdapter) {
        db.collection(PROMOSI_COLLECTION).addSnapshotListener((value, error) -> {
           if (value != null) {
               promosis.clear();
               for (QueryDocumentSnapshot snapshot : value) {
                   Promosi promosi = snapshot.toObject(Promosi.class);
                   promosis.add(promosi);
                   promosiAdapter.notifyDataSetChanged();
               }
           }
        });
    }
}