package com.remember.app.ui.cabinet.epitaphs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.RequestAddEpitaphs;
import com.remember.app.data.models.ResponseEpitaphs;
import com.remember.app.ui.adapters.EpitaphsAdapter;
import com.remember.app.ui.utils.DeleteAlertDialog;
import com.remember.app.ui.utils.DividerItemDecoration;
import com.remember.app.ui.utils.MvpAppCompatActivity;
import com.remember.app.ui.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.remember.app.data.Constants.INTENT_EXTRA_PAGE_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_SHOW;
import static com.remember.app.data.Constants.PREFS_KEY_USER_ID;

public class EpitaphsActivity extends MvpAppCompatActivity implements EpitaphsView, PopupAddEpitaph.Callback,
        EpitaphsAdapter.Callback, DeleteAlertDialog.Callback {

    @InjectPresenter
    EpitaphsPresenter presenter;

    @BindView(R.id.rv_epitaphs)
    RecyclerView recyclerView;
    @BindView(R.id.plus)
    ImageView plus;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.no_events)
    LinearLayout noEvents;
    @BindView(R.id.btn_create_event)
    Button btnCreateEvent;

    private Unbinder unbinder;
    private EpitaphsAdapter epitaphsAdapter;
    private int pageId;
    private boolean isShow;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epitaphs);
        unbinder = ButterKnife.bind(this);

        if (Utils.isThemeDark()) {
            back.setImageResource(R.drawable.ic_back_dark_theme);
            plus.setImageResource(R.drawable.ic_add2);
        }

        pageId = getIntent().getIntExtra(INTENT_EXTRA_PAGE_ID, 1);
        isShow = getIntent().getBooleanExtra(INTENT_EXTRA_SHOW, false);

        epitaphsAdapter = new EpitaphsAdapter(isShow);
        epitaphsAdapter.setCallback(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));
        recyclerView.setAdapter(epitaphsAdapter);

        presenter.getEpitaphs(pageId);
        plus.setOnClickListener(v -> {
            if (!isShow) {
                showPopupAdd();
            }
        });
        btnCreateEvent.setOnClickListener(v -> {
            if (!isShow) {
                showPopupAdd();
            }
        });
        back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void showPopupAdd() {
        View popupView = getLayoutInflater().inflate(R.layout.popup_epitaph, null);
        ConstraintLayout layout = popupView.findViewById(R.id.addepi);
        EditText editText = popupView.findViewById(R.id.text_epitaph);

        if (Utils.isThemeDark()) {
            editText.setBackground(getResources().getDrawable(R.drawable.edit_text_with_border_dark));
            layout.setBackgroundColor(getResources().getColor(R.color.colorBlackDark));
        }

        PopupAddEpitaph popupWindow = new PopupAddEpitaph(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setCallback(this);
        popupWindow.setUp(recyclerView, "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onReceivedEpitaphs(List<ResponseEpitaphs> responseEpitaphs) {
        epitaphsAdapter.setItems(responseEpitaphs);
        noEvents.setVisibility(responseEpitaphs.isEmpty() ? View.VISIBLE : View.GONE);
    }

    public void onClick(View view) {
        DeleteAlertDialog myDialogFragment = new DeleteAlertDialog();
        myDialogFragment.setCallback(this);
        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();
        myDialogFragment.show(transaction, "dialog");
    }

    @Override
    public void onSavedEpitaphs(RequestAddEpitaphs requestAddEpitaphs) {
        presenter.getEpitaphs(pageId);
        Utils.showSnack(recyclerView, "Эпитафия добавлена");
    }

    @Override
    public void onErrorSavedEpitaphs(Throwable throwable) {
        Utils.showSnack(recyclerView, "Ошибка сохранения");
    }

    @Override
    public void onErrorDeleteEpitaphs(Throwable throwable) {
        Utils.showSnack(recyclerView, "Ошибка удаления");
    }

    @Override
    public void onEditedEpitaphs(RequestAddEpitaphs requestAddEpitaphs) {
        presenter.getEpitaphs(pageId);
    }

    @Override
    public void onDeletedEpitaphs(Object obj) {
        Toast.makeText(this, "Эпитафия удалена", Toast.LENGTH_LONG).show();
        presenter.getEpitaphs(pageId);
    }

    @Override
    public void saveEpitaph(String text) {
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        RequestAddEpitaphs requestAddEpitaphs = new RequestAddEpitaphs();
        requestAddEpitaphs.setBody(text);
        requestAddEpitaphs.setPageId(pageId);
        requestAddEpitaphs.setUserId(Prefs.getString(PREFS_KEY_USER_ID, ""));
        requestAddEpitaphs.setCreated(df.format(new Date()));
        requestAddEpitaphs.setUpdated(df.format(new Date()));
        presenter.saveEpitaph(requestAddEpitaphs);
    }

    @Override
    public void editEpitaph(String text, Integer id) {
        @SuppressLint("SimpleDateFormat")
        RequestAddEpitaphs requestAddEpitaphs = new RequestAddEpitaphs();
        requestAddEpitaphs.setBody(text);
        requestAddEpitaphs.setPageId(pageId);
        requestAddEpitaphs.setUserId(Prefs.getString(PREFS_KEY_USER_ID, ""));
        presenter.editEpitaph(requestAddEpitaphs, id);
    }

    @Override
    public void change(ResponseEpitaphs responseEpitaphs) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_epitaph, null);
        ConstraintLayout layout = popupView.findViewById(R.id.addepi);
        EditText editText = popupView.findViewById(R.id.text_epitaph);

        if (Utils.isThemeDark()) {
            editText.setBackground(getResources().getDrawable(R.drawable.edit_text_with_border_dark));
            layout.setBackgroundColor(getResources().getColor(R.color.colorBlackDark));
        }

        PopupAddEpitaph popupWindow = new PopupAddEpitaph(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setCallback(this);
        popupWindow.setModel(responseEpitaphs);
        popupWindow.setUp(recyclerView, responseEpitaphs.getBody());
    }

    @Override
    public void delete(Integer id) {
//        onClick(recyclerView);
        DeleteAlertDialog myDialogFragment = new DeleteAlertDialog();
        myDialogFragment.setCallback(this);
        myDialogFragment.setEpitaphId(id);
        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();
        myDialogFragment.show(transaction, "dialog");
    }

    @Override
    public void deleteEpitaph(Integer id) {
        presenter.deleteEpitaph(id);
    }
}
