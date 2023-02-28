package com.sibkelompoke.kost.service;

import static com.sibkelompoke.kost.util.KostKonstan.KABUPATEN_BONEBOLANGO;
import static com.sibkelompoke.kost.util.KostKonstan.KABUPATEN_GORONTALO;
import static com.sibkelompoke.kost.util.KostKonstan.KOSTS_COLLECTION;
import static com.sibkelompoke.kost.util.KostKonstan.LIMBOTO;
import static com.sibkelompoke.kost.util.KostKonstan.SUWAWA;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sibkelompoke.kost.adapter.KostAdapter;
import com.sibkelompoke.kost.adapter.KostAdapter2;
import com.sibkelompoke.kost.model.Kost;

import java.util.ArrayList;

public class KostService extends Service {
    private final String TAG = "KostService";

    private final IBinder mBinder = new KostBinder();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Kost kost;

    public KostService() {
        kost = new Kost();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    boolean saved;
    public boolean addOne(Kost kost) {
    db.collection(KOSTS_COLLECTION  ).add(kost)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        saved = true;
                    }
                });
        return saved;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void findAll (KostAdapter adapter, ArrayList<Kost> kosts) {
        db.collection(KOSTS_COLLECTION).addSnapshotListener((value, error) -> {
            if (value != null) {
                kosts.clear();
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    Kost kost = documentSnapshot.toObject(Kost.class);
                    kosts.add(kost);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void getKostFilterByKabupaten(String kabupaten, ArrayList<Kost> kostsFiltered, KostAdapter adapter) {
        db.collection(KOSTS_COLLECTION).addSnapshotListener((value, error) -> {
            if (value != null) {
                kostsFiltered.clear();
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    Kost kost = documentSnapshot.toObject(Kost.class);
                    if (kost.getAlamat().getKabupaten().equals(kabupaten)) {
                        kostsFiltered.add(kost);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void getKostFilterByKabupaten(String area, ArrayList<Kost> kostsFiltered, KostAdapter2 adapter) {
        String kabupaten = area;
        if (area.equals(SUWAWA)) {
            kabupaten = KABUPATEN_BONEBOLANGO;
        } else if (area.equals(LIMBOTO)) {
            kabupaten = KABUPATEN_GORONTALO;
        }
        String finalKabupaten = kabupaten;
        db.collection(KOSTS_COLLECTION).addSnapshotListener((value, error) -> {
            if (value != null) {
                kostsFiltered.clear();
                for (QueryDocumentSnapshot documentSnapshot : value) {
                    Kost kost = documentSnapshot.toObject(Kost.class);
                    if (kost.getAlamat().getKabupaten().equals(finalKabupaten)) {
                        kostsFiltered.add(kost);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public Kost findByKostId(String kostId) {
        db.collection(KOSTS_COLLECTION).whereEqualTo("kostId", kostId)
                .addSnapshotListener((value, error) -> {
                   if (value != null) {
                       for (QueryDocumentSnapshot snapshot : value) {
                           kost = snapshot.toObject(Kost.class);
                       }
                   }
                });
        return kost;
    }

    public Kost findByUserId(String userId) {
        db.collection(KOSTS_COLLECTION).whereEqualTo("userId", userId)
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        for (QueryDocumentSnapshot snapshot : value) {
                            kost = snapshot.toObject(Kost.class);
                            kost.setId(snapshot.getId());
                        }
                    }
                });
        return kost;
    }

    public ArrayList<Kost> findAll () {
        ArrayList<Kost> kosts = new ArrayList<>();
        db.collection(KOSTS_COLLECTION).addSnapshotListener((value, error) -> {
            if (value != null) {
                for (QueryDocumentSnapshot snapshot : value) {
                    Kost kost = snapshot.toObject(Kost.class);
                    kost.setId(snapshot.getId());
                    kosts.add(kost);
                }
            }
        });
        return kosts;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void findByContains(String keyword, ArrayList<Kost> kosts, KostAdapter adapter) {
        db.collection(KOSTS_COLLECTION).whereArrayContains("namaKost", keyword).addSnapshotListener((value, error) -> {
            if (value != null){
                kosts.clear();
                for (QueryDocumentSnapshot snapshot : value) {
                    Kost kost = snapshot.toObject(Kost.class);
                    kost.setId(snapshot.getId());
                    kosts.add(kost);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public class KostBinder extends Binder {
        public KostService getInstance () {
            return KostService.this;
        }
    }
}
