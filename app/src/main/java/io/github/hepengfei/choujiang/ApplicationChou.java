package io.github.hepengfei.choujiang;

import android.app.Application;
import android.content.SharedPreferences;


public class ApplicationChou extends Application {

    private static ApplicationChou sInstance;

    public static ApplicationChou getInstance() { return sInstance; }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

    }

    public SharedPreferences getSharedPreferences(String name) {
        return getSharedPreferences(name, MODE_PRIVATE);
    }
}
