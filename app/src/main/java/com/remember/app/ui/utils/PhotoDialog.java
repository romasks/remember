package com.remember.app.ui.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.remember.app.R;

import java.io.File;
import java.io.IOException;

import static com.remember.app.ui.utils.FileUtils.saveBitmap;
import static com.remember.app.ui.utils.ImageUtils.glideLoadInto;

public class PhotoDialog extends DialogFragment {

    private Callback callback;
    private ConstraintLayout image;
    private ImageView imageView;
    private EditText editText;
    private TextView done;
    //private TextView cancel; //temporarily comment
    private Uri uri;
    private Bitmap bitmap;
    private File imageFile;
    private final String TAG = "PhotoDialog";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_look, null);
        image = view.findViewById(R.id.image_layout);
        imageView = view.findViewById(R.id.image);
        editText = view.findViewById(R.id.description);
        done = view.findViewById(R.id.done);
        //cancel = view.findViewById(R.id.cancel); //temporarily comment

        if (Utils.isThemeDark()) {
            editText.setBackground(getResources().getDrawable(R.drawable.edit_text_with_border_dark));
            image.setBackgroundColor(getResources().getColor(R.color.colorPrimaryBlack));
        }

        image.setOnClickListener(v -> {
            callback.showPhoto();
        });
        done.setOnClickListener(v -> {
            if (uri == null) {
                Toast.makeText(getActivity(), "Выберите фото", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                    imageFile = saveBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                callback.sendPhoto(imageFile, editText.getText().toString());
            }
        });
        /*cancel.setOnClickListener(v -> {
            dismiss();
        });*/ //temporarily comment
        builder.setView(view);
        return builder.create();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setUri(Uri uri) {
        Log.i(TAG, "Url= " + uri.toString());
        glideLoadInto(getContext(), uri, imageView);
        this.uri = uri;
    }

    public interface Callback {

        void showPhoto();

        void sendPhoto(File imageFile, String string);
    }
}
