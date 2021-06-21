package com.grouphiking.project.a3chikingapp.Activitys;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Layout;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.grouphiking.project.a3chikingapp.Data.Constants;
import com.grouphiking.project.a3chikingapp.Data.Trip;
import com.grouphiking.project.a3chikingapp.R;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.mapbox.navigation.core.MapboxNavigation;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.parseColor;
import static com.mapbox.mapboxsdk.style.expressions.Expression.color;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lineProgress;
import static com.mapbox.mapboxsdk.style.expressions.Expression.linear;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.match;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineGradient;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

public class MapActionActivity extends AppCompatActivity implements OnMapReadyCallback, Callback<DirectionsResponse> {

    private MapView mapView;
    private MapboxMap map;
    private LocationManager manager;
    private LocationListener listener;
    private LocationComponent locationComponent;
    private Location originLocation;
    private RelativeLayout layout;
    private Point origin;
    private Point destination;
    private DirectionsRoute currentRoute;
    private Trip trip;
    private Feature directionsRouteFeature;
    private MapboxDirections client;
    private static final String ROUTE_SOURCE_ID = "rout-source-id";
    private static final String ROUTE_LINE_SOURCE_ID = "route-source-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final float LINE_WIDTH = 6f;
    private static final String ORIGIN_COLOR = "#2096F3";
    private static final String DESTINATION_COLOR = "#F84D4D";
    private static final String ORIGIN_ICON_ID = "origin-icon-id";
    private static final String DESTINATION_ICON_ID = "destination-icon-id";
    private static final String RED_PIN_ICON_ID = "red-pin-icon-id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        origin = Point.fromLngLat(48.1128647, 13.6990269);
        destination = Point.fromLngLat(48.1683569, 13.9918258);

