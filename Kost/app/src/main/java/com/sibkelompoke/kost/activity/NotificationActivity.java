package com.sibkelompoke.kost.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.adapter.NotificationAdapter;
import com.sibkelompoke.kost.model.NotificationModel;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ImageButton btnBack;
    private RecyclerView view;

    private ArrayList<NotificationModel> notificationModels;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Intent getData = getIntent();
        String userId = getData.getStringExtra("userId");

        notificationModels = new ArrayList<>();

        initView();

        NotificationAdapter adapter = new NotificationAdapter(getApplicationContext(),notificationModels);
        view.setAdapter(adapter);
        view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        db.collection("notification").whereEqualTo("userId", userId).addSnapshotListener((value, error) -> {
            if (value != null) {
                for (QueryDocumentSnapshot snapshot : value) {
                    NotificationModel model = snapshot.toObject(NotificationModel.class);
                    model.setId(snapshot.getId());
                    notificationModels.add(model);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        view = findViewById(R.id.listNotification);
        btnBack = findViewById(R.id.btnBack);
    }
}