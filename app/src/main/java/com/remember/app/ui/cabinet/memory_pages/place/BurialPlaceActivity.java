package com.remember.app.ui.cabinet.memory_pages.place;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.snackbar.Snackbar;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseHandBook;
import com.remember.app.ui.utils.MvpAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BurialPlaceActivity extends MvpAppCompatActivity implements PopupMap.Callback,
        PlaceView, PopupCity.Callback, PopupCemetery.Callback {

    @InjectPresenter
    PlacePresenter presenter;

    @BindView(R.id.pick)
    Button pick;
    @BindView(R.id.city_value)
    AutoCompleteTextView city;
    @BindView(R.id.cemetery)
    AutoCompleteTextView cemetery;
    @BindView(R.id.sector_value)
    AutoCompleteTextView sector;
    @BindView(R.id.sec_value)
    AutoCompleteTextView sectorPlace;
    @BindView(R.id.grave_value)
    AutoCompleteTextView grave;
    @BindView(R.id.coordinates_value)
    AutoCompleteTextView coordinates;

    private ResponseHandBook responseHandBook;
    private boolean isEdit;
    private MemoryPageModel memoryPageModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burial_place);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        ButterKnife.bind(this);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        responseHandBook = new ResponseHandBook();

        isEdit = getIntent().getBooleanExtra("EDIT", false);
        if (isEdit) {
            memoryPageModel = getIntent().getParcelableExtra("MODEL");
            initEdit();
        }

        city.setOnClickListener(v -> presenter.getCities());
        cemetery.setOnClickListener(v -> {
            if (!city.getText().toString().equals("")) {
                presenter.getCemetery(responseHandBook.getId());
            } else {
                Snackbar.make(city, "Введите город", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void initEdit() {
        try {
            coordinates.setText(memoryPageModel.getCoords());
        } catch (NullPointerException e) {
            coordinates.setText("");
        }
        try {
            city.setText(memoryPageModel.getGorod());
        } catch (NullPointerException e) {
            city.setText("");
        }
        try {
            cemetery.setText(memoryPageModel.getNazvaklad());
        } catch (NullPointerException e) {
            cemetery.setText("");
        }
        try {
            sector.setText(memoryPageModel.getUchastok());
        } catch (NullPointerException e) {
            sector.setText("");
        }
        try {
            grave.setText(memoryPageModel.getNummogil());
        } catch (NullPointerException e) {
            grave.setText("");
        }
        try {
            sectorPlace.setText(memoryPageModel.getSector());
        } catch (NullPointerException e){
            sectorPlace.setText("");
        }
    }

    @OnClick(R.id.back)
    public void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("COORDS", coordinates.getText().toString());
        intent.putExtra("CITY", city.getText().toString());
        intent.putExtra("CEMETERY", cemetery.getText().toString());
        intent.putExtra("SPOT_ID", sector.getText().toString());
        intent.putExtra("GRAVE_ID", grave.getText().toString());
        intent.putExtra("SECTOR", sectorPlace.getText().toString());
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
        finish();
    }

    @OnClick(R.id.submit)
    public void submit() {
        if (city.getText().toString().equals("")) {
            Snackbar.make(city, "Введите город", Snackbar.LENGTH_LONG).show();
        } else if (cemetery.getText().toString().equals("")) {
            Snackbar.make(city, "Введите название кладбища", Snackbar.LENGTH_LONG).show();
        } else {
            onBackPressed();
        }
    }

    @OnClick(R.id.pick)
    public void openMap() {
        PopupMap popupWindow = null;
        try {
            View popupView = getLayoutInflater().inflate(R.layout.popup_google_map, null);
            popupWindow = new PopupMap(
                    popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setCallback(this);
            popupWindow.setUp(pick, getSupportFragmentManager());
        } catch (Exception e) {

        } finally {
        }
    }


    @Override
    public void setCoordinates(double latitude, double longitude) {
        String result = latitude + " " + "," + " " + longitude;
        coordinates.setText(result);
    }

    @Override
    public void onUpdatedCities(List<ResponseHandBook> responseHandBooks) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_city, null);
        PopupCity popupWindow = new PopupCity(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setCallback(this);
        popupWindow.setUp(pick, responseHandBooks);
    }

    @Override
    public void onUpdatedCemetery(ResponseCemetery responseCemeteries) {
//        if (responseCemeteries.isEmpty()) {
        if (responseCemeteries == null) {
            cemetery.setFocusableInTouchMode(true);
            cemetery.requestFocus();
            InputMethodManager imm =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            Toast.makeText(this, "Введите значение вручную", Toast.LENGTH_LONG).show();
        } else {
            View popupView = getLayoutInflater().inflate(R.layout.popup_city, null);
            PopupCemetery popupWindow = new PopupCemetery(
                    popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setCallback(this);
            ArrayList<ResponseCemetery> list = new ArrayList<>();
            list.add(responseCemeteries);
//            popupWindow.setUp(pick, responseCemeteries);
            popupWindow.setUp(pick, list);
        }
    }

    @Override
    public void saveItem(ResponseHandBook responseHandBook) {
        this.responseHandBook.setId(responseHandBook.getId());
        this.responseHandBook.setRegionId(responseHandBook.getRegionId());
        this.responseHandBook.setName(responseHandBook.getName());
        city.setText(responseHandBook.getName());
    }


    @Override
    public void saveItem(ResponseCemetery responseCemetery) {
        cemetery.setText(responseCemetery.getName());
    }
}
