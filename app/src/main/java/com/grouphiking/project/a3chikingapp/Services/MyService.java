package com.grouphiking.project.a3chikingapp.Services;

import android.app.IntentService;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class MyService extends IntentService {

    String TAG = MyService.class.getSimpleName();

    public MyService() {
        super("MyService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //Hintergrundaktivität
        Log.d(TAG, "onHandleIntent: entered");
        Log.d(TAG, "onHandleIntent: Thread: " + Thread.currentThread().getName());
        if(intent.hasExtra("msg")) {
            for (int i = 1; i <= 3; i++) {
                Log.d(TAG, "onHandleIntent: ....working");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Log.e(TAG, "onHandleIntent: " + e.getMessage());
                }
            }
            Log.d(TAG, "onHandleIntent: finished");
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onCreate() {

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent.hasExtra("msg")){
            //Do something (Notification)
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}