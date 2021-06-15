package com.grouphiking.project.a3chikingapp.Data;

public class Constants {

    //Set when you log your self in to --> true
    public static boolean loggedIn = false;

    public static boolean isLoggedIn() {
        return loggedIn;
    }

    public static void setLoggedIn(boolean loggedIn) {
        Constants.loggedIn = loggedIn;
    }

    //Set to false for day and true for night
    public static final boolean DEF_MODE = false;
}
