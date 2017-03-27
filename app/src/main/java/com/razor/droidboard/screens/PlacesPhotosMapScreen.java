package com.razor.droidboard.screens;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.razor.droidboard.R;
import com.razor.droidboard.screens.base.BaseActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlacesPhotosMapScreen extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap m_map;

    LocationManager m_locationManager;

    public static final int LOCATION_PERMISSION = 1;

    SupportMapFragment m_mapFragment;

    RelativeLayout m_parentView;

    EditText m_edtLocationDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_photos_map_screen);

        instantiateScreen();

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        m_locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    }

    private void instantiateScreen() {
        m_mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        m_mapFragment.getMapAsync(this);

        m_parentView = (RelativeLayout) findViewById(R.id.parentView);

        m_edtLocationDesc = (EditText) findViewById(R.id.edtLocationDesc);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user
                Snackbar.make(m_parentView, R.string.permission_rationale_maps, Snackbar.LENGTH_INDEFINITE)
                        .setAction(android.R.string.ok, new View.OnClickListener()
                        {
                            @Override
                            @TargetApi(Build.VERSION_CODES.M)
                            public void onClick(View v)
                            {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
            }
        } else {
            getUsersLocation();
        }
    }

    private void getUsersLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        m_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, m_locationListener);

        Location lastKnownLocation = m_locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (lastKnownLocation != null)
        {
            LatLng myPos = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            m_map.addMarker(new MarkerOptions().position(myPos).title("Your Position"));
            m_map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 15));
        }
    }

    LocationListener m_locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location)
        {
            LatLng myPos = new LatLng(location.getLatitude(), location.getLongitude());
            m_map.addMarker(new MarkerOptions().position(myPos).title("Your Position"));
            m_map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPos, 15));

            Geocoder geocoder = new Geocoder(getApplicationContext());

            List<Address> listAddresses = new ArrayList<>();

            try {
                listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (listAddresses != null && listAddresses.size() > 0)
            {
                LatLng foundPos;
                for (Address a :
                        listAddresses)
                {
                    foundPos = new LatLng(a.getLatitude(), a.getLongitude());
                    String markerTitle = a.getAddressLine(0);
                    m_map.addMarker(new MarkerOptions().position(foundPos).title(markerTitle));
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        m_map = googleMap;

        configureMapSettings();

        checkLocationPermission();
    }

    private void configureMapSettings() {
        m_map.setOnMapClickListener(m_mapClick);
    }

    GoogleMap.OnMapClickListener m_mapClick = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case LOCATION_PERMISSION:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    //permission granted
                    getUsersLocation();
                } else
                {
                    //permission denied

                }
                return;
            }
        }
    }
}
