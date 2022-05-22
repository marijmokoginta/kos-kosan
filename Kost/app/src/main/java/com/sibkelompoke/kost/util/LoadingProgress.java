package com.sibkelompoke.kost.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;

import com.sibkelompoke.kost.R;

public class LoadingProgress {
    private final Activity activity;
    private Dialog dialog;

    public LoadingProgress(Activity activity) {
        this.activity = activity;
    }

    @SuppressLint("InflateParams")
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_bar, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
