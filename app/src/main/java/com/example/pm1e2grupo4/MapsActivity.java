package com.example.pm1e2grupo4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

import com.example.pm1e2grupo4.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    EditText aelatitud;
    EditText aelongitud;
    String latitud, longitud, l1,l2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       // aelatitud = (EditText) findViewById(R.id.aeLatitud);
        //aelongitud = (EditText) findViewById(R.id.aeLongitud);

        //latitud.setText(getIntent().getStringExtra("latitud"));
       // longitud.setText(getIntent().getStringExtra("longitud"));

        Intent intent = getIntent();
        latitud = intent.getStringExtra("longitud");
        longitud = intent.getStringExtra("latitud");


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Antut(googleMap);
    }

    public void Antut (GoogleMap googleMap){

        GoogleMap map = googleMap;
        mMap = googleMap;
        //latitud = aelatitud.getText().toString();
        //longitud = aelongitud.getText().toString();

        LatLng ubi = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
        //mMap.addMarker(new MarkerOptions().position(ubi).title("AQUI ESTOY!!!"));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud)))
                .zoom(16)
                .build();
       mMap.addMarker(new MarkerOptions().position(ubi).title("AQUI ESTOY!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(ubi));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}