package com.sibkelompoke.kost.adapter;

import static com.sibkelompoke.kost.util.KostKonstan.ADMIN;
import static com.sibkelompoke.kost.util.KostKonstan.USER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.model.Tagihan;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TagihanAdapter extends RecyclerView.Adapter<TagihanAdapter.TagihanViewHolder> {
    private Context context;
    private ArrayList<Tagihan> daftarTagihan;
    private String role;

    private OnBtnAksiClickListener onBtnAksiClickListener;

    public TagihanAdapter(Context context, ArrayList<Tagihan> daftarTagihan, String role) {
        this.context = context;
        this.daftarTagihan = daftarTagihan;
        this.role = role;
    }

    @NonNull
    @Override
    public TagihanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.daftar_tagihan, parent, false);
        return new TagihanViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TagihanViewHolder holder, int position) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

        Date date = daftarTagihan.get(position).getTanggalTagihan();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Date currentDate = new Date(System.currentTimeMillis());
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(currentDate);

        holder.tanggalTagihan.setText(sdf.format(calendar.getTime()));
        holder.tagihanKe.setText("Pembayaran bulan ke " + String.valueOf(daftarTagihan.get(position).getJumlahTagihan()));

        if (currentCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
            if (currentCalendar.get(Calendar.DAY_OF_MONTH) > calendar.get(Calendar.DAY_OF_MONTH)) {
                int waktuPenundaan = currentCalendar.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH);
                holder.waktuPenundaan.setText("Waktu penundaan pembayaran > " + String.valueOf(waktuPenundaan));
            } else if (currentCalendar.get(Calendar.DAY_OF_MONTH) < calendar.get(Calendar.DAY_OF_MONTH)) {
                int waktuPembayaran = calendar.get(Calendar.DAY_OF_MONTH) - currentCalendar.get(Calendar.DAY_OF_MONTH);
                holder.waktuPenundaan.setText("Waktu pembayaran tinggal " + String.valueOf(waktuPembayaran) + "lagi!");
            } else if (currentCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                if (role.equals(USER)) {
                    holder.waktuPenundaan.setText("Waktunya membayar kost!");
                } else if (role.equals(ADMIN)) {
                    holder.waktuPenundaan.setText("Hari ini adalah waktu pembayaran kost");
                }
            }
        }

        if (daftarTagihan.get(position).isLunas()) {
            holder.statusPembayaran.setImageResource(R.drawable.ic_check);
            holder.tvLunas.setVisibility(View.VISIBLE);
            holder.btnAksi.setVisibility(View.GONE);
        } else {
            if (role.equals(USER)) {
                holder.btnAksi.setVisibility(View.GONE);
                holder.tvLunas.setVisibility(View.VISIBLE);
                holder.tvLunas.setText("Belum dibayar");
            } else if (role.equals(ADMIN)) {
                if ((currentCalendar.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH)) >= 15) {
                    holder.btnAksi.setText("Beri peringatan");
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return daftarTagihan.size();
    }

    public interface OnBtnAksiClickListener {
        void onBtnClick(View view, Tagihan tagihan);
    }

    public void setOnBtnAksiClickListener(OnBtnAksiClickListener onBtnAksiClickListener) {
        this.onBtnAksiClickListener = onBtnAksiClickListener;
    }

    public class TagihanViewHolder extends RecyclerView.ViewHolder{
        TextView tanggalTagihan, tagihanKe, waktuPenundaan, tvLunas;
        ImageView statusPembayaran;
        CardView statusWrap;
        LinearLayout boxItem;
        Button btnAksi;

        public TagihanViewHolder(View view) {
            super(view);

            tanggalTagihan = view.findViewById(R.id.tanggalTagihan);
            tagihanKe = view.findViewById(R.id.tagihanKe);
            waktuPenundaan = view.findViewById(R.id.waktuPenundaan);
            statusPembayaran = view.findViewById(R.id.statusPembayaran);
            statusWrap = view.findViewById(R.id.statusWrap);
            boxItem = view.findViewById(R.id.boxItem);
            btnAksi = view.findViewById(R.id.btnAksi);
            tvLunas = view.findViewById(R.id.tvLunas);

            btnAksi.setOnClickListener(v ->  {
                if (onBtnAksiClickListener != null)
                onBtnAksiClickListener.onBtnClick(view, daftarTagihan.get(getAdapterPosition()));
            });
        }
    }
}
