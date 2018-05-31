package com.beeplay.core.utils;



import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 * @author chenlf
 * @version 1.0
 */
public class ThreadPool {
  private final ThreadPoolExecutor pool;

  private static ThreadPool theadPool = new ThreadPool();

  private ThreadPool() {
    pool =
        new ThreadPoolExecutor(1, 100, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(500), new ThreadPoolExecutor.CallerRunsPolicy());
    pool.allowCoreThreadTimeOut(true);
  }

  public synchronized static ThreadPool getInstance() {
    return theadPool;
  }

  public void run(Runnable runnable) {
    pool.execute(runnable);
  }

  private void close() {
    if (pool != null) {
      pool.shutdown();
    }
  }

  static {
    // 加入进程关闭的钩子 将本线程池也一并关闭再关闭进程
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
      @Override
      public void run() {
        theadPool.close();
      }
    }));
  }

}
