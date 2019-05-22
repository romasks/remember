package com.remember.app.ui.cabinet.memory_pages.place;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.remember.app.R;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.ui.adapters.HandBookAdapter;
import com.remember.app.ui.adapters.PageFragmentAdapter;

import java.util.List;

public class PopupCity extends PopupWindow implements HandBookAdapter.Callback{

    private Button dismiss;
    private Callback callback;
    private RecyclerView recyclerView;
    private HandBookAdapter handBookAdapter;

    PopupCity(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public void setUp(View contentView, List<ResponseHandBook> responseHandBooks) {
        setFocusable(true);
        setOutsideTouchable(true);
        showAtLocation(contentView, Gravity.TOP, 0, 0);
        View popupView = getContentView();
        recyclerView = popupView.findViewById(R.id.rv_city);
        handBookAdapter = new HandBookAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(popupView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(handBookAdapter);
        handBookAdapter.setCallback(this);
        handBookAdapter.setItems(responseHandBooks);
    }

    public interface Callback{

        void saveItem(String name);

    }

    @Override
    public void saveItem(String name) {
        dismiss();
        callback.saveItem(name);
    }

    void setCallback(Callback callback) {
        this.callback = callback;
    }
}
