package com.zhuinden.vanillacatexample.util.schedulers;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Owner on 2017. 04. 29..
 */

public class BackgroundScheduler
        implements Scheduler {
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public void executeOnThread(Runnable runnable) {
        executor.execute(runnable);
    }
}
