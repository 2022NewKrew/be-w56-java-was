package niowebserver;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestWorkerThreadPool {

    private List<RequestWorkerThread> workerThreads;
    private AtomicInteger counter;
    private AtomicBoolean isShutdown;
    private int threadPoolSize;

    public RequestWorkerThreadPool(int threadPoolSize) {
        this.counter = new AtomicInteger(0);
        this.isShutdown = new AtomicBoolean(false);
        this.threadPoolSize = threadPoolSize;
        initWorkerThreads(threadPoolSize);
        startWorkerThreads();
    }

    public void registerSocketChannel(SocketChannel socketChannel) throws ClosedChannelException {
        RequestWorkerThread thread = getRequestWorker();
        thread.registerSocketChannel(socketChannel);
        thread.wakeup();
    }

    private void startWorkerThreads() {
        for(RequestWorkerThread workerThread : workerThreads) {
            workerThread.start();
        }
    }

    private RequestWorkerThread getRequestWorker() {
        if(isShutdown.get()) {
            throw new WebServerException("Thread Pool called Shutdown");
        }
        return workerThreads.get(counter.getAndIncrement() % threadPoolSize);
    }

    public void shutdown() {
        isShutdown.set(true);
    }

    private void initWorkerThreads(int threadPoolSize) {
        this.workerThreads = new ArrayList<>(threadPoolSize);
        for(int i = 0; i < threadPoolSize; i++) {
            workerThreads.add(new RequestWorkerThread());
        }
    }
}
