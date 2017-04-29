package com.zhuinden.vanillacatexample.util.schedulers;

/**
 * Created by Owner on 2017. 04. 29..
 */

public class ImmediateScheduler
        implements Scheduler {
    @Override
    public void executeOnThread(Runnable runnable) {
        runnable.run();
    }
}
