package com.grouphiking.project.a3chikingapp.Data;

import android.content.Context;
import android.os.Build;
import android.os.strictmode.LeakedClosableViolation;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.transition.Visibility;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.grouphiking.project.a3chikingapp.Preferences.LanguageSpinner;
import com.grouphiking.project.a3chikingapp.Preferences.SettingsActivity;
import com.grouphiking.project.a3chikingapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;



public class Constants{

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
    public static final int PRECISION_6 = 6;
    public static final String OwnStyle1 = "mapbox://styles/florian-hofmanninger/ckq6wrych2gzz17rrrqiai4j3";

    //For Settings
    public static Language LANGUAGE = Language.ENGLISH; //default
    public static Mode MODE = Mode.DAY; //default

    public static void setMODE(Mode MODE) {
        Constants.MODE = MODE;
    }

    public LanguageSpinner SPINNER;
    private static onValueChange LISTENER;

    public static boolean LANGUAGE_SET = false;
    public static boolean SPINNER_ALREADY = false;

    public static void setSpinnerAlready(boolean spinnerAlready) {
        SPINNER_ALREADY = spinnerAlready;
    }

    public void setSPINNER(LanguageSpinner SPINNER) {
        this.SPINNER = SPINNER;
        if(LISTENER != null)LISTENER.valueChanged();
    }

    public LanguageSpinner getSPINNER() {
        return SPINNER;
    }

    public static void setLISTENER(onValueChange LISTENER) {
        Constants.LISTENER = LISTENER;
    }

    public interface onValueChange{
        void valueChanged();
    }

    public static void setLANGUAGE(Language LANGUAGE) {
        Constants.LANGUAGE = LANGUAGE;
    }

    //=====Firebase=====
    public static User WORKING_USER;

    public static ArrayList<User> userList = new ArrayList<User>();


    public static User getUsersGet(String username, View view){
        final User[] user = {null};
        DocumentReference docRef = firestore.collection(USER_ID).document(username);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snap = task.getResult();
                    assert snap != null;
                    user[0] = (User)snap.toObject(User.class);
                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Snackbar.make(view, R.string.firebase_get_Error, BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
        return user[0];
    }

    public static void addUser(User user, View view){
        Task<Void> task = firestore.collection(USER_ID).document(user.username).set(user);
        task.addOnCompleteListener(new OnCompleteListener<Void> (){
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("Debug", "User added");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Snackbar.make(view, R.string.firebase_add_Error, BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
    }

    public static User getWorkingUser() {
        return WORKING_USER;
    }

    public static void setWorkingUser(User workingUser) {
        WORKING_USER = workingUser;
    }

    public static FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static final String USER_ID = "users";

}
