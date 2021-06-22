package com.grouphiking.project.a3chikingapp.Data;

import android.location.Location;
import android.location.LocationManager;

import androidx.annotation.NonNull;

import java.util.Map;

public class Trip {
    //Type
    private Type type;

    //Location
    private Location FROM;
    private Location TO;

    //Strings
    @NonNull  private String NAME;


    public Trip(Type type, Location FROM, Location TO, @NonNull String NAME) {
        this.type = type;
        this.FROM = FROM;
        this.TO = TO;
        this.NAME = NAME;
    }

    public Trip() {
        this.FROM = null;
        this.TO = null;
        this.NAME = "";
        this.type = null;
    }

    public void setFROM(Map<String, Object> map){
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude((Double) map.get(Constants.LAT));
        location.setLongitude((Double) map.get(Constants.LONGD));
        TO = location;
    }

    public void setTO(Map<String, Object> map){
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude((Double) map.get(Constants.LAT));
        location.setLongitude((Double) map.get(Constants.LONGD));
        TO = location;
    }

    public Location getFROM() {
        return FROM;
    }

    public Location getTO() {
        return TO;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return FROM.toString() + "," + TO.toString() + "," + getNAME();
    }
}
