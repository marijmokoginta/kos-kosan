package com.sibkelompoke.kost.adapter;

import static android.view.View.GONE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.model.OrderedKost;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private ArrayList<OrderedKost> orderedKosts;

    public OrderAdapter (Context context, ArrayList<OrderedKost> orderedKosts) {
        this.context = context;
        this.orderedKosts = orderedKosts;
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
        holder.username.setText(orderedKosts.get(position).getUser().getUsername());
        holder.namaLengkap.setText(orderedKosts.get(position).getUser().getNamaLengkap());
        holder.pekerjaan.setText(orderedKosts.get(position).getUser().getPekerjaan());
        holder.catatan.setText(orderedKosts.get(position).getCatatan());

        Glide.with(context).load(orderedKosts.get(position).getUser().getImageUrl()).into(holder.userImage);

    }

    @Override
    public int getItemCount() {
        return orderedKosts.size();
    }


    public class OrderViewHolder extends RecyclerView.ViewHolder{
        ImageView userImage;
        TextView username, namaLengkap, pekerjaan, catatan;
        LinearLayout userDetailOrder;
        ImageButton closeDetail, showDetail;

        public OrderViewHolder (View v) {
            super(v);

            userImage = v.findViewById(R.id.userOrderImage);
            username = v.findViewById(R.id.usernameOrder);
            namaLengkap = v.findViewById(R.id.userOrderNamaLengkap);
            pekerjaan = v.findViewById(R.id.userOrderPekerjaan);
            catatan = v.findViewById(R.id.userOrderCatatan);
            userDetailOrder = v.findViewById(R.id.userOrderDetail);
            closeDetail = v.findViewById(R.id.closeUserDetail);
            showDetail = v.findViewById(R.id.showDetail);

            v.setOnClickListener(view -> {
                userDetailOrder.setVisibility(View.VISIBLE);
                showDetail.setVisibility(GONE);
            });

            closeDetail.setOnClickListener(view -> {
                userDetailOrder.setVisibility(GONE);
            });
        }
    }
}
