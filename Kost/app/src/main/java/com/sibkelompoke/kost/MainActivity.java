package com.sibkelompoke.kost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.sibkelompoke.kost.fragment.AccountFragment;
import com.sibkelompoke.kost.fragment.DashboardFragment;
import com.sibkelompoke.kost.fragment.HomeFragment;
import com.sibkelompoke.kost.model.User;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    private FragmentManager fragmentManager;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent getData = getIntent();
        user = getData.getParcelableExtra("user");

        ChipNavigationBar navigationBar = findViewById(R.id.bottomNavbar);

        if (savedInstanceState == null) {
            navigationBar.setItemSelected(R.id.home, true);
        }

        HomeFragment homeFragment = new HomeFragment();
        Bundle bdl = new Bundle();
        bdl.putString("userId", user.getId());
        bdl.putString("role", user.getRole());
        homeFragment.setArguments(bdl);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, homeFragment).commit();

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
}