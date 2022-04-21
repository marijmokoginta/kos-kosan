package com.sibkelompoke.kost.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.database.KostData;
import com.sibkelompoke.kost.model.Kost;

public class DashboardFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private KostData kostData;

    // ui component
    private TextView greetUser;
    private TextView namaKost, waktuBukaKost, tipeKost;
    private Button menuPelanggan, chat;
    private LinearLayout isNotLoggedin, userKostInf, userNotOrdered, menu;
    private ScrollView mainContent;

    // vars
    private Kost kost;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        kostData = new KostData();
        // Inflate the layout for this fragment
        assert getArguments() != null;
        String role = getArguments().getString("role");
        String username = getArguments().getString("username");
        String userId = getArguments().getString("userId");

        int layout = R.layout.fragment_dashboard;

        if (role.equals("admin")) layout = R.layout.fragment_dashboard_admin;

        View v = inflater.inflate(layout, container, false);

        if (role.equals("admin")) {
            initAdminView(v);
        } else if (role.equals("user")) {
            initView(v);
            isNotLoggedin.setVisibility(View.GONE);
            mainContent.setVisibility(View.VISIBLE);

            db.collection("orderedKost").whereEqualTo("userId", userId).get()
                    .addOnCompleteListener(t -> {
                        if (t.isSuccessful()) {
                            String kostId = null;
                            for (QueryDocumentSnapshot snapshot : t.getResult()) {
                                kostId = snapshot.getString("kostId");
                            }
                            kost = kostData.findByKostId(kostId);
                            try {
                                namaKost.setText(kost.getNamaKost());
                                waktuBukaKost.setText(kost.getWaktuBukaKost());
                                tipeKost.setText(kost.getTipeKost());

                                menu.setVisibility(View.VISIBLE);
                                userKostInf.setVisibility(View.VISIBLE);
                                userNotOrdered.setVisibility(View.GONE);
                            } catch (NullPointerException e) {
                                namaKost.setText("");
                                waktuBukaKost.setText("");
                                tipeKost.setText("");

                                menu.setVisibility(View.GONE);
                                userKostInf.setVisibility(View.GONE);
                                userNotOrdered.setVisibility(View.VISIBLE);
                                Snackbar.make(v, "Silahkan pesan kamar dulu", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            initView(v);
            isNotLoggedin.setVisibility(View.VISIBLE);
            mainContent.setVisibility(View.GONE);
        }

        greetUser = v.findViewById(R.id.tvGreetUser);
        greetUser.setText(username);

        return v;
    }

    private void initAdminView(View v) {
    }

    private void initView(View v) {
        isNotLoggedin = v.findViewById(R.id.isNotLoggedid);
        mainContent = v.findViewById(R.id.mainContent);
        userKostInf = v.findViewById(R.id.userKostInf);
        userNotOrdered = v.findViewById(R.id.userNotOrdered);
        menu = v.findViewById(R.id.menuDashboard);

        namaKost = v.findViewById(R.id.userNamaKost);
        waktuBukaKost = v.findViewById(R.id.userKostWaktuBuka);
        tipeKost = v.findViewById(R.id.userKostTipe);
    }
}