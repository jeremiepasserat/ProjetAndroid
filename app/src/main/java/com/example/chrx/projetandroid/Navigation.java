package com.example.chrx.projetandroid;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Navigation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location mLastLocation;
    private Marker oldmarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
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
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(43.514591, 5.451379);
        mMap.addMarker(new MarkerOptions().position(sydney).title("IUT Aix").snippet("Là où les rêves commencent"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));


        // demande à l'utilisateur les droits pour la géolocalisation.
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED)) {

        }
        else {
            ActivityCompat.requestPermissions(this, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },1
                    );
        }

        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // récupère la dernière position enrengistrée du GPS pour l'afficher sur la carte
        try
        {
            Location location = new Location(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
            LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
            oldmarker = mMap.addMarker(new MarkerOptions().position(position).title("Vous êtes ici").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 14));
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }


        //mise à jour de la position de manière périodique
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60, 1, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d("GPS", "Latitude " + location.getLatitude() + " et longitude " + location.getLongitude());
                    LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                    oldmarker.remove();
                    oldmarker = mMap.addMarker(new MarkerOptions().position(position).title("Vous êtes ici").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }


    }

    //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER)

    public void onLocationChanged(Location location)
    {
        System.out.println("Test");
        Log.d("GPS", "Latitude " + location.getLatitude() + " et longitude " + location.getLongitude());
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(position).title("Vous êtes ici").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
    }


    }

