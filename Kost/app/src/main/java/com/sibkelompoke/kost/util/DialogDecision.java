package com.sibkelompoke.kost.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sibkelompoke.kost.R;

public class DialogDecision {
    private final Activity activity;
    private Dialog dialog;
    private final String message;

    private OnBtnYesClickListener onBtnYesClickListener;

    public DialogDecision(Activity activity, String message) {
        this.activity = activity;
        this.message = message;
    }

    @SuppressLint("InflateParams")
    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.decision, null);

        TextView msg = view.findViewById(R.id.decisionMessage);
        Button btnCancel, btnYes;

        btnCancel = view.findViewById(R.id.btnCancel);
        btnYes = view.findViewById(R.id.btnYes);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnYes.setOnClickListener(v -> {
            if (onBtnYesClickListener != null) {
                onBtnYesClickListener.onBtnYesClick();
            }
        });

        msg.setText(message);

        builder.setView(view);
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

    public interface OnBtnYesClickListener{
        void onBtnYesClick();
    }

    public void setOnBtnYesClickListener(OnBtnYesClickListener onBtnYesClickListener) {
        this.onBtnYesClickListener = onBtnYesClickListener;
    }
}
