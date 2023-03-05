package com.example.rento;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.rento.databinding.ActivityAddressMapsBinding;
import com.google.android.gms.maps.model.Polyline;

public class AddressMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    @Override
    public void onBackPressed() {
        finish();
    }

    private GoogleMap mMap;
    private ActivityAddressMapsBinding binding;
    String ulati1,ulaogi1;
    Double b1,b2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent=getIntent();
        ulati1=intent.getStringExtra("ulati");
        ulaogi1=intent.getStringExtra("ulogi");
        b1=Double.valueOf(ulati1);
        b2=Double.valueOf(ulaogi1);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(b1, b2);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,16f));
    }
}