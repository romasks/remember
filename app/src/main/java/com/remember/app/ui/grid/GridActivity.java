package com.remember.app.ui.grid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponsePages;
import com.remember.app.ui.adapters.ImageAdapter;
import com.remember.app.ui.auth.AuthActivity;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;
import com.remember.app.ui.utils.PopupPageScreen;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.processors.PublishProcessor;

public class GridActivity extends BaseActivity implements GridView, ImageAdapter.Callback, PopupPageScreen.Callback {

    @InjectPresenter
    GridPresenter presenter;

    @BindView(R.id.image_rv)
    RecyclerView recyclerView;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.title)
    TextView title;

    private RecyclerView.LayoutManager layoutManager;
    private ImageAdapter imageAdapter;
    private PublishProcessor<Integer> paginator = PublishProcessor.create();
    private int pageNumber = 1;
    private int countSum = 0;

    private PopupPageScreen popupWindowPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        imageAdapter = new ImageAdapter();
        imageAdapter.setCallback(this);
        recyclerView.setAdapter(imageAdapter);

        presenter.getImages(pageNumber);


    }

    @OnClick(R.id.button)
    public void entry() {
        if (!Prefs.getString("USER_ID", "").equals("")) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, AuthActivity.class));
        }
    }

    @OnClick(R.id.search)
    public void doSearch() {
        showEventScreen();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_grid;
    }

    @Override
    public void onReceivedImages(ResponsePages responsePages) {
        imageAdapter.setItems(responsePages.getResult());
        countSum = responsePages.getPages();
    }

    private void showEventScreen() {
        View popupView = getLayoutInflater().inflate(R.layout.popup_page_screen, null);
        popupWindowPage = new PopupPageScreen(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindowPage.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        popupWindowPage.setFocusable(true);
        popupWindowPage.setCallback(this);
        popupWindowPage.setUp(title);
    }

    @Override
    public void openPage(MemoryPageModel memoryPageModel) {
        Intent intent = new Intent(this, ShowPageActivity.class);
        intent.putExtra("PERSON", memoryPageModel);
        intent.putExtra("IS_LIST", true);
        intent.putExtra("SHOW", true);
        startActivity(intent);

    }

    @Override
    public void search(String lastName) {

    }
}
