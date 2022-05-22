package com.sibkelompoke.kost.user_dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.model.OrderKost;
import com.sibkelompoke.kost.model.User;

public class UserChat extends AppCompatActivity {

    private ImageButton btnBack;
    private RecyclerView listChat;

    private User user;
    private OrderKost orderKost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);

        Intent getData = getIntent();
        if (getData.getExtras() != null) {
            user = getData.getParcelableExtra("user");
            orderKost = getData.getParcelableExtra("orderKost");
        }

        initView();

        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        btnBack = findViewById(R.id.btnBack);
        listChat = findViewById(R.id.listChat);
    }
}