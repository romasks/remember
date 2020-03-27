package com.remember.app.ui.utils;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DeleteAlertDialog extends AppCompatDialogFragment {

    private Callback callback;
    private Integer epitaphId;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = "Вы уверены, что хотите удалить эпитафию?";
        String positive = "Да, удалить";
        String negative = "Нет, спасибо";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setPositiveButton(positive, (dialog, id) -> {
            callback.deleteEpitaph(epitaphId);
        });
        builder.setNegativeButton(negative, (dialog, id) -> {
            dismiss();
        });
        builder.setCancelable(true);

        return builder.create();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setEpitaphId(Integer id) {
        this.epitaphId = id;
    }

    public interface Callback {

        void deleteEpitaph(Integer epitaphId);

    }
}
