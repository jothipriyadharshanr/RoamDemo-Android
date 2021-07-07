package com.roam.demo;

import android.app.Application;

import com.roam.sdk.Roam;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Roam.initialize(this,"cbf221858a829f5b27409d1cb21f15bd8fbe93ebf1ca79a4f8e767e87416798a");
    }
}
