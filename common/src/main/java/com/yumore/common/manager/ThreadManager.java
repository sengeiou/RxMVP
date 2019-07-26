package com.yumore.common.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Nathaniel
 * @date 18-11-20-上午9:38
 */
public class ThreadManager {
    private static ThreadPollProxy threadPollProxy;

    public static synchronized ThreadPollProxy getThreadPollProxy() {
        if (threadPollProxy == null) {
            threadPollProxy = new ThreadPollProxy(3, 6, 1000);
        }
        return threadPollProxy;
    }

    public static class ThreadPollProxy {
        private ThreadPoolExecutor threadPoolExecutor;
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;

        ThreadPollProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        public void execute(Runnable runnable) {
            if (threadPoolExecutor == null || threadPoolExecutor.isShutdown()) {
                ThreadFactory threadFactory = Executors.defaultThreadFactory();
                threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                        TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
            }
            threadPoolExecutor.execute(runnable);
        }
    }
}
