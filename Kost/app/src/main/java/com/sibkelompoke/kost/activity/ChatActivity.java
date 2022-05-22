package com.sibkelompoke.kost.activity;

import static com.sibkelompoke.kost.util.KostKonstan.ADMIN;
import static com.sibkelompoke.kost.util.KostKonstan.CHATS_COLLECTION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.adapter.ChatAdapter;
import com.sibkelompoke.kost.model.Chat;
import com.sibkelompoke.kost.model.Kost;
import com.sibkelompoke.kost.model.User;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private final String TAG = "ChatActivity";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ImageButton btnBack, btnSend;
    private ImageView photoProfile;
    private TextView tvUsername;
    private RecyclerView chatView;
    private EditText chatInput;

    private ArrayList<Chat> chats;
    private String userId, userDestinationId;

    private ChatAdapter adapter;

    private User userDestination;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initView();

        chats = new ArrayList<>();

        Kost kost;

        Intent getData = getIntent();
        if (getData.getExtras() != null) {
            userId = getData.getStringExtra("userId");
            userDestinationId = getData.getStringExtra("userDestinationId");
            userDestination = getData.getParcelableExtra("userDest");

            if (userDestination.getRole().equals(ADMIN)) {
                kost = getData.getParcelableExtra("kost");
                if (kost != null) {
                    Glide.with(getApplicationContext()).load(kost.getImageUrl().get(0)).into(photoProfile);
                    tvUsername.setText(kost.getNamaKost());
                }
            } else {
                Glide.with(getApplicationContext()).load(userDestination.getImageUrl()).into(photoProfile);
                tvUsername.setText(userDestination.getUsername());
            }
        }

        adapter = new ChatAdapter(getApplicationContext(), chats, userId);
        chatView.setAdapter(adapter);
        chatView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        getChat(adapter);

        eventListener();
    }

    private void initView() {
        btnBack = findViewById(R.id.btnBack);
        btnSend = findViewById(R.id.btnSend);
        photoProfile = findViewById(R.id.photoProfile);
        tvUsername = findViewById(R.id.tvUsername);
        chatView = findViewById(R.id.chatView);
        chatInput = findViewById(R.id.chatInput);
    }

    private void eventListener() {
        btnBack.setOnClickListener(v -> finish());

        btnSend.setOnClickListener(v -> {
            if (chatInput.length() > 0) {
                Chat chat = new Chat();
                chat.setId(adapter.getItemCount() + 1);
                chat.setOutGoingMsgId(userId);
                chat.setInComingMsgId(userDestinationId);
                chat.setMessage(chatInput.getText().toString());
                db.collection(CHATS_COLLECTION).add(chat);
                chatInput.setText("");
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getChat(ChatAdapter adapter) {
        db.collection(CHATS_COLLECTION).addSnapshotListener((value, error) -> {
           if (value != null) {
               chats.clear();
               for (QueryDocumentSnapshot snapshot : value) {
                   Chat chat = snapshot.toObject(Chat.class);
                   chat.setChatId(snapshot.getId());
                   if ((userId.equals(chat.getOutGoingMsgId()) && chat.getInComingMsgId().equals(userDestination.getId())) ||
                           (userId.equals(chat.getInComingMsgId())) && chat.getOutGoingMsgId().equals(userDestination.getId())) {
                       chats.add(chat);
                   }
               }
               // sorting chats
               for (int i = 0; i < chats.size() -1; i++) {
                   for (int j = i + 1; j < chats.size(); j++) {
                       if (chats.get(i).getId() > chats.get(j).getId()) {
                           Chat chatTemp = chats.get(i);
                           chats.set(i, chats.get(j));
                           chats.set(j, chatTemp);
                           adapter.notifyDataSetChanged();
                       }
                   }
               }
               chatView.scrollToPosition(chats.size()-1);
           }
        });
    }
}