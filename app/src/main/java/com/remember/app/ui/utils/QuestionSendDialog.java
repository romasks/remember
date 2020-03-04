package com.remember.app.ui.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class QuestionSendDialog extends AppCompatDialogFragment {

    private String message = "Ваш вопрос/предложение отправлено администратору!";
    private String neutral = "Ok";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setNeutralButton(neutral, (dialog, id) -> {
            dismiss();
        });
        builder.setCancelable(true);

        return builder.create();
    }

}
