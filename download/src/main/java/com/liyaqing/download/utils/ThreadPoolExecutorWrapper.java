package com.liyaqing.download.utils;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorWrapper extends ThreadPoolExecutor {
    private static List<String> threadNamePrefix = new ArrayList(6);

    public ThreadPoolExecutorWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public ThreadPoolExecutorWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public ThreadPoolExecutorWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public ThreadPoolExecutorWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public void execute(Runnable command) {
        if (command != null) {
             command = new ThreadPoolExecutorWrapper.RunnableWrapper(command, command.toString());
            super.execute(command);
        }
    }

    static {
        threadNamePrefix.add("qtt");
        threadNamePrefix.add("cpc/ask");
        threadNamePrefix.add("uqulive");
        threadNamePrefix.add("inno");
        threadNamePrefix.add("qkm_");
        threadNamePrefix.add("dataTracker");
    }

    static class RunnableWrapper implements Runnable {
        Runnable runnable;
        String name;

        RunnableWrapper(Runnable command, String name) {
            this.runnable = command;
            this.name = name;
        }

        public void run() {
            if (this.runnable != null) {
                String threadName = Thread.currentThread().getName();
                boolean needDeal = true;
                Iterator var3 = ThreadPoolExecutorWrapper.threadNamePrefix.iterator();

                while(var3.hasNext()) {
                    String item = (String)var3.next();
                    if (threadName.startsWith(item)) {
                        needDeal = false;
                        break;
                    }
                }

                if (needDeal) {
                    Thread.currentThread().setName(this.name);
                }

                this.runnable.run();
            }

        }
    }
}

