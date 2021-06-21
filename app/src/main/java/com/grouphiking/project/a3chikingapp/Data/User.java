package com.grouphiking.project.a3chikingapp.Data;

import java.util.ArrayList;

public class User {

    String username;
    String password;
    ArrayList<Trip> trips;
    public User(String username, String password, ArrayList<Trip> trips) {
        this.username = username;
        this.password = password;
        this.trips = trips;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Trip> getTrips() {
        return trips;
    }

    public void setTrips(ArrayList<Trip> trips) {
        this.trips = trips;
    }
}
