package com.sibkelompoke.kost.adapter;

import static android.view.View.GONE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.model.OrderKost;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private ArrayList<OrderKost> orderKosts;

    private OnConfirmClickListener onConfirmClickListener;

    public OrderAdapter (Context context, ArrayList<OrderKost> orderKosts) {
        this.context = context;
        this.orderKosts = orderKosts;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.list_order, parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        if (orderKosts.size() > 0) {
            holder.username.setText(orderKosts.get(position).getUser().getUsername());
            holder.namaLengkap.setText(orderKosts.get(position).getUser().getNamaLengkap());
            holder.pekerjaan.setText(orderKosts.get(position).getUser().getPekerjaan());
            holder.catatan.setText(orderKosts.get(position).getCatatan());

            Glide.with(context).load(orderKosts.get(position).getUser().getImageUrl()).into(holder.userImage);
        }
    }

    @Override
    public int getItemCount() {
        return orderKosts.size();
    }

    public interface OnConfirmClickListener {
        void onCorfirmClick(View v, OrderKost orderKost);
    }

    public void setOnConfirmClickListener (OnConfirmClickListener onConfirmClickListener) {
        this.onConfirmClickListener = onConfirmClickListener;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{
        ImageView userImage;
        TextView username, namaLengkap, pekerjaan, catatan;
        LinearLayout userDetailOrder;
        ImageButton showDetail;
        Button btnKonfirmasi;

        public OrderViewHolder (View v) {
            super(v);

            userImage = v.findViewById(R.id.userOrderImage);
            username = v.findViewById(R.id.usernameOrder);
            namaLengkap = v.findViewById(R.id.userOrderNamaLengkap);
            pekerjaan = v.findViewById(R.id.userOrderPekerjaan);
            catatan = v.findViewById(R.id.userOrderCatatan);
            userDetailOrder = v.findViewById(R.id.userOrderDetail);
            showDetail = v.findViewById(R.id.showDetail);
            btnKonfirmasi = v.findViewById(R.id.btnKonfirmasi);

            btnKonfirmasi.setOnClickListener(view -> {
                if (onConfirmClickListener != null) onConfirmClickListener.onCorfirmClick(view, orderKosts.get(getAdapterPosition()));
            });

            showDetail.setOnClickListener(view -> {
                if (userDetailOrder.getVisibility() == GONE) {
                    userDetailOrder.setVisibility(View.VISIBLE);
                    showDetail.setImageResource(R.drawable.ic_circle_chevron_up);
                } else if (userDetailOrder.getVisibility() == View.VISIBLE){
                    userDetailOrder.setVisibility(GONE);
                    showDetail.setImageResource(R.drawable.ic_circle_chevron_down);
                }
            });
        }
    }
}
