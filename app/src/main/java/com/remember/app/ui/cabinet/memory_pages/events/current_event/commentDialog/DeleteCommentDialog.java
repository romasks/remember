package com.remember.app.ui.cabinet.memory_pages.events.current_event.commentDialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.remember.app.ui.utils.DeleteEvent;

public class DeleteCommentDialog  extends AppCompatDialogFragment {

    private Callback callback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = "Вы уверены, что хотите удалить комментарий?";
        String positive = "Да, удалить";
        String negative = "Нет, спасибо";
        int position = getArguments().getInt("position");
        int commentID = getArguments().getInt("id");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setPositiveButton(positive, (dialog, id) -> {
            callback.onDelete(commentID, position);
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
        void onDelete(int commentID, int position);
    }
}
