package com.grouphiking.project.a3chikingapp.Activitys;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;


import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.grouphiking.project.a3chikingapp.Data.Constants;
import com.grouphiking.project.a3chikingapp.Data.Mode;
import com.grouphiking.project.a3chikingapp.Data.Trip;
import com.grouphiking.project.a3chikingapp.Data.Type;
import com.grouphiking.project.a3chikingapp.Preferences.Daynightswitch;
import com.grouphiking.project.a3chikingapp.Preferences.MyContextWrapper;
import com.grouphiking.project.a3chikingapp.Preferences.SettingsActivity;
import com.grouphiking.project.a3chikingapp.R;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;

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
import com.mapbox.mapboxsdk.maps.Image;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapActionActivity extends AppCompatActivity implements OnMapReadyCallback, Callback<DirectionsResponse> {
    private Trip trip;
    private MapView mapView;
    private MapboxMap map;
    private LocationManager manager;
    private LocationListener listener;
    private LocationComponent locationComponent;
    private RelativeLayout layout;
    private Point origin;
    private Point destination;
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
    private Button button;
    private Button buttonOrigin;
    private ImageButton leaveActivity;
    private Type type;
    private Button buttonNight;
    //Variables for Textview TextOutputs
    private int timeRoute;
    private int timeRouteHour;
    private int distanceRoute;
    private int distanceRouteKm;
    private int altimeterRoute;
    private int altimeterRouteKm;
    private String TypeWalking;
    private String TypeCycling;
    //TextViews for TextOutputs
    private TextView timeViewRoute;
    private TextView distanceViewRoute;
    private TextView altimeterViewRoute;
    private TextView altimeterViewRouteEnd;
    private TextView typeViewRoute;

    private RadioButton radioButtonBike;
    private RadioButton radioButtonWalking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        Constants.setTransition(this, new LinearInterpolator());
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_map);
        buttonNight = (Button) findViewById(R.id.pref_button_night);
        typeViewRoute = (TextView) findViewById(R.id.textView11);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        trip = Constants.getTrip();
        type = trip.getType();
        layout = findViewById(R.id.layout_Map);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                MapActionActivity.this.map = mapboxMap;
                //-------------------------------------------------- DARK MODE --------------------------------------------
                if(Constants.MODE == Mode.NIGHT){
                mapboxMap.setStyle(Style.DARK, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        origin = Point.fromLngLat(trip.getFROM().getLongitude(), trip.getFROM().getLatitude());
                        destination = Point.fromLngLat(trip.getTO().getLongitude(),trip.getTO().getLatitude());

                        @SuppressLint("Range") CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(origin.latitude(),origin.longitude()))
                                .zoom(15)
                                .tilt(100)
                                .build();
                        map.animateCamera(CameraUpdateFactory.newCameraPosition(position),200);
                        getRoute(map, origin, destination, type);
                        enableLocationComponent(style);



                        //Buttons
                        button = findViewById(R.id.buttonLocation);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CameraPosition dest = new CameraPosition.Builder()
                                        .target(new LatLng(destination.latitude(),destination.longitude()))
                                        .zoom(13)
                                        .tilt(50)
                                        .build();
                                map.animateCamera(CameraUpdateFactory.newCameraPosition(dest),200);
                            }
                        });
                        buttonOrigin = findViewById(R.id.buttonLocationOrigin);
                        buttonOrigin.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CameraPosition orig = new CameraPosition.Builder()
                                        .target(new LatLng(origin.latitude(),origin.longitude()))
                                        .zoom(13)
                                        .tilt(50)
                                        .build();
                                map.animateCamera(CameraUpdateFactory.newCameraPosition(orig),200);
                            }
                        });

                        leaveActivity = findViewById(R.id.imageButtonLeaveActivity);
                        leaveActivity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                                finish();
                            }
                        });



                        initLayers(style);
                        initSource(style);

                        /*Gradient
                        style.addLayer(new LineLayer("lineLayer", "line-source").withProperties(
                                PropertyFactory.lineDasharray(new Float[]{0.01f,2f}),
                                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                                PropertyFactory.lineWidth(5f),
                                PropertyFactory.lineColor(Color.parseColor("#e55e5e"))
                        ));*/

                    }
                });}
                //----------------------------------LIGHT MODE-------------------------------------------------------------
                else{
                    mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                        @Override
                        public void onStyleLoaded(@NonNull Style style) {
                            origin = Point.fromLngLat(trip.getFROM().getLongitude(), trip.getFROM().getLatitude());
                            destination = Point.fromLngLat(trip.getTO().getLongitude(),trip.getTO().getLatitude());
                            @SuppressLint("Range") CameraPosition position = new CameraPosition.Builder()
                                    .target(new LatLng(origin.latitude(),origin.longitude()))
                                    .zoom(15)
                                    .tilt(100)
                                    .build();
                            map.animateCamera(CameraUpdateFactory.newCameraPosition(position),200);
                            getRoute(map, origin, destination, type);
                            enableLocationComponent(style);



                            //Buttons
                            button = findViewById(R.id.buttonLocation);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CameraPosition dest = new CameraPosition.Builder()
                                            .target(new LatLng(destination.latitude(),destination.longitude()))
                                            .zoom(13)
                                            .tilt(50)
                                            .build();
                                    map.animateCamera(CameraUpdateFactory.newCameraPosition(dest),200);
                                }
                            });
                            buttonOrigin = findViewById(R.id.buttonLocationOrigin);
                            buttonOrigin.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CameraPosition orig = new CameraPosition.Builder()
                                            .target(new LatLng(origin.latitude(),origin.longitude()))
                                            .zoom(13)
                                            .tilt(50)
                                            .build();
                                    map.animateCamera(CameraUpdateFactory.newCameraPosition(orig),200);
                                }
                            });

                            leaveActivity = findViewById(R.id.imageButtonLeaveActivity);
                            leaveActivity.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onBackPressed();
                                    finish();
                                }
                            });



                            initLayers(style);
                            initSource(style);

                        /*Gradient
                        style.addLayer(new LineLayer("lineLayer", "line-source").withProperties(
                                PropertyFactory.lineDasharray(new Float[]{0.01f,2f}),
                                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                                PropertyFactory.lineWidth(5f),
                                PropertyFactory.lineColor(Color.parseColor("#e55e5e"))
                        ));*/

                        }
                    });
                }
                map = mapboxMap;
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        super.attachBaseContext(MyContextWrapper.wrap(newBase,preferences.getString(newBase.getString(R.string.SpinnerKey), Constants.LANGUAGE.getLanguage())));
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
                PropertyFactory.lineWidth(8f),
                PropertyFactory.lineColor(Color.parseColor("#009688"))
        );
        loadedMapStyle.addLayer(routeLayer);
        loadedMapStyle.addImage(RED_PIN_ICON_ID, Objects.requireNonNull(BitmapUtils.getBitmapFromDrawable(
                getDrawable(R.drawable.marker)
        )));

        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                PropertyFactory.iconImage(RED_PIN_ICON_ID),
                PropertyFactory.iconIgnorePlacement(true),
                PropertyFactory.iconAllowOverlap(true),
                PropertyFactory.iconOffset(new Float[]{0f,-9f})
        ));
    }
    private void getRoute(MapboxMap mapboxMap, Point origin, Point destination, Type type){
        if(trip.getType().equals(Type.HIKE)) {
            client = MapboxDirections.builder()
                    .origin(origin)
                    .destination(destination)
                    .overview(DirectionsCriteria.OVERVIEW_FULL)
                    .profile(DirectionsCriteria.PROFILE_WALKING)
                    .accessToken(getString(R.string.mapbox_access_token))
                    .build();
            client.enqueueCall(this);
        } else if(trip.getType().equals(Type.BIKE)) {
            client = MapboxDirections.builder()
                    .origin(origin)
                    .destination(destination)
                    .overview(DirectionsCriteria.OVERVIEW_FULL)
                    .profile(DirectionsCriteria.PROFILE_CYCLING)
                    .accessToken(getString(R.string.mapbox_access_token))
                    .build();
            client.enqueueCall(this);
        }
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
        SharedPreferences time = getSharedPreferences("Time", 0);
        SharedPreferences.Editor editor = time.edit();




        //The Distance
        distanceViewRoute = (TextView) findViewById(R.id.textView6);
        distanceRoute = (int) Math.round(response.body().routes().get(0).distance());
        distanceRouteKm = (int) Math.round((response.body().routes().get(0).distance())/1000);

        if(distanceRoute < 900){
            distanceViewRoute.setText(distanceRoute+ " M.");
        }
        else if(distanceRoute >= 900){
            distanceViewRoute.setText(distanceRouteKm +" Km.");
        }

        //The Time
        timeViewRoute = (TextView) findViewById(R.id.textView7);
        timeRoute = (int) Math.round(response.body().routes().get(0).duration()/60);
        timeRouteHour = (int) Math.round((response.body().routes().get(0).duration()/60)/24);
        if(timeRoute < 300){
            timeViewRoute.setText(timeRoute +" Min.");
        } else if(timeRoute >= 300){
            timeViewRoute.setText(timeRouteHour+" Std.");
        }

        //The Type
        typeViewRoute.setText(trip.getType().name());
        //The Altimeter
        //altimeterViewRoute = (TextView) findViewById(R.id.textView8);
        //altimeterViewRouteEnd = (TextView) findViewById(R.id.textview9);


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


/*
    private void makeElevationRequest(@NonNull final Style style, @NonNull LatLng point){
        MapboxTilequery elevationQuery = MapboxTilequery.builder().accessToken(getString(R.string.mapbox_access_token))
                .tilesetIds("mapbox.mapbox-terrain-v2")
                .query(Point.fromLngLat(point.getLongitude(), point.getLatitude()))
                .geometry("polygon")
                .layers("contour")
                .build();

        elevationQuery.enqueueCall(new Callback<FeatureCollection>() {
            @Override
            public void onResponse(Call<FeatureCollection> call, Response<FeatureCollection> response) {
                if(response.body().features() != null){
                    List<Feature> featureList = response.body().features();
                    String listOfElevations = "";

                    for(Feature singleFeature : featureList){
                        listOfElevations = listOfElevations + singleFeature.getStringProperty("ele")+", ";
                    }
                    altimeterViewRoute.setText(String.format("yes");
                }
            }

            @Override
            public void onFailure(Call<FeatureCollection> call, Throwable t) {

            }
        });
    }*/
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

    //------------------------- "on..." Methods --------------------------------------
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