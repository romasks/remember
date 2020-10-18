package com.remember.app.ui.cabinet.memory_pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.adapters.PageFragmentAdapter;
import com.remember.app.ui.base.BaseFragment;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.cabinet.memory_pages.add_page.NewMemoryPageActivity;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;
import com.remember.app.utils.Utils;

import java.util.LinkedList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.remember.app.data.Constants.INTENT_EXTRA_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_IS_LIST;
import static com.remember.app.data.Constants.INTENT_EXTRA_PERSON;

public class PageFragment extends BaseFragment
        implements PageView, PageFragmentAdapter.Callback, MainActivity.CallbackPage {

    @InjectPresenter
    PagePresenter presenter;

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.show_all)
    CustomTextView showAll;
    @BindView(R.id.not_page)
    LinearLayout emptyLayout;

    private PageFragmentAdapter pageFragmentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Prefs.putBoolean("PAGE_FRAGMENT", true);
        ((MainActivity) getActivity()).setCallback(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_memory_pages;
    }

    @Override
    protected void setUp() {
        pageFragmentAdapter = new PageFragmentAdapter();
        pageFragmentAdapter.setCallback(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(pageFragmentAdapter);
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            onResume();
            Prefs.putBoolean("EVENT_FRAGMENT", false);
            Prefs.putBoolean("PAGE_FRAGMENT", true);
            presenter.getPages();
        } else {
            Prefs.putBoolean("EVENT_FRAGMENT", true);
            Prefs.putBoolean("PAGE_FRAGMENT", false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getPages();
    }

    @OnClick(R.id.show_all)
    void showAll() {
        showAll.setVisibility(View.GONE);
        presenter.getPages();
    }

    @Override
    public void onReceivedPages(LinkedList<MemoryPageModel> memoryPageModels) {
        if (!memoryPageModels.isEmpty()) {
            showAll.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
            pageFragmentAdapter.setItems(memoryPageModels);
        } else {
            emptyLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void sendItem(MemoryPageModel person) {
        Intent intent = new Intent(getActivity(), ShowPageActivity.class);
        intent.putExtra(INTENT_EXTRA_PERSON, person);
        intent.putExtra(INTENT_EXTRA_ID, person.getId());
        intent.putExtra(INTENT_EXTRA_IS_LIST, true);
        startActivity(intent);
    }

    @Override
    public void sendItemsSearch(LinkedList<MemoryPageModel> result) {
        if (result.isEmpty()) {
            Utils.showSnack(recyclerView, "Записи не найдены");
        }
        showAll.setVisibility(View.VISIBLE);
        pageFragmentAdapter.setItemsSearched(result);
    }



    @OnClick(R.id.go_to_add)
    public void onMenuClick() {
        startActivity(new Intent(getActivity(), NewMemoryPageActivity .class));
    }


}