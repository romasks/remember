package com.remember.app.ui.cabinet.memory_pages.place;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
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

import static android.content.Context.LOCATION_SERVICE;

public class PopupMap extends PopupWindow implements OnMapReadyCallback {

    private Button dismiss;
    private Callback callback;
    private double latitude;
    private double longitude;
    private LatLng myPosition;

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
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(getContentView().getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContentView().getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getContentView().getContext().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            myPosition = new LatLng(latitude, longitude);
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
            googleMap.addMarker(new MarkerOptions().position(myPosition).title("Start"));
        }
        googleMap.setOnMapClickListener(point -> {
            googleMap.clear();
            googleMap.setMyLocationEnabled(true);
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
