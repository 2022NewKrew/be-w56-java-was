package was.domain.eventLoop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class BossEventLoop implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(BossEventLoop.class);

    private final int port;
    private final Queue<WorkerEventLoop> workerEventLoops = new LinkedList<>();

    public BossEventLoop(int port, List<WorkerEventLoop> workerEventLoops) {
        this.port = port;
        this.workerEventLoops.addAll(workerEventLoops);
    }

    @Override
    public void run() {
        try (final Selector selector = Selector.open();
             final ServerSocketChannel serverChannel = ServerSocketChannel.open()) {

            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress(port));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                final int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                final Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    final SelectionKey key = keys.next();

                    if (!key.isValid()) {
                        continue;
                    }

                    if (key.isAcceptable()) {
                        register(key);
                    }

                    keys.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SocketChannel accept(ServerSocketChannel serverSocketChannel) {
        try {
            return serverSocketChannel.accept();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private void register(SelectionKey key) {
        final ServerSocketChannel severChannel = ((ServerSocketChannel) key.channel());
        SocketChannel client = accept(severChannel);
        final WorkerEventLoop workerEventLoop = workerEventLoops.remove();
        workerEventLoop.register(client);
        workerEventLoops.add(workerEventLoop);
    }
}
