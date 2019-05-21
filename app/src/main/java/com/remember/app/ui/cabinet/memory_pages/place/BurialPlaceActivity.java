package com.remember.app.ui.cabinet.memory_pages.place;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
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
import com.remember.app.data.models.ResponseCities;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BurialPlaceActivity extends MvpAppCompatActivity implements OnMapReadyCallback, PopupMap.Callback, PlaceView {

    @InjectPresenter
    PlacePresenter presenter;

    @BindView(R.id.pick)
    Button pick;
    @BindView(R.id.coordinates_value)
    AutoCompleteTextView coordinates;
    @BindView(R.id.city_value)
    AutoCompleteTextView city;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burial_place);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        ButterKnife.bind(this);

        city.setOnClickListener(v -> presenter.getCities());
    }

    @OnClick(R.id.back)
    public void back() {
        onBackPressed();
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
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void setCoordinates(double latitude, double longitude) {
        String result = latitude + " " + "," + " " + longitude;
        coordinates.setText(result);
    }

    @Override
    public void onUpdatedCities(List<ResponseCities> responseCities) {
        View popupView = getLayoutInflater().inflate(R.layout.popup_city, null);
        PopupCity popupWindow = new PopupCity(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setUp(pick);
    }
}
