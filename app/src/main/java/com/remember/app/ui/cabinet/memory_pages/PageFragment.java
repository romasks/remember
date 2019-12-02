package com.remember.app.ui.cabinet.memory_pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.adapters.PageFragmentAdapter;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.cabinet.memory_pages.add_page.NewMemoryPageActivity;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;
import com.remember.app.ui.utils.MvpAppCompatFragment;
import com.remember.app.ui.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.remember.app.data.Constants.INTENT_EXTRA_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_IS_LIST;
import static com.remember.app.data.Constants.INTENT_EXTRA_PERSON;

public class PageFragment extends MvpAppCompatFragment implements PageView, PageFragmentAdapter.Callback, MainActivity.CallbackPage {

    @InjectPresenter
    PagePresenter presenter;

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.show_all)
    TextView showAll;
    @BindView(R.id.not_page)
    LinearLayout emptyLayout;

    private Unbinder unbinder;
    private PageFragmentAdapter pageFragmentAdapter;
    private int countPage = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Prefs.putBoolean("PAGE_FRAGMENT", true);
        ((MainActivity) getActivity()).setCallback(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_memory_pages, container, false);
        unbinder = ButterKnife.bind(this, v);

        pageFragmentAdapter = new PageFragmentAdapter();
        pageFragmentAdapter.setCallback(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(pageFragmentAdapter);

        return v;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.show_all)
    void showAll() {
        presenter.getPages();
    }

    @OnClick(R.id.go_to_add)
    void newPage() {
        startActivity(new Intent(getContext(), NewMemoryPageActivity.class));
    }

    @Override
    public void onReceivedPages(List<MemoryPageModel> memoryPageModels) {
        if (!memoryPageModels.isEmpty()) {
            showAll.setVisibility(View.GONE);
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
    public void sendItemsSearch(List<MemoryPageModel> result) {
        if (result.isEmpty()) {
            Utils.showSnack(recyclerView, "Записи не найдены");
        }
        showAll.setVisibility(result.isEmpty() ? View.VISIBLE : View.GONE);
        pageFragmentAdapter.setItems(result);
        pageFragmentAdapter.notifyDataSetChanged();

    }
}