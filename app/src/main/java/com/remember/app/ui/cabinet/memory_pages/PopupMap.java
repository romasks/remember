package com.remember.app.ui.cabinet.memory_pages;

import android.app.Activity;
import android.content.Context;
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

    PopupMap(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public void setUp(View contentView, FragmentManager supportFragmentManager) {
        setFocusable(true);
        setOutsideTouchable(true);
        showAtLocation(contentView, Gravity.TOP, 0, 0);
        View popupView = getContentView();

        SupportMapFragment mapFragment = (SupportMapFragment) supportFragmentManager.findFragmentById(R.id.fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}
