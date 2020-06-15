package com.liyaqing.download.utils;

import java.util.concurrent.ThreadFactory;

public class SingleFactory implements ThreadFactory {
    private final String arg$1;

    private SingleFactory(String var1) {
        this.arg$1 = var1;
    }

    private static ThreadFactory get$Lambda(String var0) {
        return new SingleFactory(var0);
    }

    public Thread newThread(Runnable var1) {
        return new Thread(var1);
    }

    public static ThreadFactory lambdaFactory(String var0) {
        return new SingleFactory(var0);
    }
}