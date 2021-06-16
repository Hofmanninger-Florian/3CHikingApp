package com.grouphiking.project.a3chikingapp.Activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.grouphiking.project.a3chikingapp.Data.Constants;
import com.grouphiking.project.a3chikingapp.R;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.security.Permission;
import java.util.List;

public class MapActionActivity extends AppCompatActivity implements OnMapReadyCallback{

    private MapView mapView;
    private MapboxMap map;
    private LocationManager manager;
    private LocationListener listener;
    private LocationComponent locationComponent;
    private Location originLocation;
    private RelativeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.setTransition(this, new LinearInterpolator());
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        layout = (RelativeLayout)findViewById(R.id.layout_Map);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                    }
                });
                map = mapboxMap;
            }
        });

    }

    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        //checking if permissions are enabled --> if not request them
        locationComponent = map.getLocationComponent();
        locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
           if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
               System.out.println("No");
               requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_REQUEST);
           }else{
               setLocationTracking();
           }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Constants.LOCATION_REQUEST){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                setLocationTracking();
            }else{
                Snackbar.make(layout, "You Diened the Permission", BaseTransientBottomBar.LENGTH_LONG);
            }
        }

    }

    @SuppressLint("MissingPermission")
    private void setLocationTracking(){
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d("Location", "Location Changed");
            }
        };
        locationComponent.setLocationComponentEnabled(true);
        locationComponent.setCameraMode(CameraMode.TRACKING);
        locationComponent.setRenderMode(RenderMode.COMPASS);
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.TIME_REQUEST_UPDATE, Constants.DIST_REQUEST_UPDATE, listener);
    }

    //------------------------- "on..." Standart Methods --------------------------------------
    protected void onStart(){
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if(manager != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.TIME_REQUEST_UPDATE, Constants.DIST_REQUEST_UPDATE, listener);
            } else {
                enableLocationComponent(map.getStyle());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
    }

}