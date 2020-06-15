package com.liyaqing.download.utils;


import android.os.Handler;
import android.os.HandlerThread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class ThreadPool {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE;
    private static final int MAXIMUM_POOL_SIZE;
    private static final int KEEP_ALIVE_SECONDS = 30;
    private static final BlockingQueue<Runnable> sPoolWorkQueue;
    private static final ThreadFactory sThreadFactory;
    private static final ThreadPoolExecutorWrapper THREAD_POOL_EXECUTOR;
    private static Handler sWorkerHandler;
    private static HandlerThread sHandlerThread;
    private static final Object mWorkObj;
    public static final ThreadPoolExecutor SINGLE;

    private ThreadPool() {
    }

    public static ThreadPool getInstance() {
        return ThreadPool.Inner.INSTANCE;
    }

    public void execute(Runnable task) {
        THREAD_POOL_EXECUTOR.execute(task);
    }

    public void onDestroy() {
        THREAD_POOL_EXECUTOR.shutdownNow();
        SINGLE.shutdownNow();
    }

    private static void initWorkHandler() {
        if (sWorkerHandler == null) {
            synchronized(mWorkObj) {
                if (sWorkerHandler == null) {
                    sHandlerThread = new HandlerThread("qtt_base_work_handler");
                    sHandlerThread.setPriority(4);
                    sHandlerThread.start();
                    sWorkerHandler = new Handler(sHandlerThread.getLooper());
                }
            }
        }

    }

    public static Handler getWorkerHandler() {
        initWorkHandler();
        return sWorkerHandler;
    }

    public static void runOnWorker(Runnable r) {
        initWorkHandler();
        sWorkerHandler.post(r);
    }

    static {
        CORE_POOL_SIZE = Math.max(CPU_COUNT * 2, 8);
        MAXIMUM_POOL_SIZE = Math.max(CPU_COUNT * 4 + 1, 16);
        sPoolWorkQueue = new LinkedBlockingQueue(128);
        sThreadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(Runnable runnable) {
                Thread thread = new ThreadPool.BackgroundThread(runnable, "qtt_base_threadpool_" + this.mCount.getAndIncrement());
                return thread;
            }
        };
        sWorkerHandler = null;
        sHandlerThread = null;
        mWorkObj = new Object();
        ThreadPoolExecutorWrapper threadPoolExecutor = new ThreadPoolExecutorWrapper(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, 30L, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        THREAD_POOL_EXECUTOR = threadPoolExecutor;
        SINGLE = SingleThread.SINGLE;
    }

    private static class Inner {
        private static final ThreadPool INSTANCE = new ThreadPool();

        private Inner() {
        }
    }

    private static class BackgroundThread extends Thread {
        public BackgroundThread(Runnable runnable, String name) {
            super(runnable, name);
            this.setPriority(5);
        }
    }
}
