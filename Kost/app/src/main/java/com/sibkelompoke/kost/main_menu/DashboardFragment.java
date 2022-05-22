package com.sibkelompoke.kost.main_menu;

import static com.sibkelompoke.kost.util.KostKonstan.ADMIN;
import static com.sibkelompoke.kost.util.KostKonstan.USER;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.activity.Login;
import com.sibkelompoke.kost.admin_dashboard.AdminKost;
import com.sibkelompoke.kost.admin_dashboard.AdminOrder;
import com.sibkelompoke.kost.admin_dashboard.AdminPromosi;
import com.sibkelompoke.kost.model.OrderKost;
import com.sibkelompoke.kost.model.User;
import com.sibkelompoke.kost.service.KostService;
import com.sibkelompoke.kost.model.Kost;
import com.sibkelompoke.kost.service.OrderService;
import com.sibkelompoke.kost.user_dashboard.CatatanUser;
import com.sibkelompoke.kost.user_dashboard.InfoUser;
import com.sibkelompoke.kost.user_dashboard.Pembayaran;
import com.sibkelompoke.kost.user_dashboard.UserChat;

public class DashboardFragment extends Fragment {
    private final String TAG = "DashboardFragment";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private KostService kostService;
    private OrderService orderService;

    private boolean kostServiceBounded, orderServiceBounded;

    // ui component
    private TextView greetUser, tvLogin;
    private TextView namaKost, waktuBukaKost, tipeKost, nomorKamar;
    private Button menuPelanggan, chat;
    private LinearLayout isNotLoggedin, userKostInf, userNotOrdered, menu;
    private LinearLayout menuPembayaran, menuPesan, menuCatatan, menuInfo;
    private ImageView adminImageDashboard, profilePic;
    private LinearLayout adminMenuListKost, adminMenuPromosi, adminMenuOrder;
    private ScrollView mainContent;

    private Kost kost;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        kostService = new KostService();
        orderService = new OrderService();
        // Inflate the layout for this fragment
        assert getArguments() != null;
        User user = getArguments().getParcelable("user");

        Log.d(TAG, "user id : " + user.getId());

        int layout = R.layout.fragment_dashboard;

        if (user.getRole().equals("admin")) layout = R.layout.fragment_dashboard_admin;

        View v = inflater.inflate(layout, container, false);

        if (user.getRole().equals(ADMIN)) {
            initAdminView(v);
            adminMenuListener(user.getId(), user);
        } else if (user.getRole().equals(USER)) {
            initView(v);
            isNotLoggedin.setVisibility(View.GONE);
            mainContent.setVisibility(View.VISIBLE);

            db.collection("orderKost").whereEqualTo("userId", user.getId()).addSnapshotListener(((value, error) -> {
                if (value != null) {
                    for (QueryDocumentSnapshot snapshot : value) {
                        OrderKost orderKost  = snapshot.toObject(OrderKost.class);
                        if (orderKost.isContract()) {
                            db.collection("kosts").whereEqualTo("kostId", orderKost.getKostId()).addSnapshotListener(((value1, error1) -> {
                                if (value1 != null) {
                                    for (QueryDocumentSnapshot snapshot1 : value1) {
                                        kost = snapshot1.toObject(Kost.class);
                                        if (kost.getNamaKost() != null) {
                                            Log.d(TAG, "Kost name : " + kost.getNamaKost());
                                            menu.setVisibility(View.VISIBLE);
                                            userKostInf.setVisibility(View.VISIBLE);
                                            userNotOrdered.setVisibility(View.GONE);
                                            namaKost.setText(kost.getNamaKost());
                                            waktuBukaKost.setText(kost.getWaktuBukaKost());
                                            tipeKost.setText(kost.getTipeKost());
                                            nomorKamar.setText(orderKost.getNoKamar());
                                        } else {
                                            namaKost.setText("");
                                            waktuBukaKost.setText("");
                                            tipeKost.setText("");
                                            menu.setVisibility(View.GONE);
                                            userKostInf.setVisibility(View.GONE);
                                            userNotOrdered.setVisibility(View.VISIBLE);
                                        }
                                        userMenuListener(user, orderKost);
                                    }
                                }
                            }));
                        }
                    }
                }
            }));
        } else {
            initView(v);
            isNotLoggedin.setVisibility(View.VISIBLE);
            mainContent.setVisibility(View.GONE);
            tvLogin.setOnClickListener(v1 -> startActivity(new Intent(getContext(), Login.class)));
        }

