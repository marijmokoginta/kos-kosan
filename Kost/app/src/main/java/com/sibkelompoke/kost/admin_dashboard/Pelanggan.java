package com.sibkelompoke.kost.admin_dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.adapter.DaftarPelangganAdapter;
import com.sibkelompoke.kost.model.OrderKost;
import com.sibkelompoke.kost.model.User;

import java.util.ArrayList;

public class Pelanggan extends Fragment {
    private final String TAG = "Pelanggan";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayList<OrderKost> orderKosts;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        orderKosts = new ArrayList<>();

        assert getArguments() != null;
        String kostId = getArguments().getString("kostId");
        User user = getArguments().getParcelable("user");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pelanggan, container, false);

        RecyclerView daftarPelanggan = view.findViewById(R.id.daftarPelanggan);
        DaftarPelangganAdapter adapter = new DaftarPelangganAdapter(getContext(), orderKosts);
        daftarPelanggan.setAdapter(adapter);
        daftarPelanggan.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        adapter.setOnItemClickListener(((view1, orderKost) -> {
            if (orderKost != null) {
                Intent intent = new Intent(getContext(), PelangganDetail.class);
                intent.putExtra("orderId", orderKost.getOrderId());
                intent.putExtra("kostId", kostId);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        }));

        Log.i(TAG, "kostId : " + kostId);

        db.collection("orderKost").whereEqualTo("ownerKostId", user.getId()).addSnapshotListener(((value, error) -> {
            if (value != null) {
                orderKosts.clear();
                for (QueryDocumentSnapshot snapshot : value) {
                    OrderKost orderKost = snapshot.toObject(OrderKost.class);
                    orderKost.setOrderId(snapshot.getId());
                    if (orderKost.isConfirm()) {
                        orderKosts.add(orderKost);
                        Log.i(TAG, orderKost.getKostId());
                    }
                    adapter.notifyDataSetChanged();
                }
            } else {
                Log.i(TAG, "Value null");
            }
        }));

        return view;
    }
}