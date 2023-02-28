package com.sibkelompoke.kost.adapter;

import static com.sibkelompoke.kost.util.KostKonstan.ADMIN;
import static com.sibkelompoke.kost.util.KostKonstan.USER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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
    private final Context context;
    private final ArrayList<Tagihan> daftarTagihan;
    private final String role;

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

    @SuppressLint({"SetTextI18n"})
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
        holder.tagihanKe.setText("Pembayaran bulan ke " + daftarTagihan.get(position).getJumlahTagihan());

        if (daftarTagihan.get(position).isLunas()) {
            // set waktu penundaan
            holder.waktuPenundaan.setText(daftarTagihan.get(position).getWaktuPenundaan());

            holder.statusPembayaran.setImageResource(R.drawable.ic_check);
            holder.tvLunas.setVisibility(View.VISIBLE);
            holder.btnAksi.setVisibility(View.GONE);
            holder.btnWarning.setVisibility(View.GONE);

            // color green
            holder.tvLunas.setTextColor(Color.rgb(26, 176, 176));
            holder.boxItem.setBackgroundColor(Color.rgb(178, 228, 228));
            holder.statusWrap.setCardBackgroundColor(Color.rgb(26, 176, 176));

            setLineColor(holder, 178, 228, 228);
        } else {
            // set waktu penundaan
            if (currentCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
                if (currentCalendar.get(Calendar.DAY_OF_MONTH) > calendar.get(Calendar.DAY_OF_MONTH)) {
                    int waktuPenundaan = currentCalendar.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH);
                    holder.waktuPenundaan.setText("Waktu penundaan pembayaran " + waktuPenundaan + " hari");
                } else if (currentCalendar.get(Calendar.DAY_OF_MONTH) < calendar.get(Calendar.DAY_OF_MONTH)) {
                    int waktuPembayaran = calendar.get(Calendar.DAY_OF_MONTH) - currentCalendar.get(Calendar.DAY_OF_MONTH);
                    holder.waktuPenundaan.setText("Waktu pembayaran tinggal " + waktuPembayaran + "lagi!");
                } else if (currentCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                    if (role.equals(USER)) {
                        holder.waktuPenundaan.setText("Waktunya membayar kost!");
                    } else if (role.equals(ADMIN)) {
                        holder.waktuPenundaan.setText("Hari ini adalah waktu pembayaran kost");
                    }
                }
            } else if (currentCalendar.get(Calendar.MONTH) > calendar.get(Calendar.MONTH)) {
                if ((currentCalendar.get(Calendar.MONTH) - calendar.get(Calendar.MONTH)) == 1) {
                    if (currentCalendar.get(Calendar.DAY_OF_MONTH) <= calendar.get(Calendar.DAY_OF_MONTH)) {
                        int daysInLastMonth, daysInThisMonth, total;
                        daysInLastMonth = calendar.getMaximum(Calendar.DAY_OF_MONTH) - currentCalendar.get(Calendar.DAY_OF_MONTH);
                        daysInThisMonth = calendar.get(Calendar.DAY_OF_MONTH) - currentCalendar.getMinimum(Calendar.DAY_OF_MONTH);
                        total = daysInLastMonth + daysInThisMonth;
                        holder.waktuPenundaan.setText("Waktu penundaan pembayaran " + total + " hari");
                    }
                } else {
                    holder.waktuPenundaan.setText("Waktu penundaan pembayaran " + (currentCalendar.get(Calendar.MONTH) - calendar.get(Calendar.MONTH)) + " bulan");
                }
            }

            // update objek tagihan
            if (currentCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                    currentCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                daftarTagihan.get(position).setWaktuPenundaan("Pembayaran tepat waktu");
            } else {
                daftarTagihan.get(position).setWaktuPenundaan(holder.waktuPenundaan.getText().toString());
            }

            // set layout color
            // color blue
            holder.boxItem.setBackgroundColor(Color.rgb(237, 244, 254));
            holder.statusWrap.setCardBackgroundColor(Color.rgb(0, 98, 255));
            holder.btnAksi.setBackgroundColor(Color.rgb(0, 98, 255));

            if (currentCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
                if ((currentCalendar.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH)) >= 10) {
                    holder.statusPembayaran.setImageResource(R.drawable.ic_warning);
                    // color red
                    holder.boxItem.setBackgroundColor(Color.rgb(253, 200, 211));
                    holder.statusWrap.setCardBackgroundColor(Color.rgb(250, 90, 125));
                    holder.btnAksi.setBackgroundColor(Color.rgb(250, 90, 125));

                    setLineColor(holder, 253, 200, 211);
                }
            } else if (currentCalendar.get(Calendar.MONTH) > calendar.get(Calendar.MONTH)) {
                holder.statusPembayaran.setImageResource(R.drawable.ic_warning);
                // color red
                holder.boxItem.setBackgroundColor(Color.rgb(253, 200, 211));
                holder.statusWrap.setCardBackgroundColor(Color.rgb(250, 90, 125));
                holder.btnAksi.setBackgroundColor(Color.rgb(250, 90, 125));

                setLineColor(holder, 253, 200, 211);
            }

            if (role.equals(USER)) {
                holder.btnAksi.setVisibility(View.GONE);
                holder.btnWarning.setVisibility(View.GONE);
                holder.tvLunas.setVisibility(View.VISIBLE);
                holder.tvLunas.setText("Belum dibayar");

                holder.tvLunas.setTextColor(Color.rgb(250, 90, 125));
            } else if (role.equals(ADMIN)) {
                if ((currentCalendar.get(Calendar.DAY_OF_MONTH) - calendar.get(Calendar.DAY_OF_MONTH)) >= 10) {
                    holder.btnWarning.setVisibility(View.VISIBLE);
                    holder.btnWarning.setText("Beri peringatan");
                    holder.btnWarning.setBackgroundColor(Color.rgb(250, 90, 125));
                } else if (currentCalendar.get(Calendar.MONTH) > calendar.get(Calendar.MONTH)) {
                    holder.btnWarning.setVisibility(View.VISIBLE);
                    holder.btnWarning.setText("Beri peringatan");
                    holder.btnWarning.setBackgroundColor(Color.rgb(250, 90, 125));
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
        Button btnAksi, btnWarning;

        View v1, v2, v3, v4, v5, v6;

        public TagihanViewHolder(View view) {
            super(view);

            tanggalTagihan = view.findViewById(R.id.tanggalTagihan);
            tagihanKe = view.findViewById(R.id.tagihanKe);
            waktuPenundaan = view.findViewById(R.id.waktuPenundaan);
            statusPembayaran = view.findViewById(R.id.statusPembayaran);
            statusWrap = view.findViewById(R.id.statusWrap);
            boxItem = view.findViewById(R.id.boxItem);
            btnAksi = view.findViewById(R.id.btnAksi);
            btnWarning = view.findViewById(R.id.btnWarning);
            tvLunas = view.findViewById(R.id.tvLunas);

            v1 = view.findViewById(R.id.v1);
            v2 = view.findViewById(R.id.v2);
            v3 = view.findViewById(R.id.v3);
            v4 = view.findViewById(R.id.v4);
            v5 = view.findViewById(R.id.v5);
            v6 = view.findViewById(R.id.v6);

            btnAksi.setOnClickListener(v ->  {
                if (onBtnAksiClickListener != null)
                onBtnAksiClickListener.onBtnClick(view, daftarTagihan.get(getAdapterPosition()));
            });
        }
    }

    private void setLineColor(TagihanViewHolder holder, int red, int green, int blue) {
        holder.v1.setBackgroundColor(Color.rgb(red, green, blue));
        holder.v2.setBackgroundColor(Color.rgb(red, green, blue));
        holder.v3.setBackgroundColor(Color.rgb(red, green, blue));
        holder.v4.setBackgroundColor(Color.rgb(red, green, blue));
        holder.v5.setBackgroundColor(Color.rgb(red, green, blue));
        holder.v6.setBackgroundColor(Color.rgb(red, green, blue));
    }
}