        super.onCreate(savedInstanceState);
        Constants.setTransition(this, new LinearInterpolator());
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        layout = findViewById(R.id.layout_Map);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                MapActionActivity.this.map = mapboxMap;
                mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull @NotNull Style style) {
                        initLayers(style);
                        initSource(style);
                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(destination.latitude(),destination.longitude()))
                                .zoom(18)
                                .tilt(13)
                                .build();
                        map.animateCamera(CameraUpdateFactory.newCameraPosition(position),1000);
                        enableLocationComponent(style);
                        getRoute(map, origin, destination);
                    }
                });
                map = mapboxMap;
            }
        });

    }

    private void initSource(@NonNull Style loadedMapStyle){
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_SOURCE_ID));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initLayers(@NonNull Style loadedMapStyle){
        LineLayer routeLayer = new LineLayer(ROUTE_LAYER_ID, ROUTE_SOURCE_ID);

        routeLayer.setProperties(
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                PropertyFactory.lineWidth(5f),
                PropertyFactory.lineColor(Color.parseColor("#009688"))
        );
        loadedMapStyle.addLayer(routeLayer);
        loadedMapStyle.addImage(RED_PIN_ICON_ID, Objects.requireNonNull(BitmapUtils.getBitmapFromDrawable(
                getResources().getDrawable(R.drawable.marker)
        )));

        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                PropertyFactory.iconImage(RED_PIN_ICON_ID),
                PropertyFactory.iconIgnorePlacement(true),
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.iconOffset(new Float[]{0f,-9f})
        ));

    }
    private void getRoute(MapboxMap mapboxMap, Point origin, Point destination){
        client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_WALKING)
                .accessToken(getString(R.string.mapbox_access_token))
                .build();
        client.enqueueCall(this);
    }

    @Override
    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
        if (response.body() == null){
            Toast.makeText(this, "No routes found, maybe no Accesstoken", Toast.LENGTH_SHORT);
            return;
        }else if(response.body().routes().size() < 1){
            Toast.makeText(this, "No routes found", Toast.LENGTH_SHORT);
            return;
        }


        DirectionsRoute currentRoute = response.body().routes().get(0);

        if(map!=null){
            map.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull @NotNull Style style) {
                    GeoJsonSource source = style.getSourceAs(ROUTE_SOURCE_ID);
                    if(source!= null){
                        source.setGeoJson(LineString.fromPolyline(currentRoute.geometry(), Constants.PRECISION_6));
                    }
                }
            });
        }
    }

    @Override
    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
        Toast.makeText(this,t.getMessage(),Toast.LENGTH_SHORT);
    }

    //Mapbox Navigation



    /*

    private void initSources(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_LINE_SOURCE_ID, new GeoJsonOptions().withLineMetrics(true)));
        loadedMapStyle.addSource(new GeoJsonSource(ICON_SOURCE_ID, getOriginAndDestinationFeatureCollection()));
    }

    private FeatureCollection getOriginAndDestinationFeatureCollection() {
        Feature originFeature = Feature.fromGeometry(origin);
        originFeature.addStringProperty("originDestination", "origin");
        Feature destinationFeature = Feature.fromGeometry(destination);
        destinationFeature.addStringProperty("originDestination", "destination");
        return FeatureCollection.fromFeatures(new Feature[]{originFeature, destinationFeature});
    }

    private void initLayers(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addLayer(new LineLayer(ROUTE_LAYER_ID, ROUTE_LINE_SOURCE_ID).withProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(LINE_WIDTH),
                lineGradient(interpolate(
                        linear(), lineProgress(),
                        stop(0f, color(parseColor(ORIGIN_COLOR))),
                        stop(1f, color(parseColor(DESTINATION_COLOR)))
                ))));
        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                iconImage(match(get("originDestination"), literal("origin"),
                        stop("origin", ORIGIN_ICON_ID),
                        stop("destination", DESTINATION_ICON_ID))),
                iconIgnorePlacement(true),
                iconAllowOverlap(true),
                iconOffset(new Float[]{0f, -4f})
        ));
    }
*/

    /*private void getRoute(MapboxMap mapboxMap *//*ADD ORIGIN AND DESTINATION IN HERE AFTER TESTING*//*) {
        origin = Point.fromLngLat(48.1128647, 13.6990269);
        destination = Point.fromLngLat(48.1683569, 13.9918258);
        client = MapboxDirections.builder().origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_WALKING)
                .accessToken(getString(R.string.mapbox_access_token))
                .build();
        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                Timber.d("Response code: %s", response.code());
                if (response.body() == null) {
                    Timber.e("No routes found :(");
                    return;
                } else if (response.body().routes().size() < 1) {
                    Timber.e("No routes found :(");
                    return;
                }
                currentRoute = response.body().routes().get(0);
                if (currentRoute != null) {
                    if (mapboxMap != null) {
                        mapboxMap.getStyle(new Style.OnStyleLoaded() {
                            public void onStyleLoaded(@NonNull Style style) {
                                GeoJsonSource originalDestinationPointGeoJsonSource = style.getSourceAs(ICON_SOURCE_ID);
                                if (originalDestinationPointGeoJsonSource != null) {
                                    originalDestinationPointGeoJsonSource.setGeoJson(getOriginAndDestinationFeatureCollection());
                                }
                                GeoJsonSource linearLayerRouteGeoJsonSource = style.getSourceAs(ROUTE_LINE_SOURCE_ID);
                                if (linearLayerRouteGeoJsonSource != null) {
                                    LineString lineString = LineString.fromPolyline(currentRoute.geometry(), 6);
                                    linearLayerRouteGeoJsonSource.setGeoJson(Feature.fromGeometry(lineString));
                                }

                            }

                        });
                    }
                } else {
                    Timber.d("Directions route is null :(");
                    Toast.makeText(MapActionActivity.this, getString(R.string.router_can_not_be_displayed), Toast.LENGTH_SHORT).show();
                }
            }


            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Toast.makeText(MapActionActivity.this, getString(R.string.route_call_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }*/


/*
    private void Route(){
        trip = new Trip();
        //origin = Point.fromLngLat(trip.getFROM().getLongitude(), trip.getFROM().getLatitude());
        //destination = Point.fromLngLat(trip.getTO().getLongitude(), trip.getTO().getLatitude());
        origin = Point.fromLngLat(48.1128647,13.6990269);
        destination = Point.fromLngLat(48.1683569,13.9918258);


        MapboxDirections client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_WALKING)
                .accessToken(getString(R.string.mapbox_access_token))
                .build();



        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                if(response.body() == null) {
                    Log.e("FAIL!", "No Route found, make sure you set the right user and access token.");
                    return;
                }else if(response.body().routes().size() < 1){
                    Log.e("FAIL!", "No Routes found!");
                    return;
                }

                currentRoute = response.body().routes().get(0);

            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Log.e("Error:", t.getMessage());
            }
        });



        if(currentRoute != null){
            directionsRouteFeature = Feature.fromGeometry(LineString.fromPolyline(currentRoute.geometry(),6));

        }
    } */

    //get Location Permission
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        //checking if permissions are enabled --> if not request them
        locationComponent = map.getLocationComponent();
        locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, loadedMapStyle).build());
           if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
               System.out.println("No");
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                   requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.LOCATION_REQUEST);
               }
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
                Snackbar.make(layout, "You Denied the Permission", BaseTransientBottomBar.LENGTH_LONG);
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

    //------------------------- "on..." Standard Methods --------------------------------------
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