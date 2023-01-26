package com.bookkeeperfvm.android;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class Bookkeeper extends Application {

    private static Bookkeeper mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        mInstance = this;
    }

    public static synchronized Bookkeeper getInstance() {
        return mInstance;
    }



}


