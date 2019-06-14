package com.remember.app.ui.cabinet.memory_pages.show_page;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
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

    public void setUp(View contentView, FragmentManager supportFragmentManager, String coords) {
        setFocusable(true);
        setOutsideTouchable(true);
        showAtLocation(contentView, Gravity.TOP, 0, 0);
        View popupView = getContentView();
        dismiss = popupView.findViewById(R.id.dismiss);
        dismiss.setText("Закрыть");
        SupportMapFragment mapFragment = (SupportMapFragment) supportFragmentManager.findFragmentById(R.id.fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        String[] result = coords.split(",");
        latitude = Double.parseDouble(result[0]);
        longitude = Double.parseDouble(result[1]);
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
            myPosition = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(myPosition).title("Start"));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 13));
        }
    }

    interface Callback{

        void setCoordinates(double latitude, double longitude);

    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }
}
