package com.remember.app.ui.menu.notifications;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.remember.app.R;

public class NotificationFilterDialog extends DialogFragment implements View.OnClickListener {

    public interface FilterDialogClickListener {
        void onFilterSubmit(NotificationsPresenter.NotificationFilterType filterType);
    }

    private static final String KEY_CURRENT_TYPE = "key_current_type";

    private Spinner spinner;
    private String[] data = {"Все события", "Религиозные события", "События усопших"};

    private int indexCurrentType = 0;

    private FilterDialogClickListener clickListener;

    public static NotificationFilterDialog show(FragmentManager fragmentManager, @IntRange(from = 0, to = 2) int currentType) {
        Bundle args = new Bundle();
        args.putInt(KEY_CURRENT_TYPE, currentType);

        NotificationFilterDialog dialog = new NotificationFilterDialog();
        dialog.setArguments(args);

        dialog.show(fragmentManager, "Filter_Dialog");

        return dialog;
    }

    private NotificationFilterDialog() {
    }

    void setClickListener(FilterDialogClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);

        if (getArguments() != null)
            indexCurrentType = getArguments().getInt(KEY_CURRENT_TYPE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_notifications_filter, container, false);
        spinner = view.findViewById(R.id.spinner);

        view.findViewById(R.id.back).setOnClickListener(this);
        view.findViewById(R.id.search).setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();

        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupSpinner();
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setSelection(indexCurrentType);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.search) {
            if (clickListener != null) {
                NotificationsPresenter.NotificationFilterType type = NotificationsPresenter.NotificationFilterType.ALL;

                switch (spinner.getSelectedItemPosition()) {
                    case 0:
                        type = NotificationsPresenter.NotificationFilterType.ALL;
                        break;

                    case 1:
                        type = NotificationsPresenter.NotificationFilterType.RELIGIOUS_EVENTS;
                        break;

                    case 2:
                        type = NotificationsPresenter.NotificationFilterType.DEAD_EVENTS;
                        break;
                }

                clickListener.onFilterSubmit(type);
            }
        }

        dismiss();
    }
}
