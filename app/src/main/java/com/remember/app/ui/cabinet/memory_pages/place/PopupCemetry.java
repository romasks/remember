package com.remember.app.ui.cabinet.memory_pages.place;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.remember.app.R;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.ui.adapters.HandBookAdapter;

import java.util.List;

public class PopupCemetry extends PopupWindow {

    private PopupCity.Callback callback;
    private RecyclerView recyclerView;
    private HandBookAdapter handBookAdapter;

    PopupCemetry(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public void setUp(View contentView, List<ResponseHandBook> responseHandBooks) {
        setFocusable(true);
        setOutsideTouchable(true);
        showAtLocation(contentView, Gravity.TOP, 0, 0);
        View popupView = getContentView();
        recyclerView = popupView.findViewById(R.id.rv_city);
        handBookAdapter = new HandBookAdapter(responseHandBooks);
        LinearLayoutManager layoutManager = new LinearLayoutManager(popupView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(handBookAdapter);
//        handBookAdapter.setCallback(this);
        handBookAdapter.setItems(responseHandBooks);
    }

    public interface Callback{

        void saveItem(String name);

    }

    void setCallback(PopupCity.Callback callback) {
        this.callback = callback;
    }
}
