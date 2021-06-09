package com.grouphiking.project.a3chikingapp.Data;

import android.location.Location;

import androidx.annotation.NonNull;

public class Trip {

    //Location
    private final Location FROM;
    private final Location TO;

    //Strings
    @NonNull  private String NAME;
    @NonNull private String DESC;

    public Trip(Location FROM, Location TO, @NonNull String NAME, @NonNull String DESC) {
        this.FROM = FROM;
        this.TO = TO;
        this.NAME = NAME;
        this.DESC = DESC;
    }

    public Trip() {
        this.FROM = null;
        this.TO = null;
        this.DESC = "";
        this.NAME = "";
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

    public String getDESC() {
        return DESC;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setDESC(String DESC) {
        this.DESC = DESC;
    }
}
