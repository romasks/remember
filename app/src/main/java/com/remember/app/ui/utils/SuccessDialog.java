package com.remember.app.ui.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.remember.app.data.models.ResponseRegister;

public class SuccessDialog extends AppCompatDialogFragment {

    private String message = "";
    private String title = "Данные авторизации";
    private String neutral = "Ok";
    private Callback callback;

    private static String newline = System.getProperty("line.separator");

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setNeutralButton(neutral, (dialog, id) -> {
            dismiss();
            callback.clickOk();
        });
        builder.setCancelable(true);

        return builder.create();
    }

    public void setDescription(ResponseRegister responseRegister) {
       message = "Имя:  " + responseRegister.getName() + newline + "E-mail:  "
               + responseRegister.getEmail() + newline + "Пароль:  " + responseRegister.getPassword();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void clickOk();

    }

}
