package webserver;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {

    private static final int THREAD_POOL_COUNT = 10;

    ExecutorService pool = Executors.newFixedThreadPool(THREAD_POOL_COUNT);

    public void submit(Callable<Void> task) {
        pool.submit(task);
    }
}
