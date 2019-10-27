package com.remember.app.ui.cabinet.memory_pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_memory_pages, container, false);
        unbinder = ButterKnife.bind(this, v);
        pageFragmentAdapter = new PageFragmentAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(pageFragmentAdapter);
        pageFragmentAdapter.setCallback(this);
        if (Prefs.getInt("IS_THEME",0)==0||Prefs.getInt("IS_THEME",0)==1){

        }else if (Prefs.getInt("IS_THEME",0)==2){


        }
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
    public void showAll() {
        presenter.getPages();
    }

    @OnClick(R.id.go_to_add)
    public void newPage() {
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
        intent.putExtra("PERSON", person);
        intent.putExtra("ID", person.getId());
        intent.putExtra("IS_LIST", true);
        startActivity(intent);
    }

    @Override
    public void sendItemsSearch(List<MemoryPageModel> result) {
        if(result.size()==0) {
            Toast.makeText(getActivity(), "Записи не найдены", Toast.LENGTH_SHORT).show();
        }
        if (result.isEmpty()) {
            showAll.setVisibility(View.VISIBLE);
        } else {
            showAll.setVisibility(View.GONE);
        }
        pageFragmentAdapter.setItems(result);
        pageFragmentAdapter.notifyDataSetChanged();

    }
}