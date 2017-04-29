package com.zhuinden.vanillacatexample.application;

import android.app.Application;

import com.zhuinden.vanillacatexample.application.injection.ObjectGraph;

/**
 * Created by Owner on 2017. 04. 29..
 */

public class CustomApplication
        extends Application {
    ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        objectGraph = new ObjectGraph(this);
    }
}
