package com.remember.app.ui.cabinet.memory_pages.place;

import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.remember.app.R;

public class PopupMap extends PopupWindow implements OnMapReadyCallback {

    private Button dismiss;
    private Callback callback;
    private double latitude;
    private double longitude;

    PopupMap(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public void setUp(View contentView, FragmentManager supportFragmentManager) {
        setFocusable(true);
        setOutsideTouchable(true);
        showAtLocation(contentView, Gravity.TOP, 0, 0);
        View popupView = getContentView();
        dismiss = popupView.findViewById(R.id.dismiss);
        SupportMapFragment mapFragment = (SupportMapFragment) supportFragmentManager.findFragmentById(R.id.fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        dismiss.setOnClickListener(v -> {
            if (mapFragment != null) {
                supportFragmentManager.beginTransaction().remove(mapFragment).commit();
            }
            callback.setCoordinates(latitude, longitude);
            dismiss();
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        googleMap.setOnMapClickListener(point -> {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(point));
            latitude = point.latitude;
            longitude = point.longitude;
        });
    }

    interface Callback{

        void setCoordinates(double latitude, double longitude);

    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }
}
