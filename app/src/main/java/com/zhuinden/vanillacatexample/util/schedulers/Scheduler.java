package com.zhuinden.vanillacatexample.util.schedulers;

/**
 * Created by Owner on 2017. 04. 29..
 */

public interface Scheduler {
    void executeOnThread(Runnable runnable);
}
