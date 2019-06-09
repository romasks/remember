package com.remember.app.ui.cabinet.memory_pages.place;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.SearchView;

import com.remember.app.R;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.ui.adapters.HandBookAdapter;

import java.util.List;

public class PopupCity extends PopupWindow implements HandBookAdapter.Callback {

    private Callback callback;
    private HandBookAdapter handBookAdapter;

    PopupCity(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public void setUp(View contentView, List<ResponseHandBook> responseHandBooks) {
        setFocusable(true);
        setOutsideTouchable(true);
        showAtLocation(contentView, Gravity.TOP, 0, 0);
        View popupView = getContentView();
        RecyclerView recyclerView = popupView.findViewById(R.id.rv_city);
        SearchView search = popupView.findViewById(R.id.search_city);
        handBookAdapter = new HandBookAdapter(responseHandBooks);
        LinearLayoutManager layoutManager = new LinearLayoutManager(popupView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(handBookAdapter);
        handBookAdapter.setCallback(this);

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

    public interface Callback {

        void saveItem(ResponseHandBook responseHandBook);

    }

    @Override
    public void saveItem(ResponseHandBook responseHandBook) {
        dismiss();
        callback.saveItem(responseHandBook);
    }

    void setCallback(Callback callback) {
        this.callback = callback;
    }
}