        greetUser = v.findViewById(R.id.tvGreetUser);
        profilePic = v.findViewById(R.id.profilePic);

        greetUser.setText("Hi " + user.getUsername());
        Glide.with(requireContext()).load(user.getImageUrl()).into(profilePic);

        return v;
    }

    private void initAdminView(View v) {
        adminMenuListKost = v.findViewById(R.id.adminMenuListKost);
        adminMenuPromosi = v.findViewById(R.id.adminMenuPromosi);
        adminImageDashboard = v.findViewById(R.id.imageAdminDashboard);
        adminMenuOrder = v.findViewById(R.id.adminMenuOrder);
    }

    private void adminMenuListener(String userId, User user) {
        adminMenuListKost.setOnClickListener(v -> {
            Kost kost = kostService.findByUserId(userId);
            Intent in = new Intent(getContext(), AdminKost.class);
            in.putExtra("userId", userId);
            in.putExtra("user", user);
            in.putExtra("kostId", kost.getId());
            startActivity(in);
        });
        adminMenuOrder.setOnClickListener(v -> {
            Intent in = new Intent(getContext(), AdminOrder.class);
            in.putExtra("userId", userId);
            startActivity(in);
        });
        adminMenuPromosi.setOnClickListener( v -> {
            Intent in = new Intent(getContext(), AdminPromosi.class);
            in.putExtra("userId", userId);
            startActivity(in);
        });
    }

    private void userMenuListener(User user, OrderKost order) {
        menuPembayaran.setOnClickListener(v -> {
            Intent in = new Intent(getContext(), Pembayaran.class);
            in.putExtra("user", user);
            in.putExtra("orderKost", order);
            startActivity(in);
        });

        menuPesan.setOnClickListener(v -> {
            Intent in = new Intent(getContext(), UserChat.class);
            in.putExtra("user", user);
            in.putExtra("orderKost", order);
            startActivity(in);
        });

        menuCatatan.setOnClickListener(v -> {
            Intent in = new Intent(getContext(), CatatanUser.class);
            in.putExtra("user", user);
            startActivity(in);
        });

        menuInfo.setOnClickListener(v -> {
            Intent in = new Intent(getContext(), InfoUser.class);
            in.putExtra("user", user);
            startActivity(in);
        });
    }

    private void initView(View v) {
        isNotLoggedin = v.findViewById(R.id.isNotLoggedid);
        mainContent = v.findViewById(R.id.mainContent);
        userKostInf = v.findViewById(R.id.userKostInf);
        userNotOrdered = v.findViewById(R.id.userNotOrdered);
        menu = v.findViewById(R.id.menuDashboard);

        namaKost = v.findViewById(R.id.userNamaKost);
        waktuBukaKost = v.findViewById(R.id.userKostWaktuBuka);
        tipeKost = v.findViewById(R.id.userKostTipe);
        nomorKamar = v.findViewById(R.id.nomorKamar);

        tvLogin = v.findViewById(R.id.tvLogin);

        menuPembayaran = v.findViewById(R.id.menuPembayaran);
        menuPesan = v.findViewById(R.id.menuPesan);
        menuCatatan = v.findViewById(R.id.menuCatatan);
        menuInfo = v.findViewById(R.id.menuInfo);
    }

    @Override
    public void onStart() {
        super.onStart();

        Intent bindKostService = new Intent(getContext(), KostService.class);
        requireActivity().bindService(bindKostService, kostServiceConnection, Context.BIND_AUTO_CREATE);

        Intent bindOrderService = new Intent(getContext(), OrderService.class);
        requireActivity().bindService(bindOrderService, orderServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private final ServiceConnection kostServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            kostServiceBounded = true;
            KostService.KostBinder binder = (KostService.KostBinder) iBinder;
            kostService = binder.getInstance();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            kostServiceBounded = false;
            kostService = null;
        }
    };

    private final ServiceConnection orderServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            orderServiceBounded = true;
            OrderService.OrderBinder binder = (OrderService.OrderBinder) iBinder;
            orderService = binder.getInstence();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            orderServiceBounded = false;
            orderService = null;
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        if (kostServiceBounded) {
            requireActivity().unbindService(kostServiceConnection);
            kostServiceBounded = false;
        }

        if (orderServiceBounded) {
            requireActivity().unbindService(orderServiceConnection);
            orderServiceBounded = false;
        }
    }
}