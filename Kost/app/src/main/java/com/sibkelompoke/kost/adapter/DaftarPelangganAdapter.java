package com.sibkelompoke.kost.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.model.OrderKost;
import com.sibkelompoke.kost.model.Tagihan;

import java.util.ArrayList;
import java.util.Date;

public class DaftarPelangganAdapter extends RecyclerView.Adapter<DaftarPelangganAdapter.DaftarPelangganViewHolder> {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final Context context;
    private final ArrayList<OrderKost> orderKosts;

    private OnItemClickListener onItemClickListener;

    public DaftarPelangganAdapter(Context context, ArrayList<OrderKost> orderKosts) {
        this.context = context;
        this.orderKosts = orderKosts;
    }

    @NonNull
    @Override
    public DaftarPelangganViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.daftar_pelanggan, parent, false);
        return new DaftarPelangganViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull DaftarPelangganViewHolder holder, int position) {
        holder.username.setText(orderKosts.get(position).getUser().getUsername());
        holder.noKamar.setText(orderKosts.get(position).getNoKamar());

        Glide.with(context).load(orderKosts.get(position).getUser().getImageUrl()).into(holder.userImg);

        if (!orderKosts.get(position).isContract()) {
            holder.noKamarWrap.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orderKosts.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, OrderKost orderKost);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class DaftarPelangganViewHolder extends RecyclerView.ViewHolder {
        ImageView userImg;
        TextView username, noKamar;
        LinearLayout notContract, noKamarWrap;
        EditText etNoKamar;
        Button btnContract, btnClose;
        LinearLayout boxItem;

        public DaftarPelangganViewHolder (View view) {
            super(view);

            userImg = view.findViewById(R.id.userImage);
            username = view.findViewById(R.id.username);
            noKamar = view.findViewById(R.id.nomorKamar);
            notContract = view.findViewById(R.id.notContract);
            noKamarWrap = view.findViewById(R.id.noKamarWrap);
            etNoKamar = view.findViewById(R.id.etNoKamar);
            btnContract = view.findViewById(R.id.btnContract);
            btnClose = view.findViewById(R.id.closeDetail);
            boxItem = view.findViewById(R.id.boxItem);

            view.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    if (!orderKosts.get(getAdapterPosition()).isContract()) {
                        notContract.setVisibility(View.VISIBLE);

                        btnContract.setOnClickListener(view1 -> {
                            if (etNoKamar.getText().length() == 0) {
                                etNoKamar.setError("silahkan isi bagian ini!");
                            } else {
                                OrderKost orderKost = orderKosts.get(getAdapterPosition());
                                orderKost.setContract(true);
                                orderKost.setOnContract(new Date(System.currentTimeMillis()));

                                String nokamar = etNoKamar.getText().toString();
                                if (etNoKamar.getText().length() == 1) {
                                    nokamar = "0" + etNoKamar.getText().toString();
                                }
                                orderKost.setNoKamar(nokamar);
                                db.collection("orderKost").document(orderKost.getOrderId()).set(orderKost);

                                Tagihan tagihan = new Tagihan();
                                tagihan.setOrderId(orderKost.getOrderId());
                                tagihan.setJumlahTagihan(1);
                                tagihan.setTanggalTagihan(orderKost.getOnContract());
                                tagihan.setRole(orderKost.getUser().getRole());

                                db.collection("tagihan").add(tagihan).addOnSuccessListener(documentReference -> {
                                        Snackbar.make(view1, "Berhasil melakukan kontrak", Snackbar.LENGTH_SHORT).show();
                                        notContract.setVisibility(View.GONE);
                                }).addOnFailureListener(e -> Snackbar.make(view1, "Gagal melakukan kontrak", Snackbar.LENGTH_SHORT).show());
                            }
                        });
                        btnClose.setOnClickListener(view1 -> notContract.setVisibility(View.GONE));
                    } else {
                        onItemClickListener.onItemClick(view, orderKosts.get(getAdapterPosition()));
                    }
                }
            });
        }
    }
}
