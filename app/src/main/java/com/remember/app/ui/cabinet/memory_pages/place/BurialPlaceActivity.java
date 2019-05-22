package com.remember.app.ui.cabinet.memory_pages.place;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.remember.app.R;
import com.remember.app.data.models.ResponseCemetery;
import com.remember.app.data.models.ResponseHandBook;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BurialPlaceActivity extends MvpAppCompatActivity implements PopupMap.Callback, PlaceView, PopupCity.Callback {

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
    @BindView(R.id.grave_value)
    AutoCompleteTextView grave;
    @BindView(R.id.coordinates_value)
    AutoCompleteTextView coordinates;

    private ResponseHandBook responseHandBook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burial_place);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        ButterKnife.bind(this);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        responseHandBook = new ResponseHandBook();
        city.setOnClickListener(v -> presenter.getCities());
        cemetery.setOnClickListener(v -> {
            if (!city.getText().toString().equals("")) {
                presenter.getCemetery(responseHandBook.getId());
            } else {
                Snackbar.make(city, "Введите город", Snackbar.LENGTH_LONG).show();
            }

        });
    }

    @OnClick(R.id.back)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.submit)
    public void submit(){
        if (city.getText().toString().equals("")){
            Snackbar.make(city, "Введите город", Snackbar.LENGTH_LONG).show();
        } else if (cemetery.getText().toString().equals("")){
            Snackbar.make(city, "Введите название кладбища", Snackbar.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.pick)
    public void openMap() {
        View popupView = getLayoutInflater().inflate(R.layout.popup_google_map, null);
        PopupMap popupWindow = new PopupMap(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setCallback(this);
        popupWindow.setUp(pick, getSupportFragmentManager());
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
    public void onUpdatedCemetery(List<ResponseCemetery> responseCemeteries) {
        if (responseCemeteries.isEmpty()) {
            cemetery.setFocusableInTouchMode(true);
            cemetery.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            Snackbar.make(city, "Введите значение вручную", Snackbar.LENGTH_LONG).show();
        }else {
            //TODO сделать попап
        }
    }

    @Override
    public void saveItem(ResponseHandBook responseHandBook) {
        this.responseHandBook.setId(responseHandBook.getId());
        this.responseHandBook.setRegionId(responseHandBook.getRegionId());
        this.responseHandBook.setName(responseHandBook.getName());
        city.setText(responseHandBook.getName());
    }


}
