package com.liyaqing.download.utils;

import android.support.annotation.NonNull;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SingleThread {
    public static final ThreadPoolExecutor SINGLE;

    public SingleThread() {
    }

    public void onDestroy() {
        SINGLE.shutdownNow();
    }

    public static ThreadPoolExecutor of(@NonNull String threadName) {
        return of(threadName, 30);
    }

    public static ThreadPoolExecutor of(@NonNull String threadName, int keepAliveTime) {
        ThreadPoolExecutor singleThreadPool = new ThreadPoolExecutor(1, 1, (long)keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue(), crateFactory(threadName));
        singleThreadPool.allowCoreThreadTimeOut(true);
        return singleThreadPool;
    }

    private static ThreadFactory crateFactory(String name) {
        return  SingleFactory.lambdaFactory(name);
    }

    static {
        SINGLE = new ThreadPoolExecutor(1, 1, 10L, TimeUnit.MINUTES, new LinkedBlockingQueue(), crateFactory("qtt_single_thread_pool"));
        SINGLE.allowCoreThreadTimeOut(true);
    }


}

