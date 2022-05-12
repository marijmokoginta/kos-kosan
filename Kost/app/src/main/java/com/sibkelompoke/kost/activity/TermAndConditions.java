package com.sibkelompoke.kost.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.activity.AddKost;

public class TermAndConditions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_conditions);

        Intent getData = getIntent();

        Button btn = findViewById(R.id.btnTerm);
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddKost.class);
            intent.putExtra("userId", getData.getStringExtra("userId"));
            startActivity(intent);
            finish();
        });
    }
}