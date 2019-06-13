package com.remember.app.ui.cabinet.epitaphs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.remember.app.R;
import com.remember.app.data.models.RequestAddEpitaphs;
import com.remember.app.data.models.ResponseEpitaphs;
import com.remember.app.ui.adapters.EpitaphsAdapter;
import com.remember.app.ui.utils.MvpAppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EpitaphsActivity extends MvpAppCompatActivity implements EpitaphsView, PopupAddEpitaph.Callback {

    @InjectPresenter
    EpitaphsPresenter presenter;

    @BindView(R.id.rv_epitaphs)
    RecyclerView recyclerView;
    @BindView(R.id.plus)
    ImageView plus;
    @BindView(R.id.back)
    ImageView back;

    private Unbinder unbinder;
    private EpitaphsAdapter epitaphsAdapter;
    private int pageId;
    private boolean isShow;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epitaphs);
        unbinder = ButterKnife.bind(this);

        pageId = getIntent().getIntExtra("ID_PAGE", 1);
        isShow = getIntent().getBooleanExtra("SHOW", false);

        epitaphsAdapter = new EpitaphsAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(epitaphsAdapter);

        presenter.getEpitaphs(pageId);
        plus.setOnClickListener(v -> {
            if (!isShow){
                showPopupAdd();
            }
        });
        back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void showPopupAdd() {
        View popupView = getLayoutInflater().inflate(R.layout.popup_epitaph, null);
        PopupAddEpitaph popupWindow = new PopupAddEpitaph(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setCallback(this);
        popupWindow.setUp(recyclerView);
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
    public void saveEpitaph(String text) {
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        RequestAddEpitaphs requestAddEpitaphs = new RequestAddEpitaphs();
        requestAddEpitaphs.setBody(text);
        requestAddEpitaphs.setPageId(pageId);
        requestAddEpitaphs.setUserId("");
        requestAddEpitaphs.setCreated(df.format(new Date()));
        requestAddEpitaphs.setUpdated(df.format(new Date()));
        presenter.saveEpitaph(requestAddEpitaphs);
    }
}
