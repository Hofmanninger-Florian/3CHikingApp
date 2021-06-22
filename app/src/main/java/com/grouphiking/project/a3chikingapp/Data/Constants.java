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
import androidx.appcompat.app.AppCompatDelegate;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.grouphiking.project.a3chikingapp.Activitys.LoginActivity;
import com.grouphiking.project.a3chikingapp.Activitys.RegisterActivity;
import com.grouphiking.project.a3chikingapp.Preferences.LanguageSpinner;
import com.grouphiking.project.a3chikingapp.Preferences.SettingsActivity;
import com.grouphiking.project.a3chikingapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


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
    public static boolean DEF_MODE = true;
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

    //For Day/Night Button (Cheat)
    public static View view;

    public static View getView() {
        return view;
    }

    public static void setView(View view) {
        Constants.view = view;
    }

    //=====Firebase=====
    public static User WORKING_USER;

    public static void getUsersGet(String username, View view, RegisterActivity register, LoginActivity login){
        DocumentReference docRef = firestore.collection(USER_ID).document(username);
        docRef.get().addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Snackbar.make(view, R.string.firebase_get_Error, BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Snackbar.make(view, R.string.firebase_get_Error, BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DocumentSnapshot snap = documentSnapshot;
                assert snap != null;
                Constants.WORKING_USER = (User)snap.toObject(User.class);
                if(register == null){
                    if(getWorkingUser() != null){
                        Constants.setLANGUAGE(getWorkingUser().getLanguage());
                        Constants.setMODE(getWorkingUser().getMode());
                        if(getWorkingUser().getMode().isValue()){
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        }else{
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        }
                    }

                    login.proofResults();
                }else if(login == null){
                    register.proofResults();
                }
            }
        });
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

    public static void updateUser(View view){
        User user = getWorkingUser();
        DocumentReference reference = firestore.collection(USER_ID).document(user.username);
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("username", user.username);
        userMap.put("password", user.password);
        userMap.put("trips", user.trips);
        userMap.put("language", user.language.name());
        userMap.put("mode", user.mode.name());

        reference.update(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        LISTENERPOST.onpostExecute();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Snackbar.make(view, R.string.firebase_update, BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });
    }

    public static postExecuteListner LISTENERPOST;

    public static void setLISTENERPOST(postExecuteListner LISTENERPOST) {
        Constants.LISTENERPOST = LISTENERPOST;
    }

    public interface postExecuteListner{
        public void onpostExecute();
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
