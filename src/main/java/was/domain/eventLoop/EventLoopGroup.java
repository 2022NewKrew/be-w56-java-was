package was.domain.eventLoop;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class EventLoopGroup {
    private final BossEventLoop bossEventLoop;
    private final Queue<WorkerEventLoop> workerEventLoopList;
    private final ExecutorService threadPool;

    public EventLoopGroup(int port, int workerEventLoopSize, EventService eventService) {
        threadPool = Executors.newFixedThreadPool(workerEventLoopSize + 1);

        this.bossEventLoop = new BossEventLoop(port, this::register);
        this.workerEventLoopList = new LinkedList<>();

        IntStream.range(0, workerEventLoopSize)
                .forEach(i -> workerEventLoopList.add(new WorkerEventLoop(eventService)));
    }

    public void run() {
        threadPool.execute(bossEventLoop);
        workerEventLoopList.forEach(threadPool::execute);
    }

    private void register(SelectionKey key) {
        final ServerSocketChannel severChannel = ((ServerSocketChannel) key.channel());
        final SocketChannel client = accept(severChannel);

        final WorkerEventLoop workerEventLoop = workerEventLoopList.remove();
        workerEventLoop.register(client);
        workerEventLoopList.add(workerEventLoop);
    }

    private SocketChannel accept(ServerSocketChannel serverSocketChannel) {
        try {
            return serverSocketChannel.accept();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
