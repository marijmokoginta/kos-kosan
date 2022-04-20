package com.sibkelompoke.kost;

import static android.content.ContentValues.TAG;

import static com.sibkelompoke.kost.constant.KostKonstan.GUEST;
import static com.sibkelompoke.kost.constant.KostKonstan.MISSING;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.sibkelompoke.kost.fragment.AccountFragment;
import com.sibkelompoke.kost.fragment.DashboardFragment;
import com.sibkelompoke.kost.fragment.HomeFragment;
import com.sibkelompoke.kost.model.User;

public class MainActivity extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FragmentManager fragmentManager;

    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChipNavigationBar navigationBar = findViewById(R.id.bottomNavbar);

        if (savedInstanceState == null) {
            getCurrentUser();
            navigationBar.setItemSelected(R.id.home, true);
            HomeFragment homeFragment = new HomeFragment();
            Bundle bdl = new Bundle();
            bdl.putString("userId", user.getId());
            bdl.putString("role", user.getRole());
            homeFragment.setArguments(bdl);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, homeFragment).commit();
        }
        navigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            Fragment fragment = null;
            final Bundle data = new Bundle();

            @SuppressLint("NonConstantResourceId")
            @Override
            public void onItemSelected(int id) {
                switch (id) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;

                    case R.id.dashboard:
                        fragment = new DashboardFragment();
                        break;

                    case R.id.account:
                        fragment = new AccountFragment();
                        break;
                }
                if (fragment != null) {
                    data.putString("role", user.getRole());
                    data.putString("userId", user.getId());
                    data.putString("username", user.getUsername());
                    fragment.setArguments(data);
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();

                    Log.i(TAG, "user role : " + user.getRole());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentUser();
    }

    private void getCurrentUser () {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userId = mSettings.getString("userId", MISSING);
        Log.i(TAG, "user id : " + userId);

        if (!userId.equals(MISSING)) {
            db.collection("users").document(userId).get()
                    .addOnCompleteListener(task -> {
                        DocumentSnapshot document = task.getResult();
                        if (task.isSuccessful()) {
                            user = new User(document.getString("username"),
                                    document.getString("password"));
                            user.setId(document.getId());
                            user.setRole(document.getString("role"));
                            user.setNoTelepon(document.getString("noTelepon"));
                        } else {
                            Log.i(TAG, "user id tidak ditemukan");
                            user = new User();
                            user.setRole(GUEST);
                            user.setId(userId);
                            user.setUsername(GUEST);
                        }
                    });
        } else {
            user.setRole(GUEST);
            user.setId(userId);
            user.setUsername(GUEST);
        }
    }
}