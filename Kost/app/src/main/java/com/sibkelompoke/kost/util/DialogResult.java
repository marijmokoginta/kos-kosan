package com.sibkelompoke.kost.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sibkelompoke.kost.R;

public class DialogResult {
    private final Activity activity;
    private Dialog dialog;
    private final int imgResult;
    private final String messageResult;

    public DialogResult(Activity activity, int imgResult, String messageResult) {
        this.activity = activity;
        this.imgResult = imgResult;
        this.messageResult = messageResult;
    }

    @SuppressLint("InflateParams")
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.result, null);

        ImageView img = view.findViewById(R.id.resultImg);
        TextView msg = view.findViewById(R.id.resultMessage);

        img.setImageResource(imgResult);
        msg.setText(messageResult);

        builder.setView(view);
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
