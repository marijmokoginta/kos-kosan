package com.sibkelompoke.kost.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sibkelompoke.kost.adapter.OrderAdapter;
import com.sibkelompoke.kost.model.OrderKost;

import java.util.ArrayList;

public class OrderService  extends Service {
    private final String TAG = "OrderService";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    IBinder binder = new OrderBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void orderNotConfirm(String ownerId, ArrayList<OrderKost> orderKosts, OrderAdapter adapter) {
        db.collection("orderKost").whereEqualTo("ownerKostId", ownerId).addSnapshotListener((value, error) -> {
            if (value != null) {
                orderKosts.clear();
                for (QueryDocumentSnapshot snapshot : value) {
                    OrderKost orderKost = snapshot.toObject(OrderKost.class);
                    orderKost.setOrderId(snapshot.getId());
                    if (!orderKost.isConfirm()) {
                        orderKosts.add(orderKost);
                        Log.i(TAG, orderKost.getKostId());
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void updateOrder(String docId, OrderKost orderKost) {
        db.collection("orderKost").document(docId).set(orderKost);
    }

    private String kostId;
    public String findKostIdWhereContract (String userId) {
        db.collection("orderKost").whereEqualTo("userId", userId).addSnapshotListener((value, error) -> {
           if (value != null) {
               for (QueryDocumentSnapshot snapshot : value) {
                   OrderKost orderKost = snapshot.toObject(OrderKost.class);
                   if (orderKost.isContract()) {
                       kostId = orderKost.getKostId();
                   }
               }
           }
        });
        return kostId;
    }

    public class OrderBinder extends Binder {
        public OrderService getInstence () {
            return OrderService.this;
        }
    }
}
