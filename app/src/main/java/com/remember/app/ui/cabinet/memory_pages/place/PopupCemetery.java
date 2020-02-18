package com.remember.app.ui.cabinet.memory_pages.place;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.SearchView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.remember.app.R;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.ui.adapters.HandBookAdapterCemetery;
import com.remember.app.ui.utils.Utils;

import java.util.List;

public class PopupCemetery extends PopupWindow implements HandBookAdapterCemetery.Callback {

    private Callback callback;
    private RecyclerView recyclerView;
    private HandBookAdapterCemetery handBookAdapter;

    PopupCemetery(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    @SuppressLint("WrongConstant")
    public void setUp(View contentView, List<ResponseCemetery> responseHandBooks) {
        setFocusable(true);
        setOutsideTouchable(true);
        showAtLocation(contentView, Gravity.TOP, 0, 0);
        View popupView = getContentView();
        if (Utils.isThemeDark()) {
            popupView.findViewById(R.id.lay).setBackgroundColor(popupView.getContext().getResources().getColor(R.color.colorBlackDark));
        }
        recyclerView = popupView.findViewById(R.id.rv_city);
        SearchView search = popupView.findViewById(R.id.search_city);
        handBookAdapter = new HandBookAdapterCemetery(responseHandBooks);
        LinearLayoutManager layoutManager = new LinearLayoutManager(popupView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(handBookAdapter);
        handBookAdapter.setCallback(this);
        handBookAdapter.setItems(responseHandBooks);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handBookAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handBookAdapter.filter(newText);
                return false;
            }
        });
    }

    @Override
    public void saveItem(ResponseCemetery responseCemetery) {
        dismiss();
        callback.saveItem(responseCemetery);
    }

    public interface Callback {

        void saveItem(ResponseCemetery responseCemetery);

    }

    void setCallback(Callback callback) {
        this.callback = callback;
    }
}
