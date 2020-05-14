package com.remember.app.ui.cabinet.memory_pages.events.current_event.videoDialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.remember.app.data.models.DeleteVideo;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.commentDialog.DeleteCommentDialog;

public class DeleteVideoDialog  extends AppCompatDialogFragment {

    private Callback callback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String message = "Вы уверены, что хотите удалить видео?";
        String positive = "Да, удалить";
        String negative = "Нет, спасибо";
        String link = getArguments().getString("link");
        int position = getArguments().getInt("position");
        DeleteVideo body = new DeleteVideo();
        body.setLink(link);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        builder.setPositiveButton(positive, (dialog, id) -> {
            callback.onDeleteVideo(body, position);
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
        void onDeleteVideo(DeleteVideo body, int position);
    }
}
