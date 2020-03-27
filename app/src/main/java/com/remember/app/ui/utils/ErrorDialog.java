package com.remember.app.ui.utils;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;

public class ErrorDialog extends AppCompatDialogFragment {

    private FragmentManager fragmentManager;
    private String message = "";
    private String neutral = "Ok";
    private String tag = "errorDialog";

    public ErrorDialog() {
    }

    public ErrorDialog(FragmentManager fragmentManager, String message) {
        this.fragmentManager = fragmentManager;
        this.message = message;
    }

    public ErrorDialog(FragmentManager fragmentManager, String message, String tag) {
        this.fragmentManager = fragmentManager;
        this.message = message;
        this.tag = tag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setNeutralButton(neutral, (dialog, id) -> dismiss());
        builder.setCancelable(true);

        return builder.create();
    }

    public void setDescription(String text) {
        message = text;
    }

    public void show() {
        show(fragmentManager.beginTransaction(), tag);
    }

}
