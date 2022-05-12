package com.sibkelompoke.kost.admin_dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.adapter.OrderAdapter;
import com.sibkelompoke.kost.model.OrderKost;
import com.sibkelompoke.kost.service.OrderService;

import java.util.ArrayList;

public class AdminOrder extends AppCompatActivity {

    private RecyclerView allOrder;
    private ImageButton btnBack;

    private OrderService orderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);

        orderService = new OrderService();
        Intent getData = getIntent();
        String userId = getData.getStringExtra("userId");
        initView();

        ArrayList<OrderKost> kostOrders = new ArrayList<>();
        OrderAdapter adapter = new OrderAdapter(getApplicationContext(), kostOrders);
        orderService.orderNotConfirm(userId, kostOrders, adapter);

        allOrder.setAdapter(adapter);
        allOrder.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter.setOnConfirmClickListener((view, orderKost) -> {
            orderKost.setConfirm(true);
            orderService.updateOrder(orderKost.getOrderId(), orderKost);
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        allOrder = findViewById(R.id.orderList);
        btnBack = findViewById(R.id.btnBack);
    }
}