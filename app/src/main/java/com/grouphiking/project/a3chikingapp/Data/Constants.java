package com.grouphiking.project.a3chikingapp.Data;

import android.content.Context;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.Window;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;


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
    public static boolean DEF_MODE = false;
    public static final String DAY_COLOR = "#CAA407";
    public static final String NIGHT_COLOR = "#2F4858";

    public static void setTransition(AppCompatActivity activity, Interpolator interpolator){
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.RIGHT);
        slide.setDuration(400);
        slide.setInterpolator(interpolator);
        activity.getWindow().setExitTransition(slide);
        activity.getWindow().setEnterTransition(slide);
        activity.getWindow().setAllowEnterTransitionOverlap(true);
        activity.getWindow().setAllowReturnTransitionOverlap(true);
    }

    //For Location
    public static final int LOCATION_REQUEST = "Location".hashCode();
    public static final int TIME_REQUEST_UPDATE = 5000;
    public static final int DIST_REQUEST_UPDATE = 50;
}
