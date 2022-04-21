package com.sibkelompoke.kost.fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.sibkelompoke.kost.AddKost;
import com.sibkelompoke.kost.Login;
import com.sibkelompoke.kost.MainActivity;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.Register;
import com.sibkelompoke.kost.TermAndConditions;
import com.sibkelompoke.kost.service.KostData;
import com.sibkelompoke.kost.model.Kost;
import com.sibkelompoke.kost.model.User;

import java.util.ArrayList;

public class AccountFragment extends Fragment {
    Button btnLogin, btnRegister;
    Button btnLogout, btnAddKost, btnEditAccount;
    TextView tvUsername;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        KostData kostData = new KostData();
        ArrayList<Kost> kosts = kostData.findAll();
        // get saved user
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = mSettings.edit();
        String userId = mSettings.getString("userId", "missing");

        // get role from current user
        assert getArguments() != null;
        String role = getArguments().getString("role");
        String username = getArguments().getString("username");

        int layout = R.layout.fragment_account;

        boolean isLoggedin = false;
        if (!userId.equals("missing") || !role.equals("guest")) {
            layout = R.layout.fragment_account_loggedin;
            isLoggedin = true;
        }

        View view = inflater.inflate(layout, container, false);

        // init view in layout when user not loggedin
        btnLogin = view.findViewById(R.id.btnCLogin);
        btnRegister = view.findViewById(R.id.btnCRegister);

        //init view in layout when user loggedin
        if (isLoggedin) {
            btnLogout = view.findViewById(R.id.btnLogout);
            btnAddKost = view.findViewById(R.id.addKost);
            btnEditAccount = view.findViewById(R.id.editAccount);
            tvUsername = view.findViewById(R.id.accUsername);

            tvUsername.setText(username);

            btnAddKost.setOnClickListener(v -> {
                Intent intent;
                boolean firstTime = true;
                try {
                    for (Kost kost : kostData.findAll()) {
                        if (kost.getKostId().equals(userId)) {
                            intent = new Intent(getContext(), AddKost.class);
                            intent.putExtra("userId", userId);
                            startActivity(intent);
                            firstTime = false;
                        }
                    }
                } catch (NullPointerException e) {
                    Snackbar.make(v, "Terjadi Kesalahan", Snackbar.LENGTH_SHORT).show();
                }

                if (firstTime) {
                    intent = new Intent(getContext(), TermAndConditions.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                }
            });

            btnLogout.setOnClickListener(v -> {
                editor.clear().apply();

                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("user", new User());
                startActivity(intent);
                requireActivity().finish();

                Toast.makeText(getContext(), "logout", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "logout berhasil. user id : " + userId);
            });
        } else {
            btnLogin.setOnClickListener(v -> {
                startActivity(new Intent(getContext(), Login.class));
            });

            btnRegister.setOnClickListener(v -> {
                startActivity(new Intent(getContext(), Register.class));
            });
        }

        return view;
    }

}