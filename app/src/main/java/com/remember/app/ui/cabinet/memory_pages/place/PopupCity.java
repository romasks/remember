package com.remember.app.ui.cabinet.memory_pages.place;

import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.remember.app.R;
import com.remember.app.customView.CustomButton;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.ui.adapters.HandBookAdapter;
import com.remember.app.utils.Utils;

import java.util.List;

public class PopupCity extends PopupWindow implements HandBookAdapter.Callback {

    private Callback callback;
    private HandBookAdapter handBookAdapter;
    private CustomButton addButton;
    private CustomTextView searchHint;

    PopupCity(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public void setUp(View contentView, List<ResponseHandBook> responseHandBooks) {
        setFocusable(true);
        setOutsideTouchable(true);
        showAtLocation(contentView, Gravity.TOP, 0, 0);

        View popupView = getContentView();
        if (Utils.isThemeDark()) {
            popupView.findViewById(R.id.lay).setBackgroundColor(popupView.getContext().getResources().getColor(R.color.colorBlackDark));
        }
        RecyclerView recyclerView = popupView.findViewById(R.id.rv_city);
        searchHint = popupView.findViewById(R.id.searchHint);
        SearchView search = popupView.findViewById(R.id.search_city);
        addButton = popupView.findViewById(R.id.btnAddTown);
        addButton.setOnClickListener(v -> {
           callback.saveTown(search.getQuery().toString());
           dismiss();
        });

        handBookAdapter = new HandBookAdapter(responseHandBooks);
        handBookAdapter.setCallback(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(popupView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(handBookAdapter);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                handBookAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                handBookAdapter.filter(newText);
                if (newText!="")
                    searchHint.setVisibility(View.GONE);
                else searchHint.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

    public interface Callback {

        void saveItem(ResponseHandBook responseHandBook);
        void saveTown(String town);

    }

    @Override
    public void saveItem(ResponseHandBook responseHandBook) {
        dismiss();
        callback.saveItem(responseHandBook);

    }

    @Override
    public void visibleButton(boolean isVisible) {
        if (isVisible)
            addButton.setVisibility(View.VISIBLE);
        else
            addButton.setVisibility(View.GONE);
    }

    void setCallback(Callback callback) {
        this.callback = callback;
    }
}
