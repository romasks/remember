package com.remember.app.ui.cabinet.epitaphs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.RequestAddEpitaphs;
import com.remember.app.data.models.ResponseEpitaphs;
import com.remember.app.ui.adapters.EpitaphsAdapter;
import com.remember.app.ui.utils.DeleteAlertDialog;
import com.remember.app.ui.utils.DividerItemDecoration;
import com.remember.app.ui.utils.MvpAppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
    @BindView(R.id.titliepi)
    TextView title;

    private Unbinder unbinder;
    private EpitaphsAdapter epitaphsAdapter;
    private int pageId;
    private boolean isShow;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Prefs.getInt("IS_THEME",0)==2) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.AppTheme_Dark);
        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epitaphs);
        unbinder = ButterKnife.bind(this);
        if (Prefs.getInt("IS_THEME",0)==2){
            back.setImageResource(R.drawable.ic_back_dark_theme);
            plus.setImageResource(R.drawable.ic_add2);

        }
        pageId = getIntent().getIntExtra("ID_PAGE", 1);
        isShow = getIntent().getBooleanExtra("SHOW", false);

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
        back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void showPopupAdd() {
        View popupView = getLayoutInflater().inflate(R.layout.popup_epitaph, null);
        ConstraintLayout layout=popupView.findViewById(R.id.addepi);
        EditText editText=popupView.findViewById(R.id.text_epitaph);
        if (Prefs.getInt("IS_THEME",0)==2) {
            editText.setBackground(getResources().getDrawable(R.drawable.edit_text_with_border_dark));
            layout.setBackgroundColor(getResources().getColor(R.color.colorBlacDark));
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
        Snackbar.make(recyclerView, "Эпитафия добавлена", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onErrorSavedEpitaphs(Throwable throwable) {
        Snackbar.make(recyclerView, "Ошибка сохранения", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onEditedEpitaphs(RequestAddEpitaphs requestAddEpitaphs) {
        presenter.getEpitaphs(pageId);
    }

    @Override
    public void saveEpitaph(String text) {
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        RequestAddEpitaphs requestAddEpitaphs = new RequestAddEpitaphs();
        requestAddEpitaphs.setBody(text);
        requestAddEpitaphs.setPageId(pageId);
        requestAddEpitaphs.setUserId(Prefs.getString("USER_ID", ""));
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
        requestAddEpitaphs.setUserId(Prefs.getString("USER_ID", ""));
        presenter.editEpitaph(requestAddEpitaphs, id);
    }

    @Override
    public void change(ResponseEpitaphs responseEpitaphs) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_epitaph, null);
        ConstraintLayout layout=popupView.findViewById(R.id.addepi);
        EditText editText=popupView.findViewById(R.id.text_epitaph);
        if (Prefs.getInt("IS_THEME",0)==2) {
            editText.setBackground(getResources().getDrawable(R.drawable.edit_text_with_border_dark));
            layout.setBackgroundColor(getResources().getColor(R.color.colorBlacDark));
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
    public void delete() {
        onClick(recyclerView);
    }

    @Override
    public void deleteEpitaph() {
        Toast.makeText(this, "Эпитафия удалена",
                Toast.LENGTH_LONG).show();
    }
}
