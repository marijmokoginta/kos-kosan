package com.sibkelompoke.kost.service;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sibkelompoke.kost.model.User;

import java.util.ArrayList;

public class UserService {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final ArrayList<User> users;
    private User user;

    public UserService() {
        users = new ArrayList<>();
    }

    boolean saved;
    public boolean save(User user) {
        db.collection("users").add(user)
                .addOnSuccessListener(documentReference -> saved = true)
                .addOnFailureListener(e -> saved = false);
        return saved;
    }

    public void edit (String documentId, User user) {
        db.collection("users").document(documentId).set(user);
    }

    public User findOne(String id) {
        db.collection("users").document(id).get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot document = task.getResult();
                    if (task.isSuccessful()) {
                        user = new User(document.getString("username"),
                                document.getString("password"));
                        user.setId(document.getId());
                        user.setRole(document.getString("role"));
                        user.setNoTelepon(document.getString("noTelepon"));
                    }
                });
        return user;
    }

    public ArrayList<User> findAll() {
        db.collection("users").get()
                .addOnCompleteListener(task -> {
                    users.clear();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            user.setId(document.getId());
                            users.add(user);
                        }
                    }
                });
        return users;
    }
}
