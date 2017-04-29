package com.zhuinden.vanillacatexample.util.schedulers;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Owner on 2017. 04. 29..
 */

public class MainThreadScheduler
        implements Scheduler {
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void executeOnThread(Runnable runnable) {
        handler.post(runnable);
    }
}
