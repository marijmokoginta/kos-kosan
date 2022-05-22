package com.sibkelompoke.kost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.sibkelompoke.kost.R;
import com.sibkelompoke.kost.model.Promosi;

import java.util.ArrayList;

public class PromosiAdapter extends PagerAdapter {
    private final Context context;
    private final ArrayList<Promosi> promosis;

    public PromosiAdapter(Context context, ArrayList<Promosi> promosis) {
        this.context = context;
        this.promosis = promosis;
    }

    @Override
    public int getCount() {
        return promosis.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.promosi, container, false);
        ImageView img = view.findViewById(R.id.img);
        Glide.with(context).load(promosis.get(position).getImgUrl()).into(img);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
