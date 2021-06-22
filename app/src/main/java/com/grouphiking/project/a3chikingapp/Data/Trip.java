package com.grouphiking.project.a3chikingapp.Data;

import android.location.Location;

import androidx.annotation.NonNull;

public class Trip {
    //Type
    private Type type;

    //Location
    private final Location FROM;
    private final Location TO;

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

    @Override
    public String toString() {
        return FROM.toString() + "," + TO.toString() + "," + getNAME();
    }
}
