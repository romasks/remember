package com.remember.app.ui.utils;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DeleteSlidingPhotoDialog extends AppCompatDialogFragment {

    private Callback callback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = "Вы уверены, что хотите удалить фото?";
        String positive = "Да, удалить";
        String negative = "Нет, спасибо";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setPositiveButton(positive, (dialog, id) -> {
            callback.onDeletePhoto();
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

    public interface Callback {
        void onDeletePhoto();
    }
}