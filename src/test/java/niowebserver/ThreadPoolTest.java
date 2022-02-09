package niowebserver;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ThreadPoolTest {

    @Test
    public void test() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> System.out.println(1234));
        }
    }

    private static class CustomThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            System.out.println("쓰레드 생성");
            return new CustomThread();
        }
    }

    private static class CustomThread extends Thread {
        private int count = 0;

        @Override
        public void run() {
            System.out.println("Thread ID : " + currentThread().getId() + " count : " + count++);
        }
    }
}
