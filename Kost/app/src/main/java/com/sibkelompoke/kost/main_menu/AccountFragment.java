package com.sibkelompoke.kost.main_menu;

import static android.content.ContentValues.TAG;

import static com.sibkelompoke.kost.util.KostKonstan.ADMIN;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sibkelompoke.kost.activity.AddKost;
import com.sibkelompoke.kost.activity.Login;
import com.sibkelompoke.kost.activity.MainActivity;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.activity.Register;
import com.sibkelompoke.kost.activity.TermAndConditions;
import com.sibkelompoke.kost.service.KostService;
import com.sibkelompoke.kost.model.Kost;
import com.sibkelompoke.kost.model.User;

import java.util.ArrayList;

public class AccountFragment extends Fragment {
    private Button btnLogin, btnRegister, btnLaporkanBug;
    private Button btnLogout, btnAddKost, btnEditAccount;
    private TextView tvUsername, tvPekerjaan;
    private ImageView profilePic;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        KostService kostService = new KostService();
        ArrayList<Kost> kosts = kostService.findAll();
        // get saved user
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = mSettings.edit();
        String userId = mSettings.getString("userId", "missing");

        // get role from current user
        assert getArguments() != null;
        User user = getArguments().getParcelable("user");

        int layout = R.layout.fragment_account;

        boolean isLoggedin = false;
        if (!userId.equals("missing") || !user.getRole().equals("guest")) {
            layout = R.layout.fragment_account_loggedin;
            isLoggedin = true;
        }

        View view = inflater.inflate(layout, container, false);

        // init view in layout when user not loggedin
        btnLogin = view.findViewById(R.id.btnCLogin);
        btnRegister = view.findViewById(R.id.btnCRegister);

        // init view in layout when user loggedin
        if (isLoggedin) {
            initView(view);

            tvUsername.setText(user.getUsername());
            tvPekerjaan.setText(user.getPekerjaan());
            Glide.with(requireContext()).load(user.getImageUrl()).into(profilePic);

            if (user.getRole().equals(ADMIN)) {
                btnAddKost.setVisibility(View.GONE);
            }

            btnAddKost.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), TermAndConditions.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            });

            btnEditAccount.setOnClickListener(v ->
                    Toast.makeText(getContext(), "Dalam pengembangan", Toast.LENGTH_SHORT).show());

            btnLaporkanBug.setOnClickListener(v -> {
                PackageManager pm = requireContext().getPackageManager();
                String phoneNumber = "+6287840108124";
                String message = "Terdapat Bug\nusername : " + user.getUsername() + "\nJenis Bug : ";

                Intent waIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + message));
                startActivity(waIntent);
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
            btnLogin.setOnClickListener(v -> startActivity(new Intent(getContext(), Login.class)));

            btnRegister.setOnClickListener(v -> startActivity(new Intent(getContext(), Register.class)));
        }

        return view;
    }

    private void initView(View view) {
        btnLogout = view.findViewById(R.id.btnLogout);
        btnAddKost = view.findViewById(R.id.addKost);
        btnEditAccount = view.findViewById(R.id.editAccount);
        tvUsername = view.findViewById(R.id.accUsername);
        tvPekerjaan = view.findViewById(R.id.accPekerjaan);
        profilePic = view.findViewById(R.id.profilePic);
        btnLaporkanBug = view.findViewById(R.id.btn1);
    }

}