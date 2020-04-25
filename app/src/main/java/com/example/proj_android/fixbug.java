package com.example.proj_android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


import java.io.File;

public class fixbug extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences googleBug = getSharedPreferences("google_bug_154855417", Context.MODE_PRIVATE);
        if (!((SharedPreferences) googleBug).contains("fixed")) {
            File corruptedZoomTables = new File(getFilesDir(), "ZoomTables.data");
            corruptedZoomTables.delete();
            googleBug.edit().putBoolean("fixed", true).apply();
        }
    }
}
