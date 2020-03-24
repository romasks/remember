package com.remember.app.ui.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.remember.app.R;

public class RepairPasswordDialog extends DialogFragment {

    private String neutral = "Отправить";
    private EditText email;
    private Callback callback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_repair_password, null);
        email = view.findViewById(R.id.email);
        builder.setView(view)
                .setTitle(R.string.pick_email)
                .setNeutralButton(neutral, (dialog, id) -> {
                    dismiss();
                    callback.sendEmail(email.getText().toString());
                });
        return builder.create();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void sendEmail(String email);

    }
}
