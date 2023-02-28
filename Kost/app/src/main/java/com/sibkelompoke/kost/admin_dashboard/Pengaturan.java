package com.sibkelompoke.kost.admin_dashboard;

import static com.sibkelompoke.kost.util.KostKonstan.ALAMAT_PGTRN;
import static com.sibkelompoke.kost.util.KostKonstan.CATATAN_KOST;
import static com.sibkelompoke.kost.util.KostKonstan.FASILTAS_KAMAR_PGTRN;
import static com.sibkelompoke.kost.util.KostKonstan.HARGA_PGTRN;
import static com.sibkelompoke.kost.util.KostKonstan.JAM_BUKA_KOST_PGTRN;
import static com.sibkelompoke.kost.util.KostKonstan.JUMLAH_KAMAR_PGTRN;
import static com.sibkelompoke.kost.util.KostKonstan.KOSTS_COLLECTION;
import static com.sibkelompoke.kost.util.KostKonstan.NAMA_KOST_PGTRN;
import static com.sibkelompoke.kost.util.KostKonstan.ORDER_KOST_COLLECTION;
import static com.sibkelompoke.kost.util.KostKonstan.PERATURAN_KOST;
import static com.sibkelompoke.kost.util.KostKonstan.TAGIHAN_COLLECTION;
import static com.sibkelompoke.kost.util.KostKonstan.TIPE_KOST_PGTRN;
import static com.sibkelompoke.kost.util.KostKonstan.USER;
import static com.sibkelompoke.kost.util.KostKonstan.USERS_COLLECTION;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.SplashScreen;
import com.sibkelompoke.kost.adapter.PengaturanAdapter;
import com.sibkelompoke.kost.model.Kost;
import com.sibkelompoke.kost.model.OrderKost;
import com.sibkelompoke.kost.model.Tagihan;
import com.sibkelompoke.kost.model.User;
import com.sibkelompoke.kost.util.DialogDecision;
import com.sibkelompoke.kost.util.LoadingProgress;

import java.util.ArrayList;

public class Pengaturan extends Fragment {
    private final String TAG = "PengaturanFragment";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final String[] pengaturan = {NAMA_KOST_PGTRN, JAM_BUKA_KOST_PGTRN, TIPE_KOST_PGTRN,
            ALAMAT_PGTRN, HARGA_PGTRN, JUMLAH_KAMAR_PGTRN, PERATURAN_KOST, CATATAN_KOST, FASILTAS_KAMAR_PGTRN};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert getArguments() != null;
        String kostId = getArguments().getString("kostId");
        User user = getArguments().getParcelable("user");
        Kost kost = getArguments().getParcelable("kost");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pengaturan, container, false);

        RecyclerView listPengaturan = view.findViewById(R.id.listPengaturan);
        PengaturanAdapter adapter = new PengaturanAdapter(getContext(), pengaturan);
        listPengaturan.setAdapter(adapter);
        listPengaturan.setLayoutManager(new LinearLayoutManager(getContext()));

        Button btnDelete = view.findViewById(R.id.btnDeleteKost);

        btnDelete.setOnClickListener(v -> {
            DialogDecision decision = new DialogDecision(getActivity(),
                    "Menghapus kost akan menghapus seluruh data tagihan, order, chat, dan data kost lainnya!");
            decision.showDialog();
            decision.setOnBtnYesClickListener(() -> {
                decision.dismissDialog();
                LoadingProgress progress = new LoadingProgress(getActivity());
                progress.showDialog();
                db.collection(ORDER_KOST_COLLECTION).whereEqualTo("kostId", kostId).get().addOnSuccessListener(documentSnapshots -> {
                   for (QueryDocumentSnapshot documentSnapshot : documentSnapshots) {
                       OrderKost orderKost = documentSnapshot.toObject(OrderKost.class);
                       orderKost.setOrderId(documentSnapshot.getId());
                       Log.i(TAG, orderKost.getOrderId());
                       db.collection(TAGIHAN_COLLECTION).whereEqualTo("orderId", documentSnapshot.getId()).get().addOnSuccessListener(documentsSnapshots -> {
                           for (QueryDocumentSnapshot snapshot : documentsSnapshots) {
                               Tagihan tagihan = snapshot.toObject(Tagihan.class);
                               tagihan.setTagihanId(snapshot.getId());
                               Log.i(TAG, tagihan.getOrderId());
                               db.collection(TAGIHAN_COLLECTION).document(tagihan.getTagihanId()).delete();
                           }
                       });
                       db.collection(ORDER_KOST_COLLECTION).document(documentSnapshot.getId()).delete();
                   }
                });
                db.collection(KOSTS_COLLECTION).whereEqualTo("kostId", kostId).get().addOnSuccessListener(documentsSnapshots -> {
                    Log.i(TAG, kostId);
                    for (QueryDocumentSnapshot snapshot : documentsSnapshots) {
                        db.collection(KOSTS_COLLECTION).document(snapshot.getId()).delete().addOnSuccessListener(unused -> {
                            user.setRole(USER);
                            db.collection(USERS_COLLECTION).document(user.getId()).set(user);

                            progress.dismissDialog();
                            Intent intent = new Intent(getContext(), SplashScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            requireActivity().finish();
                        });
                    }
                }).addOnFailureListener(e -> progress.dismissDialog());
            });
        });

        adapter.setOnItemClickListener(nmPengaturan -> {
            Intent intent = new Intent(getContext(), EditKost.class);
            intent.putExtra("nmPengaturan", nmPengaturan);
            intent.putExtra("kost", kost);
            startActivity(intent);
        });

        return view;
    }
}