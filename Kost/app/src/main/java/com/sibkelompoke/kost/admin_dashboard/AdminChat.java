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
import com.sibkelompoke.kost.activity.ChatActivity;
import com.sibkelompoke.kost.adapter.ListChatAdapter;
import com.sibkelompoke.kost.model.User;

import java.util.ArrayList;

public class AdminChat extends Fragment {
    private final String TAG = "AdminChat";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ArrayList<User> users;
    private String userId;

    private ListChatAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        users = new ArrayList<>();

        assert getArguments() != null;
        userId = getArguments().getString("userId");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_chat, container, false);

        RecyclerView listChat = view.findViewById(R.id.listChat);

        adapter = new ListChatAdapter(getContext(), users);
        listChat.setAdapter(adapter);
        listChat.setLayoutManager(new LinearLayoutManager(getContext()));

        getPelanggan();

        adapter.setOnChatClickListener((v, user) -> {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("userDestinationId", user.getId());
            intent.putExtra("userDest", user);
            startActivity(intent);
        });

        return view;
    }

    private void getPublicChat() {

    }

    @SuppressLint("NotifyDataSetChanged")
    private void getPelanggan() {
        db.collection("orderKost").whereEqualTo("ownerKostId", userId).addSnapshotListener((value, error) -> {
           if (value != null) {
               for (QueryDocumentSnapshot snapshot : value) {
                   String id = snapshot.getString("userId");
                   if (id != null) {
                       db.collection("users").document(id).addSnapshotListener((val, err) -> {
                            if (val != null) {
                                User user = val.toObject(User.class);
                                assert user != null;
                                user.setId(val.getId());
                                users.add(user);
                                adapter.notifyDataSetChanged();
                            }
                       });
                   }
               }
           }
        });
    }
}