package com.remember.app.ui.cabinet.memory_pages.events.current_event.commentDialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.remember.app.R;
import com.remember.app.customView.CustomEditText;

public class EditDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "dialog:CommentDialog";
    public EditDialog.EditCommentDialogListener listener;
    CustomEditText etComment;
    int commentID = 0;
    int position = 0;
    String comment = "";


    @Override
    public void onStart() {
        super.onStart();
        // val width = LinearLayout.LayoutParams.MATCH_PARENT
        int width = setWidthByDensity(this);
        int height = getResources().getDimensionPixelSize(R.dimen.comment_dialog_height);
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setLayout(width, height);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit_comment, null);
        view.findViewById(R.id.btnSendComment).setOnClickListener(this);
        view.findViewById(R.id.imgClose).setOnClickListener(this);
        etComment = view.findViewById(R.id.etMessage);
        Bundle bundle = getArguments();
        comment = bundle.getString("comment");
        position = bundle.getInt("position");
        commentID = bundle.getInt("id");
        etComment.setText(comment);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        setCancelable(true);
        return view;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.btnSendComment:
                listener.editComment(etComment.getText().toString(), commentID, position);
                this.dismiss();
                break;
            case R.id.imgClose:
                this.dismiss();
                break;
        }
    }

    public interface EditCommentDialogListener {
        void editComment(String text, int commentID, int position);
    }

    private int setWidthByDensity(DialogFragment fragment) {
        DisplayMetrics metrics = new DisplayMetrics();
        FragmentActivity fragmentActivity = fragment.getActivity();

        if (fragmentActivity != null) {
            WindowManager windowManager = fragmentActivity.getWindowManager();
            if (windowManager != null) {
                windowManager.getDefaultDisplay().getMetrics(metrics);
            }
        }
        return (metrics.widthPixels) / 100 * 95;
    }
}
